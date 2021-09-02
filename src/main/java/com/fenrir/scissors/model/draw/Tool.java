package com.fenrir.scissors.model.draw;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class Tool {

    protected Canvas canvas;
    protected StackPane canvasContainer;

    private final EventHandler<MouseEvent> mousePressedHandler = this::handleMousePressed;
    private final EventHandler<MouseEvent> mouseDraggedHandler = this::handleMouseDragged;
    private final EventHandler<MouseEvent> mouseReleasedHandler = this::handleMouseReleased;

    public Tool(Canvas canvas, StackPane canvasContainer) {
        this.canvas = canvas;
        this.canvasContainer = canvasContainer;
    }

    public void enableTool() {
        initTool();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
    }

    public void disableTool() {
        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
    }

    abstract protected void handleMousePressed(MouseEvent event);

    abstract protected void handleMouseDragged(MouseEvent event);

    abstract protected void handleMouseReleased(MouseEvent event);

    abstract protected void initTool();

}
