package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class controls GUI responsible for creating and adding new instance of
 * {@link com.fenrir.scissors.model.Properties.Favorite Favorite} class to the list of favorite saving locations for
 * saving screenshots quickly.
 */
public class FavoriteInputWindowController {
    @FXML private TextField nameTextField;
    @FXML private TextField pathTextField;

    private final Properties properties = Properties.getInstance();

    @FXML
    private void initialize() {
        ControllerMediatorImpl.getInstance().registerFavoriteInputWindowController(this);
    }

    /**
     * Handles the button action to allow choosing destination directory which will be added to favorite.
     */
    @FXML
    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File selectedDirectory = directoryChooser.showDialog(pathTextField.getScene().getWindow());

        if (selectedDirectory != null) {
            pathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Handles the button action to add chosen directory to favorite.
     *
     * The directory path and name of {@link com.fenrir.scissors.model.Properties.Favorite Favorite} class instance,
     * which are taken from Text Fields, cannot be empty and name cannot duplicate the name of another Favorite instance.
     * If any of these conditions are not met appropriate alert will be displayed. Otherwise, new Favorite instance will
     * be added to list.
     */
    @FXML
    private void add() {
        try {
            String name = nameTextField.getText();
            String path = pathTextField.getText();
            addToFavorite(name, path);
            ControllerMediatorImpl.getInstance().receiveFavoriteList();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Path added to favorites");
            alert.showAndWait();
            close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void addToFavorite(String name, String path) throws IllegalArgumentException {
        if(isFieldEmpty(name, path)) {
            throw new IllegalArgumentException("All fields must be completed!");
        }
        name = name.trim();

        if(isDuplicate(name)) {
            throw new IllegalArgumentException("Name must be unique!");
        }
        properties.addToFavorite(name, path);
        properties.write();
    }

    private boolean isFieldEmpty(String name, String path) {
        return name == null || name.isBlank() || path == null || path.isBlank();
    }

    private boolean isDuplicate(String name) {
        List<String> favoriteNames = getListOfFavoriteNames();
        return favoriteNames.contains(name);
    }

    private List<String> getListOfFavoriteNames() {
        return properties.getFavoriteList()
                .stream()
                .map(Properties.Favorite::name)
                .collect(Collectors.toList());
    }

    /**
     * Handles the button action to close Favorite Input Window GUI.
     */
    @FXML
    private void close() {
        pathTextField.getScene().getWindow().hide();
    }
}
