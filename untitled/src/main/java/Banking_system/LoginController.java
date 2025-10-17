package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.ArrayList;

public class LoginController {

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    private customer loggedInCustomer;

    @FXML
    private void loginCustomer(ActionEvent event) throws Exception {
        String enteredID = idField.getText();
        String enteredPassword = passwordField.getText();

        if (enteredID.isEmpty() || enteredPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter Customer ID and Password.");
            return;
        }

        ArrayList<customer> customers = CustomerRegistrationController.getCustomers();
        boolean found = false;

        for (customer c : customers) {
            if (c.getCustomerID().equals(enteredID)) {
                found = true;
                if(c.getPassword().equals(enteredPassword)) {
                    loggedInCustomer = c;
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful! Welcome, " + c.getFirstname());

                    // Load next screen (e.g., Account Dashboard)
                    Parent root = FXMLLoader.load(getClass().getResource("/AccountDashboard.fxml"));
                    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Incorrect password.");
                }
                break;
            }
        }

        if (!found) {
            showAlert(Alert.AlertType.ERROR, "Error", "Customer ID not found.");
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public customer getLoggedInCustomer() {
        return loggedInCustomer;
    }
}
