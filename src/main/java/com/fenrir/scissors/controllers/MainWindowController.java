package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.Properties;
import com.fenrir.scissors.model.ScreenshotSaver;
import com.fenrir.scissors.model.ScreenDetector;
import com.fenrir.scissors.model.draw.Tool;
import com.fenrir.scissors.model.draw.drawtools.EraserTool;
import com.fenrir.scissors.model.draw.drawtools.MarkerTool;
import com.fenrir.scissors.model.draw.drawtools.PencilTool;
import com.fenrir.scissors.model.draw.shapetools.ArrowTool;
import com.fenrir.scissors.model.draw.shapetools.EllipseTool;
import com.fenrir.scissors.model.draw.shapetools.LineTool;
import com.fenrir.scissors.model.draw.shapetools.RectangleTool;
import com.fenrir.scissors.model.screenshot.Screenshot;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MainWindowController {
    private final Logger logger = LoggerFactory.getLogger(MainWindowController.class);

    private static MainWindowController instance;

    @FXML private Button copyButton;
    @FXML private Button saveButton;
    @FXML private MenuButton saveAsMenuButton;
    @FXML private TextField screenNameField;
    @FXML private HBox toolbox;
    @FXML private Canvas screenshotCanvas;
    @FXML private ScrollPane screenshotContainer;
    @FXML private StackPane canvasContainer;

    private List<MenuItem> favoriteItems;

    private Tool currentTool;
    private boolean isToolbar;

    private final Properties properties = Properties.getInstance();
    private Screenshot screenshot;

    @FXML
    public void initialize() {
        instance = this;

        hideToolbar();
        disableSavingButtons();
        populateFavorites();

        screenshotContainer.setFitToWidth(false);
        screenshotContainer.setFitToHeight(false);

        screenshotCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            screenshot = screenshot.canvasSnapshot(screenshotCanvas);
            if(properties.isSaveToClipboard()) {
                ScreenshotSaver.copyToClipBoard(screenshot.getImage());
            }
        });

        screenNameField.textProperty().addListener(((observable, oldValue, newValue) -> screenshot.setName(newValue)));

        currentTool = new PencilTool(screenshotCanvas, canvasContainer);

    }

    @FXML
    public void captureScreen() {
        Scissors.getInstance().getStage().setIconified(true);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/CaptureWindow.fxml")));
            StackPane pane = fxmlLoader.load();
            ScreenCaptureWindowController captureWindowController = fxmlLoader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            captureWindowController.setStage(stage);
            captureWindowController.setScene(scene);

            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    Platform.runLater(captureWindowController::startCapturing);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            logger.error("Error opening the capture window");
            logger.error(e.getMessage());
        }
    }

    @FXML
    private void openSettings() {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/SettingsWindow.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setResizable(true);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            logger.error("Error opening the settings window");
            logger.error(e.getMessage());
        }
    }

    @FXML
    private void copyToClipboard() {
        ScreenshotSaver.copyToClipBoard(screenshot.getImage());
    }

    @FXML
    private void saveToDefault() {
        try {
            ScreenshotSaver.saveTo(screenshot.getImage(), properties.getDefaultPath(), screenshot.getName());
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }

    @FXML
    private void saveToLocal() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            File selectedFile = fileChooser.showSaveDialog(Scissors.scissors.getStage());

            if (selectedFile != null) {
                ScreenshotSaver.saveTo(screenshot.getImage(), selectedFile);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }

    @FXML
    private void saveToImgur() {
        new Thread(() -> {
            try {
                String url = ScreenshotSaver.saveToImgur(screenshot.getImage());
                Platform.runLater(() -> {
                    ScreenshotSaver.copyToClipBoard(url);
                    showUploadNotification();
                });
            } catch (IOException e) {
                logger.error(e.getMessage());
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Uploading to imgur failed.");
                    alert.showAndWait();
                });
            }
        }).start();
    }

    private void saveToFavorite(Path path) {
        try {
            ScreenshotSaver.saveTo(screenshot.getImage(), path, screenshot.getName());
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }


    @FXML
    private void pencilTool() {
        currentTool.disableTool();
        currentTool = new PencilTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void markerTool() {
        currentTool.disableTool();
        currentTool = new MarkerTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void eraserTool() {
        currentTool.disableTool();
        currentTool = new EraserTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void lineTool() {
        currentTool.disableTool();
        currentTool = new LineTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void rectangleTool() {
        currentTool.disableTool();
        currentTool = new RectangleTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void ellipseTool() {
        currentTool.disableTool();
        currentTool = new EllipseTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void arrowTool() {
        currentTool.disableTool();
        currentTool = new ArrowTool(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    public void processScreenshot(Screenshot screenshot) {
        this.screenshot = screenshot;

        if(properties.isSaveToClipboard()) {
            copyToClipboard();
        }

        if(properties.isSaveToDefault()) {
            saveToDefault();
        }

        screenNameField.setText(screenshot.getName());
        drawScreenshot();
        Scissors.getInstance().getStage().setIconified(false);

        if(!isToolbar) {
            showToolbar();
            enableSavingButtons();
        }
    }

    private void hideToolbar() {
        isToolbar = false;
        screenNameField.setVisible(false);
        screenNameField.setManaged(false);
        toolbox.setVisible(false);
        toolbox.setManaged(false);
        screenshotCanvas.setVisible(false);
        screenshotCanvas.setManaged(false);
        screenshotContainer.setVisible(false);
        screenshotContainer.setManaged(false);
    }

    private void showToolbar() {
        isToolbar = true;
        screenNameField.setVisible(true);
        screenNameField.setManaged(true);
        toolbox.setVisible(true);
        toolbox.setManaged(true);
        screenshotCanvas.setVisible(true);
        screenshotCanvas.setManaged(true);
        screenshotContainer.setVisible(true);
        screenshotContainer.setManaged(true);
    }

    private void disableSavingButtons() {
        copyButton.setDisable(true);
        saveAsMenuButton.setDisable(true);
        saveButton.setDisable(true);
    }

    private void enableSavingButtons() {
        copyButton.setDisable(false);
        saveAsMenuButton.setDisable(false);
        saveButton.setDisable(false);
    }

    private void populateFavorites() {
        favoriteItems = new ArrayList<>();
        List<Properties.Favorite> favoriteList = properties.getFavoriteList();

        for (Properties.Favorite favorite: favoriteList) {
            MenuItem item = new MenuItem();
            item.setText(favorite.name());
            item.setId(favorite.name());
            item.setOnAction(e -> saveToFavorite(favorite.path()));
            favoriteItems.add(item);
            saveAsMenuButton.getItems().add(item);
        }
    }

    private void showUploadNotification() {
        Notifications.create()
                .title("Scissors")
                .text("URL copied to clipboard")
                .darkStyle()
                .show();
    }

    public void refreshFavorites() {
        List<Properties.Favorite> favoriteList = properties.getFavoriteList();
        List<String> favoriteNameList = favoriteList.stream()
                .map(Properties.Favorite::name)
                .collect(Collectors.toList());

        List<MenuItem> toRemove = new ArrayList<>();
        for(MenuItem item: favoriteItems) {
            if(!favoriteNameList.contains(item.getId())) {
                toRemove.add(item);
            }
        }
        favoriteItems.removeAll(toRemove);
        saveAsMenuButton.getItems().removeAll(toRemove);

        List<String> itemsIDs = favoriteItems.stream()
                .map(MenuItem::getId)
                .collect(Collectors.toList());

        List<MenuItem> toAdd = new ArrayList<>();
        for(Properties.Favorite favorite: favoriteList) {
            if(!itemsIDs.contains(favorite.name())) {
                MenuItem item = new MenuItem();
                item.setText(favorite.name());
                item.setId(favorite.name());
                item.setOnAction(e -> saveToFavorite(favorite.path()));
                toAdd.add(item);
            }
        }
        favoriteItems.addAll(toAdd);
        saveAsMenuButton.getItems().addAll(toAdd);
    }

    public void drawScreenshot() {
        Rectangle2D stageScreenBounds = new ScreenDetector()
                .detectStageScreens(Scissors.getInstance().getStage())
                .get(0)
                .getVisualBounds();

        double maxWidth = stageScreenBounds.getWidth();
        double maxHeight = stageScreenBounds.getHeight();

        screenshotContainer.setPrefViewportWidth(maxWidth);
        screenshotContainer.setPrefViewportHeight(maxHeight);

        double screenshotWidth = screenshot.getImage().getWidth();
        double screenshotHeight = screenshot.getImage().getHeight();

        screenshotCanvas.setWidth(screenshotWidth);
        screenshotCanvas.setHeight(screenshotHeight);
        screenshotCanvas.getGraphicsContext2D()
                .drawImage(screenshot.getImage(), 0, 0, screenshotWidth, screenshotHeight);

        double stageWidth = Math.max(Scissors.MIN_WIDTH, screenshotWidth);
        double stageHeight = Scissors.MIN_HEIGHT + Scissors.TOOLBAR_HEIGHT + Math.max(screenshotHeight, Scissors.MIN_CANVAS_HEIGHT);

        if (stageWidth > maxWidth) {
            stageWidth = maxWidth;
        }

        if (stageHeight > maxHeight) {
            stageHeight = maxHeight;
        }

        Stage primaryStage = Scissors.getInstance().getStage();
        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);
    }

    public static MainWindowController getInstance() {
        return instance;
    }

}
