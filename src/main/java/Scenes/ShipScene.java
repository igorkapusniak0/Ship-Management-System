package Scenes;

import Controller.API;
import LinkedList.List;
import LinkedList.Node;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Container;
import models.Pallet;
import models.Port;
import models.Ship;
import utils.Utilities;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ShipScene extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;
    private Ship ship;
    private ContainerInPortScene shipScene;
    private Container container;
    private Container chosenContainer;
    private TableView<Container> containerListView = new TableView();
    private ComboBox<Port> portComboBox = new ComboBox();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ShipScene(Pane root, MainScene mainScene, PortScene portScene, API api, Ship ship)throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.ship = ship;
        this.api = api;

        Label displayName;
        displayName = new Label();
        displayName.setFont(new Font("Arial", 50));
        if(ship!=null) {
            displayName.setText("Ship: "+ship.shipName);
        }




        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxHeight(50);
        vBox.setStyle(" -fx-padding: 40px;");

        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setMinSize(700, 600);
        vBox1.setStyle(" -fx-padding: 40px;");

        ComboBox<Integer> contSize = new ComboBox();
        contSize.setPromptText("Select Container Size");
        contSize.getItems().addAll(10,20,40);
        contSize.setMaxWidth(300);

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

        TextField searchCont = new TextField();
        searchCont.setPromptText("Enter Container");


        Consumer<Container> setContTotalValueAction = Container::setTotalValue;
        if(ship!=null) {
            scheduler.scheduleAtFixedRate(() -> {
                api.updateListView(searchCont.getText(),containerListView,this.ship.containers.head,setContTotalValueAction);
            }, 0, 1, TimeUnit.SECONDS);
            scheduler.scheduleAtFixedRate(this::updateComboBoxContainer, 0, 1, TimeUnit.SECONDS);
        }

        Label error = new Label("");

        Button saveContButton = new Button("Add Container");
        saveContButton.setOnAction(event -> {
            Integer size = contSize.getValue();
            String code = Utilities.uniqueCodeGenerator();
            if (size==null){
                error.setText("Container Size cannot be empty");
            }else {
                Container newContainer = new Container(code,size,new List<Pallet>(),this.port,this.ship);
                ship.addContainer(newContainer);
                error.setText("");
            }
        });

        Label removeContainerLabel = new Label("");
        Button removeButton = new Button("Remove Container");
        removeButton.setOnAction(event -> {
            if (chosenContainer!=null){
                ship.removeContainer(chosenContainer);
                removeContainerLabel.setText("");
            }
        });

        portComboBox.setPromptText("Select Port:");

        containerListView.setOnMouseClicked(e3 -> {
            if (e3.getButton() == MouseButton.PRIMARY && e3.getClickCount() == 2) {
                Container selectedContainer = containerListView.getSelectionModel().getSelectedItem();
                if (selectedContainer != null) {
                    container = selectedContainer;
                    try {
                        Pane individualPortRoot = new Pane();
                        shipScene = new ContainerInPortScene(individualPortRoot, mainScene,portScene , api, selectedContainer);
                        mainScene.switchScene(shipScene);
                        System.out.println(container);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (e3.getButton() == MouseButton.SECONDARY && e3.getClickCount() == 2) {
                chosenContainer = containerListView.getSelectionModel().getSelectedItem();
                if (chosenContainer!=null){
                    removeContainerLabel.setText("Container: "+chosenContainer.getContCode() + " is selected");
                }
            }
        });
        Button moveButton = new Button("Move Container to Port");
        moveButton.setOnAction(event -> {
            if (portComboBox!=null&&chosenContainer!=null) {
                api.unloadContainer(ship, portComboBox.getValue(), chosenContainer);
            }
        });

        Button updateButton = new Button("Update Container");
        updateButton.setOnAction(event -> {
            if (chosenContainer!=null){
                chosenContainer.setContSize(contSize.getValue());
                chosenContainer.setTotalValue();
                chosenContainer=null;
                removeContainerLabel.setText("");
            }
        });

        Button unselect = new Button("Unselect Container");
        unselect.setOnAction(event -> {
            chosenContainer = null;
            removeContainerLabel.setText("");
        });

        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchScene(portScene));

        vBox.getChildren().addAll(displayName);
        vBox1.getChildren().addAll(contSize,error,containerListView,searchCont,saveContButton,removeButton,unselect,updateButton, portComboBox,moveButton,removeContainerLabel);

        HBox hBox=new HBox();
        hBox.getChildren().addAll(button);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setStyle(" -fx-padding: 40px;");
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setCenter(vBox1);
        borderPane.setBottom(hBox);

        root.getChildren().addAll(borderPane);
    }
    private void updateContainerListView(String container) {
        if (this.ship != null) {
            Platform.runLater(() -> {
                containerListView.getItems().clear();
                containerListView.getItems().removeIf(container1 -> !container1.toString().contains(container));
                Node<Container> current = this.ship.containers.head;
                while (current != null){
                    if (current.data.toString().contains(container)){
                        if (!containerListView.getItems().contains(current.data)){
                            containerListView.getItems().add(current.data);
                        }
                    }
                    current = current.next;
                }
            });
        }
    }
    private void updateComboBoxContainer() {
        if (this.api != null && this.api.list != null) {
            Platform.runLater(() -> {
                Port selectedPort = portComboBox.getValue();
                portComboBox.getItems().clear();
                Node<Port> current = this.api.list.head;
                while (current != null) {
                    Port port = current.data;
                    portComboBox.getItems().add(port);
                    current = current.next;
                }
                if (selectedPort != null && portComboBox.getItems().contains(selectedPort)) {
                    portComboBox.setValue(selectedPort);
                }
            });
        }
    }

}
