package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.Properties;
import com.fenrir.scissors.model.ScreenDetector;
import com.fenrir.scissors.model.area.Area;
import com.fenrir.scissors.model.area.AreaSelector;
import com.fenrir.scissors.model.screenshot.ScreenShooter;
import com.fenrir.scissors.model.screenshot.Screenshot;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;

/**
 * This class provides the ability to select and screenshot part of the screen.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 15, 2021
 */
public class ScreenCaptureWindowController {
    private ScreenCaptureWindowController instance;

    @FXML private StackPane backgroundHolder;
    @FXML private Canvas captureAreaCanvas;
    private Stage captureWindow;
    private Scene scene;

    private final ScreenDetector detector;
    private final AnimationTimer screenDetection;
    private AreaSelector selector;
    private Screenshot screenshot;

    /**
     * Creates new instance of Screen Capture Window Controller. Does not start capturing process, it must be done later,
     * after creating instance of this class by calling {@link #startCapturing()} method.
     */
    public ScreenCaptureWindowController() {
        instance = this;

        detector = new ScreenDetector();
        selector = new AreaSelector(detector.getCurrentScreen().getBounds());

        screenDetection = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (detector.isScreeChange()) {
                    setFocusScreen();
                    selector = new AreaSelector(detector.getCurrentScreen().getBounds());
                }
            }
        };
    }

    /**
     * Starts capturing process.
     *
     * Starts detection of screen on which the mouse pointer is currently located, sets stage configuration and sets
     * events handlers.
     */
    public void startCapturing() {
        screenDetection.start();

        captureWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        captureWindow.show();
        setFocusScreen();

        captureAreaCanvas.addEventHandler(
                MouseEvent.MOUSE_PRESSED, mouseEvent -> handleMousePressed(mouseEvent.getX(), mouseEvent.getY())
        );
        captureAreaCanvas.addEventHandler(
                MouseEvent.MOUSE_DRAGGED, mouseEvent -> handleMouseDragged(mouseEvent.getX(), mouseEvent.getY())
        );
        captureAreaCanvas.addEventHandler(
                MouseEvent.MOUSE_RELEASED, mouseEvent -> handleMouseReleased(mouseEvent.getX(), mouseEvent.getY())
        );

        scene.setOnKeyPressed(keyEvent -> {
            screenDetection.stop();
            captureWindow.close();
            Scissors.getInstance().getStage().setIconified(false);
        });
    }

    /**
     * Handle mouse pressed event.
     *
     * When pressed event occurs, screen detection is stopped and the first point of the selected area for the
     * screenshot is set.
     *
     * @param x Horizontal position of the mouse event
     * @param y Vertical position of the mouse event
     */
    private void handleMousePressed(double x, double y) {
        screenDetection.stop();
        selector.setFirstPoint(x, y);
    }

    /**
     * Handle mouse dragged event.
     *
     * When dragged event occurs, a second point of the selected area for the screenshot is set. Based on this point and
     * point set in {@link #handleMousePressed(double, double)} method, the selected area is drawn on screen.
     *
     * @param x Horizontal position of the mouse event
     * @param y Vertical position of the mouse event
     */
    private void handleMouseDragged(double x, double y) {
        selector.setSecondPoint(x, y);
        drawOverlay();
        drawSelectedArea();
    }

    /**
     * Handle mouse released event.
     *
     * When released event occurs and selected area field is greater than 0, {@link #captureWindow} stage is closed and
     * screenshot is cropped according to selected area.
     *
     * @param x Horizontal position of the mouse event
     * @param y Vertical position of the mouse event
     */
    private void handleMouseReleased(double x, double y) {
        selector.setSecondPoint(x, y);
        Area selectedArea = selector.getArea();

        if (selectedArea.getWidth() > 0 && selectedArea.getWidth() > 0) {
            clearCanvas();
            captureWindow.close();
            MainWindowController controller = MainWindowController.getInstance();
            controller.processScreenshot(screenshot.cropScreenshot(selectedArea));
        } else {
            drawOverlay();
        }
    }

    /**
     * Sets the location of the stage to the appropriate screen according to the mouse pointer location.
     */
    private void setFocusScreen() {
        Point location = detector.getCurrentScreenLocation();
        int screenWidth = detector.getCurrentScreenWidth();
        int screenHeight = detector.getCurrentScreenHeight();

        screenshot = new Screenshot(ScreenShooter.takeScreenshot(detector.getCurrentScreen()));

        backgroundHolder.setBackground(screenshot.getScreenshotAsBackground());

        captureAreaCanvas.setWidth(screenWidth);
        captureAreaCanvas.setHeight(screenHeight);
        captureAreaCanvas.setLayoutX(location.x);
        captureAreaCanvas.setLayoutY(location.y);

        captureWindow.setWidth(screenWidth);
        captureWindow.setHeight(screenHeight);
        captureWindow.setX(location.x);
        captureWindow.setY(location.y);

        captureWindow.setFullScreen(false);
        captureWindow.setFullScreen(true);

        drawScreenBorder();
        drawOverlay();
    }

    /**
     * Draws an overlay over entire screen excluding screen borders.
     */
    private void drawOverlay() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();

        graphicsContext.clearRect(
                Properties.BORDER_WIDTH,
                Properties.BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (Properties.BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (Properties.BORDER_WIDTH * 2)
        );

        graphicsContext.setFill(Color.rgb(0, 0, 0, Properties.getInstance().getOpacity() / 100.0));
        graphicsContext.fillRect(
                Properties.BORDER_WIDTH,
                Properties.BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (Properties.BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (Properties.BORDER_WIDTH * 2)
        );
    }

    /**
     * Draws boundaries surrounding entire screen.
     */
    private void drawScreenBorder() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();

        graphicsContext.setFill(Color.rgb(5, 190, 112));
        graphicsContext.fillRect(
                0,
                0,
                graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight()
        );

        graphicsContext.clearRect(
                Properties.BORDER_WIDTH,
                Properties.BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (Properties.BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (Properties.BORDER_WIDTH * 2)
        );
    }

    /**
     * Draws selected area surrounded by boundaries.
     */
    private void drawSelectedArea() {
        Area selectedArea = selector.getArea();

        AreaSelector selector = new AreaSelector(detector.getCurrentScreen().getBounds());
        selector.setFirstPoint(
                (int) selectedArea.getRelativeStartX() - Properties.BORDER_WIDTH,
                (int) selectedArea.getRelativeStartY() - Properties.BORDER_WIDTH
        );
        selector.setSecondPoint(
                (int) selectedArea.getRelativeEndX() + Properties.BORDER_WIDTH,
                (int) selectedArea.getRelativeEndY() + Properties.BORDER_WIDTH
        );

        Area borderArea = selector.getArea();

        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.rgb(5, 190, 112));

        graphicsContext.fillRect(
                borderArea.getRelativeStartX(),
                borderArea.getRelativeStartY(),
                borderArea.getWidth(),
                borderArea.getHeight()
        );

        graphicsContext.clearRect(
                selectedArea.getRelativeStartX(),
                selectedArea.getRelativeStartY(),
                selectedArea.getWidth(),
                selectedArea.getHeight()
        );
    }

    /**
     * Clears entire canvas.
     */
    public void clearCanvas() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();

        graphicsContext.clearRect(
                0,
                0,
                graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight()
        );
    }

    /**
     * Sets the stage used by this controller.
     *
     * @param stage Stage used by this controller.
     */
    public void setStage(Stage stage) {
        captureWindow = stage;
    }

    /**
     * Sets the scene used by this controller.
     *
     * @param scene Scene used by this controller.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Gets the instance of this controller.
     *
     * @return  Instance of this controller.
     */
    public ScreenCaptureWindowController getInstance() {
        return instance;
    }
}
