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
    private final int BORDER_WIDTH;

    private Stage captureWindow;
    private Scene scene;

    @FXML private StackPane backgroundHolder;
    @FXML private Canvas captureAreaCanvas;

    private final ScreenDetector detector;
    private AreaSelector selector;
    private final AnimationTimer screenDetection;

    private Screenshot screenshot;

    public CaptureWindowController() {
        BORDER_WIDTH = Properties.getInstance().getBorderWidth();

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
        captureWindow.setFullScreen(true);
        captureWindow.show();
        setFocusScreen();

        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> mousePressedEvent(mouseEvent.getX(), mouseEvent.getY()));
        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> mouseDraggedEvent(mouseEvent.getX(), mouseEvent.getY()));
        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> mouseReleasedEvent(mouseEvent.getX(), mouseEvent.getY()));

        scene.setOnKeyPressed(keyEvent -> {
            screenDetection.stop();
            captureWindow.close();
        });
    }

    private void mousePressedEvent(double x, double y) {
        screenDetection.stop();
        selector.setFirstPoint(x, y);
    }

    private void mouseDraggedEvent(double x, double y) {
        selector.setSecondPoint(x, y);
        drawOverlay();
        drawSelectedArea();
    }

    private void mouseReleasedEvent(double x, double y) {
        selector.setSecondPoint(x, y);
        Area selectedArea = selector.getArea();

        if(selectedArea.getWidth() > 0 && selectedArea.getWidth() > 0) {
            clearCanvas();
            captureWindow.close();
            MainWindowController.getInstance().drawScreenshotOnCanvas(screenshot.cropScreenshot(selectedArea));
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

        drawOverlay();
    }

    private void drawOverlay() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();

        graphicsContext.setFill(javafx.scene.paint.Color.rgb(5, 190, 112));
        graphicsContext.fillRect(
                graphicsContext.getCanvas().getLayoutX(),
                graphicsContext.getCanvas().getLayoutY(),
                graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight()
        );

        graphicsContext.clearRect(
                graphicsContext.getCanvas().getLayoutX() + BORDER_WIDTH,
                graphicsContext.getCanvas().getLayoutY() + BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (BORDER_WIDTH * 2)
        );

        graphicsContext.setFill(Color.rgb(0, 0, 0, Properties.getInstance().getOpacity() / 100.0));
        graphicsContext.fillRect(
                graphicsContext.getCanvas().getLayoutX() + BORDER_WIDTH,
                graphicsContext.getCanvas().getLayoutY() + BORDER_WIDTH,
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
                graphicsContext.getCanvas().getLayoutX(),
                graphicsContext.getCanvas().getLayoutY(),
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
}
