package com.fenrir.scissors.model.area;

import com.fenrir.scissors.model.Properties;
import javafx.geometry.Rectangle2D;

public class AreaSelector {
    private final Area area;
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    public AreaSelector(Rectangle2D areaBounds) {
        this.minX = areaBounds.getMinX();
        this.minY = areaBounds.getMinY();
        this.maxX = areaBounds.getMaxX();
        this.maxY = areaBounds.getMaxY();

        this.area = new Area(minX, minY);
    }

    public void setFirstPoint(double x, double y) {
        area.setX1(parseX(x));
        area.setY1(parseY(y));
    }

    public void setSecondPoint(double x, double y) {
        area.setX2(parseX(x));
        area.setY2(parseY(y));
    }

    private double parseX(double x) {
        if(x + minX < minX + Properties.BORDER_WIDTH) {
            x = Properties.BORDER_WIDTH;
        } else if(x + minX > maxX - Properties.BORDER_WIDTH) {
            x = maxX - minX - Properties.BORDER_WIDTH;
        }
        return x;
    }

    private double parseY(double y) {
        if(y + minY < minY + Properties.BORDER_WIDTH) {
            y = Properties.BORDER_WIDTH;
        } else if(y + minY > maxY - Properties.BORDER_WIDTH) {
            y = maxY - minY - Properties.BORDER_WIDTH;
        }
        return y;
    }

    public Area getArea() {
        return area;
    }
}
