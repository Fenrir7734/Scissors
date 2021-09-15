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

/**
 * This class controls GUI responsible for program Settings.
 */
public class SettingsWindowController {
    private static SettingsWindowController instance;

    @FXML private CheckBox automaticSaveCheckbox;
    @FXML private CheckBox automaticCopyCheckbox;
    @FXML private TextField pathTextField;
    @FXML private Button changePathButton;
    @FXML private Slider opacitySlider;
    @FXML private ListView<HBox> favoriteListView;

    private ObservableList<HBox> favoritesViewItems;

    private final Properties properties = Properties.getInstance();
    private int opacityValue;

    /**
     * Initializes instance of this class.
     */
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
            addFavorite(favorite);
        }
        favoriteListView.setItems(favoritesViewItems);

        automaticSaveCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> properties.setSaveToDefault(newValue));
        automaticCopyCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> properties.setSaveToClipboard(newValue));
        opacitySlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> opacityValue = newNumber.intValue());
        opacitySlider.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> properties.setOpacity(opacityValue));
    }

    /**
     * Handle button action to allow choosing default save destination directory.
     */
    @FXML
    private void changeDefaultDirectoryPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(changePathButton.getScene().getWindow());

        if(selectedDirectory != null) {
            properties.setDefaultPath(Path.of(selectedDirectory.getAbsolutePath()));
            pathTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    /**
     * Handle button action to open Favorite Input Window GUI.
     */
    @FXML
    private void openFavoriteInputWindow() {
        if(properties.getFavoriteList().size() >= 10) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "You can't have more than 10 favorites");
            alert.showAndWait();
            return;
        }

        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FavoriteInputWindow.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Control that displayed Favorite items are the same as these stored in properties file.
     */
    public void refreshFavorite() {
        List<Properties.Favorite> favoriteList = properties.getFavoriteList();
        List<String> favoriteItemsIDs = favoritesViewItems.stream()
                .map(Node::getId)
                .collect(Collectors.toList());

        for(Properties.Favorite favorite: favoriteList) {
            if(!favoriteItemsIDs.contains(favorite.name())) {
                addFavorite(favorite);
            }
        }

        List<String> favoriteNamesList = favoriteList.stream()
                .map(Properties.Favorite::name)
                .collect(Collectors.toList());

        for(String id: favoriteItemsIDs) {
            if(!favoriteNamesList.contains(id)) {
                removeFavorite(id);
            }
        }
    }

    /**
     * Adds Favorite item to the ListView.
     *
     * @param favorite  Favorite entity which will be added to ListView.
     */
    private void addFavorite(Properties.Favorite favorite) {
        HBox item = new HBox();
        item.setId(favorite.name());
        Button button = new Button();
        button.setOnMouseClicked(e -> {
            removeFavorite(item.getId());
            MainWindowController.getInstance().refreshFavorites();
        });

        item.getChildren().addAll(
                new Label(favorite.name()),
                new Label(favorite.path().toString()),
                button
        );

        favoritesViewItems.add(item);
        favoriteListView.setItems(favoritesViewItems);
    }

    /**
     * Removes Favorite item from ListView.
     *
     * @param name  Name of the Favorite entity which uniquely identifies the ListView item to be removed.
     */
    private void removeFavorite(String name) {
        for(HBox item: favoritesViewItems) {
            if(item.getId().equals(name)) {
                favoritesViewItems.remove(item);
                break;
            }
        }
        properties.removeFromFavorite(name);
        favoriteListView.setItems(favoritesViewItems);
    }

    /**
     * Gets the instance of this controller.
     *
     * @return  Instance of this controller.
     */
    public static SettingsWindowController getInstance() {
        return instance;
    }
}
