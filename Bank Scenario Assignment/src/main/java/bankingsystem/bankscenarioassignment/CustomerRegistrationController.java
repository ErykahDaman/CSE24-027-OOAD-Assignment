package bankingsystem.bankscenarioassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class CustomerRegistrationController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSurname;
    @FXML private TextField txtID;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnSave;

    // Path to customer file
    private final String CUSTOMER_FILE = "src/main/resources/bankingsystem/bankscenarioassignment/customer.txt";

    @FXML
    private void saveCustomer(ActionEvent event) {
        String id = txtID.getText().trim();
        String firstName = txtFirstName.getText().trim();
        String surname = txtSurname.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        if (id.isEmpty() || firstName.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        // Optional: Add simple ID format check
        if (!id.matches("C\\d{3}")) {
            showAlert("Error", "Customer ID must be in format C001, C002, etc.");
            return;
        }

        // Build the line to save
        String newCustomerLine = id + "," + firstName + "," + surname + "," + password + "," + "UnknownAddress" + "," + "UnknownPhone" + "," + email;

        try {
            File file = new File(CUSTOMER_FILE);
            file.getParentFile().mkdirs(); // Ensure folder exists
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // append mode
                writer.write(newCustomerLine);
                writer.newLine();
            }

            showAlert("Success", "Customer registered successfully!");

            // Clear the form
            txtID.clear();
            txtFirstName.clear();
            txtSurname.clear();
            txtEmail.clear();
            txtPassword.clear();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save customer.");
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
