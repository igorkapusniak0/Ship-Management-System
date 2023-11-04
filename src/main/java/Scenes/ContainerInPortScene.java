package Scenes;

import Controller.API;
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

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ContainerInPortScene extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;
    private Ship ship;
    private Container container;
    private ShipScene shipScene;
    public Pallet pallet;
    private Pallet chosenPallet;
    private TableView<Pallet> palletTableView = new TableView();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public ContainerInPortScene(Pane root, MainScene mainScene, PortScene portScene, API api, Container container) throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        this.port = portScene.port;
        this.container = container;

        Label displayName= new Label();
        displayName.setFont(new Font("Arial", 50));

        if(port!=null) {
            displayName.setText("Container: "+container.getContCode());
        }

        Label palletDescription = new Label("Description");
        Label quantityofItems = new Label("Quantity of Items");
        Label palletValue = new Label("Value of Pallet");
        Label palletWeight = new Label("Weight of Pallet");
        Label palletVolume = new Label("Volume of Pallet");

        Label desError = new Label();
        Label quantityError = new Label();
        Label valueError = new Label();
        Label weightError = new Label();
        Label volumeError = new Label();

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
        TableColumn<Pallet, String> weightColumn = new TableColumn<>("Pallet Weight");
        TableColumn<Pallet, String> volumeColumn = new TableColumn<>("Pallet Volume");
        TableColumn<Pallet, String> totalValueColumn = new TableColumn<>("Pallet Total Value");

        descriptionColumn.setMinWidth(300);
        quantityColumn.setMinWidth(100);
        valueColumn.setMinWidth(100);
        weightColumn.setMinWidth(100);
        volumeColumn.setMinWidth(100);
        totalValueColumn.setMinWidth(100);
        palletTableView.getColumns().addAll(descriptionColumn,quantityColumn,valueColumn,weightColumn,volumeColumn,totalValueColumn);


        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        volumeColumn.setCellValueFactory(new PropertyValueFactory<>("volume"));
        totalValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalValue"));

        palletTableView.setPlaceholder(new Label("No Pallets Added Yet"));


        Button addPalletButton = new Button("Add Pallet");
        addPalletButton.setOnAction(actionEvent -> {
            boolean isValid = true;

            String descriptionText = description.getText();
            String quantityText = quantity.getText();
            String valueText = value.getText();
            String weightText = weight.getText();
            String volumeText = volume.getText();

            if (descriptionText.isBlank()) {
                desError.setText("Description cannot be empty");
                isValid = false;
            } else {
                desError.setText("");
            }

            if (quantityText.isBlank() || !quantityText.matches("\\d+")) {
                quantityError.setText(quantityText.isBlank() ? "Pallet quantity cannot be empty" : "Invalid quantity");
                isValid = false;
            } else {
                quantityError.setText("");
            }

            if (valueText.isBlank() || !valueText.matches("[0-9]+\\.?[0-9]*")) {
                valueError.setText(valueText.isBlank() ? "Pallet value cannot be empty" : "Invalid value");
                isValid = false;
            } else {
                valueError.setText("");
            }

            if (weightText.isBlank() || !weightText.matches("[0-9]+\\.?[0-9]*")) {
                weightError.setText(weightText.isBlank() ? "Pallet weight cannot be empty" : "Invalid weight");
                isValid = false;
            } else {
                weightError.setText("");
            }

            if (volumeText.isBlank() || !volumeText.matches("[0-9]+\\.?[0-9]*")) {
                volumeError.setText(volumeText.isBlank() ? "Pallet volume cannot be empty" : "Invalid volume");
                isValid = false;
            } else {
                volumeError.setText("");
            }

            if (isValid) {
                Integer quantityValue = Integer.parseInt(quantityText);
                Double valueValue = Double.parseDouble(valueText);
                Double weightValue = Double.parseDouble(weightText);
                Double volumeValue = Double.parseDouble(volumeText);

                Pallet newPallet = new Pallet(descriptionText, quantityValue, valueValue, weightValue, volumeValue,this.container);
                container.addPallet(newPallet);
            }
        });

        TextField searchPallet = new TextField();
        searchPallet.setPromptText("Search Pallet");

        Consumer<Pallet> setPalletTotalValueAction = Pallet::setTotalValue;
        if(container!=null) {
            scheduler.scheduleAtFixedRate(() -> {
                api.updateListView(searchPallet.getText(),palletTableView,this.container.pallets.head,setPalletTotalValueAction);
            }, 0, 1, TimeUnit.SECONDS);
        }
        Label removePalletLabel = new Label("");
        Button removePallet = new Button("Remove Pallet");
        removePallet.setOnAction(event -> {
            if (chosenPallet!=null) {
                container.pallets.remove(chosenPallet);
                removePalletLabel.setText("");
            }
        });

        palletTableView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY && event.getClickCount() ==2){
                Pallet selectedPallet = palletTableView.getSelectionModel().getSelectedItem();
                if(selectedPallet != null){
                    chosenPallet = selectedPallet;
                    chosenPallet = palletTableView.getSelectionModel().getSelectedItem();
                    removePalletLabel.setText("Pallet: "+ chosenPallet.getDescription() + " is selected");
                }
            }
        });

        Button unselectPallet = new Button("Unselect Pallet");
        unselectPallet.setOnAction(event -> {
            chosenPallet = null;
            removePalletLabel.setText("");
        });

        Button updateButton = new Button("Update Ship");
        updateButton.setOnAction(event -> {
            if (chosenPallet!=null){
            chosenPallet.setDescription(description.getText());
            chosenPallet.setQuantity(Integer.parseInt(quantity.getText()));
            chosenPallet.setVolume(Double.parseDouble(volume.getText()));
            chosenPallet.setWeight(Double.parseDouble(weight.getText()));
            chosenPallet.setValue(Double.parseDouble(value.getText()));
            chosenPallet.setTotalValue();
            removePalletLabel.setText("");
            chosenPallet=null;
            }
        });

        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchScene(portScene.individualPort));

        VBox vBox2 = new VBox();

        vBox.getChildren().add(displayName);
        vBox1.getChildren().addAll(palletTableView,searchPallet,addPalletButton,removePallet,unselectPallet,updateButton,removePalletLabel);
        vBox2.getChildren().addAll(addPallet,palletDescription,description,desError,quantityofItems,quantity,quantityError,palletValue,value,valueError,palletWeight,weight,weightError,palletVolume,volume,volumeError);
        HBox hBox=new HBox();
        hBox.getChildren().addAll(button);

        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setStyle(" -fx-padding: 40px;");

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(vBox);
        borderPane.setLeft(vBox2);
        borderPane.setRight(vBox1);
        borderPane.setBottom(hBox);

        root.getChildren().add(borderPane);
    }
    private void updatePalletView(String pallet) {
        if (this.container.pallets!=null) {
            Platform.runLater(() -> {
                palletTableView.getItems().clear();
                palletTableView.getItems().removeIf(pallet1 -> !pallet1.toString().contains(pallet));
                Node<Pallet> current = this.container.pallets.head;
                while (current != null) {
                    if (current.data.toString().contains(pallet)) {
                        if (!palletTableView.getItems().contains(current.data)) {
                            palletTableView.getItems().add(current.data);
                        }
                    }
                    current=current.next;
                }
            });
        }
    }

}
