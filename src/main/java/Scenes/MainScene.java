package Scenes;

import Controller.API;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class MainScene extends Application {

    private WelcomeScene scene1;
    private PortScene scene2;
    private IndividualPort scene3;
    private API api;

    private Stage primaryStage;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        Pane root1 = new Pane();
        Pane root2 = new Pane();
        Pane root3 = new Pane();

        scene1 = new WelcomeScene(root1,this);
        scene2 = new PortScene(root2, this);
        scene3 = new IndividualPort(root3,this, scene2, scene2.api, scene2.port);



        primaryStage.setTitle("CA1");

        primaryStage.setScene(scene1);
        primaryStage.show();
    }
    public void switchToScene1(){
        primaryStage.setScene(scene1);
    }
    public void switchToScene2(){
        primaryStage.setScene(scene2);
    }
    public void switchToScene3(){
        primaryStage.setScene(scene3);
    }
    public void switchScene(Scene scene){primaryStage.setScene(scene);}



}