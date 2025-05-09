package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class IssueReturnController {

    @FXML
    private TextField memberField;
    @FXML
    private TextArea memberArea;
    @FXML
    private TextField bookField;
    @FXML
    private TextArea bookArea;

    private int currentMemberId = -1;
    private int currentBookId = -1;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the current stage
            MainController mainController = loader.getController();
            Stage stage = (Stage) memberField.getScene().getWindow();
            mainController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.setResizable(true);
            stage.show();
            
        } catch (Exception e) {
            showAlert("Error", "Unable to load Main Dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleFindMember() {
        String input = memberField.getText();
        String query = input.matches("\\d+") ? 
                "SELECT * FROM member WHERE memberId = ?" : 
                "SELECT * FROM member WHERE firstName LIKE ? OR lastName LIKE ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (input.matches("\\d+")) {
                stmt.setInt(1, Integer.parseInt(input));
            } else {
                stmt.setString(1, "%" + input + "%");
                stmt.setString(2, "%" + input + "%");
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentMemberId = rs.getInt("memberId");
                memberArea.setText("ID: " + currentMemberId + "\nName: " +
                        rs.getString("firstName") + " " + rs.getString("lastName"));
            } else {
                memberArea.setText("Member not found.");
                currentMemberId = -1;
            }
        } catch (SQLException e) {
            showAlert("Error", "Unable to find member: " + e.getMessage());
        }
    }

    @FXML
    private void handleFindBook() {
        String input = bookField.getText();
        String query = input.matches("\\d+") ? 
                "SELECT * FROM book WHERE bookId = ?" : 
                "SELECT * FROM book WHERE title LIKE ?";

        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            if (input.matches("\\d+")) {
                stmt.setInt(1, Integer.parseInt(input));
            } else {
                stmt.setString(1, "%" + input + "%");
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentBookId = rs.getInt("bookId");
                int total = rs.getInt("totalCopies");
                int available = rs.getInt("availableCopies");

                bookArea.setText("ID: " + currentBookId + "\nTitle: " + rs.getString("title") +
                        "\nAvailable: " + available + "/" + total);
            } else {
                bookArea.setText("Book not found.");
                currentBookId = -1;
            }
        } catch (SQLException e) {
            showAlert("Error", "Unable to find book: " + e.getMessage());
        }
    }

    @FXML
    private void handleIssueBook() {
        if (currentMemberId == -1 || currentBookId == -1) {
            showAlert("Error", "Select both member and book first.");
            return;
        }

        String insertQuery = "INSERT INTO transaction (bookId, memberId, issueDate, dueDate) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE book SET availableCopies = availableCopies - 1 WHERE bookId = ? AND availableCopies > 0";

        try (Connection conn = connect()) {
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, currentBookId);
            int updated = updateStmt.executeUpdate();

            if (updated == 0) {
                showAlert("Unavailable", "No available copies to issue.");
                return;
            }
            
            LocalDate issueDate = LocalDate.now();
            LocalDate dueDate = issueDate.plusDays(14);
            
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setInt(1, currentBookId);
            insertStmt.setInt(2, currentMemberId);
            insertStmt.setDate(3, Date.valueOf(LocalDate.now()));  // Set issueDate as today's date
            insertStmt.setDate(4, Date.valueOf(LocalDate.now().plusDays(14)));  // Set dueDate (14 days from now)

            insertStmt.executeUpdate();

            showAlert("Issued", "Book successfully issued. The Due date is: " + dueDate.toString());
            handleFindBook(); // Update availability
        } catch (SQLException e) {
            showAlert("Error", "Unable to issue book: " + e.getMessage());
        }
    }

    @FXML
    private void handleReturnBook() {
        if (currentMemberId == -1 || currentBookId == -1) {
            showAlert("Error", "Select both member and book first.");
            return;
        }

        String updateTransaction = "UPDATE transaction SET returnDate = ? WHERE bookId = ? AND memberId = ? AND returnDate IS NULL";
        String updateBook = "UPDATE book SET availableCopies = availableCopies + 1 WHERE bookId = ?";

        try (Connection conn = connect()) {
            PreparedStatement updateStmt = conn.prepareStatement(updateTransaction);
            updateStmt.setDate(1, Date.valueOf(LocalDate.now()));  // Set returnDate as today's date
            updateStmt.setInt(2, currentBookId);
            updateStmt.setInt(3, currentMemberId);

            int rows = updateStmt.executeUpdate();
            if (rows == 0) {
                showAlert("Not Found", "No active issue found.");
                return;
            }

            PreparedStatement updateBookStmt = conn.prepareStatement(updateBook);
            updateBookStmt.setInt(1, currentBookId);
            updateBookStmt.executeUpdate();

            showAlert("Returned", "Book successfully returned.");
            handleFindBook();  // Update availability
        } catch (SQLException e) {
            showAlert("Error", "Unable to return book: " + e.getMessage());
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
