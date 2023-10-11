package Scenes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import models.Container;
import models.Ship;
import models.Port;
import utils.Utilities;

import java.util.Collection;

public class PortScene extends Application {
    private Port port;
    private Ship ships;
    private Container containersInPort;

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
        TextField countryField = new TextField();
        /*ComboBox<Ship> shipField = new ComboBox<>();
        ComboBox<Container> contField = new ComboBox<>();


        contField.getItems().addAll();*/


        nameField.setMaxWidth(300);
        countryField.setMaxWidth(300);
        /*contField.setMaxWidth(300);
        shipField.setMaxWidth(300);
        shipField.setMinHeight(300);*/
        Label portLabel = new Label("Port Name");
        Label countryLabel = new Label("Country");
        Label shipsLabel = new Label("Ships");
        Label contLabel = new Label("Containers");
        nameField.setPromptText("Enter Port Name:");
        countryField.setPromptText("Enter Country:");
        //contField.setPromptText("Enter appointment time:");
        //shipField.setPromptText("Enter your issue:");

        TableView<Port> tableView = new TableView<>();

        TableColumn<Port, String> codeColumn = new TableColumn<>("Port Code");
        TableColumn<Port, String> nameColumn = new TableColumn<>("Port Name");
        TableColumn<Port, String> countryColumn = new TableColumn<>("Port Country");
        TableColumn<Port, String> shipColumn = new TableColumn<>("Ships in Port");
        TableColumn<Port, String> containerColumn = new TableColumn<>("Containers in Port");

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("portCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("portCountry"));
        shipColumn.setCellValueFactory(new PropertyValueFactory<>(null));
        containerColumn.setCellValueFactory(new PropertyValueFactory<>(null));
        containerColumn.setMinWidth(1000);
        tableView.getColumns().addAll(codeColumn, nameColumn, countryColumn,shipColumn,containerColumn);
        tableView.setEditable(true);
        tableView.setMinWidth(1000);

        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String country = countryField.getText();
            String code = Utilities.uniqueCodeGenerator();
            tableView.getItems().add(new Port(name, country,code));
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(portLabel, nameField, countryLabel, countryField, saveButton);

        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(vBox, tableView);

        borderPane.setCenter(hBox);
        borderPane.setStyle(" -fx-padding: 10px;");

        Scene scene = new Scene(borderPane, 1500, 800);
        window.setScene(scene);
        window.show();
    }

}
