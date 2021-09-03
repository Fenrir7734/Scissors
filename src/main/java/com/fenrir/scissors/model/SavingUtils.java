package com.fenrir.scissors.model;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SavingUtils {
    private SavingUtils() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public static void copyToClipBoard(Image image) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
    }

    public static void saveTo(Image image, File file) throws IOException  {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
    }

    public static void saveTo(Image image, Path dirPath, String name) throws IOException {
        Path path = dirPath.resolve(Path.of(name));
        saveTo(image, new File(path.toString()));
    }
}
