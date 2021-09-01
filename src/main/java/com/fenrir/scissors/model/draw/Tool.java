package com.fenrir.scissors.model.draw;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class Tool {

    protected Canvas canvas;
    protected StackPane canvasContainer;

    public Tool(Canvas canvas, StackPane canvasContainer) {
        this.canvas = canvas;
        this.canvasContainer = canvasContainer;

        initTool();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mouseEvent -> mousePressedEvent(mouseEvent.getX(), mouseEvent.getY()));
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseEvent -> mouseDraggedEvent(mouseEvent.getX(), mouseEvent.getY()));
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseEvent -> mouseReleasedEvent(mouseEvent.getX(), mouseEvent.getY()));
    }

    abstract protected void mousePressedEvent(double x, double y);

    abstract protected void mouseDraggedEvent(double x, double y);

    abstract protected void mouseReleasedEvent(double x, double y);

    abstract protected void initTool();

}
