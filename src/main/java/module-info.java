module com.fenrir.scissors {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.fenrir.scissors.controllers to javafx.fxml;
    exports com.fenrir.scissors;
}