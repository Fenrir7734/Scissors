package com.fenrir.scissors.model.screenshot;

import com.fenrir.scissors.model.area.Area;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.robot.Robot;
import javafx.stage.Screen;

/**
 * Helper class to take screenshots.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 17, 2021
 */
public class ScreenShooter {

    private ScreenShooter() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Takes screenshot of the given rectangular area relative to the primary screen.
     *
     * @param x         Starting horizontal position of the rectangular area to capture.
     * @param y         Starting vertical position of the rectangular area to capture.
     * @param width     Width of the rectangular area to capture, must be grater than 0.
     * @param height    Height of the rectangular area to capture, must be greater than 0.
     *
     * @return          The screen capture of the given area.
     */
    public static WritableImage takeScreenshot(int x, int y, int width, int height) {
        if(width <= 0 || height <= 0) {
            return null;
        }

        WritableImage image = new WritableImage(width, height);
        return new Robot().getScreenCapture(image, x, y, width, height);
    }

    /**
     * Takes screenshot of the given rectangular area relative to the primary screen.
     *
     * @param rectangle Rectangular area to capture.
     *
     * @return          The screen capture of the given area.
     */
    public static WritableImage takeScreenshot(Rectangle2D rectangle) {
        return takeScreenshot(
                (int) rectangle.getMinX(),
                (int) rectangle.getMinY(),
                (int) rectangle.getWidth(),
                (int) rectangle.getHeight()
        );
    }

    /**
     * Takes screenshot of the given rectangular area relative to the primary screen.
     *
     * @param area  Rectangular area to capture.
     *
     * @return      The screen capture of the given area.
     */
    public static WritableImage takeScreenshot(Area area) {
        return takeScreenshot(
                (int) area.getAbsoluteStartX(),
                (int) area.getAbsoluteStartY(),
                (int) area.getWidth(),
                (int) area.getHeight()
        );
    }

    /**
     * Takes screenshot of the entire screen.
     *
     * @param screen    Screen to capture.
     *
     * @return          The screen capture of the given screen.
     */
    public static WritableImage takeScreenshot(Screen screen) {
        return takeScreenshot(screen.getBounds());
    }

}
