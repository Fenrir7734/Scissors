package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteInputWindowController {
    private FavoriteInputWindowController instance;

    @FXML private TextField nameTextField;
    @FXML private TextField pathTextField;

    private final Properties properties = Properties.getInstance();

    @FXML
    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("."));
        File selectedDirectory = directoryChooser.showDialog(pathTextField.getScene().getWindow());

        if (selectedDirectory != null) {
            pathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

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

    @FXML
    private void close() {
        pathTextField.getScene().getWindow().hide();
    }

    public FavoriteInputWindowController getInstance() {
        return instance;
    }
}
