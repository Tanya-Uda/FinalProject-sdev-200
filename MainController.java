package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;

    // Setter to pass the stage reference from Main.java
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void handleQuit() {
        System.exit(0); 
    }
    private void switchScene(String fxmlFile) throws IOException {
        if (stage == null) {
            System.out.println("Stage is not initialized.");
            return;  // Exit early if stage is null
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Pass stage to the next controller
        Object controller = loader.getController();
        if (controller instanceof MainController) {
            ((MainController) controller).setStage(stage);
        }

        stage.setScene(new Scene(root, 400, 400));
        stage.show();
    }

    @FXML
    private void handleManageBooks() throws IOException {
        switchScene("ManageBooks.fxml");
    }

    @FXML
    private void handleManageMembers() throws IOException {
        switchScene("ManageMembers.fxml");
    }

    @FXML
    private void handleIssueReturn() throws IOException {
        switchScene("IssueReturn.fxml");
    }

    @FXML
    private void handleSearch() throws IOException {
        switchScene("Search.fxml");
    }

    @FXML
    private void handleReport() throws IOException {
        switchScene("Report.fxml");
    }

    public void initializeButtons() {
        // If you need to reset or initialize something when returning
        System.out.println("MainController: Buttons initialized.");
    }
}
