package Scenes;

import LinkedList.List;
import LinkedList.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import models.Container;
import models.Ship;
import models.Port;
import models.Singleton;
import utils.Utilities;
import utils.Countries;
import Controller.API;

import java.io.FileNotFoundException;

import static java.lang.Integer.*;

public class PortScene extends Scene {
    public API api;
    private MainScene mainScene;
    public Port port;
    private Ship ships;
    private Container containersInPort;
    public IndividualPort individualPort;
    Pane window;
    private List<Countries> countriesList;
   // private IndividualPort individualPort;

    public PortScene(Pane root,MainScene mainScene) {
        super(root);
        this.mainScene = mainScene;
        window = root;
        this.api = new API();
        BorderPane borderPane = new BorderPane();

        TextField nameField = new TextField();
        TextField countryField = new TextField();
        TextField test = new TextField();
        ComboBox<String> countryBox = new ComboBox<>();

//        ComboBox<Ship> shipField = new ComboBox<>();
//        ComboBox<Container> contField = new ComboBox<>();
//
//
//        contField.getItems().addAll();

        countryBox.getItems().addAll(Utilities.countries);


        nameField.setMaxWidth(300);
        countryField.setMaxWidth(300);


        Label portLabel = new Label("Port Name");
        Label countryLabel = new Label("Country");
        Label error = new Label();
        Label error2 = new Label();

//        Label shipsLabel = new Label("Ships");
//        Label contLabel = new Label("Containers");
        nameField.setPromptText("Enter Port Name:");
        countryField.setPromptText("Enter Country:");
        countryBox.setPromptText("Select Country:");
//        contField.setPromptText("Add s:");
//        shipField.setPromptText("Enter your issue:");

        TableView<Port> tableView = new TableView<>();

        TableColumn<Port, String> codeColumn = new TableColumn<>("Port Code");
        TableColumn<Port, String> nameColumn = new TableColumn<>("Port Name");
        TableColumn<Port, String> countryColumn = new TableColumn<>("Port Country");
        TableColumn<Port, String> shipColumn = new TableColumn<>("Ships in Port");
        TableColumn<Port, String> containerColumn = new TableColumn<>("Containers in Port");

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("portCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("portCountry"));
        shipColumn.setCellValueFactory(new PropertyValueFactory<>(null));
        containerColumn.setCellValueFactory(new PropertyValueFactory<>(null));

        codeColumn.setMinWidth(100);
        nameColumn.setMinWidth(100);
        countryColumn.setMinWidth(100);
        containerColumn.setMinWidth(1000);
        tableView.getColumns().addAll(codeColumn, nameColumn, countryColumn, shipColumn, containerColumn);
        tableView.setEditable(true);
        tableView.setMinWidth(1000);

        Button saveButton = new Button("Save");
        Button button = new Button("sd");



        button.setOnAction(e -> {
            String data = api.getPortAtIndex(0);
            if (data != null) {
                System.out.println("Data at index 0: " + data);
            } else {
                System.out.println("Data at index 0 is null.");
            }
        });

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String country = countryBox.getValue();
            String code = Utilities.uniqueCodeGenerator();
            boolean portNameExists = tableView.getItems().stream().anyMatch(port -> port.getPortName().equals(name));
            boolean portCodeExists = tableView.getItems().stream().anyMatch(port -> port.getPortName().equals(code));

            if (!name.isBlank() && countryBox.getValue()!=null && !portNameExists && !portCodeExists){
                tableView.getItems().add(new Port(name, country,code));
                Port newPort = new Port(name,country,code);
                api.addPort(newPort);
                error.setText("");
                error2.setText("");
            }else{
                if (name.isBlank()){
                    error.setText("Field cannot be empty");
                }
                else {
                    error.setText("");
                }
                if (countryBox.getValue()==null){
                    error2.setText("Field cannot be empty");
                }else {
                    error2.setText("");
                }
                if (portNameExists){
                    error.setText("Port with the same name already exists");
                }
                if (portCodeExists){
                    error2.setText("Port with the code name already exists");
                }
            }
        });
//        Button delete = new Button();
//        delete.setOnAction(e-> {
//            if (nameField.toString().equals(ports.portCode)){
//
//                list.remove();
//            }
//        });

        tableView.setOnMouseClicked(e3 -> {
            if (e3.getClickCount() == 2) {
                port = tableView.getSelectionModel().getSelectedItem();
                if (port != null) {
                    try {
                        Pane individualPortRoot = new Pane();
                        individualPort = new IndividualPort(individualPortRoot, mainScene,this,api, port);
                        mainScene.switchToScene3();
                        System.out.println("portScene: " + port);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

//        tableView.setOnMouseClicked(e2->{
//            if(e2.getClickCount() == 2){
//                Port selectedPort = tableView.getSelectionModel().getSelectedItem();
//                if (selectedPort != null){
//                    Stage individualPortStage = new Stage();
//                    Scene individualPortScene = ;
//                    individualPortStage.setScene(individualPortScene);
//                    individualPortStage.show();
//                }
//            }
//        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(portLabel, nameField, error, countryLabel, countryBox, error2, saveButton, button, test);

        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(vBox, tableView);

        borderPane.setCenter(hBox);
        borderPane.setStyle(" -fx-padding: 10px;");

        root.getChildren().add(borderPane);
        root.setMinSize(1800,800);



    }

}
