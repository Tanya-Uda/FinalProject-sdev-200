package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchController {
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> searchType;
    @FXML
    private TableView<Book> resultTable;
    
    // Placeholder for TableColumns (for displaying book details)
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, Integer> bookIdColumn;
    @FXML
    private TableColumn<Book, Integer> availableCopiesColumn;  // Added column for available copies

    @FXML
    public void initialize() {
        // Populate search options with only book-related searches
        searchType.getItems().addAll("Book by ID", "Book by Title", "Author", "Genre");

        // Initialize table columns to display book details
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());
        genreColumn.setCellValueFactory(cellData -> cellData.getValue().genreProperty());
        bookIdColumn.setCellValueFactory(cellData -> cellData.getValue().bookIdProperty().asObject());
        availableCopiesColumn.setCellValueFactory(cellData -> cellData.getValue().availableCopiesProperty().asObject());  // Set available copies column
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the current stage
            MainController mainController = loader.getController();
            Stage stage = (Stage) searchField.getScene().getWindow();
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
    private void handleSearch() {
        String keyword = searchField.getText();
        String type = searchType.getValue();

        if (keyword.isEmpty() || type == null) {
            showAlert("Error", "Please enter a keyword and select a search type.");
            return;
        }

        String query = buildSearchQuery(type);
        if (query == null) {
            showAlert("Error", "Invalid search type selected.");
            return;
        }

        ObservableList<Book> results = executeSearchQuery(query, keyword);
        resultTable.setItems(results);
    }

    private String buildSearchQuery(String type) {
        switch (type) {
            case "Book by ID":
                return "SELECT * FROM book WHERE bookId = ?";
            case "Book by Title":
                return "SELECT * FROM book WHERE title LIKE ?";
            case "Author":
                return "SELECT * FROM book WHERE author LIKE ?";
            case "Genre":
                return "SELECT * FROM book WHERE genre LIKE ?";
            default:
                return null;
        }
    }

    private ObservableList<Book> executeSearchQuery(String query, String keyword) {
        ObservableList<Book> results = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (query.contains("bookId")) {
                stmt.setInt(1, Integer.parseInt(keyword));
            } else {
                stmt.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Modify this part to fetch availableCopies as well
                results.add(new Book(
                        rs.getInt("bookId"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("availableCopies")  // Added availableCopies from the result set
                ));
            }
        } catch (SQLException e) {
            showAlert("Error", "Unable to perform search: " + e.getMessage());
        }

        return results;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
