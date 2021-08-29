package com.fenrir.scissors.model;

import com.fenrir.scissors.model.area.Area;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.robot.Robot;
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
        return takeScreenshot(area.getAbsoluteStartX(), area.getAbsoluteStartY(), area.getWidth(), area.getHeight());
    }

    public static Background getCurrentWindowScreenshotAsBackground(Screen screen) {
        WritableImage image = ScreenShotUtils.takeScreenshot(screen);

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
}
