package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Main extends Application {

	private LibrarySystem librarySystem;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize the library system
            librarySystem = new LibrarySystem();

            // Load the FXML file
            Parent root = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
            Scene scene = new Scene(root, 400, 400);

            primaryStage.setTitle("Library Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Event handler for "Manage Books"
    public void handleManageBooks() {
        showInfo("Manage Books", "This feature will allow you to add and manage books.");
        // This is where you will open the "Manage Books" window or form
    }

    // Event handler for "Manage Members"
    public void handleManageMembers() {
        showInfo("Manage Members", "This feature will allow you to add and manage members.");
        // This is where you will open the "Manage Members" window or form
    }

    // Event handler for "Issue/Return Book"
    public void handleIssueReturn() {
        showInfo("Issue/Return Book", "This feature will allow you to issue or return books.");
        // This is where you will open the "Issue/Return Book" window or form
    }

    // Event handler for "Search Book"
    public void handleSearchBook() {
        showInfo("Search Book", "This feature will allow you to search for books.");
        // This is where you will open the "Search Book" window or form
    }

    // Event handler for "Show Report"
    public void handleShowReport() {
        showInfo("Show Report", "This feature will allow you to view reports.");
        // This is where you will open the "Show Report" window or form
    }

    // Show an informational message
    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
