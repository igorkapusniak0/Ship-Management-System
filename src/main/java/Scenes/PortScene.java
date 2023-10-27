package Scenes;

import LinkedList.List;
import LinkedList.Node;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import models.Container;
import models.Ship;
import models.Port;
import utils.Utilities;
import Controller.API;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PortScene extends Scene {
    public API api;
    private MainScene mainScene;
    public Port port;
    private Ship ships;
    public IndividualPort individualPort;
    private SeaScene seaScene;
    private List list;
    private TableView<Port> listView = new TableView();
    Pane window;
    private Port choosePort;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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


        listView.setPlaceholder(new Label("No ports added yet"));

        TableColumn<Port, String> codeColumn = new TableColumn<>("Port Code");
        TableColumn<Port, String> nameColumn = new TableColumn<>("Port Name");
        TableColumn<Port, String> countryColumn = new TableColumn<>("Port Country");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);

        listView.getColumns().addAll(codeColumn,nameColumn,countryColumn);
        listView.setMinWidth(1000);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("portCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("portCountry"));

        if(api.list.isEmpty()) {
            scheduler.scheduleAtFixedRate(this::updateListView, 0, 1, TimeUnit.SECONDS);

        }
        scheduler.scheduleAtFixedRate(() -> {System.out.println(api.listAllPorts());}, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> {System.out.println(api.list.isEmpty());}, 0, 1, TimeUnit.SECONDS);

        Button saveButton = new Button("Add Port");
        Button button = new Button("Remove Ship");

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String country = countryBox.getValue();
            String code = Utilities.uniqueCodeGenerator();
            boolean portNameExists = listView.getItems().stream().anyMatch(port -> port.getPortName().equals(name));
            boolean portCodeExists = listView.getItems().stream().anyMatch(port -> port.getPortName().equals(code));

            if (!name.isBlank() && countryBox.getValue() != null && !portNameExists && !portCodeExists) {
                Port newPort = new Port(name, country, code, new List<Ship>(), new List<Container>());
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

        Label removeLabel = new Label("a");
        button.setOnAction(e -> {
            api.list.remove(choosePort);
        });



        listView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
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
            } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2) {
                choosePort = listView.getSelectionModel().getSelectedItem();
                removeLabel.setText(choosePort.portName + " is Selected");
            }
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(portLabel, nameField, error, countryLabel, countryBox, error2, saveButton, button,removeLabel);

        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(vBox, listView);

        borderPane.setCenter(hBox);
        borderPane.setStyle(" -fx-padding: 10px;");

        root.getChildren().add(borderPane);
        root.setMinSize(1800, 800);
    }
    private void updateListView() {
        if (this.api.list != null) {
            Platform.runLater(() -> {
                Node<Port> current = this.api.list.head;
                while (current != null){
                    if(!(listView.getItems().contains(current.data))) {
                        listView.getItems().add(current.data);
                    }
                    current = current.next;

                }
                listView.getItems().removeIf(port -> !this.api.list.contains(port));
            });
        }
    }
}