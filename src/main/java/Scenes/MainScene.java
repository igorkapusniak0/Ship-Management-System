package Scenes;


import Controller.API;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;

public class MainScene extends Application {
    private PortScene scene2;
    private Stage primaryStage;
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;


        Pane root1 = new Pane();
        Pane root2 = new Pane();

        WelcomeScene scene1 = new WelcomeScene(root1, this);
        scene2 = new PortScene(root2, this);

        primaryStage.setTitle("CA1");

        primaryStage.setScene(scene1);
        primaryStage.show();

        // API.load("data.ser");
//        primaryStage.setOnCloseRequest(windowEvent -> {
//            API.save("data.ser");
//            System.out.println("saved");
//        });

    }
    public void switchToScene2(){
        primaryStage.setScene(scene2);
    }

    public void switchScene(Scene scene){primaryStage.setScene(scene);}



}