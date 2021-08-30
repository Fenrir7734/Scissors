package com.fenrir.scissors.model.area;

public class Area {
    private double X1;
    private double Y1;
    private double X2;
    private double Y2;
    private final double minX;
    private final double minY;

    public Area(double minX, double minY) {
        X1 = 0;
        X2 = 0;
        Y1 = 0;
        Y2 = 0;
        this.minX = minX;
        this.minY = minY;
    }

    public double getRelativeStartX() {
        return Math.min(X1, X2);
    }

    public double getRelativeStartY() {
        return Math.min(Y1, Y2);
    }

    public double getRelativeEndX() {
        return Math.max(X1, X2);
    }

    public double getRelativeEndY() {
        return Math.max(Y1, Y2);
    }

    public double getAbsoluteStartX() {
        return Math.min(X1, X2) + minX;
    }

    public double getAbsoluteStartY() {
        return Math.min(Y1, Y2) + minY;
    }

    public double getAbsoluteEndX() {
        return Math.max(X1, X2) + minX;
    }

    public double getAbsoluteEndY() {
        return Math.max(Y1, Y2) + minY;
    }

    public double getWidth() {
        return Math.abs(X2 - X1);
    }

    public double getHeight() {
        return Math.abs(Y2 - Y1);
    }

    public void setX1(double x) {
        this.X1 = x;
    }

    public void setY1(double y) {
        this.Y1 = y;
    }

    public void setX2(double x) {
        this.X2 = x;
    }

    public void setY2(double y) {
        this.Y2 = y;
    }
}
