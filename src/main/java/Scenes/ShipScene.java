package Scenes;

import Controller.API;
import LinkedList.List;
import LinkedList.Node;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class ShipScene extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;
    private Ship ship;
    private ContainerInPortScene shipScene;
    private Container container;
    private TableView<Container> containerListView = new TableView();
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
        if(port!=null) {
            displayName.setText(ship.shipName);
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


        codeCont.setMinWidth(200);
        contSizeColumn.setMinWidth(200);

        containerListView.getColumns().addAll(codeCont,contSizeColumn);

        codeCont.setCellValueFactory(new PropertyValueFactory<>("contCode"));
        contSizeColumn.setCellValueFactory(new PropertyValueFactory<>("contSize"));

        if(ship!=null) {
            scheduler.scheduleAtFixedRate(this::updateContainerListView, 0, 1, TimeUnit.SECONDS);
        }

        Button saveContButton = new Button("Add Container");
        saveContButton.setOnAction(event -> {
            int size = contSize.getValue();
            String code = Utilities.uniqueCodeGenerator();
            Container newContainer = new Container(code,size,new List<Pallet>());
            ship.addContainer(newContainer);
            System.out.println(newContainer);
        });

        containerListView.setOnMouseClicked(e3 -> {
            if (e3.getClickCount() == 2) {
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
                } else {
                    System.out.println("ship is null");
                }
            }
        });

        Button button = new Button("Proceed");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchToScene2());

        vBox.getChildren().addAll(displayName);
        vBox1.getChildren().addAll(contSize,containerListView,saveContButton);

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
    private void updateContainerListView() {
        if (this.ship != null) {
            Platform.runLater(() -> {
                Node<Container> current = this.ship.containers.head;
                System.out.println("this"+current);
                while (current != null){
                    if(!(containerListView.getItems().contains(current.data))) {
                        containerListView.getItems().add(current.data);
                        System.out.println(current);
                    }
                    current = current.next;
                }
            });
        }
    }
}
