package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.ScreenDetector;
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
    @FXML private Canvas screenDisplay;
    @FXML private ToggleButton pencilToolButton;
    @FXML private ToggleButton markerToolButton;
    @FXML private ToggleButton blurToolButton;
    @FXML private ToggleButton arrowToolButton;
    @FXML private ToggleButton rectangleToolButton;
    @FXML private ToggleButton lineToolButton;
    @FXML private ToggleButton ellipseToolButton;
    @FXML private ToggleButton eraserToolButton;
    @FXML private ScrollPane canvasContainer;

    private CaptureWindowController captureWindowController;

    @FXML
    public void initialize() {
        screenNameField.setVisible(false);
        screenNameField.setManaged(false);
        toolbox.setVisible(false);
        toolbox.setManaged(false);
        screenDisplay.setVisible(false);
        screenDisplay.setManaged(false);
        canvasContainer.setVisible(false);
        canvasContainer.setManaged(false);

        canvasContainer.setFitToWidth(false);
        canvasContainer.setFitToHeight(false);

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

        canvasContainer.setPrefViewportWidth(maxWidth);
        canvasContainer.setPrefViewportHeight(maxHeight);

        double screenshotWidth = screenshot.getImage().getWidth();
        double screenshotHeight = screenshot.getImage().getHeight();

        if(screenshotWidth < Scissors.MIN_CANVAS_WIDTH) {
            canvasContainer.getContent()
                    .setTranslateX((Scissors.MIN_CANVAS_WIDTH - screenshotWidth) / 2);
        } else {
            canvasContainer.getContent()
                    .setTranslateX(0);
        }

        if(screenshotHeight < Scissors.MIN_CANVAS_HEIGHT) {
            canvasContainer.getContent()
                    .setTranslateY((Scissors.MIN_CANVAS_HEIGHT - screenshotHeight) / 2);
        } else {
            canvasContainer.getContent()
                    .setTranslateY(0);
        }

        screenDisplay.setWidth(screenshotWidth);
        screenDisplay.setHeight(screenshotHeight);
        screenDisplay.getGraphicsContext2D()
                .drawImage(screenshot.getImage(), 0, 0, screenshotWidth, screenshotHeight);

        double stageWidth = Math.max(Scissors.MIN_WIDTH, screenshotWidth);
        double stageHeight = Scissors.MIN_HEIGHT + Scissors.TOOLBAR_HEIGHT + Math.max(screenshotHeight, Scissors.MIN_CANVAS_HEIGHT);
        System.out.println(stageHeight);

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
        screenDisplay.setVisible(true);
        screenDisplay.setManaged(true);
        canvasContainer.setVisible(true);
        canvasContainer.setManaged(true);
    }

    public static MainWindowController getInstance() {
        return instance;
    }
}
