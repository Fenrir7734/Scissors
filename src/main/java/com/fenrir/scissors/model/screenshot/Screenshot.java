package com.fenrir.scissors.model.screenshot;

import com.fenrir.scissors.model.area.Area;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Screenshot {
    private String name;
    private final Image image;

    public Screenshot(Image image, String name) {
        this.image = image;
        this.name = name;
    }

    public Screenshot(Image image) {
        this.image = image;
        this.name = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
    }

    public Screenshot cropScreenshot(Area area) {
        if((area.getWidth() < image.getWidth() || area.getHeight() < image.getHeight())
                && area.getWidth() > 0 && area.getHeight() > 0) {

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

    public Screenshot canvasSnapshot(Canvas canvas) {
        WritableImage image = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, image);
        return new Screenshot(image, this.name);
    }

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

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
