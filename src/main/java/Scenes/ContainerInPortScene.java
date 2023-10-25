package Scenes;

import Controller.API;
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

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContainerInPortScene extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;
    private Ship ship;
    private Container container;
    public Pallet pallet;
    private TableView<Pallet> palletTableView = new TableView();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ContainerInPortScene(Pane root, MainScene mainScene, PortScene portScene, API api, Container container) throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        this.port = portScene.port;
        this.container = container;

        Label displayName;
        displayName = new Label();
        displayName.setFont(new Font("Arial", 50));
        if(port!=null) {
            displayName.setText(port.getPortName());
        }

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxHeight(50);
        vBox.setStyle(" -fx-padding: 40px;");

        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setMinSize(700, 600);
        vBox1.setStyle(" -fx-padding: 40px;");

        Label addPallet = new Label("Add Pallet");

        TextField description = new TextField();
        TextField quantity = new TextField();
        TextField weight = new TextField();
        TextField volume = new TextField();
        TextField value = new TextField();

        description.setPromptText("Enter Pallet Description:");
        description.setMaxSize(200,300);

        quantity.setPromptText("Enter Quantity of Items:");
        quantity.setMaxWidth(300);

        weight.setPromptText("Enter Pallet Weight:");
        weight.setMaxWidth(300);

        volume.setPromptText("Enter Pallet Volume:");
        volume.setMaxWidth(300);

        value.setPromptText("Enter Pallet Value:");
        value.setMaxWidth(300);



        TableColumn<Pallet, String> descriptionColumn = new TableColumn<>("Pallet Description");
        TableColumn<Pallet, String> quantityColumn = new TableColumn<>("Quantity of Items");
        TableColumn<Pallet, String> valueColumn = new TableColumn<>("Pallet Value");
        TableColumn<Pallet, String> weightColumn = new TableColumn<>("Pallet Value");
        TableColumn<Pallet, String> volumeColumn = new TableColumn<>("Pallet Volume");

        descriptionColumn.setMinWidth(500);
        quantityColumn.setMinWidth(100);
        valueColumn.setMinWidth(100);
        weightColumn.setMinWidth(100);
        volumeColumn.setMinWidth(100);
        palletTableView.getColumns().addAll(descriptionColumn,quantityColumn,valueColumn,weightColumn,volumeColumn);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));

        palletTableView.setPlaceholder(new Label("No Pallets Added Yet"));


        Button addPalletButton = new Button("Add Pallet");
        addPalletButton.setOnAction(actionEvent -> {
            String descriptionText = description.getText();
            int quantityText  = Integer.parseInt(quantity.getText());
            double valueText  = Double.parseDouble(value.getText());
            double weightText  = Double.parseDouble(weight.getText());
            double volumeText  = Double.parseDouble(volume.getText());
            Pallet newPallet = new Pallet(descriptionText,quantityText,valueText,weightText,volumeText);

            container.addPallet(newPallet);

        });

        if(container!=null) {
            scheduler.scheduleAtFixedRate(this::updatePalletView, 0, 1, TimeUnit.SECONDS);
        }

//        palletTableView.setOnMouseClicked(mouseEvent -> {
//            if(mouseEvent.getClickCount() ==2){
//                Pallet selectedPallet = palletTableView.getSelectionModel().getSelectedItem();
//                if(selectedPallet != null){
//
//                }
//            }
//        });

        Button button = new Button("Proceed");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchToScene2());

        vBox.getChildren().add(displayName);
        vBox1.getChildren().addAll(addPallet,description,quantity,value,weight,volume,palletTableView,addPalletButton);

        HBox hBox=new HBox();
        hBox.getChildren().addAll(button);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setStyle(" -fx-padding: 40px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setCenter(vBox1);
        borderPane.setBottom(hBox);

        root.getChildren().add(borderPane);
    }
    private void updatePalletView() {
        if (this.container.pallets!=null) {
            Platform.runLater(() -> {
                Node<Pallet> current = this.container.pallets.head;
                while (current != null) {
                    if (!(palletTableView.getItems().contains(current.data))) {
                        palletTableView.getItems().add(current.data);
                    }
                    current=current.next;
                }
            });
        }
    }






}
