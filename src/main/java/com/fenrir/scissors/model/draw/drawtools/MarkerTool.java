package com.fenrir.scissors.model.draw.drawtools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * @author Fenrir7734
 * @version v1.0.1 September 18, 2021
 */
public class MarkerTool extends DrawTool {

    public MarkerTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    public void disableTool() {
        super.disableTool();

        super.canvas.getGraphicsContext2D().setEffect(null);
    }

    @Override
    protected void initTool() {
        GraphicsContext graphicsContext = super.canvas.getGraphicsContext2D();

        graphicsContext.setFill(Color.rgb(255, 255, 0, 0.1));
        graphicsContext.setStroke(Color.rgb(255, 255, 0, 0.1));
        graphicsContext.setLineWidth(20);
        graphicsContext.setEffect(new GaussianBlur());
    }
}
