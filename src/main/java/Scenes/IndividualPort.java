package Scenes;

import LinkedList.List;
import LinkedList.Node;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class IndividualPort extends Scene {
    private PortScene portScene;
    private MainScene mainScene;
    private API api;
    private Port port;
    private TableView<Ship> shipListView = new TableView();
    private TableView<Container> containerListView = new TableView();
    private Ship ship;
    private ContainerInPortScene containerInPortScene;
    private ShipScene shipScene;
    private Container container;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public IndividualPort(Pane root, MainScene mainScene, PortScene portScene, API api, Port port) throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        this.port = portScene.port;

        Label showPortName;
        showPortName = new Label();
        showPortName.setFont(new Font("Arial", 50));

        if(port!=null) {
            showPortName.setText(port.getPortName());
        }


        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxHeight(50);
        vBox.setStyle(" -fx-padding: 40px;");

       /* VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setMinSize(700, 600);
        vBox1.setStyle(" -fx-padding: 40px;");*/

        VBox vBox2 = new VBox(10);
        vBox2.setAlignment(Pos.TOP_RIGHT);
        vBox2.setMinSize(700, 600);
        vBox2.setStyle(" -fx-padding: 40px;");

        //Label addShip = new Label("Add Ship to Port");
        //addShip.setFont(new Font("Arial",20));
        //addShip.setAlignment(Pos.TOP_CENTER);
        Label addContainer = new Label("Add Container to Port");
        addContainer.setFont(new Font("Arial",20));
        addContainer.setAlignment(Pos.TOP_CENTER);
        Label error = new Label();
        /*Label error1 = new Label();
        Label error2 = new Label();
        Label error3 = new Label();*/

        Label shipNameLabel = new Label("Ship Name");
        Label shipPictureLabel = new Label("Ship Picture");
        Label shipCountryLabel = new Label("Ship Country");

        Label contSizeLabel = new Label("Container Size");

        /*TextField shipName = new TextField();
        TextField shipPicture = new TextField();
        ComboBox<String> shipCountry = new ComboBox<>();
        shipCountry.getItems().addAll(Utilities.countries);
        shipName.setPromptText("Enter Ship Name:");
        shipName.setMaxWidth(300);
        shipCountry.setPromptText("Select Ship Country:");
        shipCountry.setMaxWidth(300);
        shipPicture.setPromptText("Enter Ship picture:");
        shipPicture.setMaxWidth(300);*/

        ComboBox<Integer> contSize = new ComboBox();
        contSize.setPromptText("Select Container Size");
        contSize.getItems().addAll(10,20,40);
        contSize.setMaxWidth(300);
/*
        TableColumn<Ship, String> codeColumn = new TableColumn<>("Ship Code");
        TableColumn<Ship, String> nameColumn = new TableColumn<>("Ship Name");
        TableColumn<Ship, String> countryColumn = new TableColumn<>("Ship Country");
        TableColumn<Ship, String> pictureColumn = new TableColumn<>("Ship Picture");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);
        pictureColumn.setMinWidth(100);
        shipListView.getColumns().addAll(codeColumn,nameColumn,countryColumn,pictureColumn);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("shipCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("shipCountry"));
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("shipPicture"));

        shipListView.setPlaceholder(new Label("No ships added yet"));*/

        containerListView.setPlaceholder(new Label("No containers added yet"));

        TableColumn<Container, String> codeCont = new TableColumn<>("Container Code");
        TableColumn<Container, Integer> contSizeColumn = new TableColumn<>("Container Size (feet^3)");


        codeCont.setMinWidth(200);
        contSizeColumn.setMinWidth(200);

        containerListView.getColumns().addAll(codeCont,contSizeColumn);

        codeCont.setCellValueFactory(new PropertyValueFactory<>("contCode"));
        contSizeColumn.setCellValueFactory(new PropertyValueFactory<>("contSize"));


        if(port!=null&&port.ships!=null&&port.containersInPort!=null) {
            scheduler.scheduleAtFixedRate(this::updateShipListView, 0, 1, TimeUnit.SECONDS);
            scheduler.scheduleAtFixedRate(this::updateContainerListView, 0, 1, TimeUnit.SECONDS);
        }

        Button saveContButton = new Button("Add Container");

        saveContButton.setOnAction(event -> {
            Integer size = contSize.getValue();
            String code = Utilities.uniqueCodeGenerator();
            boolean containerCodeExists = containerListView.getItems().stream().anyMatch(container -> container.getContCode().equals(code));

            if (size == null){
                error.setText("Container Size cannot be empty");
            }else {
                if(!containerCodeExists){
                    Container newContainer = new Container(code,size,new List<Pallet>());
                    port.addContainer(newContainer);
                    error.setText("");
                }
            }

        });

        containerListView.setOnMouseClicked(e3 -> {
            if (e3.getClickCount() == 2) {
                Container selectedContainer = containerListView.getSelectionModel().getSelectedItem();
                if (selectedContainer != null) {
                    container = selectedContainer;
                    try {
                        Pane individualPortRoot = new Pane();
                         containerInPortScene = new ContainerInPortScene(individualPortRoot, mainScene,portScene , api, selectedContainer);
                        mainScene.switchScene(containerInPortScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        /*shipListView.setOnMouseClicked(e3 -> {
            if (e3.getClickCount() == 2) {
                Ship selectedShip = shipListView.getSelectionModel().getSelectedItem();
                if (selectedShip != null) {
                    ship = selectedShip;
                    try {
                        Pane individualPortRoot = new Pane();
                        shipScene = new ShipScene(individualPortRoot,mainScene,portScene,api,selectedShip);
                        mainScene.switchScene(shipScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Button saveShipButton = new Button("Add Ship");
        saveShipButton.setOnAction(event -> {
            String name = shipName.getText();
            String country = shipCountry.getValue();
            String code = Utilities.uniqueCodeGenerator();
            String picture = shipPicture.getText();
            boolean shipCodeExists = shipListView.getItems().stream().anyMatch(ship -> ship.getShipCode().equals(code));


            if (!name.isBlank() && country != null && !picture.isBlank() && !shipCodeExists) {
                Ship newShip = new Ship(name, country, picture, code);
                port.ships.add(newShip);
                error1.setText("");
                error2.setText("");
                error3.setText("");
            } else {
                if (name.isBlank()) {
                    error1.setText("Ship Name cannot be empty");
                } else {
                    error1.setText("");
                }
                if (country == null) {
                    error2.setText("Ship Country cannot be empty");
                } else {
                    error2.setText("");
                }
                if (picture.isBlank()){
                    error3.setText("Ship picture cannot be empty");
                }else {
                    error3.setText("");
                }
            }

        });*/

        Button button = new Button("Return");
        button.setFont(new Font("Arial", 30));
        button.setOnAction(event -> mainScene.switchToScene2());

        vBox.getChildren().addAll(showPortName);

        //vBox1.getChildren().addAll(addShip,shipNameLabel,shipName, error1,shipPictureLabel,shipPicture, error3,shipCountryLabel,shipCountry, error2,shipListView ,saveShipButton);

        vBox2.getChildren().addAll(addContainer,contSizeLabel,contSize,error,containerListView,saveContButton);

        HBox hBox=new HBox();
        hBox.getChildren().addAll(button);
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        hBox.setStyle(" -fx-padding: 40px;");

        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(shipDisplay());
        borderPane.setRight(vBox2);
        borderPane.setTop(vBox);
        borderPane.setCenter(hBox);

        root.getChildren().addAll(borderPane);



    }
    private void updateShipListView() {
        if (this.port != null && this.port.ships != null) {
            Platform.runLater(() -> {
                Node<Ship> current = this.port.ships.head;
                while (current != null){
                    if(!(shipListView.getItems().contains(current.data))) {
                        shipListView.getItems().add(current.data);
                    }
                    current = current.next;
                }
            });
        }
    }
    private void updateContainerListView() {
        if (this.port != null && this.port.containersInPort != null) {
            Platform.runLater(() -> {
                Node<Container> current = this.port.containersInPort.head;
                while (current != null){
                    if(!(containerListView.getItems().contains(current.data))) {
                        containerListView.getItems().add(current.data);
                    }
                    current = current.next;
                }
            });
        }
    }
    public VBox shipDisplay(){
        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.TOP_LEFT);
        vBox1.setMinSize(700, 600);
        vBox1.setStyle(" -fx-padding: 40px;");

        Label addShip = new Label("Add Ship to Port");
        addShip.setFont(new Font("Arial",20));
        addShip.setAlignment(Pos.TOP_CENTER);
        Label error1 = new Label();
        Label error2 = new Label();
        Label error3 = new Label();

        Label shipNameLabel = new Label("Ship Name");
        Label shipPictureLabel = new Label("Ship Picture");
        Label shipCountryLabel = new Label("Ship Country");


        TextField shipName = new TextField();
        TextField shipPicture = new TextField();
        ComboBox<String> shipCountry = new ComboBox<>();
        shipCountry.getItems().addAll(Utilities.countries);
        shipName.setPromptText("Enter Ship Name:");
        shipName.setMaxWidth(300);
        shipCountry.setPromptText("Select Ship Country:");
        shipCountry.setMaxWidth(300);
        shipPicture.setPromptText("Enter Ship picture:");
        shipPicture.setMaxWidth(300);

        TableColumn<Ship, String> codeColumn = new TableColumn<>("Ship Code");
        TableColumn<Ship, String> nameColumn = new TableColumn<>("Ship Name");
        TableColumn<Ship, String> countryColumn = new TableColumn<>("Ship Country");
        TableColumn<Ship, String> pictureColumn = new TableColumn<>("Ship Picture");

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);
        pictureColumn.setMinWidth(100);
        shipListView.getColumns().addAll(codeColumn,nameColumn,countryColumn,pictureColumn);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("shipCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("shipName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("shipCountry"));
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("shipPicture"));

        shipListView.setPlaceholder(new Label("No ships added yet"));

        shipListView.setOnMouseClicked(e3 -> {
            if (e3.getClickCount() == 2) {
                Ship selectedShip = shipListView.getSelectionModel().getSelectedItem();
                if (selectedShip != null) {
                    ship = selectedShip;
                    try {
                        Pane individualPortRoot = new Pane();
                        shipScene = new ShipScene(individualPortRoot,mainScene,portScene,api,selectedShip);
                        mainScene.switchScene(shipScene);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Button saveShipButton = new Button("Add Ship");
        saveShipButton.setOnAction(event -> {
            String name = shipName.getText();
            String country = shipCountry.getValue();
            String code = Utilities.uniqueCodeGenerator();
            String picture = shipPicture.getText();
            boolean shipCodeExists = shipListView.getItems().stream().anyMatch(ship -> ship.getShipCode().equals(code));


            if (!name.isBlank() && country != null && !picture.isBlank() && !shipCodeExists) {
                Ship newShip = new Ship(name, country, picture, code);
                port.ships.add(newShip);
                error1.setText("");
                error2.setText("");
                error3.setText("");
            } else {
                if (name.isBlank()) {
                    error1.setText("Ship Name cannot be empty");
                } else {
                    error1.setText("");
                }
                if (country == null) {
                    error2.setText("Ship Country cannot be empty");
                } else {
                    error2.setText("");
                }
                if (picture.isBlank()){
                    error3.setText("Ship picture cannot be empty");
                }else {
                    error3.setText("");
                }
            }

        });
        vBox1.getChildren().addAll(addShip,shipNameLabel,shipName, error1,shipPictureLabel,shipPicture, error3,shipCountryLabel,shipCountry, error2,shipListView ,saveShipButton);
        return vBox1;
    }



}
