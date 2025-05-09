package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the MainDashboard.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Get the controller and set the primary stage
            MainController controller = loader.getController();
            controller.setStage(primaryStage); // Make sure this line is called to pass the stage

            // Set the scene and show the primaryStage
            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(new Scene(root, 400, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
