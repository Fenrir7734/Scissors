package com.fenrir.scissors.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds program properties.
 *
 * @author Fenrir7734
 * @version v1.0.0 September 16, 2021
 */
public class Properties {
    public final static int BORDER_WIDTH = 2;
    public static final String DEFAULT_SCREENSHOT_DIRECTORY_PATH =
            Path.of(System.getProperty("user.dir")).resolve("screenshots").toString();

    private static Properties instance = null;

    private final JSONObject propertiesFileContent;
    private boolean saveToDefault;
    private boolean saveToClipboard;
    private Path defaultPath;
    private int opacity;
    private List<Favorite> favoriteList;
    private final boolean isWindows;

    /**
     * Class that represents named path to directory. It is used to store the path to the directory, which user adds for
     * quick access.
     *
     * @param name  Name identifying path to directory.
     * @param path  Path to directory.
     */
    public final record Favorite(String name, Path path) {}

    /**
     * Creates a new instance loaded with program properties read from a JSON file.
     */
    private Properties() {
        propertiesFileContent = PropertiesUtils.readProperties();

        readFavorite();
        saveToDefault = propertiesFileContent.getBoolean("save-to-default");
        saveToClipboard = propertiesFileContent.getBoolean("save-to-clipboard");
        defaultPath = Path.of(propertiesFileContent.getString("default-path"));
        opacity = propertiesFileContent.getInt("opacity");
        isWindows = determineSystem();
    }

    /**
     * Returns instance of this class.
     *
     * @return  Instance of this class.
     */
    public static Properties getInstance() {
        if(instance == null) {
            instance = new Properties();
        }
        return instance;
    }

    private boolean determineSystem() {
        String osName = System.getProperty("os.name");
        return osName.startsWith("Windows");
    }

    private void readFavorite() {
        JSONArray array = propertiesFileContent.getJSONArray("favorite");
        favoriteList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Favorite favorite = getFavoriteFrom(object);
            favoriteList.add(favorite);
        }
    }

    private Favorite getFavoriteFrom(JSONObject object) {
        return new Favorite(
                object.getString("name"),
                Path.of(object.getString("path"))
        );
    }

    /**
     * Returns whether program should automatically save screenshot to the default directory.
     *
     * @return  True, if automatic saving to default directory is enabled.
     */
    public boolean isSaveToDefault() {
        return saveToDefault;
    }

    /**
     * Returns whether program should automatically copy screenshot to clipboard.
     *
     * @return  True, if automatic saving to clipboard is enabled.
     */
    public boolean isSaveToClipboard() {
        return saveToClipboard;
    }

    /**
     * Returns path to default saving directory.
     *
     * @return  Path to default saving directory.
     */
    public Path getDefaultPath() {
        return defaultPath;
    }

    /**
     * Returns the degree of opacity used for the
     * {@link com.fenrir.scissors.controllers.ScreenCaptureWindowController ScreenCaptureWindowController} GUI overlay.
     *
     * @return  degree of opacity.
     */
    public int getOpacity() {
        return opacity;
    }

    /**
     * Returns list of all stored favorite saving locations.
     *
     * @return  List of favorite saving locations.
     */
    public List<Favorite> getFavoriteList() {
        return favoriteList;
    }

    /**
     * Sets the value of the property SaveToDefault.
     *
     * @param saveToDefault Whether screenshots should be automatically saved to the default directory.
     */
    public void setSaveToDefault(boolean saveToDefault) {
        this.saveToDefault = saveToDefault;
    }

    /**
     * Sets the value of the property saveToClipboard.
     *
     * @param saveToClipboard   Whether screenshots should be automatically copied to the clipboard.
     */
    public void setSaveToClipboard(boolean saveToClipboard) {
        this.saveToClipboard = saveToClipboard;
    }

    /**
     * Sets default save path.
     *
     * @param defaultPath   Default saving path.
     */
    public void setDefaultPath(Path defaultPath) {
        this.defaultPath = defaultPath;
    }

    /**
     * Sets degree of the opacity used for the
     * {@link com.fenrir.scissors.controllers.ScreenCaptureWindowController ScreenCaptureWindowController} GUI overlay.
     *
     * @param opacity   Degree of the opacity.
     */
    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    /**
     * Adds new instance of {@link Favorite Favorite} to list of favorite save locations.
     *
     * @param name  Unique name of {@link Favorite} class instance.
     * @param path  Path to destination directory.
     */
    public void addToFavorite(String name, String path) {
        favoriteList.add(new Favorite(name, Path.of(path)));
    }

    /**
     * Removes instance of {@link Favorite Favorite} class from list of favorite save locations.
     *
     * @param name  Name of {@link Favorite Favorite} class instance that uniquely identifies the instance to be removed.
     */
    public void removeFromFavorite(String name) {
        Favorite toDelete = favoriteList.stream()
                .filter(f -> f.name.equals(name))
                .findFirst()
                .get();
        favoriteList.remove(toDelete);
    }

    public boolean isWindows() {
        return isWindows;
    }

    /**
     * Returns JSONObject representing default program properties.
     *
     * @return  Default program properties.
     */
    private static JSONObject getDefaultProperties() {
        return new JSONObject().put("save-to-default", true)
                .put("save-to-clipboard", true)
                .put("default-path", DEFAULT_SCREENSHOT_DIRECTORY_PATH)
                .put("opacity", 70)
                .put("favorite", new JSONArray());
    }

    /**
     * Maps properties to JSONObject and writes them to JSON file.
     */
    public void write() {
        propertiesFileContent.put("save-to-default", saveToDefault);
        propertiesFileContent.put("save-to-clipboard", saveToClipboard);
        propertiesFileContent.put("default-path", defaultPath);
        propertiesFileContent.put("opacity", opacity);
        propertiesFileContent.put("favorite", favoriteToJSONArray());

        PropertiesUtils.writeProperties(propertiesFileContent);
    }

    /**
     * Maps favorite list to JSONArray.
     *
     * @return favorite list as JSONArray.
     */
    private JSONArray favoriteToJSONArray() {
        JSONArray favoriteArray = new JSONArray();
        for(Favorite f: favoriteList) {
            favoriteArray.put(new JSONObject().put("name", f.name).put("path", f.path));
        }
        return favoriteArray;
    }

    /**
     * Helper class for reading JSON file that stores program properties.
     */
    private static class PropertiesUtils {
        private final static Logger logger = LoggerFactory.getLogger(Properties.class);
        private final static String PROPERTIES_FILE_PATH =
                Path.of(System.getProperty("user.dir")).resolve("properties.json").toString();

        /**
         * Reads JSON file that stores program properties and returns JSONObject representing content of that file. If
         * file cannot be read, the default program properties will be returned and file will be overwritten with these
         * properties.
         *
         * @return  JSONObject that represents program properties.
         */
        public static JSONObject readProperties() {
            try {
                String content = new String(Files.readAllBytes(Paths.get(PROPERTIES_FILE_PATH)));

                return new JSONObject(content);
            } catch (IOException | JSONException e) {
                if (e instanceof NoSuchFileException) {
                    logger.warn("Properties file not found.");
                } else {
                    logger.error("An error occurred when reading {} file: {}", PROPERTIES_FILE_PATH, e.getMessage());
                }
                logger.warn("Falling back to default properties.");

                JSONObject defaultProperties = getDefaultProperties();
                writeProperties(defaultProperties);

                return defaultProperties;
            }
        }

        /**
         * Writes the content of JSONObject to a JSON file that stores program properties.
         *
         * @param object    The object whose content will be written to the file.
         */
        public static void writeProperties(JSONObject object) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(PROPERTIES_FILE_PATH))) {
                object.write(writer, 4, 0);
            } catch (IOException | JSONException e) {
                logger.error("An error occurred when writing to {} file: {}", PROPERTIES_FILE_PATH, e.getMessage());
            }
        }
    }
}
