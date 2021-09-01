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
    protected void mouseDraggedEvent(MouseEvent event) {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, super.layer.getWidth(), super.layer.getHeight());
        graphicsContext.strokeLine(
                startPoint.getKey(),
                startPoint.getValue(),
                event.getX(),
                event.getY()
        );
        double dx = startPoint.getKey() - event.getX();
        double dy = startPoint.getValue() - event.getY();
        double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        dx /= dist;
        dy /= dist;
        double x1 = event.getX() + 10 * dy;
        double y1 = event.getY() - 10 * dx;
        double x2 = event.getX() - 10 * dy;
        double y2 = event.getY() + 10 * dx;
        System.out.println("dist: " + dist);
        System.out.println("dx: " + dx + "dy: " + dy);
        System.out.println("Start: " + startPoint.getKey() + " " + startPoint.getValue());
        System.out.println("end: " + event.getX() + " " + event.getY());
        System.out.println("p1: " + x1 + " " + y1);
        System.out.println("p2: " + x2 + " " + y2);
        graphicsContext.fillPolygon(
                new double[] {
                        x1,
                        x2
                },
                new double[] {
                        y1,
                        y2
                },
                2
        );
    }

    @Override
    protected void initTool() {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();

        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2);
    }
}
