package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class ManageMembersController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField birthDateField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField emailField;
    @FXML private TextField searchField;
    @FXML private TextArea memberInfoArea;
    @FXML private TextField deleteIdField;

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
            Stage stage = (Stage) firstNameField.getScene().getWindow();
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
    private void handleAddMember() {
        String query = "INSERT INTO member (firstName, lastName, birthDate, phoneNumber, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, firstNameField.getText());
            stmt.setString(2, lastNameField.getText());
            stmt.setString(3, birthDateField.getText());
            stmt.setString(4, phoneNumberField.getText());
            stmt.setString(5, emailField.getText());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int memberId = generatedKeys.getInt(1);
                showAlert("Success", "Member added successfully with ID: " + memberId);
            } else {
                showAlert("Success", "Member added successfully.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
        }
    }

    @FXML
    private void handleSearchMember() {
        String input = searchField.getText();
        String query;
        boolean isId = input.matches("\\d+");

        if (isId) {
            query = "SELECT * FROM member WHERE memberId = ?";
        } else {
            query = "SELECT * FROM member WHERE firstName LIKE ? OR lastName LIKE ?";
        }

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (isId) {
                stmt.setInt(1, Integer.parseInt(input));
            } else {
                stmt.setString(1, "%" + input + "%");
                stmt.setString(2, "%" + input + "%");
            }

            ResultSet rs = stmt.executeQuery();
            StringBuilder result = new StringBuilder();

            while (rs.next()) {
                int memberId = rs.getInt("memberId");
                result.append("ID: ").append(memberId).append("\n");
                result.append("Name: ").append(rs.getString("firstName")).append(" ")
                      .append(rs.getString("lastName")).append("\n");
                result.append("Birth Date: ").append(rs.getDate("birthDate")).append("\n");
                result.append("Phone: ").append(rs.getString("phoneNumber")).append("\n");
                result.append("Email: ").append(rs.getString("email")).append("\n");
                result.append("Issued Books:\n");

                String historyQuery = "SELECT b.title, t.issueDate, t.returnDate " +
                        "FROM transaction t JOIN book b ON t.bookId = b.bookId WHERE t.memberId = ?";

                try (PreparedStatement historyStmt = conn.prepareStatement(historyQuery)) {
                    historyStmt.setInt(1, memberId);
                    ResultSet hr = historyStmt.executeQuery();
                    while (hr.next()) {
                        result.append(" - ").append(hr.getString("title"))
                              .append(" | Issued: ").append(hr.getDate("issueDate"))
                              .append(" | Returned: ").append(hr.getDate("returnDate")).append("\n");
                    }
                }
                result.append("\n");
            }

            if (result.length() == 0) {
                result.append("No member found.");
            }

            memberInfoArea.setText(result.toString());

        } catch (SQLException e) {
            showAlert("Error", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteMember() {
        try {
            int memberId = Integer.parseInt(deleteIdField.getText());
            String selectQuery = "SELECT * FROM member WHERE memberId = ?";
            String deleteQuery = "DELETE FROM member WHERE memberId = ?";

            try (Connection conn = connect();
                 PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

                selectStmt.setInt(1, memberId);
                ResultSet rs = selectStmt.executeQuery();

                if (!rs.next()) {
                    showAlert("Not Found", "Member not found.");
                    return;
                }

                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, memberId);
                    deleteStmt.executeUpdate();
                    showAlert("Deleted", "Member deleted successfully.");
                }
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "ID must be a number.");
        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
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
