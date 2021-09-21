package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import com.fenrir.scissors.model.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
 *
 * @author Fenrir7734
 * @version v1.0.1 September 15, 2021
 */
public class SettingsWindowController {
    private static SettingsWindowController instance;
    public TabPane settingsPane;

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

        automaticSaveCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            properties.setSaveToDefault(newValue);
            properties.write();
        });
        automaticCopyCheckbox.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            properties.setSaveToClipboard(newValue);
            properties.write();
        });
        opacitySlider.valueProperty().addListener((observableValue, oldNumber, newNumber) -> opacityValue = newNumber.intValue());
        opacitySlider.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            properties.setOpacity(opacityValue);
            properties.write();
        });
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
            properties.write();

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
     * Handles action to open hyperlink.
     *
     * @param event event
     */
    @FXML
    private void openHyperlink(ActionEvent event) {
        String url = ((Hyperlink) event.getTarget()).getText();
        Scissors.getInstance().getHostServices().showDocument(url);
    }

    /**
     * Control that displayed Favorite items placed in ListView are the same as these stored in properties file.
     *
     * @see com.fenrir.scissors.model.Properties.Favorite
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
     * @param favorite  {@link com.fenrir.scissors.model.Properties.Favorite Favorite} class instance which content will
     *                                                                                be added to ListView.
     */
    private void addFavorite(Properties.Favorite favorite) {
        HBox item = createItem(favorite);
        favoritesViewItems.add(item);
        favoriteListView.setItems(favoritesViewItems);
    }

    /**
     * Creates item containing name and path from {@link com.fenrir.scissors.model.Properties.Favorite}.
     *
     * @param favorite {@link com.fenrir.scissors.model.Properties.Favorite Favorite} class instance which content will
     *                                                                                 be added to ListView.
     * @return          HBox containing name and path from {@link com.fenrir.scissors.model.Properties.Favorite}.
     */
    private HBox createItem(Properties.Favorite favorite) {
        HBox item = new HBox();
        item.setId(favorite.name());
        item.getStyleClass().add("favorite-container");

        Button button = new Button();
        button.setAlignment(Pos.BOTTOM_CENTER);
        button.getStyleClass().add("favorite-delete");
        button.setOnMouseClicked(e -> {
            removeFavorite(item.getId());
            MainWindowController.getInstance().refreshFavorites();
        });

        Label nameLabel = new Label(favorite.name());
        nameLabel.getStyleClass().add("favorite-name");

        Label pathLabel = new Label(favorite.path().toString());
        pathLabel.getStyleClass().add("favorite-path");

        VBox nameContainer = new VBox(nameLabel, pathLabel);
        nameContainer.getStyleClass().add("favorite-name-container");

        item.getChildren().addAll(nameContainer, button);

        return item;
    }

    /**
     * Removes Favorite item from ListView.
     *
     * @param name  Name of the {@link com.fenrir.scissors.model.Properties.Favorite Favorite} class instance which
     *              uniquely identifies the ListView item to be removed.
     */
    private void removeFavorite(String name) {
        for(HBox item: favoritesViewItems) {
            if(item.getId().equals(name)) {
                favoritesViewItems.remove(item);
                break;
            }
        }
        properties.removeFromFavorite(name);
        properties.write();

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
