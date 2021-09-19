package com.fenrir.scissors.model.area;

import com.fenrir.scissors.model.Properties;
import javafx.geometry.Rectangle2D;

/**
 * Class that parse coordinates of selected area to be within the defined selectable area.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 19, 2021
 */
public class AreaSelector {
    private final Area area;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    /**
     * Creates new instance loaded with bounds of the selectable area.
     *
     * @param areaBounds    Bounds of the selectable area.
     */
    public AreaSelector(Rectangle2D areaBounds) {
        this.minX = areaBounds.getMinX();
        this.minY = areaBounds.getMinY();
        this.maxX = areaBounds.getMaxX();
        this.maxY = areaBounds.getMaxY();

        this.area = new Area(minX, minY);
    }

    /**
     * Sets first point of the rectangular area.
     *
     * Coordinates of the point should be relative to the origin of the defined selectable area.
     * If the given point is outside the selectable area, its coordinates will be changed to the nearest coordinates to
     * that point inside the selectable area.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void setFirstPoint(double x, double y) {
        area.setX1(parseX(x));
        area.setY1(parseY(y));
    }

    /**
     * Sets second point of the rectangular area.
     *
     * Coordinates of the point should be relative to the origin of the defined selectable area.
     * If the given point is outside the selectable area, its coordinates will be changed to the nearest coordinates to
     * that point inside the selectable area.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     */
    public void setSecondPoint(double x, double y) {
        area.setX2(parseX(x));
        area.setY2(parseY(y));
    }

    /**
     * Checks whether the given X coordinate is within the selectable area. If it is not, it will be changed to the
     * coordinate of the corresponding border of the selectable area.
     *
     * @param x X coordinate.
     *
     * @return  X coordinate.
     */
    private double parseX(double x) {
        if(x + minX < minX + Properties.BORDER_WIDTH) {
            x = Properties.BORDER_WIDTH;
        } else if(x + minX > maxX - Properties.BORDER_WIDTH) {
            x = maxX - minX - Properties.BORDER_WIDTH;
        }
        return x;
    }

    /**
     * Checks whether the given Y coordinate is within the selectable area. If it is not, it will be changed to the
     * coordinate of the corresponding border of the selectable area.
     *
     * @param y Y coordinate.
     *
     * @return  Y coordinate.
     */
    private double parseY(double y) {
        if(y + minY < minY + Properties.BORDER_WIDTH) {
            y = Properties.BORDER_WIDTH;
        } else if(y + minY > maxY - Properties.BORDER_WIDTH) {
            y = maxY - minY - Properties.BORDER_WIDTH;
        }
        return y;
    }

    /**
     * Returns selected area.
     *
     * @return  selected area.
     */
    public Area getArea() {
        return area;
    }
}
