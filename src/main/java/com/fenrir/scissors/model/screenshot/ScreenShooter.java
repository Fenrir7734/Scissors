package com.fenrir.scissors.model.screenshot;

import com.fenrir.scissors.model.area.Area;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;

public class ScreenShooter {
    public static WritableImage takeScreenshot(double x, double y, double width, double height) {
        if(width <= 0 || height <= 0) {
            return null;
        }

        WritableImage image = new WritableImage((int) width, (int) height);
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
        return takeScreenshot(area.getAbsoluteStartX(), area.getAbsoluteStartY(), area.getWidth(), area.getHeight());
    }
}
