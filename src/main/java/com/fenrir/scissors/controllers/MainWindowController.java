package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.ScreenDetector;
import com.fenrir.scissors.model.draw.Tool;
import com.fenrir.scissors.model.draw.drawtools.EraserTool;
import com.fenrir.scissors.model.draw.drawtools.MarkerTool;
import com.fenrir.scissors.model.draw.shapetools.DrawLine;
import com.fenrir.scissors.model.draw.drawtools.PencilTool;
import com.fenrir.scissors.model.screenshot.Screenshot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class MainWindowController {
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

    private CaptureWindowController captureWindowController;

    private Tool currentTool;

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

        instance = this;
    }

    @FXML
    public void captureScreen(ActionEvent event) {
        Scissors.getInstance().getStage().setIconified(true);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/CaptureWindow.fxml")));
            StackPane pane = fxmlLoader.load();
            captureWindowController = fxmlLoader.getController();
            Stage stage = new Stage();
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            captureWindowController.setStage(stage);
            captureWindowController.setScene(scene);
            captureWindowController.startCapturing();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void drawScreenshotOnCanvas(Screenshot screenshot) {
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

        if(stageWidth > maxWidth) {
            stageWidth = maxWidth;
        }

        if(stageHeight > maxHeight) {
            stageHeight = maxHeight;
        }

        Stage primaryStage = Scissors.getInstance().getStage();
        primaryStage.setWidth(stageWidth);
        primaryStage.setHeight(stageHeight);
        showToolbar();
        primaryStage.setIconified(false);
    }

    private void showToolbar() {
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
    private void blurTool() {

    }

    @FXML
    private void lineTool() {
        currentTool.disableTool();
        //currentTool = new DrawLine(screenshotCanvas, canvasContainer);
        currentTool.enableTool();
    }

    @FXML
    private void rectangleTool() {

    }

    @FXML
    private void ellipseTool() {

    }

    @FXML
    private void arrowTool() {

    }


    public static MainWindowController getInstance() {
        return instance;
    }
}
