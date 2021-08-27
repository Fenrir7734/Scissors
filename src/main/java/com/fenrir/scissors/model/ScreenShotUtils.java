package com.fenrir.scissors.model;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

public class ScreenShotUtils {
    public static WritableImage takeScreenshot(int x, int y, int width, int height) {
        WritableImage image = new WritableImage(width, height);
        return new Robot().getScreenCapture(image, x, y, width, height);
    }

    public static WritableImage takeScreenshot(Rectangle2D rectangle) {
        return takeScreenshot(
                (int) rectangle.getMinX(),
                (int) rectangle.getMinY(),
                (int) rectangle.getWidth(),
                (int) rectangle.getHeight()
        );
    }

    public static WritableImage takeScreenshot(Screen screen) {
        return takeScreenshot(screen.getBounds());
    }

    public static WritableImage takeScreenshot(Area area) {
        return takeScreenshot(area.getStartX(), area.getStartY(), area.getWidth(), area.getHeight());
    }
}
