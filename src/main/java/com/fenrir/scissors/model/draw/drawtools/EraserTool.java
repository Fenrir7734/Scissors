package com.fenrir.scissors.model.draw.drawtools;

import com.fenrir.scissors.model.draw.Tool;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class EraserTool extends Tool {

    public EraserTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void mousePressedEvent(MouseEvent event) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.clearRect(event.getX() - 2, event.getY() - 2, 20, 20);
    }

    @Override
    protected void mouseDraggedEvent(MouseEvent event) {
        mousePressedEvent(event);
    }

    @Override
    protected void mouseReleasedEvent(MouseEvent event) {
        mousePressedEvent(event);
    }

    @Override
    protected void initTool() {}
}
