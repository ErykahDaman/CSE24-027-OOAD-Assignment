package bankingsystem.bankscenarioassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CustomerRegistrationController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSurname;
    @FXML private TextField txtID;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnSave;

    @FXML
    private void registerCustomer(ActionEvent event) {
        saveToFile();
    }

    private void saveToFile() {
        String first = txtFirstName.getText();
        String last = txtSurname.getText();
        String id = txtID.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        if (first.isEmpty() || last.isEmpty() || id.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            showAlert("Error", "All fields must be filled!");
            return;
        }

        String customerData = id + "," + first + "," + last + "," + email + "," + pass;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt", true))) {
            writer.write(customerData);
            writer.newLine();
            showAlert("Success", "Customer registered successfully!");

            // Clear fields
            txtFirstName.clear();
            txtSurname.clear();
            txtID.clear();
            txtEmail.clear();
            txtPassword.clear();

        } catch (IOException e) {
            showAlert("File Error", "Could not save customer.");
            e.printStackTrace();
        }
    }
    @FXML
    private void backToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
