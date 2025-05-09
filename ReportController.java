package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportController {

    @FXML
    private TextField inputId;
    @FXML
    private ComboBox<String> reportType;
    @FXML
    private TextArea reportArea;

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the current stage
            MainController mainController = loader.getController();
            Stage stage = (Stage) inputId.getScene().getWindow();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        reportType.getItems().addAll("Book", "Member");
    }

    @FXML
    private void handleReport() {
        String id = inputId.getText();
        String type = reportType.getValue();
        if (id == null || id.isEmpty() || type == null) {
            showAlert("Missing Input", "Please enter an ID and select a report type.");
            return;
        }

        StringBuilder report = new StringBuilder();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "")) {

            if (type.equals("Book")) {
                PreparedStatement bookStmt = conn.prepareStatement("SELECT * FROM Book WHERE bookId = ?");
                bookStmt.setString(1, id);
                ResultSet bookRs = bookStmt.executeQuery();

                if (bookRs.next()) {
                    report.append("Book Report\n===========\n");
                    report.append("ID: ").append(bookRs.getString("bookId")).append("\n");
                    report.append("Title: ").append(bookRs.getString("title")).append("\n");
                    report.append("Author: ").append(bookRs.getString("author")).append("\n");
                    report.append("Genre: ").append(bookRs.getString("genre")).append("\n");
                    report.append("Total Copies: ").append(bookRs.getInt("totalCopies")).append("\n");
                    report.append("Available Copies: ").append(bookRs.getInt("availableCopies")).append("\n");
                    report.append("Date Added: ").append(bookRs.getDate("dateAdded")).append("\n\n");

                    PreparedStatement issuedStmt = conn.prepareStatement(
                        "SELECT m.memberId, m.firstName, m.lastName, t.issueDate, t.returnDate " +
                        "FROM Transaction t JOIN Member m ON t.memberId = m.memberId " +
                        "WHERE t.bookId = ?"
                    );
                    issuedStmt.setString(1, id);
                    ResultSet issuedRs = issuedStmt.executeQuery();

                    report.append("Issued By:\n");
                    while (issuedRs.next()) {
                        report.append("- Member ID: ").append(issuedRs.getString("memberId"))
                              .append(", Name: ").append(issuedRs.getString("firstName")).append(" ").append(issuedRs.getString("lastName"))
                              .append(", Issued: ").append(issuedRs.getDate("issueDate"))
                              .append(", Returned: ").append(issuedRs.getDate("returnDate"))
                              .append("\n");
                    }

                } else {
                    report.append("No book found with ID: ").append(id);
                }

            } else if (type.equals("Member")) {
                PreparedStatement memberStmt = conn.prepareStatement("SELECT * FROM Member WHERE memberId = ?");
                memberStmt.setString(1, id);
                ResultSet memberRs = memberStmt.executeQuery();

                if (memberRs.next()) {
                    report.append("Member Report\n=============\n");
                    report.append("ID: ").append(memberRs.getString("memberId")).append("\n");
                    report.append("Name: ").append(memberRs.getString("firstName")).append(" ").append(memberRs.getString("lastName")).append("\n");
                    report.append("Birth Date: ").append(memberRs.getDate("birthDate")).append("\n");
                    report.append("Phone: ").append(memberRs.getString("phoneNumber")).append("\n");
                    report.append("Email: ").append(memberRs.getString("email")).append("\n\n");

                    PreparedStatement issuedStmt = conn.prepareStatement(
                        "SELECT b.bookId, b.title, t.issueDate, t.returnDate " +
                        "FROM Transaction t JOIN Book b ON t.bookId = b.bookId " +
                        "WHERE t.memberId = ?"
                    );
                    issuedStmt.setString(1, id);
                    ResultSet issuedRs = issuedStmt.executeQuery();

                    report.append("Books Issued:\n");
                    while (issuedRs.next()) {
                        report.append("- Book ID: ").append(issuedRs.getString("bookId"))
                              .append(", Title: ").append(issuedRs.getString("title"))
                              .append(", Issued: ").append(issuedRs.getDate("issueDate"))
                              .append(", Returned: ").append(issuedRs.getDate("returnDate"))
                              .append("\n");
                    }

                } else {
                    report.append("No member found with ID: ").append(id);
                }
            }

        } catch (Exception e) {
            showAlert("Database Error", e.getMessage());
            return;
        }

        reportArea.setText(report.toString());
    }
}

