module Scenes {
    requires javafx.controls;
    requires javafx.fxml;

    opens Scenes to javafx.fxml;
    exports Scenes;
}