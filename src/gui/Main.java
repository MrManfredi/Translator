package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
            primaryStage.setTitle("Translator");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
