package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.Properties;
import com.fenrir.scissors.model.SavingUtils;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

    @FXML private AnchorPane mainWindowPane;
    @FXML private VBox mainWindowVBox;
    @FXML private ButtonBar mainButtonBar;
    @FXML private Button captureButton;
    @FXML private Button settingsButton;
    @FXML private Button copyButton;
    @FXML private Button saveButton;
    @FXML private MenuButton saveAsMenuButton;
    @FXML private HBox screenNameContainer;
    @FXML private TextField screenNameField;
    @FXML private HBox toolbox;
    @FXML private ToggleGroup toolboxGroup;
    @FXML private Canvas screenshotCanvas;
    @FXML private ToggleButton pencilToolButton;
    @FXML private ToggleButton markerToolButton;
    @FXML private ToggleButton blurToolButton;
    @FXML private ToggleButton arrowToolButton;
    @FXML private ToggleButton rectangleToolButton;
    @FXML private ToggleButton lineToolButton;
    @FXML private ToggleButton ellipseToolButton;
    @FXML private ToggleButton eraserToolButton;
    @FXML private ScrollPane screenshotContainer;
    @FXML private StackPane canvasContainer;
    private List<MenuItem> favoriteItems;

    private final Properties properties = Properties.getInstance();
    private Tool currentTool;
    private Screenshot screenshot;

    @FXML
    public void initialize() {
        screenNameField.setVisible(false);
        screenNameField.setManaged(false);
        toolbox.setVisible(false);
        toolbox.setManaged(false);
        screenshotCanvas.setVisible(false);
        screenshotCanvas.setManaged(false);
        screenshotContainer.setVisible(false);
        screenshotContainer.setManaged(false);

        screenshotContainer.setFitToWidth(false);
        screenshotContainer.setFitToHeight(false);

        currentTool = new PencilTool(screenshotCanvas, canvasContainer);

        favoriteItems = new ArrayList<>();
        properties.getFavoriteList().forEach(f -> {
            MenuItem item = new MenuItem();
            item.setText(f.name());
            item.setId(f.name());
            item.setOnAction(e -> saveToFavorite(f.path()));
            favoriteItems.add(item);
            saveAsMenuButton.getItems().add(item);
        });

        instance = this;

        screenshotCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            screenshot = screenshot.canvasSnapshot(screenshotCanvas);
            if(properties.isSaveToClipboard()) {
                SavingUtils.copyToClipBoard(screenshot.getImage());
            }
        });

        screenNameField.textProperty().addListener(((observable, oldValue, newValue) -> screenshot.setName(newValue)));
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
            refreshFavorite();
        } catch (IOException e) {
            logger.error("Error opening the settings window");
            logger.error(e.getMessage());
        }
    }

    @FXML
    private void saveToDefault() {
        try {
            SavingUtils.saveTo(screenshot.getImage(), properties.getDefaultPath(), screenshot.getName());
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
                SavingUtils.saveTo(screenshot.getImage(), selectedFile);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }

    @FXML
    private void saveToImgur() {

    }

    private void saveToFavorite(Path path) {
        try {
            SavingUtils.saveTo(screenshot.getImage(), path, screenshot.getName());
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }

    private void refreshFavorite() {
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

    @FXML
    public void captureScreen(ActionEvent event) {
        Scissors.getInstance().getStage().setIconified(true);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/CaptureWindow.fxml")));
            StackPane pane = fxmlLoader.load();
            CaptureWindowController captureWindowController = fxmlLoader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            captureWindowController.setStage(stage);
            captureWindowController.setScene(scene);
            captureWindowController.startCapturing();
        } catch (IOException e) {
            logger.error("Error opening the capture window");
            logger.error(e.getMessage());
        }
    }

    public void drawScreenshotOnCanvas() {
        screenNameField.setText(screenshot.getName());

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
        showToolbar();
        primaryStage.setIconified(false);
    }

    private void showToolbar() {
        copyButton.setDisable(false);
        saveAsMenuButton.setDisable(false);
        saveButton.setDisable(false);
        screenNameField.setVisible(true);
        screenNameField.setManaged(true);
        toolbox.setVisible(true);
        toolbox.setManaged(true);
        screenshotCanvas.setVisible(true);
        screenshotCanvas.setManaged(true);
        screenshotContainer.setVisible(true);
        screenshotContainer.setManaged(true);
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

    public void setScreenshot(Screenshot screenshot) {
        this.screenshot = screenshot;
    }

    public static MainWindowController getInstance() {
        return instance;
    }

}
