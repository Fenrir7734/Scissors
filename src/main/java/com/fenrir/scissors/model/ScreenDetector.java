package com.fenrir.scissors.model;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Class to detect on which screen mouse pointer is located.
 *
 * Initially class stores information about screen where mouse pointer was located during creating instance of this
 * object. To detects movement of the mouse pointer between screens {@link #refresh()} method must be called.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 18, 2021
 */
public class ScreenDetector {
    private Screen currentScreen;
    private Screen previousScreen;
    private final ObservableList<Screen> screens;

    /**
     * Creates a new instance. Current screen will be set to screen where mouse pointer was located during creating
     * instance of this class.
     */
    public ScreenDetector() {
        screens = Screen.getScreens();
        currentScreen = detectCurrentScreen();
        previousScreen = currentScreen;
    }

    /**
     * Must be called to update the information on which screen the mouse pointer is currently located.
     */
    public void refresh() {
        previousScreen = currentScreen;
        currentScreen = detectCurrentScreen();
    }

    /**
     * Detects the screen where the mouse pointer is currently located.
     *
     * @return  Screen where mouse pointer is located.
     */
    private Screen detectCurrentScreen() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        for (Screen screen: screens) {
            if(screen.getBounds().getMinX() <= p.x
                    && screen.getBounds().getMaxX() > p.x
                    && screen.getBounds().getMinY() <= p.y
                    && screen.getBounds().getMaxY() > p.y
            ) {
                return screen;
            }
        }
        return null;
    }

    /**
     * Returns whether mouse pointer is located on different screen than during previous refresh.
     *
     * @return  True if mouse pointer location change to the different screen.
     */
    public boolean isScreeChange() {
        if(!currentScreen.equals(previousScreen)) {
            return true;
        }
        return false;
    }

    /**
     * Detects all screens that intersects the provided Stage.
     *
     * @param stage The Specified Stage.
     *
     * @return      ObservableList of Screens that intersect with provided stage bounds.
     */
    public ObservableList<Screen> detectStageScreens(Stage stage) {
        return Screen.getScreensForRectangle(
                stage.getX(),
                stage.getY(),
                stage.getWidth(),
                stage.getWidth()
        );
    }

    /**
     * Returns Screen where mouse pointer was located during last refresh.
     *
     * @return  Screen where mouse pointer was located.
     */
    public Screen getCurrentScreen() {
        return currentScreen;
    }

    /**
     * Gets Point that marks the bottom left corner of the screen where mouse pointer was located during last refresh.
     * Point location is relative to the bottom left corner of the primary screen.
     *
     * @return  Point that marks the bottom left corner of the screen.
     */
    public Point getCurrentScreenLocation() {
        Rectangle2D rectangle2D = currentScreen.getBounds();
        return new Point((int) rectangle2D.getMinX(), (int) rectangle2D.getMinY());
    }

    /**
     * Gets width of the screen where mouse pointer was located during last refresh.
     *
     * @return  Width of the screen.
     */
    public int getCurrentScreenWidth() {
        return (int) currentScreen.getBounds().getWidth();
    }

    /**
     * Gets height of the screen where mouse pointer was located during last refresh.
     *
     * @return  Height of the screen.
     */
    public int getCurrentScreenHeight() {
        return (int) currentScreen.getBounds().getHeight();
    }
}
