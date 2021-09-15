package com.fenrir.scissors.controllers;

import com.fenrir.scissors.model.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SettingsWindowController {
    private SettingsWindowController instance;

    @FXML private CheckBox automaticSaveCheckbox;
    @FXML private CheckBox automaticCopyCheckbox;
    @FXML private TextField pathTextField;
    @FXML private Button changePathButton;
    @FXML private Slider opacitySlider;
    @FXML private ListView<HBox> favoriteListView;
    @FXML private Button addToFavoriteButton;

    private final Properties properties = Properties.getInstance();
    private int opacityValue;
    private ObservableList<HBox> favoritesViewItems;

    @FXML
    private void initialize() {
        instance = this;

        automaticSaveCheckbox.setSelected(properties.isSaveToDefault());
        automaticCopyCheckbox.setSelected(properties.isSaveToClipboard());
        pathTextField.setText(properties.getDefaultPath().toString());
        opacitySlider.setValue(properties.getOpacity());
        opacityValue = properties.getOpacity();

        favoritesViewItems = FXCollections.observableArrayList();

        for(Properties.Favorite favorite: properties.getFavoriteList()) {
            addFavoriteToList(favorite);
        }
        favoriteListView.setItems(favoritesViewItems);

        automaticSaveCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> properties.setSaveToDefault(newValue));
        automaticCopyCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> properties.setSaveToClipboard(newValue));
        opacitySlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> opacityValue = newNumber.intValue());
        opacitySlider.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> properties.setOpacity(opacityValue));
    }

    @FXML
    private void changePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(changePathButton.getScene().getWindow());

        if(selectedDirectory != null) {
            properties.setDefaultPath(Path.of(selectedDirectory.getAbsolutePath()));
            pathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void addToFavorite() {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FavoriteInputWindow.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
            refreshFavorite();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFavoriteToList(Properties.Favorite favorite) {
        HBox item = new HBox();
        item.setId(favorite.name());
        Button button = new Button();
        button.setOnMouseClicked(e -> removeFavoriteFromList(item.getId()));

        item.getChildren().addAll(
                new Label(favorite.name()),
                new Label(favorite.path().toString()),
                button
        );

        favoritesViewItems.add(item);
        favoriteListView.setItems(favoritesViewItems);
    }

    private void removeFavoriteFromList(String name) {
        for(HBox item: favoritesViewItems) {
            if(item.getId().equals(name)) {
                favoritesViewItems.remove(item);
                break;
            }
        }
        properties.removeFromFavorite(name);
        favoriteListView.setItems(favoritesViewItems);
    }

    private void refreshFavorite() {
        List<Properties.Favorite> favoriteList = properties.getFavoriteList();
        List<String> favoriteItemsIDs = favoritesViewItems.stream()
                .map(Node::getId)
                .collect(Collectors.toList());

        for(Properties.Favorite favorite: favoriteList) {
            if(!favoriteItemsIDs.contains(favorite.name())) {
                addFavoriteToList(favorite);
            }
        }

        List<String> favoriteNamesList = favoriteList.stream()
                .map(Properties.Favorite::name)
                .collect(Collectors.toList());

        for(String id: favoriteItemsIDs) {
            if(!favoriteNamesList.contains(id)) {
                removeFavoriteFromList(id);
            }
        }
    }

    public SettingsWindowController getInstance() {
        return instance;
    }
}
