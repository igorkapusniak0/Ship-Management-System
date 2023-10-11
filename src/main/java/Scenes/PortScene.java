package Scenes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import models.Container;
import models.Ship;

public class PortScene extends Application {

    Stage window;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("WorkSheet3");
        BorderPane borderPane = new BorderPane();

        TextField nameField = new TextField();
        TextField codeField = new TextField();
        ComboBox<Ship> shipField = new ComboBox<>();
        ComboBox<Container> contField = new ComboBox<>();

        /*Ship ships =
        contField.getItems().addAll(ships);*/


        nameField.setMaxWidth(300);
        codeField.setMaxWidth(300);
        contField.setMaxWidth(300);
        shipField.setMaxWidth(300);
        shipField.setMinHeight(300);
        Label portLabel = new Label("Port Name");
        Label codeLabel = new Label("Port Code");
        Label shipsLabel = new Label("Ships");
        Label contLabel = new Label("Containers");
        nameField.setPromptText("Enter your name:");
        codeField.setPromptText("Enter your age:");
        contField.setPromptText("Enter appointment time:");
        shipField.setPromptText("Enter your issue:");

        TableView<Appointment> tableView = new TableView<>();

        TableColumn<Appointment, String> timeColumn = new TableColumn<>("Time");
        TableColumn<Appointment, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Appointment, String> ageColumn = new TableColumn<>("Age");
        TableColumn<Appointment, String> descriptionColumn = new TableColumn<>("Issue");

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setMinWidth(1000);
        tableView.getColumns().addAll(timeColumn, nameColumn, ageColumn,descriptionColumn);
        tableView.setEditable(true);
        tableView.setMinWidth(1000);

        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            String time = contField.getValue();
            String name = nameField.getText();
            int age = Integer.parseInt(codeField.getText());
            String description = shipField.getText();
            tableView.getItems().add(new Appointment(time, name, age, description));
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(portLabel, nameField, shipsLabel, codeField, codeLabel, contField, contLabel, shipField, saveButton);

        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(vBox, tableView);

        borderPane.setCenter(hBox);
        borderPane.setStyle(" -fx-padding: 10px;");

        Scene scene = new Scene(borderPane, 1500, 800);
        window.setScene(scene);
        window.show();
    }

    public static class Appointment {
        private String name = "";
        private String description = "";
        private int age = 0;
        private String time = "";

        public Appointment(String time, String name, int age, String description) {
            this.time = time;
            this.name = name;
            this.description = description;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }

        public String getDescription() {
            return description;
        }

        public int getAge() {
            return age;
        }
    }
}
