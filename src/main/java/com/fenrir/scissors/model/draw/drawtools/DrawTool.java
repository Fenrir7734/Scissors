package com.fenrir.scissors.model.draw.drawtools;

import com.fenrir.scissors.model.draw.Tool;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class DrawTool extends Tool {

    public DrawTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void mousePressedEvent(MouseEvent event) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.beginPath();
        graphicsContext.moveTo(event.getX(), event.getY());
        graphicsContext.stroke();
    }

    @Override
    protected void mouseDraggedEvent(MouseEvent event) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.lineTo(event.getX(), event.getY());
        graphicsContext.stroke();
        graphicsContext.closePath();
        graphicsContext.beginPath();
        graphicsContext.moveTo(event.getX(), event.getY());
    }

    @Override
    protected void mouseReleasedEvent(MouseEvent event) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.lineTo(event.getX(), event.getY());
        graphicsContext.stroke();
        graphicsContext.closePath();
    }
}
