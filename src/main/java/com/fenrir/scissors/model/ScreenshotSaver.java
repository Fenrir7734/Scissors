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
import java.util.Locale;

/**
 * Helper class to save screenshots on local machine.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 18, 2021
 */
public class ScreenshotSaver {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenshotSaver.class);

    private ScreenshotSaver() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Copies image to the system clipboard.
     *
     * @param image Image to be copied to the clipboard.
     */
    public static void copyToClipBoard(Image image) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        clipboard.setContent(content);
    }

    /**
     * Copies string to the system clipboard.
     *
     * @param string    String to be copied to the clipboard.
     */
    public static void copyToClipBoard(String string) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(string);
        clipboard.setContent(content);
    }


    /**
     * Saves image as a .png file format to the given File. If file parent directory does not exist it will try to
     * create it. On Windows it will append file extension to file name if it is not specified.
     *
     * @param image         Image to be saved.
     * @param file          File to be written to.
     *
     * @throws IOException  If an error occurs during writing.
     */
    public static void saveTo(Image image, File file) throws IOException  {
        try {
            createParentDirectoryIfNotExists(file);
            file = appendFileExtensionIfOnWindows(file);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            LOGGER.error("Saving file locally error: {}", e.getMessage());
            throw e;
        }
    }

    private static void createParentDirectoryIfNotExists(File file) {
        File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    private static File appendFileExtensionIfOnWindows(File file) {
        if (Properties.getInstance().isWindows()) {
            String filename = file.getName()
                    .trim()
                    .toLowerCase(Locale.ROOT);
            filename = filename.endsWith(".png") ? file.getName() : file.getName() + ".png";
            Path parentDirPath = file.getParentFile().toPath();
            return parentDirPath.resolve(filename).toFile();
        }
        return file;
    }

    /**
     * Saves image as a .png file format to the given directory. Creates a new File in the given directory and writes to
     * it. If file parent directory does not exist it will try to create it. On Windows it will append file extension to
     * file name if it is not specified.
     *
     * @param image         Image to be saved.
     * @param dirPath       Directory where image will be saved.
     * @param name          Name of file to be written to.
     *
     * @throws IOException  If an error occurs during writing.
     */
    public static void saveTo(Image image, Path dirPath, String name) throws IOException {
        Path path = dirPath.resolve(Path.of(name));
        saveTo(image, new File(path.toString()));
    }
}
