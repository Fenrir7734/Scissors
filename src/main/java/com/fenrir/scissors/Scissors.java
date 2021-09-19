package com.fenrir.scissors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * @author Fenrir7734
 * @version v1.0.1 September 17, 2021
 */
public class Scissors extends Application {

    /**
     * Minimum primary stage width.
     */
    public static final double MIN_WIDTH = 600;

    /**
     * Height of the bar with control buttons.
     */
    public static final double CONTROL_BUTTONS_BAR_HEIGHT = 79;

    /**
     * Height of the toolbar
     */
    public static final double TOOLBAR_HEIGHT = 74;

    /**
     * Minimum width of the canvas on which screenshot is drawn.
     */
    public static final double MIN_CANVAS_WIDTH = 600;

    /**
     * Minimum height of the canvas on which screenshot is drawn.
     */
    public static final double MIN_CANVAS_HEIGHT = 400;

    private static Scissors instance;

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        instance = this;

        this.stage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainWindow.fxml")));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setHeight(CONTROL_BUTTONS_BAR_HEIGHT);
        stage.show();
    }

    /**
     * Sets size of the primary stage.
     *
     * @param width width to set for stage.
     * @param height    height to set for stage.
     */
    public void setSize(double width, double height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    /**
     * Returns primary stage.
     *
     * @return  primary stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Gets the instance of this class.
     *
     * @return  Instance of this class.
     */
    public static Scissors getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
