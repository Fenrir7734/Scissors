package com.fenrir.scissors.model.screenshot;

import com.fenrir.scissors.model.area.Area;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to hold taken screenshot as {@link Image Image}.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 17, 2021
 */
public class Screenshot {
    private String name;
    private final Image image;

    /**
     * Create instance of this class.
     *
     * @param image Image.
     * @param name  Name of the image.
     */
    public Screenshot(Image image, String name) {
        this.image = image;
        this.name = name;
    }

    /**
     * Create instance of this class. Name of the image will be date in format: yyyy-MM-dd_HH:mm:ss.
     *
     * @param image Image.
     */
    public Screenshot(Image image) {
        this.image = image;
        this.name = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
    }

    /**
     * Create instance of this class. Takes snapshot of the given canvas and holds it as an {@link Image Image}.
     *
     * @param canvas    Canvas to snapshot.
     * @param name      Name of the image.
     */
    public Screenshot(Canvas canvas, String name) {
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);

        this.image = image;
        this.name = name;
    }

    /**
     * Crops image to the given rectangular area. The width and height of the area cannot be greater than image size and
     * must be greater them 0.
     *
     * @param area  The area based on which the image will be cropped.
     *
     * @return  New instance of this class holding cropped image.
     */
    public Screenshot cropScreenshot(Area area) {
        if(isAreaDimensionsCorrect(area)) {
            PixelReader reader = image.getPixelReader();
            WritableImage image = new WritableImage(
                    reader,
                    (int) area.getRelativeStartX(),
                    (int) area.getRelativeStartY(),
                    (int) area.getWidth(),
                    (int) area.getHeight()
            );

            return new Screenshot(image);
        }
        return this;
    }

    private boolean isAreaDimensionsCorrect(Area area) {
        return (area.getWidth() < image.getWidth() || area.getHeight() < image.getHeight())
                && area.getWidth() > 0
                && area.getHeight() > 0;
    }

    /**
     * Creates instance of {@link Background Background} class from image.
     *
     * @return  background
     */
    public Background getScreenshotAsBackground() {
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

    /**
     * Gets image.
     *
     * @return image.
     */
    public Image getImage() {
        return image;
    }

    /**
     * Gets name of the image.
     *
     * @return  name of the image.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the image.
     *
     * @param name  name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
