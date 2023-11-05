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
import javafx.scene.text.Font;
import models.Container;
import models.Port;
import Controller.API;
import models.Ship;
import utils.Utilities;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class IndividualPort extends Scene {
    private final Port port;
    private final TableView<Ship> shipListView = new TableView<>();
    private final TableView<Container> containerListView = new TableView<>();
    private Ship ship;
    private ContainerInPortScene containerInPortScene;
    private ShipScene shipScene;
    private Container chosenContainer;
    private Ship chosenShip;
    private final ComboBox<Ship> shipsToLoad = new ComboBox<>();
    private final ComboBox<Port> shipsToDock = new ComboBox<>();


    public IndividualPort(Pane root, MainScene mainScene, PortScene portScene, API api, Port port) throws FileNotFoundException {
        super(root);
        this.port = portScene.port;

        Label showPortName;
        showPortName = new Label();
        showPortName.setFont(new Font("Arial", 50));

        if(port!=null) {
            showPortName.setText("Port: "+port.getPortName());
        }


        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxHeight(50);
        vBox.setStyle(" -fx-padding: 40px;");

        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setMinSize(700, 600);
        vBox1.setStyle(" -fx-padding: 40px;");

        VBox vBox2 = new VBox(10);
        vBox2.setAlignment(Pos.TOP_RIGHT);
        vBox2.setMinSize(700, 600);
        vBox2.setStyle(" -fx-padding: 40px;");

        Label addShip = new Label("Add Ship to Port");
        addShip.setFont(new Font("Arial",20));
        addShip.setAlignment(Pos.TOP_CENTER);
        Label addContainer = new Label("Add Container to Port");
        addContainer.setFont(new Font("Arial",20));
        addContainer.setAlignment(Pos.TOP_CENTER);
        Label error = new Label();
        Label error1 = new Label();
        Label error2 = new Label();
        Label error3 = new Label();

        Label shipNameLabel = new Label("Ship Name");
        Label shipPictureLabel = new Label("Ship Picture");
        Label shipCountryLabel = new Label("Ship Country");

        Label contSizeLabel = new Label("Container Size");

        TextField shipName = new TextField();
        TextField shipPicture = new TextField();
        ComboBox<String> shipCountry = new ComboBox<>();
        shipCountry.getItems().addAll(Utilities.countries);
        shipName.setPromptText("Enter Ship Name:");
        shipName.setMaxWidth(300);
        shipCountry.setPromptText("Select Ship Country:");
        shipCountry.setMaxWidth(300);
        shipPicture.setPromptText("Enter Ship picture:");
        shipPicture.setMaxWidth(300);

        ComboBox<Integer> contSize = new ComboBox<>();
        contSize.setPromptText("Select Container Size");
        contSize.getItems().addAll(10,20,40);
        contSize.setMaxWidth(300);
        TableColumn<Ship, String> codeColumn = new TableColumn<>("Ship Code");
        TableColumn<Ship, String> nameColumn = new TableColumn<>("Ship Name");
        TableColumn<Ship, String> countryColumn = new TableColumn<>("Ship Country");
        TableColumn<Ship, String> pictureColumn = new TableColumn<>("Ship Picture");
        TableColumn<Ship, String> totalValueColumn = new TableColumn<>("Total Value");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);
        pictureColumn.setMinWidth(100);
        totalValueColumn.setMinWidth(100);
        shipListView.getColumns().addAll(codeColumn,nameColumn,countryColumn,pictureColumn,totalValueColumn);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("shipCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("shipCountry"));
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("shipPicture"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        shipListView.setPlaceholder(new Label("No ships added yet"));

        containerListView.setPlaceholder(new Label("No containers added yet"));

        TableColumn<Container, String> codeCont = new TableColumn<>("Container Code");
        TableColumn<Container, Integer> contSizeColumn = new TableColumn<>("Container Size (feet^3)");
        TableColumn<Container, Double> totalPalletValue = new TableColumn<>("Total Value");


        codeCont.setMinWidth(200);
        contSizeColumn.setMinWidth(200);
        totalPalletValue.setMinWidth(200);

        containerListView.getColumns().addAll(codeCont,contSizeColumn,totalPalletValue);

        codeCont.setCellValueFactory(new PropertyValueFactory<>("contCode"));
        contSizeColumn.setCellValueFactory(new PropertyValueFactory<>("contSize"));
        totalPalletValue.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        TextField searchShip = new TextField();
        searchShip.setPromptText("Search Ship");
        TextField searchCont = new TextField();
        searchCont.setPromptText("Search Container");


        Consumer<Container> setContTotalValueAction = Container::setTotalValue;
        Consumer<Ship> setShipTotalValueAction = Ship::setTotalValue;

        if(port!=null&&port.ships!=null&&port.containersInPort!=null) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            if (port.ships!=null){
                scheduler.scheduleAtFixedRate(() -> api.updateListView(searchShip.getText(),shipListView,this.port.ships.head,setShipTotalValueAction), 0, 1, TimeUnit.SECONDS);
                scheduler.scheduleAtFixedRate(this::updateComboBoxShip,0,1,TimeUnit.SECONDS);
            }
            if (port.containersInPort!=null){
                scheduler.scheduleAtFixedRate(() -> api.updateListView(searchCont.getText(),containerListView,this.port.containersInPort.head,setContTotalValueAction), 0, 1, TimeUnit.SECONDS);
                scheduler.scheduleAtFixedRate(this::updateComboBoxContainer,0,1,TimeUnit.SECONDS);
            }
        }

        Button saveContButton = new Button("Add Container");

        saveContButton.setOnAction(event -> {
            int size = contSize.getValue();
            if (contSize.getItems().isEmpty()){
                error.setText("Container Size cannot be empty");
            }else {
                Container newContainer = new Container(size,new List<>(),this.port);
                if (port!=null){
                    port.addContainer(newContainer);
                    error.setText("");
                }
            }

        });

        shipsToDock.setPromptText("Select Port:");
        shipsToLoad.setPromptText("Select Ship:");

        Label removeContLabel = new Label("");
        Button removeContainer = new Button("Remove Container");

        removeContainer.setOnAction(event -> {
            if (chosenContainer!=null){
                if (port!=null){
                    port.removeContainer(chosenContainer);
                    removeContLabel.setText("");
                }
            }
        });

        Label removeShipLabel = new Label("");
        Button removeShip = new Button("Remove Ship");
        removeShip.setOnAction(event -> {
            if (chosenShip!=null && port!=null){
                port.removeShip(chosenShip);
                removeShipLabel.setText("");
            }
        });

        containerListView.setOnMouseClicked(e3 -> {
            if (e3.getButton() == MouseButton.PRIMARY && e3.getClickCount() == 2) {
                Container selectedContainer = containerListView.getSelectionModel().getSelectedItem();
                if (selectedContainer != null) {
                    try {
                        Pane individualPortRoot = new Pane();
                         containerInPortScene = new ContainerInPortScene(individualPortRoot, mainScene,portScene , api, selectedContainer);
                        mainScene.switchScene(containerInPortScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else if (e3.getButton() == MouseButton.SECONDARY && e3.getClickCount() == 2){
                chosenContainer = containerListView.getSelectionModel().getSelectedItem();
                if (chosenContainer!=null){
                    removeContLabel.setText("Container: "+chosenContainer.getContCode() + " is selected");
                }
            }
        });

        Button unselectContainer = new Button("Unselect Container");
        unselectContainer.setOnAction(event -> {
            chosenContainer = null;
            removeContLabel.setText("");
        });

        Button unselectShip = new Button("Unselect Ship");
        unselectShip.setOnAction(event -> {
            chosenShip = null;
            removeShipLabel.setText("");
        });
        
        shipListView.setOnMouseClicked(e3 -> {
            if (e3.getButton() == MouseButton.PRIMARY && e3.getClickCount() == 2) {
                Ship selectedShip = shipListView.getSelectionModel().getSelectedItem();
                if (selectedShip != null) {
                    ship = selectedShip;
                    try {
                        Pane individualPortRoot = new Pane();
                        shipScene = new ShipScene(individualPortRoot, port, mainScene,portScene,api,selectedShip);
                        mainScene.switchScene(shipScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else if (e3.getButton() == MouseButton.SECONDARY && e3.getClickCount() == 2){
                chosenShip = shipListView.getSelectionModel().getSelectedItem();
                if (chosenShip!=null){
                    removeShipLabel.setText("Ship: "+chosenShip.getShipName() + " is selected");
                }
            }
        });


        Button loadContainer = new Button("Move Container Onto Ship");
        loadContainer.setOnAction(event -> {
            if (!shipsToLoad.getItems().isEmpty() && (chosenContainer != null) && (port!=null)){
                api.loadContainer(port,shipsToLoad.getValue(),chosenContainer);
            }
        });
        Button moveShipToDifPort = new Button("Move Ship to Other Port");
        moveShipToDifPort.setOnAction(event -> {
            if (!shipsToDock.getItems().isEmpty() && (chosenShip != null) && (port!=null)){
                api.moveShip(port,shipsToDock.getValue(),chosenShip);
            }
        });

        Button saveShipButton = new Button("Add Ship");
        saveShipButton.setOnAction(event -> {
            String name = shipName.getText();
            String country = shipCountry.getValue();
            String picture = shipPicture.getText();

            if (!name.isBlank() && country != null && !picture.isBlank() && port!=null) {
                Ship newShip = new Ship(name, country,picture);
                port.ships.add(newShip);
                error1.setText("");
                error2.setText("");
                error3.setText("");

            } else {
                if (name.isBlank()) {
                    error1.setText("Ship Name cannot be empty");
                } else {
                    error1.setText("");
                }
                if (country == null) {
                    error2.setText("Ship Country cannot be empty");
                } else {
                    error2.setText("");
                }
                if (picture.isBlank()){
                    error3.setText("Ship picture cannot be empty");
                }else {
                    error3.setText("");
                }
            }

        });
        Button updateContButton = new Button("Update Container");
        updateContButton.setOnAction(event -> {
            if (chosenContainer!=null){
                chosenContainer.setContSize(contSize.getValue());
                 chosenContainer.setTotalValue();
                removeContLabel.setText("");
            }
        });

        Button undockShip = new Button("UnDock Ship");
        undockShip.setOnAction(event -> {
            if (chosenShip!=null&&port!=null){
                api.moveShipToSea(port,chosenShip);
            }
        });

        Button updateShipButton = new Button("Update Ship");
        updateShipButton.setOnAction(event -> {
            if (chosenShip!=null){
                chosenShip.setShipName(shipName.getText());
                chosenShip.setShipCountry(shipCountry.getValue());
                chosenShip.setShipPicture(shipPicture.getText());
                chosenShip.setTotalValue();
                removeShipLabel.setText("");
            }
        });


        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchToScene2());

        vBox.getChildren().addAll(showPortName);

        vBox1.getChildren().addAll(addShip,shipNameLabel,shipName, error1,shipPictureLabel,shipPicture, error3,shipCountryLabel,shipCountry, error2,shipListView,searchShip ,saveShipButton,removeShip,updateShipButton,unselectShip,shipsToDock,moveShipToDifPort,undockShip,removeShipLabel);

        vBox2.getChildren().addAll(addContainer,contSizeLabel,contSize,error,containerListView,searchCont,saveContButton,removeContainer,unselectContainer,updateContButton,shipsToLoad,loadContainer,removeContLabel);

        HBox hBox=new HBox();
        hBox.getChildren().addAll(button);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setStyle(" -fx-padding: 40px;");

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(vBox1);
        borderPane.setRight(vBox2);
        borderPane.setTop(vBox);
        borderPane.setCenter(hBox);

        root.getChildren().addAll(borderPane);



    }


    private void updateComboBoxShip() {
        if (this.port != null && this.port.ships != null) {
            Platform.runLater(() -> {
                Ship selectedShip = shipsToLoad.getValue();
                shipsToLoad.getItems().clear();
                Node<Ship> current = this.port.ships.head;
                while (current != null) {
                    Ship ship = current.data;
                    shipsToLoad.getItems().add(ship);
                    current = current.next;
                }
                if (selectedShip != null && shipsToLoad.getItems().contains(selectedShip)) {
                    shipsToLoad.setValue(selectedShip);
                }
            });
        }
    }

    private void updateComboBoxContainer() {
        if (this.port != null && API.list != null) {
            Platform.runLater(() -> {
                Port selectedPort = shipsToDock.getValue();
                shipsToDock.getItems().clear();
                Node<Port> current = API.list.head;
                while (current != null) {
                    Port port = current.data;
                    shipsToDock.getItems().add(port);
                    current = current.next;
                }
                if (selectedPort != null && shipsToDock.getItems().contains(selectedPort)) {
                    shipsToDock.setValue(selectedPort);
                }
            });
        }
    }

}
