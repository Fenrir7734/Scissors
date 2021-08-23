package com.fenrir.scissors.controllers;

import com.fenrir.scissors.Scissors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainWindowController {
    @FXML public HBox screenNameContainer;
    @FXML private AnchorPane mainWindowPane;
    @FXML private VBox mainWindowVBox;

    @FXML private ButtonBar mainButtonBar;

    @FXML private Button captureButton;
    @FXML private Button settingsButton;
    @FXML private Button copyButton;
    @FXML private Button saveButton;
    @FXML private MenuButton saveAsMenuButton;

    @FXML private TextField screenNameField;

    @FXML private HBox toolbox;
    @FXML private Button penTool;
    @FXML private Button markerTool;
    @FXML private Button blurTool;
    @FXML private SplitMenuButton shapesTool;
    @FXML private MenuItem lineTool;
    @FXML private MenuItem arrowTool;
    @FXML private MenuItem rectangleTool;
    @FXML private MenuItem ellipseTool;
    @FXML private Button rubberTool;
    @FXML private SplitMenuButton colorTool;

    @FXML private Canvas screenDisplay;

    @FXML
    public void initialize() {
        screenNameField.setVisible(false);
        screenNameField.setManaged(false);
        toolbox.setVisible(false);
        toolbox.setManaged(false);
        screenDisplay.setVisible(false);
        screenDisplay.setManaged(false);
    }

    @FXML
    public void onClick(ActionEvent event) {
        screenNameField.setVisible(true);
        screenNameField.setManaged(true);
        toolbox.setVisible(true);
        toolbox.setManaged(true);
        screenDisplay.setVisible(true);
        screenDisplay.setManaged(true);
        Scissors.getInstance().setSize(Scissors.MIN_HEIGHT + 75);
    }

}
