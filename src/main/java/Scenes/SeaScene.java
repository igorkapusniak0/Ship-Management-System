package Scenes;

import Controller.API;
import LinkedList.Node;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import models.Ocean;
import models.Pallet;
import models.Port;
import models.Ship;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SeaScene extends Scene {
    private MainScene mainScene;
    private PortScene portScene;
    private API api;
    private IndividualPort individualPort;
    private TableView<Ship> shipTableView = new TableView();
    private Ocean ocean;
    private Port port;
    private Ship chosenShip;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SeaScene(Pane root, MainScene mainScene, PortScene portScene, API api){
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;

        TableColumn<Ship, String> codeColumn = new TableColumn<>("Ship Code");
        TableColumn<Ship, String> nameColumn = new TableColumn<>("Ship Name");
        TableColumn<Ship, String> countryColumn = new TableColumn<>("Ship Country");
        TableColumn<Ship, String> pictureColumn = new TableColumn<>("Ship Picture");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);
        pictureColumn.setMinWidth(100);
        shipTableView.getColumns().addAll(codeColumn,nameColumn,countryColumn,pictureColumn);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("shipCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("shipCountry"));
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("shipPicture"));

        shipTableView.setPlaceholder(new Label("No ships added yet"));

        if(port!=null) {
            scheduler.scheduleAtFixedRate(this::updateShipView, 0, 1, TimeUnit.SECONDS);
        }


        Label removeShipLabel = new Label("");
        Button unselectShip = new Button("Unselect Pallet");
        unselectShip.setOnAction(event -> {
            chosenShip = null;
            removeShipLabel.setText("");
        });

        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchScene(portScene));

        shipTableView.setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.SECONDARY && event.getClickCount() ==2){
                Ship selectedPallet = shipTableView.getSelectionModel().getSelectedItem();
                if(selectedPallet != null){
                    chosenShip = selectedPallet;
                    chosenShip = shipTableView.getSelectionModel().getSelectedItem();
                    removeShipLabel.setText("Ship: "+ chosenShip.getShipName() + " is selected");
                }
            }
        });

        BorderPane borderPane = new BorderPane();
        borderPane.getChildren().addAll(shipTableView,removeShipLabel,unselectShip,button);
        root.getChildren().add(borderPane);

    }
    private void updateShipView() {
        if (this.api.shipsAtSea!=null) {
            Platform.runLater(() -> {
                Node<Ship> current = this.api.shipsAtSea.head;
                while (current != null) {
                    Ship ship = current.data;
                    if (shipTableView.getItems().contains(ship)) {
                        int index = shipTableView.getItems().indexOf(ship);
                        shipTableView.getItems().set(index,ship);
                    }else{
                        shipTableView.getItems().add(ship);
                    }
                    current=current.next;
                }
                shipTableView.getItems().removeIf(ship -> !this.api.shipsAtSea.contains(ship));
            });
        }
    }
}
