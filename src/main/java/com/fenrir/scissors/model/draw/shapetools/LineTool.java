package com.fenrir.scissors.model.draw.shapetools;

import com.fenrir.scissors.model.draw.Tool;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public class LineTool extends ShapeTool {

    public LineTool(Canvas canvas, StackPane canvasContainer) {
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
    }

    @Override
    protected void initTool() {
        GraphicsContext graphicsContext = super.layer.getGraphicsContext2D();

        graphicsContext.setFill(Color.RED);
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2);
    }
}
