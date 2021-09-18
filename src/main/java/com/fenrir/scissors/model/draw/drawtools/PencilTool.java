package com.fenrir.scissors.model.draw.drawtools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @author Fenrir7734
 * @version v1.0.0 September 18, 2021
 */
public class PencilTool extends DrawTool {

    public PencilTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void initTool() {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();

        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2);
    }
}
