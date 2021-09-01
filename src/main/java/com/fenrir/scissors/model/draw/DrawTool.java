package com.fenrir.scissors.model.draw;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;

public abstract class DrawTool extends Tool {

    public DrawTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void mousePressedEvent(double x, double y) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        initTool();
        graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
        graphicsContext.stroke();
    }

    @Override
    protected void mouseDraggedEvent(double x, double y) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.lineTo(x, y);
        graphicsContext.stroke();
        graphicsContext.closePath();
        graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
    }

    @Override
    protected void mouseReleasedEvent(double x, double y) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.lineTo(x, y);
        graphicsContext.stroke();
        graphicsContext.closePath();
    }
}
