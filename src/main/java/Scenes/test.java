package Scenes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import models.Port;
import Controller.API;
import models.Ship;
import utils.Utilities;

import java.io.FileNotFoundException;

public class test extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;

    public test(Pane root, MainScene mainScene) throws FileNotFoundException {
        super(root);


        Label showPortName;
        showPortName = new Label();
        showPortName.setFont(new Font("Arial", 50));
        System.out.println(port);

        String abc;
        if (port != null) {
            abc = port.getPortName();
            showPortName.setText(abc);
        } else {
            abc = "no";
        }

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMinSize(1800, 800);
        vBox.setStyle(" -fx-padding: 40px;");

        Label addShip = new Label("Add Ship to Port");
        Label error = new Label();
        Label error2 = new Label();

        TextField shipName = new TextField();
        TextField shipPicture = new TextField();
        ComboBox<String> shipCountry = new ComboBox<>();
        shipCountry.getItems().addAll(Utilities.countries);
        shipName.setPromptText("Enter Ship Name:");
        shipCountry.setPromptText("Enter Ship Country:");
        shipPicture.setPromptText("Enter Ship picture:");

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            String name = shipName.getText();
            String country = shipCountry.getValue();
            String code = Utilities.uniqueCodeGenerator();
            String picture = shipPicture.getText();

            if (!name.isBlank() && country != null) {
                Ship newShip = new Ship(name, country, picture, code);
                portScene.port.ships.add(newShip);
            } else {
                if (name.isBlank()) {
                    error.setText("Ship Name cannot be empty");
                } else {
                    error.setText("");
                }
                if (country == null) {
                    error2.setText("Ship Country cannot be empty");
                } else {
                    error2.setText("");
                }
            }
        });

        Button button = new Button("Proceed");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchToScene2());

        vBox.getChildren().addAll(showPortName, addShip, shipName, shipPicture, shipCountry, saveButton, error, error2, button);
        root.getChildren().addAll(vBox);
    }
}
