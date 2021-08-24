package com.fenrir.scissors.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SettingsWindowController {
    @FXML private CheckBox automaticSaveCheckbox;
    @FXML private CheckBox automaticCopyCheckbox;
    @FXML private TextField pathTextField;
    @FXML private Button changePathButton;
    @FXML private Slider opacitySlider;
    @FXML private ListView favoriteListView;
    @FXML private Button addToFavoriteButton;
}
