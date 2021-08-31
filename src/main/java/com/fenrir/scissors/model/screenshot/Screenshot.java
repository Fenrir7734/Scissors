package com.fenrir.scissors.model.screenshot;

import com.fenrir.scissors.model.area.Area;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

public class Screenshot {
    private final Image image;

    public Screenshot(Image image) {
        this.image = image;
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
}
