package com.fenrir.scissors.model.area;

/**
 * Class that describes rectangular area by a location defined as the coordinate points of two opposite vertices. These
 * points are called Start and End points:
 * <ul>
 *      <li>Start point - top-left vertices of the area.</li>
 *      <li>End point - bottom-right vertices of the area.</li>
 * <ul/>
 *
 * @author Fenrir7734
 * @version v1.0.0 September 18, 2021
 */
public class Area {
    private double X1;
    private double Y1;
    private double X2;
    private double Y2;
    private final double minX;
    private final double minY;

    /**
     * Creates new instance loaded with coordinates of the point defining the origin of the coordinate system.
     *
     * @param minX  X coordinate of the origin of the coordinate system.
     * @param minY  Y coordinate of the origin of the coordinate system.
     */
    public Area(double minX, double minY) {
        X1 = 0;
        X2 = 0;
        Y1 = 0;
        Y2 = 0;
        this.minX = minX;
        this.minY = minY;
    }

    /**
     * Returns the X coordinate of the start point, relative to the defined origin of the coordinate system.
     *
     * @return X coordinate.
     */
    public double getRelativeStartX() {
        return Math.min(X1, X2);
    }

    /**
     * Returns the Y coordinate of the start point, relative to the defined origin of the coordinate system.
     *
     * @return Y coordinate.
     */
    public double getRelativeStartY() {
        return Math.min(Y1, Y2);
    }

    /**
     * Returns the X coordinate of the end point, relative to the defined origin of the coordinate system.
     *
     * @return X coordinate.
     */
    public double getRelativeEndX() {
        return Math.max(X1, X2);
    }

    /**
     * Returns the Y coordinate of the end point, relative to the defined origin of the coordinate system.
     *
     * @return Y coordinate.
     */
    public double getRelativeEndY() {
        return Math.max(Y1, Y2);
    }

    /**
     * Returns the X coordinate of the start point, relative to the point P(0, 0).
     *
     * @return X coordinate.
     */
    public double getAbsoluteStartX() {
        return Math.min(X1, X2) + minX;
    }

    /**
     * Returns the Y coordinate of the start point, relative to the point P(0, 0).
     *
     * @return Y coordinate.
     */
    public double getAbsoluteStartY() {
        return Math.min(Y1, Y2) + minY;
    }

    /**
     * Returns the X coordinate of the end point, relative to the point P(0, 0).
     *
     * @return X coordinate.
     */
    public double getAbsoluteEndX() {
        return Math.max(X1, X2) + minX;
    }

    /**
     * Returns the Y coordinate of the end point, relative to the point P(0, 0).
     *
     * @return Y coordinate.
     */
    public double getAbsoluteEndY() {
        return Math.max(Y1, Y2) + minY;
    }

    /**
     * Returns width of the area.
     *
     * @return  Width of the area.
     */
    public double getWidth() {
        return Math.abs(X2 - X1);
    }

    /**
     * Returns height of the area.
     *
     * @return  Height of the area.
     */
    public double getHeight() {
        return Math.abs(Y2 - Y1);
    }

    /**
     * Sets X coordinate of the first point.
     *
     * @param x X coordinate, relative to the defined origin of the coordinate system.
     */
    public void setX1(double x) {
        this.X1 = x;
    }

    /**
     * Sets Y coordinate of the first point.
     *
     * @param y Y coordinate, relative to the defined origin of the coordinate system.
     */
    public void setY1(double y) {
        this.Y1 = y;
    }

    /**
     * Sets X coordinate of the second point.
     *
     * @param x X coordinate, relative to the defined origin of the coordinate system.
     */
    public void setX2(double x) {
        this.X2 = x;
    }

    /**
     * Sets Y coordinate of the second point.
     *
     * @param y Y coordinate, relative to the defined origin of the coordinate system.
     */
    public void setY2(double y) {
        this.Y2 = y;
    }
}
