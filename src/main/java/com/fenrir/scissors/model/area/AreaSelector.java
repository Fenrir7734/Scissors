package com.fenrir.scissors.model.area;

import com.fenrir.scissors.model.Properties;
import javafx.geometry.Rectangle2D;

public class AreaSelector {
    private final int BORDER_WIDTH;

    private Area area;
    private Rectangle2D areaBounds;

    public AreaSelector(Rectangle2D areaBounds) {
        this.BORDER_WIDTH = Properties.getInstance().getBorderWidth();

        this.area = new Area();
        this.areaBounds = areaBounds;
    }

    public void setStartPoint(int x, int y) {
        area.setStartX(parseX(x));
        area.setStartY(parseY(y));
    }

    public void setEndPoint(int x, int y) {
        area.setEndX(parseX(x));
        area.setEndY(parseY(y));
    }

    private int parseX(int x) {
        if(x < areaBounds.getMinX() + BORDER_WIDTH) {
            x = (int) areaBounds.getMinX() + BORDER_WIDTH;
        } else if(x > areaBounds.getMaxX() - BORDER_WIDTH) {
            x = (int) areaBounds.getMaxX() - BORDER_WIDTH;
        }
        return x;
    }

    private int parseY(int y) {
        if(y < areaBounds.getMinY() + BORDER_WIDTH) {
            y = (int) areaBounds.getMinY() + BORDER_WIDTH;
        } else if(y > areaBounds.getMaxY() - BORDER_WIDTH) {
            y = (int) areaBounds.getMaxY() - BORDER_WIDTH;
        }
        return y;
    }

    public Area getArea() {
        return area;
    }
}
