package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerRegistrationController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField passwordField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    private static final String CUSTOMER_FILE = "customers.txt"; // file will be created in project root

    @FXML
    private void registerCustomer(ActionEvent event) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String password = passwordField.getText().trim();
        String address = addressField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        // Validate required fields
        if(id.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in ID, First Name, Surname, and Password.");
            return;
        }

        // Append to customers.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMER_FILE, true))) {
            bw.write(String.join(",", id, name, surname, password, address, phone, email));
            bw.newLine();
            showAlert("Success", "Customer registered successfully!");
        } catch(IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save customer.");
        }

        // Clear fields
        idField.clear(); nameField.clear(); surnameField.clear();
        passwordField.clear(); addressField.clear(); phoneField.clear(); emailField.clear();
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
