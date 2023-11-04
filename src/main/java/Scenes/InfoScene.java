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
import utils.Utilities;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InfoScene extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;
    private Container container;
    private Container chosenContainer;
    private TableView<Pallet> allPallets = new TableView();
    private TableView<Container> allContainers= new TableView();
    private Pallet chosenPallet;
    private ContainerInPortScene containerInPortScene;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Label totalValue = new Label("The Total Value of all Pallets is: 0");

    public InfoScene(Pane root, MainScene mainScene, PortScene portScene,API api){
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;


        TableColumn<Container, String> containerColumn = new TableColumn<>("Container Code");
        TableColumn<Container, String> containerSize = new TableColumn<>("Container Size");
        TableColumn<Container, String> containerPortLocation = new TableColumn<>("Container Port Location");
        TableColumn<Container, String> containerShipLocation = new TableColumn<>("Container Ship Location");


        containerColumn.setMinWidth(200);
        containerSize.setMinWidth(200);
        containerPortLocation.setMinWidth(200);
        containerShipLocation.setMinWidth(200);
        allContainers.getColumns().addAll(containerColumn,containerSize,containerPortLocation,containerShipLocation);



        containerColumn.setCellValueFactory(new PropertyValueFactory<>("contCode"));
        containerSize.setCellValueFactory(new PropertyValueFactory<>("contSize"));
        containerPortLocation.setCellValueFactory(new PropertyValueFactory<>("port"));
        containerShipLocation.setCellValueFactory(new PropertyValueFactory<>("ship"));

        TableColumn<Pallet, String> descriptionColumn = new TableColumn<>("Pallet Description");
        TableColumn<Pallet, String> quantityColumn = new TableColumn<>("Quantity of Items");
        TableColumn<Pallet, String> valueColumn = new TableColumn<>("Pallet Value");
        TableColumn<Pallet, String> weightColumn = new TableColumn<>("Pallet Value");
        TableColumn<Pallet, String> volumeColumn = new TableColumn<>("Pallet Volume");
        TableColumn<Pallet, String> palletLocationColumn = new TableColumn<>("Pallet Location");


        descriptionColumn.setMinWidth(500);
        quantityColumn.setMinWidth(100);
        valueColumn.setMinWidth(100);
        weightColumn.setMinWidth(100);
        volumeColumn.setMinWidth(100);
        allPallets.getColumns().addAll(descriptionColumn,quantityColumn,valueColumn,weightColumn,volumeColumn,palletLocationColumn);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        palletLocationColumn.setCellValueFactory(new PropertyValueFactory<>("container"));

        allPallets.setPlaceholder(new Label("No Pallets Added Yet"));
        allContainers.setPlaceholder(new Label("No Containers Added Yet"));

        Label removePalletLabel = new Label("");


        scheduler.scheduleAtFixedRate(() -> updatePalletView(), 0, 1, TimeUnit.SECONDS);

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

        VBox vBox = new VBox(10);
        VBox vBox1 = new VBox(10);
        vBox.getChildren().addAll(allPallets,unselectPallet,removePalletLabel);
        vBox1.getChildren().addAll(allContainers,unselectContainer,selectedCont);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBox,vBox1);
        VBox vBox2 = new VBox(10);
        vBox2.setAlignment(Pos.CENTER);
        vBox2.getChildren().addAll(hBox,totalValue,button);
        root.getChildren().addAll(vBox2);
    }

    private void updatePalletView() {
        if (api != null) {
            Platform.runLater(() -> {
                allPallets.getItems().clear();
                allContainers.getItems().clear();
                totalValue.setText("The Total Value of all Pallets is: " + this.api.getTotalValue());
                Node<Port> portNode = api.list.head;
                while (portNode != null) {
                    Port currentPort = portNode.data;
                    Node<Ship> shipNode = portNode.data.ships.head;
                    while (shipNode!=null){
                        Ship currentShip = shipNode.data;
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
                Node<Ship> shipNode = api.shipsAtSea.head;
                while (shipNode != null) {
                    Ship currentShip = shipNode.data;
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
