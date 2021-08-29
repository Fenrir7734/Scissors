package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.Properties;
import com.fenrir.scissors.model.area.Area;
import com.fenrir.scissors.model.ScreenDetector;
import com.fenrir.scissors.model.ScreenShotUtils;
import com.fenrir.scissors.model.area.AreaSelector;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;


public class CaptureWindowController {
    private final int BORDER_WIDTH;

    private final ScreenDetector screenDetector = new ScreenDetector();
    private AreaSelector areaSelector;

    private final Stage captureWindow;
    private final StackPane backgroundHolder;
    private final Canvas captureAreaCanvas;

    private AnimationTimer screenDetection;

    public CaptureWindowController() {
        BORDER_WIDTH = Properties.getInstance().getBorderWidth();

        captureWindow = new Stage();
        StackPane root = new StackPane();
        backgroundHolder = new StackPane();
        captureAreaCanvas = new Canvas();

        backgroundHolder.getChildren().add(captureAreaCanvas);
        root.getChildren().add(backgroundHolder);

        Scene scene = new Scene(
                root,
                screenDetector.getCurrentScreenWidth(),
                screenDetector.getCurrentScreenHeight()
        );

        Point screenLocation = screenDetector.getCurrentScreenLocation();

        captureWindow.setScene(scene);
        captureWindow.setX(screenLocation.x);
        captureWindow.setY(screenLocation.y);
        captureWindow.setFullScreen(true);
        captureWindow.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        changeFocusedScreen();
        drawOverlay();
        initScreenDetection();
        selectAreaEvent();

        captureWindow.show();

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                screenDetection.stop();
                captureWindow.close();
            }
        });
    }

    private void initScreenDetection() {
        screenDetection = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(screenDetector.isScreeChange()) {
                    changeFocusedScreen();
                }
            }
        };
        screenDetection.start();
    }

    private void selectAreaEvent() {
        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
            screenDetection.stop();
            areaSelector = new AreaSelector(screenDetector.getCurrentScreen().getBounds());
            areaSelector.setFirstPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
        });

        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            areaSelector.setSecondPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
            drawOverlay();
            drawSelectedArea();
        });

        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            areaSelector.setSecondPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
            Area selectedArea = areaSelector.getArea();

            if(selectedArea.getWidth() > 0 && selectedArea.getWidth() > 0) {
                WritableImage image = ScreenShotUtils.takeScreenshot(selectedArea);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("/home/fenrir/screenshot.png"));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                captureWindow.close();
            } else {
                drawOverlay();
            }
        });
    }

    private void changeFocusedScreen() {
        Point location = screenDetector.getCurrentScreenLocation();
        int screenWidth = screenDetector.getCurrentScreenWidth();
        int screenHeight = screenDetector.getCurrentScreenHeight();

        backgroundHolder.setBackground(
                ScreenShotUtils.getCurrentWindowScreenshotAsBackground(screenDetector.getCurrentScreen())
        );

        captureAreaCanvas.setWidth(screenWidth);
        captureAreaCanvas.setHeight(screenHeight);

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

        graphicsContext.setFill(Color.rgb(5, 190, 112));
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

        graphicsContext.setFill(Color.rgb(0, 0, 0, 0.5));
        graphicsContext.fillRect(
                graphicsContext.getCanvas().getLayoutX() + BORDER_WIDTH,
                graphicsContext.getCanvas().getLayoutY() + BORDER_WIDTH,
                graphicsContext.getCanvas().getWidth() - (BORDER_WIDTH * 2),
                graphicsContext.getCanvas().getHeight() - (BORDER_WIDTH * 2)
        );
    }

    private void drawSelectedArea() {
        drawAreaBorder();
        Area selectedArea = areaSelector.getArea();

        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();
        graphicsContext.clearRect(
                selectedArea.getRelativeStartX(),
                selectedArea.getRelativeStartY(),
                selectedArea.getWidth(),
                selectedArea.getHeight()
        );
    }

    private void drawAreaBorder() {
        Area selectedArea = areaSelector.getArea();

        AreaSelector selector = new AreaSelector(screenDetector.getCurrentScreen().getBounds());
        selector.setFirstPoint(
                selectedArea.getRelativeStartX() - BORDER_WIDTH,
                selectedArea.getRelativeStartY() - BORDER_WIDTH
        );
        selector.setSecondPoint(
                selectedArea.getRelativeEndX() + BORDER_WIDTH,
                selectedArea.getRelativeEndY() + BORDER_WIDTH
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

}
