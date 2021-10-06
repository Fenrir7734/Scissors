package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.uploader.ImgurUploader;
import com.fenrir.scissors.model.Properties;
import com.fenrir.scissors.model.ScreenshotSaver;
import com.fenrir.scissors.model.ScreenDetector;
import com.fenrir.scissors.model.draw.Tool;
import com.fenrir.scissors.model.draw.drawtools.EraserTool;
import com.fenrir.scissors.model.draw.drawtools.MarkerTool;
import com.fenrir.scissors.model.draw.drawtools.PencilTool;
import com.fenrir.scissors.model.draw.shapetools.ArrowTool;
import com.fenrir.scissors.model.draw.shapetools.OvalTool;
import com.fenrir.scissors.model.draw.shapetools.LineTool;
import com.fenrir.scissors.model.draw.shapetools.RectangleTool;
import com.fenrir.scissors.model.screenshot.Screenshot;
import com.fenrir.scissors.model.uploader.WebException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

/**
 * This class controls main window of the program.
 *
 * @author Fenrir7734
 * @version v1.0.1 September 17, 2021
 */
public class MainWindowController {
    private final Logger logger = LoggerFactory.getLogger(MainWindowController.class);
    private final Properties properties = Properties.getInstance();

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
    private Screenshot screenshot;

    /**
     * Initializes instance of this class.
     */
    @FXML
    public void initialize() {
        ControllerMediatorImpl.getInstance().registerMainWindowController(this);

        hide();
        disableSavingButtons();
        populateFavorites();

        screenshotContainer.setFitToWidth(false);
        screenshotContainer.setFitToHeight(false);

        screenshotCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            screenshot = new Screenshot(screenshotCanvas, screenshot.getName());
            if(properties.isSaveToClipboard()) {
                ScreenshotSaver.copyToClipBoard(screenshot.getImage());
            }
        });
        screenNameField.textProperty().addListener(((observable, oldValue, newValue) -> screenshot.setName(newValue)));

        currentTool = new PencilTool(screenshotCanvas, canvasContainer);
    }

    /**
     * Handle button action to start selecting screen area to screenshot.
     */
    @FXML
    public void captureScreen() {
        Scissors.getInstance().getStage().setIconified(true);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("/CaptureWindow.fxml"))
            );
            StackPane pane = fxmlLoader.load();
            ScreenCaptureWindowController captureWindowController = fxmlLoader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            captureWindowController.setStage(stage);
            captureWindowController.setScene(scene);


            // Screen capture works that way that it screenshot entire screen and them crop this screenshot according to
            // the selected area. So we run screen capture in different thread so that we can wait until primary
            // stage is iconified before screenshot of entire screen is taken. This way GUI of a program isn't visible
            // on screenshot.
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

    /**
     * Handle button action to open Settings Window GUI.
     */
    @FXML
    private void openSettings() {
        try {
            Parent parent = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/SettingsWindow.fxml"))
            );
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

    /**
     * Handle button action to copy screenshot to clipboard.
     */
    @FXML
    private void copyToClipboard() {
        ScreenshotSaver.copyToClipBoard(screenshot.getImage());
    }

    /**
     * Handle button action to save screenshot to default destination directory.
     */
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

    /**
     * Handle button action to select destination directory on local machine and save screenshot to selected directory.
     */
    @FXML
    private void saveToLocal() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("."));
            File selectedFile = fileChooser.showSaveDialog(Scissors.getInstance().getStage());

            if (selectedFile != null) {
                ScreenshotSaver.saveTo(screenshot.getImage(), selectedFile);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }

    /**
     * Handle button action to upload screenshot to Imgur.
     */
    @FXML
    private void saveToImgur() {
        new Thread(() -> {
            try {
                String url = ImgurUploader.upload(screenshot.getImage());
                Platform.runLater(() -> {
                    ScreenshotSaver.copyToClipBoard(url);
                    showUploadNotification();
                });
            } catch (IOException | WebException e) {
                logger.error(e.getMessage());
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Uploading to imgur failed.\n" + e.getMessage());
                    alert.showAndWait();
                });
            }
        }).start();
    }

    /**
     * Handle button action to save screenshot to one of the selected favorite locations.
     *
     * @param path  Path to directory.
     *
     * @see com.fenrir.scissors.model.Properties.Favorite
     */
    private void saveToFavorite(Path path) {
        try {
            ScreenshotSaver.saveTo(screenshot.getImage(), path, screenshot.getName());
        } catch (IOException e) {
            logger.error(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Saving failed.");
            alert.showAndWait();
        }
    }

    /**
     * Handle button to enable pencil tool.
     *
     * @see PencilTool
     */
    @FXML
    private void pencilTool() {
        changeTool(new PencilTool(screenshotCanvas, canvasContainer));
    }

    /**
     * Handle button to enable marker tool.
     *
     * @see MarkerTool
     */
    @FXML
    private void markerTool() {
        changeTool(new MarkerTool(screenshotCanvas, canvasContainer));
    }

    /**
     * Handle button to enable eraser tool.
     *
     * @see EraserTool
     */
    @FXML
    private void eraserTool() {
        changeTool(new EraserTool(screenshotCanvas, canvasContainer));
    }

    /**
     * Handle button to enable line tool.
     *
     * @see LineTool
     */
    @FXML
    private void lineTool() {
        changeTool(new LineTool(screenshotCanvas, canvasContainer));
    }

    /**
     * Handle button to enable rectangle tool.
     *
     * @see RectangleTool
     */
    @FXML
    private void rectangleTool() {
        changeTool(new RectangleTool(screenshotCanvas, canvasContainer));
    }

    /**
     * Handle button to enable ellipse tool.
     *
     * @see OvalTool
     */
    @FXML
    private void ellipseTool() {
        changeTool(new OvalTool(screenshotCanvas, canvasContainer));
    }

    /**
     * Handle button to enable arrow tool.
     *
     * @see ArrowTool
     */
    @FXML
    private void arrowTool() {
        changeTool(new ArrowTool(screenshotCanvas, canvasContainer));
    }

    private void changeTool(Tool tool) {
        currentTool.disableTool();
        currentTool = tool;
        currentTool.enableTool();
    }

    /**
     *  Process the {@link Screenshot Screenshot} class instance.
     *
     *  If appropriate flags are set in program settings image from Screenshot class instance will be copied to
     *  clipboard and/or save to default directory. Moreover, image is drawn on canvas for editing.
     *
     * @param screenshot    Screenshot to process.
     */
    public void receiveScreenshot(Screenshot screenshot) {
        this.screenshot = screenshot;

        setComponentsDimensions();
        drawScreenshot();

        screenNameField.setText(screenshot.getName());
        Scissors.getInstance().getStage().setIconified(false);

        if(!isToolbar) {
            show();
            enableSavingButtons();
        }

        if(properties.isSaveToClipboard()) {
            copyToClipboard();
        }

        if(properties.isSaveToDefault()) {
            saveToDefault();
        }
    }

    /**
     * Hides GUI elements that aren't needed before screenshot is taken.
     */
    private void hide() {
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

    /**
     * Shows all primary stage GUI elements which may have been hidden.
     */
    private void show() {
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

    /**
     * Shows notification informing that the screenshot was successfully uploaded to Imgur.
     */
    private void showUploadNotification() {
        Notifications.create()
                .title("Scissors")
                .text("URL copied to clipboard")
                .darkStyle()
                .show();
    }

    /**
     * Control that displayed Favorite items placed in MenuButton are the same as these stored in properties file.
     *
     * @see com.fenrir.scissors.model.Properties.Favorite
     */
    public void receiveFavorites() {
        favoriteItems.clear();
        saveAsMenuButton.getItems().clear();

        populateFavorites();
    }

    /**
     * Creates MenuItems based on favorite list and add them to MenuButton for quick access to favorite saving
     * locations.
     *
     * @see com.fenrir.scissors.model.Properties.Favorite
     */
    private void populateFavorites() {
        favoriteItems = new ArrayList<>();
        List<Properties.Favorite> favoriteList = properties.getFavoriteList();

        for (Properties.Favorite favorite: favoriteList) {
            MenuItem item = createFavoriteItem(favorite);
            favoriteItems.add(item);
            saveAsMenuButton.getItems().add(item);
        }
    }

    private MenuItem createFavoriteItem(Properties.Favorite favorite) {
        MenuItem item = new MenuItem();
        item.setText(favorite.name());
        item.setId(favorite.name());
        item.setOnAction(e -> saveToFavorite(favorite.path()));
        return item;
    }

    private void setComponentsDimensions() {
        Rectangle2D currentScreenBounds = getCurrentScreenBounds();
        setStageDimensions(currentScreenBounds);
        setScreenshotContainerDimensions(currentScreenBounds);
        setCanvasDimensions();
    }

    private Rectangle2D getCurrentScreenBounds() {
        return new ScreenDetector()
                .detectStageScreens(Scissors.getInstance().getStage())
                .get(0)
                .getVisualBounds();
    }

    private void setScreenshotContainerDimensions(Rectangle2D bounds) {
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        screenshotContainer.setPrefViewportWidth(width);
        screenshotContainer.setPrefViewportHeight(height);
    }

    private void setCanvasDimensions() {
        double width = screenshot.getImage().getWidth();
        double height = screenshot.getImage().getHeight();

        screenshotCanvas.setWidth(width);
        screenshotCanvas.setHeight(height);
    }

    private void setStageDimensions(Rectangle2D bounds) {
        double width = calculateStageWidth(bounds);
        double height = calculateStageHeight(bounds);

        Stage stage = Scissors.getInstance().getStage();
        stage.setWidth(width);
        stage.setHeight(height);
    }

    private double calculateStageWidth(Rectangle2D bounds) {
        double maxWidth = bounds.getWidth();
        double screenshotWidth = screenshot.getImage().getWidth();
        double stageWidth = Math.max(Scissors.MIN_WIDTH, screenshotWidth);

        if(stageWidth > maxWidth) {
            stageWidth = maxWidth;
        }

        return stageWidth;
    }

    private double calculateStageHeight(Rectangle2D bounds) {
        double maxHeight = bounds.getHeight();
        double screenshotHeight = screenshot.getImage().getHeight();
        double stageHeight = Scissors.CONTROL_BUTTONS_BAR_HEIGHT + Scissors.TOOLBAR_HEIGHT +
                Math.max(screenshotHeight, Scissors.MIN_CANVAS_HEIGHT);

        if (stageHeight > maxHeight) {
            stageHeight = maxHeight;
        }

        return stageHeight;
    }

    private void drawScreenshot() {
        double width = screenshot.getImage().getWidth();
        double height = screenshot.getImage().getHeight();

        GraphicsContext gc = screenshotCanvas.getGraphicsContext2D();
        gc.drawImage(screenshot.getImage(), 0, 0, width, height);
    }
}
