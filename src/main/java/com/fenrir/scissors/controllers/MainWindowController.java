package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.ScreenDetector;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class MainWindowController {
    private static MainWindowController instance;

    private final double MIN_CANVAS_WIDTH = 600;
    private final double MIN_CANVAS_HEIGHT = 300;

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

    private CaptureWindowController captureWindowController;

    @FXML
    public void initialize() {
        screenNameField.setVisible(false);
        screenNameField.setManaged(false);
        toolbox.setVisible(false);
        toolbox.setManaged(false);
        screenDisplay.setVisible(false);
        screenDisplay.setManaged(false);
        instance = this;
    }

    @FXML
    public void captureScreen(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/CaptureWindow.fxml")));
            StackPane pane = fxmlLoader.load();
            captureWindowController = fxmlLoader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            captureWindowController.setStage(stage);
            captureWindowController.startCapturing();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void drawImageOnCanvas(Image image) {
        System.out.println("this");
        Rectangle2D stageScreenBounds = new ScreenDetector()
                .detectStageScreens(Scissors.getInstance().getStage())
                .get(0)
                .getVisualBounds();

        double width = image.getWidth();
        double height = image.getHeight();

        int maxWidth = (int) stageScreenBounds.getWidth();
        int maxHeight = (int) stageScreenBounds.getHeight() - 154;

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);

        if(image.getWidth() < MIN_CANVAS_WIDTH) {
            width = MIN_CANVAS_WIDTH;
        } else if(image.getWidth() > maxWidth) {
            width = maxWidth;
            imageView.setFitWidth(width);
        }

        if(image.getHeight() < MIN_CANVAS_HEIGHT) {
            height = MIN_CANVAS_HEIGHT;
        } else if(image.getHeight() > maxHeight) {
            height = maxHeight;
            System.out.println("tutaj");
            imageView.setFitHeight(height);
        }

        screenDisplay.setHeight(height);
        screenDisplay.setWidth(width);
        Scissors.getInstance().setSize(width, height + Scissors.MIN_HEIGHT + 75);
        System.out.println(maxHeight);
        System.out.println(image.getHeight());
        System.out.println(imageView.getImage().getHeight());

        GraphicsContext gc = screenDisplay.getGraphicsContext2D();
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("/home/fenrir/screenshot.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        WritableImage writableImage = imageView.snapshot(null, null);

        gc.drawImage(writableImage, 0, 0, writableImage.getWidth(), writableImage.getHeight());
    }

    public static MainWindowController getInstance() {
        return instance;
    }
}
