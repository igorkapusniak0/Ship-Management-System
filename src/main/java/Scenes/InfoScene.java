package Scenes;

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
import models.Pallet;
import models.Port;
import Controller.API;
import models.Ship;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InfoScene extends Scene {
    private final API api;
    private Container chosenContainer;
    private final TableView<Pallet> allPallets = new TableView<>();
    private final TableView<Container> allContainers= new TableView<>();
    private final TableView<Ship> allShips = new TableView<>();
    private Pallet chosenPallet;
    private Ship chosenShip;
    private ContainerInPortScene containerInPortScene;
    private ShipScene shipScene;
    private final Label totalValue = new Label("The Total Value of all Pallets is: 0");

    public InfoScene(Pane root, MainScene mainScene, PortScene portScene,API api){
        super(root);
        this.api = api;

        TableColumn<Ship, String> codeColumn = new TableColumn<>("Ship Code");
        TableColumn<Ship, String> nameColumn = new TableColumn<>("Ship Name");
        TableColumn<Ship, String> countryColumn = new TableColumn<>("Ship Country");
        TableColumn<Ship, String> pictureColumn = new TableColumn<>("Ship Picture");
        TableColumn<Ship, String> totalValueColumn = new TableColumn<>("Total Value");
        TableColumn<Ship, String> locationColumn = new TableColumn<>("Ship Location");
        TableColumn<Ship, String> shipPortColumn = new TableColumn<>("Port");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);
        pictureColumn.setMinWidth(100);
        totalValueColumn.setMinWidth(100);
        locationColumn.setMinWidth(100);
        shipPortColumn.setMinWidth(100);
        allShips.getColumns().addAll(codeColumn,nameColumn,countryColumn,pictureColumn,totalValueColumn,locationColumn,shipPortColumn);
        allShips.setMaxHeight(200);
        allShips.setPlaceholder(new Label("No ships added yet"));

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("shipCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("shipCountry"));
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("shipPicture"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        shipPortColumn.setCellValueFactory(new PropertyValueFactory<>("port"));


        TableColumn<Container, String> containerColumn = new TableColumn<>("Container Code");
        TableColumn<Container, String> containerSize = new TableColumn<>("Container Size");
        TableColumn<Container, String> containerLocation = new TableColumn<>("Container Port Location");
        TableColumn<Container, String> containerShip = new TableColumn<>("Container Port Location");
        TableColumn<Container, String> containerPort = new TableColumn<>("Container Port Location");




        containerColumn.setMinWidth(100);
        containerSize.setMinWidth(100);
        containerLocation.setMinWidth(100);
        containerShip.setMinWidth(100);
        containerPort.setMinWidth(100);
        allContainers.getColumns().addAll(containerColumn,containerSize,containerLocation,containerShip,containerPort);
        allContainers.setMaxHeight(200);



        containerColumn.setCellValueFactory(new PropertyValueFactory<>("contCode"));
        containerSize.setCellValueFactory(new PropertyValueFactory<>("contSize"));
        containerLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        containerShip.setCellValueFactory(new PropertyValueFactory<>("ship"));
        containerPort.setCellValueFactory(new PropertyValueFactory<>("port"));

        TableColumn<Pallet, String> descriptionColumn = new TableColumn<>("Pallet Description");
        TableColumn<Pallet, String> quantityColumn = new TableColumn<>("Quantity of Items");
        TableColumn<Pallet, String> valueColumn = new TableColumn<>("Pallet Value");
        TableColumn<Pallet, String> weightColumn = new TableColumn<>("Pallet Value");
        TableColumn<Pallet, String> volumeColumn = new TableColumn<>("Pallet Volume");
        TableColumn<Pallet, String> palletLocationColumn = new TableColumn<>("Pallet Location");
        TableColumn<Pallet, String> palletContainerColumn = new TableColumn<>("Container");
        TableColumn<Pallet, String> palletShipColumn = new TableColumn<>("Ship");
        TableColumn<Pallet, String> palletPortColumn = new TableColumn<>("Port");



        descriptionColumn.setMinWidth(300);
        quantityColumn.setMinWidth(100);
        valueColumn.setMinWidth(100);
        weightColumn.setMinWidth(100);
        volumeColumn.setMinWidth(100);
        palletLocationColumn.setMinWidth(100);
        palletContainerColumn.setMinWidth(100);
        palletShipColumn.setMinWidth(100);
        palletPortColumn.setMinWidth(100);
        allPallets.getColumns().addAll(descriptionColumn,quantityColumn,valueColumn,weightColumn,volumeColumn,palletLocationColumn,palletContainerColumn,palletShipColumn,palletPortColumn);
        allPallets.setMaxHeight(200);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        palletLocationColumn.setCellValueFactory(new PropertyValueFactory<>("palletLocation"));
        palletContainerColumn.setCellValueFactory(new PropertyValueFactory<>("container"));
        palletShipColumn.setCellValueFactory(new PropertyValueFactory<>("ship"));
        palletPortColumn.setCellValueFactory(new PropertyValueFactory<>("port"));

        allPallets.setPlaceholder(new Label("No Pallets Added Yet"));
        allContainers.setPlaceholder(new Label("No Containers Added Yet"));

        Label removePalletLabel = new Label("");


        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updatePalletView, 0, 1, TimeUnit.SECONDS);

        Label selectedCont = new Label("");

        allContainers.setOnMouseClicked(e3 -> {
            if (e3.getButton() == MouseButton.PRIMARY && e3.getClickCount() == 2) {
                Container selectedContainer = allContainers.getSelectionModel().getSelectedItem();
                if (selectedContainer != null) {
                    chosenContainer = selectedContainer;
                    try {
                        Pane individualPortRoot = new Pane();
                        containerInPortScene = new ContainerInPortScene(individualPortRoot,mainScene,portScene,api,selectedContainer);
                        mainScene.switchScene(containerInPortScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else if (e3.getButton() == MouseButton.SECONDARY && e3.getClickCount() == 2){
                chosenContainer = allContainers.getSelectionModel().getSelectedItem();
                if (chosenContainer!=null){
                    selectedCont.setText("Container: "+ chosenContainer.getContCode() + " is selected");
                }
            }
        });
        Button unselectContainer = new Button("Unselect Container");
        unselectContainer.setOnAction(event -> {
            chosenContainer = null;
            selectedCont.setText("");
        });
        Label selectedShip = new Label("");

        Button unselectShip = new Button("Unselect Ship");
        unselectContainer.setOnAction(event -> {
            chosenShip = null;
            selectedShip.setText("");
        });

        allShips.setOnMouseClicked(e3 -> {
            if (e3.getButton() == MouseButton.PRIMARY && e3.getClickCount() == 2) {
                Ship ship = allShips.getSelectionModel().getSelectedItem();
                if (ship != null) {
                    chosenShip = ship;
                    try {
                        Pane individualPortRoot = new Pane();
                        shipScene = new ShipScene(individualPortRoot,chosenShip.getPort(),mainScene,portScene,api,chosenShip);
                        mainScene.switchScene(shipScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else if (e3.getButton() == MouseButton.SECONDARY && e3.getClickCount() == 2){
                Ship ship = allShips.getSelectionModel().getSelectedItem();
                if (ship!=null){
                    removePalletLabel.setText("Ship: "+ chosenShip.getShipName() + " is selected");
                }
            }
        });

        allPallets.setOnMouseClicked(e3 -> {
            if (e3.getButton() == MouseButton.PRIMARY && e3.getClickCount() == 2) {
                Pallet selectedPallet = allPallets.getSelectionModel().getSelectedItem();
                if (selectedPallet != null) {
                    chosenPallet = selectedPallet;
                    try {
                        Pane individualPortRoot = new Pane();
                        containerInPortScene = new ContainerInPortScene(individualPortRoot,mainScene,portScene,api,selectedPallet.getContainer());
                        mainScene.switchScene(containerInPortScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else if (e3.getButton() == MouseButton.SECONDARY && e3.getClickCount() == 2){
                chosenPallet = allPallets.getSelectionModel().getSelectedItem();
                if (chosenPallet!=null){
                    removePalletLabel.setText("Pallet: "+ chosenPallet.getDescription() + " is selected");
                }
            }
        });

        Button unselectPallet = new Button("Unselect Pallet");
        unselectPallet.setOnAction(event -> {
            chosenPallet = null;
            removePalletLabel.setText("");
        });


        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchScene(portScene));

        VBox vBox = new VBox(3);
        VBox vBox1 = new VBox(3);
        VBox vBox3 = new VBox(3);
        vBox.getChildren().addAll(allPallets,unselectPallet,removePalletLabel);
        vBox1.getChildren().addAll(allContainers,unselectContainer,selectedCont);
        vBox3.getChildren().addAll(allShips,unselectShip,selectedShip);
        VBox vBox4 = new VBox(3);
        vBox4.getChildren().addAll(vBox,vBox1,vBox3);
        VBox vBox2 = new VBox(3);
        vBox2.setAlignment(Pos.CENTER);
        vBox2.getChildren().addAll(vBox4,totalValue,button);
        root.getChildren().addAll(vBox2);
    }

    private void updatePalletView() {
        if (api != null) {
            Platform.runLater(() -> {
                allPallets.getItems().clear();
                allContainers.getItems().clear();
                allShips.getItems().clear();
                totalValue.setText("The Total Value of all Pallets is: " + this.api.getTotalValue());
                Node<Port> portNode = API.list.head;
                while (portNode != null) {
                    Port currentPort = portNode.data;
                    Node<Ship> shipNode = portNode.data.ships.head;
                    while (shipNode!=null){
                        Ship currentShip = shipNode.data;
                        allShips.getItems().add(currentShip);
                        Node<Container> containerNode = shipNode.data.containers.head;
                        while (containerNode!=null){
                            Container currentContainer = containerNode.data;
                            allContainers.getItems().addAll(currentContainer);
                            Node<Pallet> palletNode = containerNode.data.pallets.head;
                            while (palletNode!=null){
                                Pallet currentPallet = palletNode.data;
                                allPallets.getItems().add(currentPallet);
                                palletNode = palletNode.next;
                            }
                            containerNode = containerNode.next;
                        }
                        shipNode =shipNode.next;
                    }
                    Node<Container> containerNode = currentPort.containersInPort.head;
                    while (containerNode != null) {
                        Container currentContainer = containerNode.data;
                        allContainers.getItems().addAll(currentContainer);
                        Node<Pallet> palletNode = currentContainer.pallets.head;
                        while (palletNode != null) {
                            Pallet currentPallet = palletNode.data;
                            allPallets.getItems().add(currentPallet);
                            palletNode = palletNode.next;
                        }
                        containerNode = containerNode.next;
                    }
                    portNode = portNode.next;
                }
                Node<Ship> shipNode = API.shipsAtSea.head;
                while (shipNode != null) {
                    Ship currentShip = shipNode.data;
                    allShips.getItems().add(currentShip);
                    Node<Container> containerNode = currentShip.containers.head;
                    while (containerNode != null) {
                        Container currentContainer = containerNode.data;
                        allContainers.getItems().addAll(currentContainer);
                        Node<Pallet> palletNode = currentContainer.pallets.head;
                        while (palletNode != null) {
                            Pallet currentPallet = palletNode.data;
                            allPallets.getItems().add(currentPallet);
                            palletNode = palletNode.next;
                        }
                        containerNode = containerNode.next;
                    }
                    shipNode = shipNode.next;
                }
            });
        }
    }

}
