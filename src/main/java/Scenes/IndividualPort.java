package Scenes;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import models.Container;
import models.Port;
import Controller.API;
import models.Ship;
import utils.Utilities;

import java.io.FileNotFoundException;

public class IndividualPort extends Scene {
    private PortScene portScene;
    public Label showPortName;
    private MainScene mainScene;
    private API api;
    private Port port;

    //public void setShowPortsText(String text) {
     //   showPortName.setText(text);
    //}

    public IndividualPort(Pane root, MainScene mainScene, PortScene portScene, API api, Port port) throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        this.port = portScene.port;

        showPortName = new Label();
        showPortName.setFont(new Font("Arial", 50));

        System.out.println("outside check: " + port);
        if (port!=null){
            port.setPortName("update");
        }




        if (port != null) {
            showPortName.setText(port.portName);
        } else {
            showPortName.setText("No data available");
        }

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMinSize(1800, 800);
        vBox.setStyle(" -fx-padding: 40px;");

        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.BOTTOM_CENTER);
        vBox1.setMinSize(1800, 800);
        vBox1.setStyle(" -fx-padding: 40px;");

        Label addShip = new Label();
        Label error = new Label();
        Label error2 = new Label();
        addShip.setText("Add Ship to Port");

        TextField shipName = new TextField();
        TextField shipPicture = new TextField();
        ComboBox<String> shipCountry = new ComboBox<>();

        shipCountry.getItems().addAll(Utilities.countries);

        shipName.setPromptText("Enter Ship Name:");
        shipCountry.setPromptText("Enter Ship Country:");
        shipPicture.setPromptText("Enter Ship picture:");


        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            Port port1 = portScene.port;
            String name = shipName.getText();
            String country = shipCountry.getValue();
            String code = Utilities.uniqueCodeGenerator();
            String picture = shipPicture.getText();
            //Port abc = portScene.port;
            System.out.println("inside check: " + port1);

            if (!name.isBlank() && shipCountry.getValue()!=null){
                if(port1 != null){
                    Ship newShip = new Ship(name,country,picture,code);

                    System.out.println(newShip);
                    System.out.println("inside check: " + port1);
                    port1.addShip(newShip);
                }
            }else{
                if (name.isBlank()){
                    error.setText("Field cannot be empty");
                }
                else {
                    error.setText("");
                }
                if (shipCountry.getValue()==null){
                    error2.setText("Field cannot be empty");
                }else {
                    error2.setText("");
                }
            }
        });

        Button button = new Button("Proceed");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchToScene2());

        vBox.getChildren().addAll(showPortName,addShip,shipName,shipPicture,shipCountry,saveButton,button);

        root.getChildren().addAll(vBox);
    }
}
