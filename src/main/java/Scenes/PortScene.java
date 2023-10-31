package Scenes;

import LinkedList.List;
import LinkedList.Node;
import javafx.application.Platform;
import javafx.geometry.Pos;
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
    private Ship ship;
    public IndividualPort individualPort;
    private ShipScene shipScene;
    private List list;
    private TableView<Port> listView = new TableView();
    private TableView<Ship> shipsAtSeaTableView = new TableView();
    Pane window;
    private Port choosePort;
    private Ship chosenShip;
    private ComboBox<Port> portsToDockAt = new ComboBox<>();
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

        listView.setMaxHeight(500);


        listView.setPlaceholder(new Label("No ports added yet"));

        TableColumn<Port, String> codeColumn = new TableColumn<>("Port Code");
        TableColumn<Port, String> nameColumn = new TableColumn<>("Port Name");
        TableColumn<Port, String> countryColumn = new TableColumn<>("Port Country");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);

        listView.getColumns().addAll(codeColumn,nameColumn,countryColumn);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("portCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("portCountry"));

        //-------------------------------------------------------------

        TableColumn<Ship, String> shipCodeColumn = new TableColumn<>("Ship Code");
        TableColumn<Ship, String> shipNameColumn = new TableColumn<>("Ship Name");
        TableColumn<Ship, String> shipCountryColumn = new TableColumn<>("Ship Country");
        TableColumn<Ship, String> shipPictureColumn = new TableColumn<>("Ship Picture");

        shipCodeColumn.setMinWidth(100);
        shipNameColumn.setMinWidth(100);
        shipCountryColumn.setMinWidth(100);
        shipPictureColumn.setMinWidth(100);
        shipsAtSeaTableView.getColumns().addAll(shipCodeColumn,shipNameColumn,shipCountryColumn,shipPictureColumn);

        shipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("shipCode"));
        shipNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        shipCountryColumn.setCellValueFactory(new PropertyValueFactory<>("shipCountry"));
        shipPictureColumn.setCellValueFactory(new PropertyValueFactory<>("shipPicture"));

        portsToDockAt.setPromptText("Select Port:");
        shipsAtSeaTableView.setPlaceholder(new Label("No ships added yet"));

        Label removeShipLabel = new Label("");
        Button unselectShip = new Button("Unselect Ship");
        unselectShip.setOnAction(event -> {
            chosenShip = null;
            removeShipLabel.setText("");
        });

        shipsAtSeaTableView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY && event.getClickCount() ==2){
                Ship selectedPallet = shipsAtSeaTableView.getSelectionModel().getSelectedItem();
                if(selectedPallet != null){
                    chosenShip = selectedPallet;
                    chosenShip = shipsAtSeaTableView.getSelectionModel().getSelectedItem();
                    removeShipLabel.setText("Ship: "+ chosenShip.getShipName() + " is selected");
                }
            }
        });

        //-------------------------------------------------------------

        if(api.list.isEmpty()) {
            scheduler.scheduleAtFixedRate(this::updateListView, 0, 1, TimeUnit.SECONDS);
            scheduler.scheduleAtFixedRate(this::updateShipView, 0, 1, TimeUnit.SECONDS);
            scheduler.scheduleAtFixedRate(this::updateComboBoxContainer, 0, 1, TimeUnit.SECONDS);
        }
        scheduler.scheduleAtFixedRate(() -> {System.out.println(api.listAllPorts());}, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> {System.out.println(api.list.isEmpty());}, 0, 1, TimeUnit.SECONDS);

        Button saveButton = new Button("Add Port");
        Button button = new Button("Remove Port");

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

        Label removeLabel = new Label("");
        button.setOnAction(e -> {
            api.list.remove(choosePort);
            removeLabel.setText("");
        });

        Button unselect = new Button("Unselect Port");
        unselect.setOnAction(event -> {
            choosePort = null;
            removeLabel.setText("");
        });
        Button updateButton = new Button("Update");
        updateButton.setOnAction(event -> {
            choosePort.setPortName(nameField.getText());
            choosePort.setPortCountry(countryBox.getValue());
            removeLabel.setText("");
        });


        Button dockShip = new Button("Move Ship to Port");
        dockShip.setOnAction(event -> {
            api.moveShipFromSea(portsToDockAt.getValue(),chosenShip);

        });
        shipsAtSeaTableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Ship selectedShip = shipsAtSeaTableView.getSelectionModel().getSelectedItem();
                if (selectedShip != null) {
                    ship = selectedShip;
                    try {
                        Pane individualPortRoot = new Pane();
                        shipScene = new ShipScene(individualPortRoot, mainScene, this, api, selectedShip);
                        mainScene.switchScene(shipScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (event.getButton() == MouseButton.SECONDARY && event.getClickCount() == 2) {
                chosenShip = shipsAtSeaTableView.getSelectionModel().getSelectedItem();
                removeShipLabel.setText(chosenShip.shipName + " is Selected");
            }
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

        VBox leftVBox = new VBox(10);
        leftVBox.getChildren().addAll(portLabel, nameField, error, countryLabel, countryBox, error2, saveButton, button, updateButton, unselect, removeLabel);

        VBox rightVBox = new VBox(10);
        rightVBox.getChildren().addAll(shipsAtSeaTableView, unselectShip,portsToDockAt,dockShip, removeShipLabel);

        HBox centerHBox = new HBox(100);
        centerHBox.getChildren().addAll(listView);

        borderPane.setLeft(leftVBox);
        borderPane.setRight(rightVBox);
        borderPane.setCenter(centerHBox);

        leftVBox.setAlignment(Pos.TOP_LEFT);
        rightVBox.setAlignment(Pos.TOP_RIGHT);
        borderPane.setMinSize(1400,800);

        borderPane.setStyle("-fx-padding: 10px;");

        root.getChildren().add(borderPane);
        root.setMinSize(1800, 800);
    }
    private void updateListView() {
        if (this.api.list != null) {
            Platform.runLater(() -> {
                Node<Port> current = this.api.list.head;
                while (current != null) {
                    Port port = current.data;
                    if (listView.getItems().contains(port)) {
                        int index = listView.getItems().indexOf(port);
                        listView.getItems().set(index, port);
                    } else {
                        listView.getItems().add(port);
                    }
                    current = current.next;
                }
                listView.getItems().removeIf(port -> !this.api.list.contains(port));
            });
        }
    }

    private void updateShipView() {
        if (this.api.shipsAtSea!=null) {
            Platform.runLater(() -> {
                Node<Ship> current = this.api.shipsAtSea.head;
                while (current != null) {
                    Ship ship = current.data;
                    if (shipsAtSeaTableView.getItems().contains(ship)) {
                        int index = shipsAtSeaTableView.getItems().indexOf(ship);
                        shipsAtSeaTableView.getItems().set(index,ship);
                    }else{
                        shipsAtSeaTableView.getItems().add(ship);
                    }
                    current=current.next;
                }
                shipsAtSeaTableView.getItems().removeIf(ship -> !this.api.shipsAtSea.contains(ship));
            });
        }
    }

    private void updateComboBoxContainer() {
        if (api.list != null) {
            Platform.runLater(() -> {
                Port selectedPort = portsToDockAt.getValue();
                portsToDockAt.getItems().clear();
                Node<Port> current = api.list.head;
                while (current != null) {
                    Port port = current.data;
                    portsToDockAt.getItems().add(port);
                    current = current.next;
                }
                if (selectedPort != null && portsToDockAt.getItems().contains(selectedPort)) {
                    portsToDockAt.setValue(selectedPort);
                }
            });
        }
    }

}