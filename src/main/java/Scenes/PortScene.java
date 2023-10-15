package Scenes;

import LinkedList.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import models.Container;
import models.Ship;
import models.Port;
import utils.Utilities;
import utils.Countries;

public class PortScene extends Scene {
    private MainScene mainScene;
    private Port ports;
    private Ship ships;
    private Container containersInPort;
    private List list;
    Pane window;
    private List<Countries> countriesList;
   // private IndividualPort individualPort;


    public PortScene(Pane root,MainScene mainScene) {
        super(root);
        this.mainScene = mainScene;
        window = root;
        list = new List<Port>();
        BorderPane borderPane = new BorderPane();

        TextField nameField = new TextField();
        TextField countryField = new TextField();
        ComboBox<String> countryBox = new ComboBox<>();

//        ComboBox<Ship> shipField = new ComboBox<>();
//        ComboBox<Container> contField = new ComboBox<>();
//
//
//        contField.getItems().addAll();
        String[] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda",
                "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain",
                "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan",
                "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria",
                "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada",
                "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros",
                "Congo (Congo-Brazzaville)", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba",
                "Cyprus", "Czechia", "Democratic Republic of the Congo (Congo-Kinshasa)", "Denmark",
                "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
                "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji",
                "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana",
                "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
                "Holy See", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
                "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan",
                "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho",
                "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia (North Macedonia)",
                "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
                "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
                "Montenegro", "Morocco", "Mozambique", "Myanmar (formerly Burma)", "Namibia", "Nauru",
                "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea",
                "Norway", "Oman", "Pakistan", "Palau", "Palestine State", "Panama", "Papua New Guinea",
                "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania",
                "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
                "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia",
                "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
                "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan",
                "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania",
                "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia",
                "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates",
                "United Kingdom", "United States of America", "Uruguay", "Uzbekistan", "Vanuatu",
                "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe"};

        countryBox.getItems().addAll(countries);


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
        button.setOnAction(e-> {
            String he =list.listAll();
            System.out.println(he);
        });

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String country = countryBox.getValue();
            String code = Utilities.uniqueCodeGenerator();
            boolean portNameExists = tableView.getItems().stream().anyMatch(port -> port.getPortName().equals(name));
            boolean portCodeExists = tableView.getItems().stream().anyMatch(port -> port.getPortName().equals(code));

            if (!name.isBlank() && countryBox.getValue()!=null && !portNameExists && !portCodeExists){
                tableView.getItems().add(new Port(name, country,code));
                list.add(ports);
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
        vBox.getChildren().addAll(portLabel, nameField, error, countryLabel, countryBox, error2, saveButton, button);

        HBox hBox = new HBox(100);
        hBox.getChildren().addAll(vBox, tableView);

        borderPane.setCenter(hBox);
        borderPane.setStyle(" -fx-padding: 10px;");

        root.getChildren().add(borderPane);
        root.setMinSize(1800,800);



    }

}
