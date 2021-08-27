package com.fenrir.scissors.model.area;

public class Area {
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public Area(int x, int y) {
        this.startX = x;
        this.startY = y;
        this.endX = x;
        this.endY = y;
    }

    public Area() {
        this(0, 0);
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public int getWidth() {
        return endX - startX;
    }

    public int getHeight() {
        return endY - startY;
    }

    public void setStartX(int x) {
        this.startX = x;
    }

    public void setStartY(int y) {
        this.startY = y;
    }

    public void setEndX(int x) {
        this.endX = x;
    }

    public void setEndY(int y) {
        this.endY = y;
    }

}
