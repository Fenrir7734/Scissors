package com.fenrir.scissors.model;

import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Screen;

import java.awt.*;

public class ScreenDetector {
    private Screen currentScreen;
    private final ObservableList<Screen> screens;

    public ScreenDetector() {
        screens = Screen.getScreens();
        currentScreen = detectCurrentScreen();
    }

    public boolean isScreeChange() {
        Screen screen = detectCurrentScreen();
        if(!currentScreen.equals(screen)) {
            currentScreen = screen;
            return true;
        }
        return false;
    }

    private Screen detectCurrentScreen() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        for (Screen screen: screens) {
            if(screen.getBounds().getMinX() <= p.x
                    && screen.getBounds().getMaxX() >= p.x
                    && screen.getBounds().getMinY() <= p.y
                    && screen.getBounds().getMaxY() >= p.y
            ) {
                return screen;
            }
        }
        return null;
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public Background getCurrentWindowScreenshot() {
        WritableImage image = ScreenShotUtils.takeScreenshot(currentScreen);

        return new Background(
                new BackgroundImage(
                        image,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT
                )
        );
    }

    public Point getCurrentScreenLocation() {
        Rectangle2D rectangle2D = currentScreen.getBounds();
        return new Point((int) rectangle2D.getMinX(), (int) rectangle2D.getMinY());
    }

    public int getCurrentScreenWidth() {
        return (int) currentScreen.getBounds().getWidth();
    }

    public int getCurrentScreenHeight() {
        return (int) currentScreen.getBounds().getHeight();
    }
}
