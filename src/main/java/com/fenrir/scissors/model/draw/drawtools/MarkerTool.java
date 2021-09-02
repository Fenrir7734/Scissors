package com.fenrir.scissors.model.draw.drawtools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class MarkerTool extends DrawTool {

    public MarkerTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void handleMouseReleased(MouseEvent event) {
        super.handleMouseReleased(event);

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
