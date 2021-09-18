package com.fenrir.scissors.model.draw.drawtools;

import com.fenrir.scissors.model.draw.Tool;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * @author Fenrir7734
 * @version v1.0.0 September 18, 2021
 */
public class EraserTool extends Tool {

    public EraserTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void handleMousePressed(MouseEvent event) {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();
        graphicsContext.clearRect(event.getX() - 2, event.getY() - 2, 20, 20);
    }

    @Override
    protected void handleMouseDragged(MouseEvent event) {
        handleMousePressed(event);
    }

    @Override
    protected void handleMouseReleased(MouseEvent event) {
        handleMousePressed(event);
    }

    @Override
    protected void initTool() {}
}
