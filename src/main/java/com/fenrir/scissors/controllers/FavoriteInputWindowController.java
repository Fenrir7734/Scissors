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
        String name = nameTextField.getText();
        String path = pathTextField.getText();

        if (name != null && !name.isBlank() && path != null && !path.isBlank()) {
            name = name.trim();
            List<String> favoritesNames = properties.getFavoriteList()
                    .stream()
                    .map(Properties.Favorite::name)
                    .collect(Collectors.toList());

            if (!favoritesNames.contains(name)) {
                properties.addToFavorite(name, path);
                properties.write();

                ControllerMediatorImpl.getInstance().receiveFavoriteList();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Path added to favorites");
                alert.showAndWait();
                close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Name must be unique!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "All fields must be completed!");
            alert.showAndWait();
        }
    }

    /**
     * Handles the button action to close Favorite Input Window GUI.
     */
    @FXML
    private void close() {
        pathTextField.getScene().getWindow().hide();
    }
}
