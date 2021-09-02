package com.fenrir.scissors.model.draw.shapetools;

import com.fenrir.scissors.model.draw.Tool;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Pair;

public abstract class ShapeTool extends Tool {
    protected Canvas layer = new Canvas();
    protected Pair<Double, Double> startPoint;

    public ShapeTool(Canvas canvas, StackPane canvasContainer) {
        super(canvas, canvasContainer);
        this.layer = new Canvas(super.canvas.getWidth(), super.canvas.getHeight());
    }

    @Override
    protected void handleMousePressed(MouseEvent event) {
        super.canvasContainer.getChildren().add(this.layer);
        this.startPoint = new Pair<>(event.getX(), event.getY());
    }

    @Override
    protected void handleMouseReleased(MouseEvent event) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        GraphicsContext context = super.canvas.getGraphicsContext2D();
        context.drawImage(
                this.layer.snapshot(parameters, null),
                0,
                0,
                super.canvas.getWidth(),
                super.canvas.getHeight()
        );
        canvasContainer.getChildren().remove(this.layer);
    }


}
