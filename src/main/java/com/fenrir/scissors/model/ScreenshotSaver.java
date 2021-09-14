package com.fenrir.scissors.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class ScreenshotSaver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotSaver.class);

    private ScreenshotSaver() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static void copyToClipBoard(Image image) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
    }

    public static void copyToClipBoard(String string) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(string);
        clipboard.setContent(content);
    }

    public static void saveTo(Image image, File file) throws IOException  {
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            LOGGER.error("Saving file locally error: {}", e.getMessage());
            throw e;
        }
    }

    public static void saveTo(Image image, Path dirPath, String name) throws IOException {
        Path path = dirPath.resolve(Path.of(name));
        saveTo(image, new File(path.toString()));
    }

    public static String saveToImgur(Image image) throws IOException {
        return ImgurUploader.upload(SwingFXUtils.fromFXImage(image, null));
    }
}
