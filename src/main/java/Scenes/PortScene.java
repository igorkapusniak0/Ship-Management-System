package Scenes;

import LinkedList.List;
import Scenes.IndividualPort;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.Container;
import models.Ship;
import models.Port;
import utils.Utilities;
import utils.Countries;
import Controller.API;

import java.io.FileNotFoundException;

public class PortScene extends Scene {
    public API api;
    private MainScene mainScene;
    public Port port;
    private Ship ships;
    public IndividualPort individualPort;
    Pane window;

    public PortScene(Pane root, MainScene mainScene) {
        super(root);
        this.mainScene = mainScene;
        window = root;
        this.api = new API();
        BorderPane borderPane = new BorderPane();

        TextField nameField = new TextField();
        TextField countryField = new TextField();
        ComboBox<String> countryBox = new ComboBox<>();

        countryBox.getItems().addAll(Utilities.countries);

        nameField.setMaxWidth(300);
        countryField.setMaxWidth(300);

        Label portLabel = new Label("Port Name");
        Label countryLabel = new Label("Country");
        Label error = new Label();
        Label error2 = new Label();

        nameField.setPromptText("Enter Port Name:");
        countryField.setPromptText("Enter Country:");
        countryBox.setPromptText("Select Country:");

        TableView<Port> listView = new TableView();
        listView.setPlaceholder(new Label("No ports added yet"));

        TableColumn<Port, String> codeColumn = new TableColumn<>("Ship Code");
        TableColumn<Port, String> nameColumn = new TableColumn<>("Ship Name");
        TableColumn<Port, String> countryColumn = new TableColumn<>("Ship Country");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);

        listView.getColumns().addAll(codeColumn,nameColumn,countryColumn);
        listView.setMinWidth(1000);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("portCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("portCountry"));


        Button saveButton = new Button("Add Port");
        Button button = new Button("sd");

        button.setOnAction(e -> {
            String data = api.getPortAtIndex(0);
            if (data != null) {
                System.out.println("Data at index 0: " + data);
            } else {
                System.out.println("Data at index 0 is null.");
            }
        });

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String country = countryBox.getValue();
            String code = Utilities.uniqueCodeGenerator();
            boolean portNameExists = listView.getItems().stream().anyMatch(port -> port.getPortName().equals(name));
            boolean portCodeExists = listView.getItems().stream().anyMatch(port -> port.getPortName().equals(code));

            if (!name.isBlank() && countryBox.getValue() != null && !portNameExists && !portCodeExists) {
                Port newPort = new Port(name, country, code, new List<Ship>(), new List<Container>());
                listView.getItems().add(newPort);
                api.addPort(newPort);
                error.setText("");
                error2.setText("");
            } else {
                if (name.isBlank()) {
                    error.setText("Field cannot be empty");
                } else {
                    error.setText("");
                }
                if (countryBox.getValue() == null) {
                    error2.setText("Field cannot be empty");
                } else {
                    error2.setText("");
                }
                if (portNameExists) {
                    error.setText("Port with the same name already exists");
                }
                if (portCodeExists) {
                    error2.setText("Port with the code name already exists");
                }
            }
        });

        listView.setOnMouseClicked(e3 -> {
            if (e3.getClickCount() == 2) {
                Port selectedPort = listView.getSelectionModel().getSelectedItem();
                if (selectedPort != null) {
                    port = selectedPort;
                    try {
                        Pane individualPortRoot = new Pane();
                        individualPort = new IndividualPort(individualPortRoot, mainScene, this, api, port);
                        mainScene.switchScene(individualPort);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(portLabel, nameField, error, countryLabel, countryBox, error2, saveButton, button);

        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(vBox, listView);

        borderPane.setCenter(hBox);
        borderPane.setStyle(" -fx-padding: 10px;");

        root.getChildren().add(borderPane);
        root.setMinSize(1800, 800);
    }
}