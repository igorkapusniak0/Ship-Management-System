package Scenes;


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

public class WelcomeScene extends Scene{
    private MainScene mainScene;
    public WelcomeScene(Pane root, MainScene mainScene) throws FileNotFoundException {
        super(root);
        this.mainScene = mainScene;


        FileInputStream inp = new FileInputStream("src/main/Images/download.jpeg");
        Image im = new Image(inp);
        BackgroundImage bi = new BackgroundImage(im,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(bi));

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMinSize(1800,800);
        vBox.setStyle(" -fx-padding: 40px;");

        VBox vBox1 = new VBox(10);
        vBox1.setAlignment(Pos.BOTTOM_CENTER);
        vBox1.setMinSize(1800,800);
        vBox1.setStyle(" -fx-padding: 40px;");

        Label welcome = new Label("Welcome To My CA1");
        welcome.setFont(new Font("Arial",50));
        welcome.setTextFill(Color.web("#ffffff"));
        Label proceed = new Label("Student Number: 20102236");
        proceed.setFont(new Font("Arial",25));
        proceed.setTextFill(Color.web("#ffffff"));
        Button button = new Button("Proceed");
        button.setFont(new Font("Arial",30));
        button.setOnAction(event -> mainScene.switchToScene2());


        vBox.getChildren().addAll(welcome,proceed);
        vBox1.getChildren().add(button);
        root.getChildren().addAll(vBox,vBox1);
    }
}
