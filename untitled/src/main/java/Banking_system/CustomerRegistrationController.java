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

import java.util.ArrayList;

public class CustomerRegistrationController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField idField;

    @FXML
    private TextField contactField;

    @FXML
    private TextField passwordField;

    // ArrayList to store registered customers (demo purposes)
    private static ArrayList<customer> customers = new ArrayList<>();

    @FXML
    private void registerCustomer(ActionEvent event) {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String customerID = idField.getText();
        String contact = contactField.getText();
        String password = passwordField.getText();

        // Basic validation
        if(name.isEmpty() || ageText.isEmpty() || customerID.isEmpty() || contact.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if(age < 17) {
                showAlert(Alert.AlertType.ERROR, "Error", "Customer must be at least 17 years old.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Age must be a number.");
            return;
        }

        if(!contact.matches("\\d{10}")) { // 10-digit contact number
            showAlert(Alert.AlertType.ERROR, "Error", "Contact number must be 10 digits.");
            return;
        }

        // Save customer
        customer newCustomer = new customer(name, age, customerID, contact);
        customers.add(newCustomer);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer registered successfully!");

        // Clear fields
        nameField.clear();
        ageField.clear();
        idField.clear();
        contactField.clear();
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

    // Getter to access customer list from other controllers
    public static ArrayList<customer> getCustomers() {
        return customers;
    }
}
