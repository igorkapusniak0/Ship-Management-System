package Scenes;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.Port;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainScene extends Application {

    private WelcomeScene scene1;
    private PortScene scene2;
    private IndividualPort scene3;
    private Port port;

    private Stage primaryStage;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        this.primaryStage = primaryStage;

        Pane root1 = new Pane();
        Pane root2 = new Pane();
        Pane root3 = new Pane();

        scene1 = new WelcomeScene(root1,this);
        scene2 = new PortScene(root2, this);
        scene3 = new IndividualPort(root3,this, scene2, scene2.api, port);

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



}