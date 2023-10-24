package Scenes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private MainScene mainScene;
    private API api;
    private Port port;

    public IndividualPort(Pane root, MainScene mainScene, PortScene portScene, API api, Port port) throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        this.port = portScene.port;

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
        vBox.setMinSize(1800, 200);
        vBox.setStyle(" -fx-padding: 40px;");

        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setMinSize(900, 600);
        vBox1.setStyle(" -fx-padding: 40px;");

        VBox vBox2 = new VBox(10);
        vBox2.setAlignment(Pos.TOP_RIGHT);
        vBox2.setMinSize(900, 600);
        vBox2.setStyle(" -fx-padding: 40px;");

        Label addShip = new Label("Add Ship to Port");
        Label addContainer = new Label("Add Container to Port");
        Label error = new Label();
        Label error2 = new Label();

        TextField shipName = new TextField();
        TextField shipPicture = new TextField();
        ComboBox<String> shipCountry = new ComboBox<>();
        shipCountry.getItems().addAll(Utilities.countries);
        shipName.setPromptText("Enter Ship Name:");
        shipCountry.setPromptText("Select Ship Country:");
        shipPicture.setPromptText("Enter Ship picture:");

        ListView<Ship> shipListView = new ListView<>();

        ListView<Container> containerListView = new ListView<>();

        ComboBox<Integer> contSize = new ComboBox();
        contSize.setPromptText("Select Container Size");
        contSize.getItems().addAll(10,20,40);




        shipListView.setPlaceholder(new Label("No ships added yet"));
        containerListView.setPlaceholder(new Label("No containers added yet"));

        Button saveContButton = new Button("Save");
        saveContButton.setOnAction(event -> {
            int size = contSize.getValue();
            String code = Utilities.uniqueCodeGenerator();
            Container newContainer = new Container(code,size);
            port.addContainer(newContainer);
        });

        Button saveShipButton = new Button("Save");
        saveShipButton.setOnAction(event -> {
            String name = shipName.getText();
            String country = shipCountry.getValue();
            String code = Utilities.uniqueCodeGenerator();
            String picture = shipPicture.getText();

            if (!name.isBlank() && country != null) {
                Ship newShip = new Ship(name, country, picture, code);
                port.ships.add(newShip);
                shipListView.getItems().add(newShip);
                System.out.println(newShip);
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

        vBox.getChildren().addAll(showPortName);

        vBox1.getChildren().addAll(addShip, shipName, shipPicture, shipCountry, shipListView ,saveShipButton);

        vBox2.getChildren().addAll(addContainer,contSize,containerListView,saveContButton);

        HBox hBox=new HBox();
        hBox.getChildren().addAll(button);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setStyle(" -fx-padding: 40px;");

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(vBox1);
        borderPane.setRight(vBox2);
        borderPane.setCenter(vBox);
        borderPane.setCenter(hBox);

        root.getChildren().addAll(borderPane);

    }
}
