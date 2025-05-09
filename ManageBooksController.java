package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class ManageBooksController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField genreField;
    @FXML private TextField copiesField;
    @FXML private TextField deleteIdField;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Pass the current stage to MainController
            MainController mainController = loader.getController();
            Stage stage = (Stage) titleField.getScene().getWindow();
            mainController.setStage(stage);

            stage.setScene(new Scene(root, 400, 400));
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to load Main Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String genre = genreField.getText();
        int totalCopies;

        try {
            totalCopies = Integer.parseInt(copiesField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Total copies must be a number.");
            return;
        }

        String query = "INSERT INTO book (title, author, genre, totalCopies, availableCopies, dateAdded) VALUES (?, ?, ?, ?, ?, CURDATE())";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, genre);
            stmt.setInt(4, totalCopies);
            stmt.setInt(5, totalCopies);
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int bookId = generatedKeys.getInt(1);
                showAlert("Success", "Book added successfully with ID: " + bookId);
            } else {
                showAlert("Success", "Book added successfully.");
            }

        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteBook() {
        int bookId;

        try {
            bookId = Integer.parseInt(deleteIdField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Book ID must be a number.");
            return;
        }

        String selectQuery = "SELECT * FROM book WHERE bookId = ?";
        String deleteQuery = "DELETE FROM book WHERE bookId = ?";

        try (Connection conn = connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            selectStmt.setInt(1, bookId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                showAlert("Not Found", "Book with ID " + bookId + " not found.");
                return;
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, bookId);
                deleteStmt.executeUpdate();
                showAlert("Deleted", "Book deleted successfully.");
            }

        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
