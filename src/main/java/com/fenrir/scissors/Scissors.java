package com.fenrir.scissors;

import com.fenrir.scissors.model.ScreenDetector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.util.Objects;

public class Scissors extends Application {
    public static final double MIN_HEIGHT = 79;
    public static final double MIN_WIDTH = 600;
    public static final double TOOLBAR_HEIGHT = 74;
    public static final double MIN_CANVAS_WIDTH = 600;
    public static final double MIN_CANVAS_HEIGHT = 400;

    public static Scissors scissors;

    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        scissors = this;

        this.stage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainWindow.fxml")));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setHeight(MIN_HEIGHT);
        stage.show();
    }

    public void setSize(double width, double height) {
        stage.setWidth(width);
        stage.setHeight(height);
    }

    public Stage getStage() {
        return stage;
    }

    public static Scissors getInstance() {
        return scissors;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
