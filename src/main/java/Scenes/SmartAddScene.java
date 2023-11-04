package Scenes;


import Controller.API;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import models.Container;
import models.Pallet;



public class SmartAddScene extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Container container;
    private Label nullCont = new Label("");


    public SmartAddScene(Pane root, MainScene mainScene,PortScene portScene, API api, Container container) {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        this.container = container;


        Label displayName = new Label();
        displayName.setFont(new Font("Arial", 50));

        if(container!=null) {
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

                if (container!=null){
                    Pallet newPallet = new Pallet(descriptionText, quantityValue, valueValue, weightValue, volumeValue,this.container);
                    container.addPallet(newPallet);
                }
                else{
                    nullCont.setText("No containers in System");
                }
            }
        });

        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchScene(portScene));

        VBox vBox2 = new VBox();

        vBox.getChildren().add(displayName);
        vBox1.getChildren().addAll(addPalletButton);
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


}

