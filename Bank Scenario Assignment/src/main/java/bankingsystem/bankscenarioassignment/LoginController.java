package bankingsystem.bankscenarioassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField; // change your FXML TextField to PasswordField for security

    // -----------------------------
    // Login Button
    // -----------------------------
    @FXML
    private void loginCustomer(ActionEvent event) {
        String id = idField.getText().trim();
        String password = passwordField.getText().trim();

        if (id.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter ID and password.");
            return;
        }

        try {
            // -----------------------------
            // 1️⃣ Check customer.txt
            // -----------------------------
            BufferedReader customerReader = new BufferedReader(
                    new FileReader("src/main/resources/bankingsystem/bankscenarioassignment/customer.txt")
            );

            String line;
            boolean customerFound = false;

            while ((line = customerReader.readLine()) != null) {
                String[] parts = line.split(",");
                String customerID = parts[0];
                String custPassword = parts[3];

                if (customerID.equalsIgnoreCase(id) && custPassword.equals(password)) {
                    // Customer login successful
                    customerFound = true;
                    customer c = new customer(
                            parts[0], parts[1], parts[2],
                            parts[3], parts[4], parts[5], parts[6]
                    );

                    // Open Customer Dashboard
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/bankingsystem/bankscenarioassignment/CustomerDashboard.fxml"
                    ));
                    Parent root = loader.load();
                    CustomerDashboardController controller = loader.getController();
                    controller.setCustomer(c);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                    customerReader.close();
                    return;
                }
            }

            customerReader.close();

            // -----------------------------
            // 2️⃣ Check tellers.txt
            // -----------------------------
            BufferedReader tellerReader = new BufferedReader(
                    new FileReader("src/main/resources/bankingsystem/bankscenarioassignment/teller.txt")
            );

            while ((line = tellerReader.readLine()) != null) {
                String[] parts = line.split(",");
                String tellerID = parts[0];
                String tellerPassword = parts[3];

                if (tellerID.equalsIgnoreCase(id) && tellerPassword.equals(password)) {
                    // Teller login successful
                    teller t = new teller(parts[0], parts[1], parts[2], parts[3]);

                    // Open Teller Dashboard
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/bankingsystem/bankscenarioassignment/TellerDashboard.fxml"
                    ));
                    Parent root = loader.load();
                    TellerDashboardController controller = loader.getController();
                    controller.setTeller(t);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                    tellerReader.close();
                    return;
                }
            }

            tellerReader.close();

            // -----------------------------
            // Login failed
            // -----------------------------
            showAlert("Error", "Invalid ID or password.");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to read user files.");
        }
    }
    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/bankingsystem/bankscenarioassignment/CustomerRegistration.fxml"
            ));
            Parent root = loader.load();
            Stage stage = new Stage(); // open in a new window
            stage.setTitle("Customer Registration");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open registration page.");
        }
    }
    // -----------------------------
    // Back Button
    // -----------------------------
    @FXML
    private void backToMain(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/bankingsystem/bankscenarioassignment/MainMenu.fxml"
            ));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load main menu.");
        }
    }

    // -----------------------------
    // Helper: Show alert
    // -----------------------------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
