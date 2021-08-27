package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.Area;
import com.fenrir.scissors.model.ScreenDetector;
import com.fenrir.scissors.model.ScreenShotUtils;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
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
import javafx.scene.shape.Rectangle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;


public class CaptureWindowController {
    private final int BORDER_WIDTH = 3;

    private final ScreenDetector screenDetector = new ScreenDetector();
    private Area selectedArea;

    private final Stage captureWindow;
    private final StackPane backgroundHolder;
    private final Canvas captureAreaCanvas;

    private AnimationTimer screenDetection;

    public CaptureWindowController() {
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
        redrawOverlay();
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
            selectedArea = new Area((int) mouseEvent.getX(), (int) mouseEvent.getY());
        });

        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> {
            selectedArea.updateEndPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
            redrawOverlay();
            drawSelectedArea();
        });

        captureAreaCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> {
            selectedArea.updateEndPoint((int) mouseEvent.getX(), (int) mouseEvent.getY());
            redrawOverlay();
            drawSelectedArea();
            /*
            Group group = new Group();
            selectedAreaBorder = new Rectangle(selectedArea.getStartX(), selectedArea.getStartY(), selectedArea.getWidth(), selectedArea.getHeight());
            selectedAreaBorder.setFill(Color.TRANSPARENT);
            selectedAreaBorder.setStroke(Color.BLUE);
            backgroundHolder.getChildren().add(selectedAreaBorder);

             */

            //backgroundHolder.getChildren().add(selectedAreaBorder);
            //captureWindow.close();
        });
    }

    private void changeFocusedScreen() {
        Point location = screenDetector.getCurrentScreenLocation();
        int screenWidth = screenDetector.getCurrentScreenWidth();
        int screenHeight = screenDetector.getCurrentScreenHeight();

        backgroundHolder.setBackground(screenDetector.getCurrentWindowScreenshot());

        captureAreaCanvas.setWidth(screenWidth);
        captureAreaCanvas.setHeight(screenHeight);

        captureWindow.setWidth(screenWidth);
        captureWindow.setHeight(screenHeight);

        captureWindow.setX(location.x);
        captureWindow.setY(location.y);

        captureWindow.setFullScreen(false);
        captureWindow.setFullScreen(true);
    }

    private void redrawOverlay() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();
        graphicsContext.clearRect(
                graphicsContext.getCanvas().getLayoutX(),
                graphicsContext.getCanvas().getLayoutY(),
                graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight()
        );
        graphicsContext.setFill(Color.rgb(0, 0, 0, 0.3));
        graphicsContext.fillRect(
                graphicsContext.getCanvas().getLayoutX(),
                graphicsContext.getCanvas().getLayoutY(),
                graphicsContext.getCanvas().getWidth(),
                graphicsContext.getCanvas().getHeight()
        );
    }

    private void drawSelectedArea() {
        drawAreaBorder();

        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();
        graphicsContext.clearRect(
                selectedArea.getStartX(),
                selectedArea.getStartY(),
                selectedArea.getWidth(),
                selectedArea.getHeight()
        );
    }

    private void drawAreaBorder() {
        GraphicsContext graphicsContext = captureAreaCanvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.rgb(5, 190, 112));

        Area borderArea = new Area();

        if(selectedArea.getStartX() < selectedArea.getEndX()) {
            borderArea.setStartX(selectedArea.getStartX() - BORDER_WIDTH);
            borderArea.setEndX(selectedArea.getEndX() + BORDER_WIDTH);
        } else {
            borderArea.setStartX(selectedArea.getStartX() + BORDER_WIDTH);
            borderArea.setEndX(selectedArea.getEndX() - BORDER_WIDTH);
        }

        if(selectedArea.getStartY() < selectedArea.getEndY()) {
            borderArea.setStartY(selectedArea.getStartY() - BORDER_WIDTH);
            borderArea.setEndY(selectedArea.getEndY() + BORDER_WIDTH);
        } else {
            borderArea.setStartY(selectedArea.getStartY() + BORDER_WIDTH);
            borderArea.setEndY(selectedArea.getEndY() - BORDER_WIDTH);
        }

        graphicsContext.fillRect(
                borderArea.getStartX(),
                borderArea.getStartY(),
                borderArea.getWidth(),
                borderArea.getHeight()
        );
    }
}
