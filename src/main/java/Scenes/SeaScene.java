package Scenes;

import Controller.API;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import models.Pallet;
import models.Port;
import models.Ship;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


public class SeaScene extends Scene {
    private MainScene mainScene;
    private PortScene portScene;
    private API api;
    private IndividualPort individualPort;
    private TableView<Ship> shipTableView = new TableView();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SeaScene(Pane root, MainScene mainScene, PortScene portScene, API api){
        super(root);
        this.mainScene = mainScene;
        this.portScene = portScene;
        this.api = api;
        BorderPane borderPane = new BorderPane();


        root.getChildren().add(borderPane);

    }

}
