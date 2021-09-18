package com.fenrir.scissors.model.draw;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Top-level class for all drawing tools.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 18, 2021
 */
public abstract class Tool {
    protected Canvas canvas;
    protected StackPane canvasContainer;

    private final EventHandler<MouseEvent> mousePressedHandler = this::handleMousePressed;
    private final EventHandler<MouseEvent> mouseDraggedHandler = this::handleMouseDragged;
    private final EventHandler<MouseEvent> mouseReleasedHandler = this::handleMouseReleased;

    /**
     * Creates a new Tool.
     *
     * @param canvas            Canvas on which tool will be drawing.
     * @param canvasContainer   Parent of the given canvas.
     */
    public Tool(Canvas canvas, StackPane canvasContainer) {
        this.canvas = canvas;
        this.canvasContainer = canvasContainer;
    }

    /**
     * Enable tool. Sets tool property and event handlers for mouse action.
     */
    public void enableTool() {
        initTool();

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
    }

    /**
     * Disable tool.
     */
    public void disableTool() {
        canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressedHandler);
        canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedHandler);
        canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouseReleasedHandler);
    }

    /**
     * Handle mouse pressed event.
     *
     * @param event Mouse event which occurred.
     */
    abstract protected void handleMousePressed(MouseEvent event);

    /**
     * Handle mouse dragged event.
     *
     * @param event Mouse event which occurred.
     */
    abstract protected void handleMouseDragged(MouseEvent event);

    /**
     * Handle mouse released event.
     *
     * @param event Mouse event which occurred.
     */
    abstract protected void handleMouseReleased(MouseEvent event);

    /**
     * Initialize tool by setting its property.
     */
    abstract protected void initTool();

}
