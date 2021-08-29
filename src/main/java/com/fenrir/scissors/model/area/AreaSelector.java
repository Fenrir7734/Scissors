package com.fenrir.scissors.model.area;

import com.fenrir.scissors.model.Properties;
import javafx.geometry.Rectangle2D;

public class AreaSelector {
    private final int BORDER_WIDTH;

    private Area area;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    public AreaSelector(Rectangle2D areaBounds) {
        this.BORDER_WIDTH = Properties.getInstance().getBorderWidth();

        this.minX = (int) areaBounds.getMinX();
        this.minY = (int) areaBounds.getMinY();
        this.maxX = (int) areaBounds.getMaxX();
        this.maxY = (int) areaBounds.getMaxY();

        this.area = new Area(minX, minY);
    }

    public void setFirstPoint(int x, int y) {
        area.setX1(parseX(x));
        area.setY1(parseY(y));
    }

    public void setSecondPoint(int x, int y) {
        area.setX2(parseX(x));
        area.setY2(parseY(y));
    }

    private int parseX(int x) {
        if(x + minX < minX + BORDER_WIDTH) {
            x = BORDER_WIDTH;
        } else if(x + minX > maxX - BORDER_WIDTH) {
            x = maxX - minX - BORDER_WIDTH;
        }
        return x;
    }

    private int parseY(int y) {
        if(y + minY < minY + BORDER_WIDTH) {
            y = BORDER_WIDTH;
        } else if(y + minY > maxY - BORDER_WIDTH) {
            y = maxY - minY - BORDER_WIDTH;
        }
        return y;
    }

    public Area getArea() {
        return area;
    }
}
