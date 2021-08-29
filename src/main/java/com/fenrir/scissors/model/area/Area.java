package com.fenrir.scissors.model.area;

public class Area {
    private int X1;
    private int Y1;
    private int X2;
    private int Y2;
    private final int minX;
    private final int minY;

    public Area(int minX, int minY) {
        X1 = 0;
        X2 = 0;
        Y1 = 0;
        Y2 = 0;
        this.minX = minX;
        this.minY = minY;
    }

    public int getRelativeStartX() {
        return Math.min(X1, X2);
    }

    public int getRelativeStartY() {
        return Math.min(Y1, Y2);
    }

    public int getRelativeEndX() {
        return Math.max(X1, X2);
    }

    public int getRelativeEndY() {
        return Math.max(Y1, Y2);
    }

    public int getAbsoluteStartX() {
        return Math.min(X1, X2) + minX;
    }

    public int getAbsoluteStartY() {
        return Math.min(Y1, Y2) + minY;
    }

    public int getAbsoluteEndX() {
        return Math.max(X1, X2) + minX;
    }

    public int getAbsoluteEndY() {
        return Math.max(Y1, Y2) + minY;
    }

    public int getWidth() {
        return Math.abs(X2 - X1);
    }

    public int getHeight() {
        return Math.abs(Y2 - Y1);
    }

    public void setX1(int x) {
        this.X1 = x;
    }

    public void setY1(int y) {
        this.Y1 = y;
    }

    public void setX2(int x) {
        this.X2 = x;
    }

    public void setY2(int y) {
        this.Y2 = y;
    }
}
