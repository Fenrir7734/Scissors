module com.fenrir.scissors {
    requires javafx.controls;
    requires org.controlsfx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires javafx.base;
    requires org.slf4j;
    requires org.json;
    requires java.desktop;

    opens com.fenrir.scissors.controllers to javafx.fxml;
    exports com.fenrir.scissors;
    exports com.fenrir.scissors.model;
    exports com.fenrir.scissors.controllers;
    exports com.fenrir.scissors.model.area;
    exports com.fenrir.scissors.model.screenshot;
}