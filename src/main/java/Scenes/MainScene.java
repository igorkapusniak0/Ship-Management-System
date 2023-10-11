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

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainScene extends Application {
    Stage window;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage main) throws FileNotFoundException {
        window = main;
        window.setTitle("CA1");

        FileInputStream inp = new FileInputStream("/home/igor/IdeaProjects/CA1/src/main/Images/download.jpeg");
        Image im = new Image(inp);
        BackgroundImage bi = new BackgroundImage(im,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        VBox hBox = new VBox(175);
        hBox.setAlignment(Pos.CENTER);
        hBox.setBackground(new Background(bi));

        Label welcome = new Label("Welcome To My CA1");
        welcome.setFont(new Font("Arial",50));
        Label proceed = new Label("Student Number: 20102236");
        proceed.setTextFill(Color.web("#ffffff"));
        Button button = new Button("Proceed");
        button.setFont(new Font("Arial",30));
        button.setOnAction(event -> System.out.println("hello"));

        hBox.getChildren().addAll(welcome,proceed,button);
        Scene scene = new Scene(hBox,1500,800);
        window.setScene(scene);
        window.show();

    }

}