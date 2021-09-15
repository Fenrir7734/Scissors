package com.fenrir.scissors.controllers;

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

public class CaptureWindowController {
    public final static int BORDER_WIDTH = 2;

    private CaptureWindowController instance;

    private Stage captureWindow;
    private Scene scene;

    @FXML private StackPane backgroundHolder;
    @FXML private Canvas captureAreaCanvas;

    private final ScreenDetector detector;
    private AreaSelector selector;
    private final AnimationTimer screenDetection;

    private Screenshot screenshot;

    public CaptureWindowController() {
        instance = this;

        detector = new ScreenDetector();
        selector = new AreaSelector(detector.getCurrentScreen().getBounds());

        screenDetection = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(detector.isScreeChange()) {
                    setFocusScreen();
                    selector = new AreaSelector(detector.getCurrentScreen().getBounds());
                }
            }
        };
    }

    public void startCapturing() {
        screenDetection.start();

        captureWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        captureWindow.show();
        setFocusScreen();

        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> handleMousePressed(mouseEvent.getX(), mouseEvent.getY()));
        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> handleMouseDragged(mouseEvent.getX(), mouseEvent.getY()));
        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> handleMouseReleased(mouseEvent.getX(), mouseEvent.getY()));

        scene.setOnKeyPressed(keyEvent -> {
            screenDetection.stop();
            captureWindow.close();
        });
    }

    private void handleMousePressed(double x, double y) {
        screenDetection.stop();
        selector.setFirstPoint(x, y);
    }

    private void handleMouseDragged(double x, double y) {
        selector.setSecondPoint(x, y);
        drawOverlay();
        drawSelectedArea();
    }

    private void handleMouseReleased(double x, double y) {
        selector.setSecondPoint(x, y);
        Area selectedArea = selector.getArea();

        if(selectedArea.getWidth() > 0 && selectedArea.getWidth() > 0) {
            clearCanvas();
            captureWindow.close();
            MainWindowController controller = MainWindowController.getInstance();
            controller.processScreenshot(screenshot.cropScreenshot(selectedArea));
        } else {
            drawOverlay();
        }
    }

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

    private void drawOverlay() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();

        graphicsContext.clearRect(
                BORDER_WIDTH,
                BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (BORDER_WIDTH * 2)
        );

        graphicsContext.setFill(Color.rgb(0, 0, 0, Properties.getInstance().getOpacity() / 100.0));
        graphicsContext.fillRect(
                BORDER_WIDTH,
                BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (BORDER_WIDTH * 2)
        );
    }

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
                BORDER_WIDTH,
                BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (BORDER_WIDTH * 2)
        );
    }

    private void drawSelectedArea() {
        drawAreaBorder();
        Area selectedArea = selector.getArea();

        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();
        graphicsContext.clearRect(
                selectedArea.getRelativeStartX(),
                selectedArea.getRelativeStartY(),
                selectedArea.getWidth(),
                selectedArea.getHeight()
        );
    }

    private void drawAreaBorder() {
        Area selectedArea = selector.getArea();

        AreaSelector selector = new AreaSelector(detector.getCurrentScreen().getBounds());
        selector.setFirstPoint(
                (int) selectedArea.getRelativeStartX() - BORDER_WIDTH,
                (int) selectedArea.getRelativeStartY() - BORDER_WIDTH
        );
        selector.setSecondPoint(
                (int) selectedArea.getRelativeEndX() + BORDER_WIDTH,
                (int) selectedArea.getRelativeEndY() + BORDER_WIDTH
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
    }

    public void clearCanvas() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();

        graphicsContext.clearRect(
                0,
                0,
                graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight()
        );
    }

    public void setStage(Stage stage) {
        captureWindow = stage;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public CaptureWindowController getInstance() {
        return instance;
    }
}
