package com.fenrir.scissors.model.draw.shapetools;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ArrowTool extends ShapeTool {

    public ArrowTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
    }

    @Override
    protected void handleMouseDragged(MouseEvent event) {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, super.layer.getWidth(), super.layer.getHeight());

        drawArrow(startPoint.getKey(), startPoint.getValue(), event.getX(), event.getY());
    }

    private void drawArrow(double x1, double y1, double x2, double y2) {
        // vector
        double dx = x1 - x2;
        double dy = y1 - y2;

        // distance between (x1, y1) and (x2, y2)
        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        dx /= dist; //cos
        dy /= dist; //sin

        // endpoint of line
        double x3 = x2 + 12 * dx;
        double y3 = y2 + 12 * dy;

        // two perpendicular points to the line on the end of this line,
        // forming the base of the triangle
        double x4 = x3 + 6 * dy;
        double y4 = y3 - 6 * dx;
        double x5 = x3 - 6 * dy;
        double y5 = y3 + 6 * dx;

        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();

        // drawing triangle
        graphicsContext.fillPolygon(
                new double[] {x4, x5, x2},
                new double[] {y4, y5, y2},
                3
        );

        graphicsContext.strokeLine(x1, y1, x3, y3);
    }

    @Override
    protected void initTool() {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();

        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2);
    }
}
