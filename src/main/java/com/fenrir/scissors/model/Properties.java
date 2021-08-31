package com.fenrir.scissors.model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Properties {
    private static Properties instance = null;

    private final JSONObject propertiesFileContent;

    private boolean saveToDefault;
    private boolean saveToClipboard;
    private Path defaultPath;
    private int opacity;
    private int borderWidth;
    private final List<Favorite> favoriteList;

    private final record Favorite(String name, Path path) {

        @Override
        public String name() {
            return name;
        }

        @Override
        public Path path() {
            return path;
        }
    }

    private Properties() {
        propertiesFileContent = PropertiesUtils.readProperties();

        saveToDefault = propertiesFileContent.getBoolean("save-to-default");
        saveToClipboard = propertiesFileContent.getBoolean("save-to-clipboard");
        defaultPath = Path.of(propertiesFileContent.getString("default-path"));
        opacity = propertiesFileContent.getInt("opacity");
        borderWidth = propertiesFileContent.getInt("border-width");

        JSONArray array = propertiesFileContent.getJSONArray("favorite");
        favoriteList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject favorite = array.getJSONObject(i);
            favoriteList.add(new Favorite(
                    favorite.getString("name"),
                    Path.of(favorite.getString("path"))
            ));
        }
        System.out.println(favoriteList);
    }

    public static Properties getInstance() {
        if(instance == null) {
            instance = new Properties();
        }
        return instance;
    }

    public boolean isSaveToDefault() {
        return saveToDefault;
    }

    public boolean isSaveToClipboard() {
        return saveToClipboard;
    }

    public Path getDefaultPath() {
        return defaultPath;
    }

    public int getOpacity() {
        return opacity;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public List<Favorite> getFavoriteList() {
        return favoriteList;
    }

    public void setSaveToDefault(boolean saveToDefault) {
        propertiesFileContent.put("save-to-default", saveToDefault);
        PropertiesUtils.writeProperties(propertiesFileContent);

        this.saveToDefault = saveToDefault;
    }

    public void setSaveToClipboard(boolean saveToClipboard) {
        propertiesFileContent.put("save-to-clipboard", saveToClipboard);
        PropertiesUtils.writeProperties(propertiesFileContent);

        this.saveToClipboard = saveToClipboard;
    }

    public void setDefaultPath(Path defaultPath) {
        propertiesFileContent.put("default-path", defaultPath);
        PropertiesUtils.writeProperties(propertiesFileContent);

        this.defaultPath = defaultPath;
    }

    public void setOpacity(int opacity) {
        propertiesFileContent.put("opacity", opacity);
        PropertiesUtils.writeProperties(propertiesFileContent);

        this.opacity = opacity;
    }

    public void setBorderWidth(int borderWidth) {
        propertiesFileContent.put("border-width", borderWidth);
        PropertiesUtils.writeProperties(propertiesFileContent);

        this.borderWidth = borderWidth;
    }

    public void addToFavorite(String name, String path) {
        propertiesFileContent.put(
                "favorite", new JSONObject().put("name", name).put("path", path)
        );
        PropertiesUtils.writeProperties(propertiesFileContent);

        favoriteList.add(new Favorite(name, Path.of(path)));
    }

    private static JSONObject getDefaultProperties() {
        return new JSONObject().put("save-to-default", true)
                .put("save-to-clipboard", true)
                .put("default-path", "./")
                .put("opacity", 70)
                .put("border-width", 2)
                .put("favorite", new JSONArray());
    }

    private static class PropertiesUtils {
        private final static Logger logger = LoggerFactory.getLogger(Properties.class);
        private final static String PATH = "src/main/resources/properties.json";

        public static JSONObject readProperties() {
            try {
                String content = new String(Files.readAllBytes(Paths.get(PATH)));

                return new JSONObject(content);
            } catch (IOException e) {
                logger.error("An error occurred when reading {} file: {}", PATH, e.getMessage());

                JSONObject defaultProperties = getDefaultProperties();
                writeProperties(defaultProperties);

                return defaultProperties;
            }
        }

        public static void writeProperties(JSONObject object) {
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(PATH))) {
                object.write(writer);
            } catch (IOException e) {
                logger.error("An error occurred when writing to {} file: {}", PATH, e.getMessage());
            }
        }
    }
}
