module models {
    requires javafx.graphics;
    requires javafx.controls;
    exports models;
    opens models to javafx.base, javafx.fxml; // Open to both javafx.base and javafx.fxml
    exports Scenes to javafx.graphics, javafx.fxml;
}