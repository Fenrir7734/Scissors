package com.fenrir.scissors.model.draw.shapetools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class EllipseTool extends ShapeTool {

    public EllipseTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void handleMouseDragged(MouseEvent event) {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, super.layer.getWidth(), super.layer.getHeight());
        drawOval(
                graphicsContext,
                startPoint.getKey(),
                startPoint.getValue(),
                event.getX() - startPoint.getKey(),
                event.getY() - startPoint.getValue()
        );
    }

    @Override
    protected void initTool() {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();

        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2);
    }

    private void drawOval(GraphicsContext graphicsContext, double x, double y, double width, double height) {
        if(width < 0) {
            x += width;
            width = -width;
        }

        if(height < 0) {
            y += height;
            height = -height;
        }

        graphicsContext.strokeOval(x, y, width, height);
    }
}
