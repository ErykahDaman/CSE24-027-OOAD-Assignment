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

import java.io.*;

public class CustomerRegistrationController {

    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private TextField passwordField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    private static final String CUSTOMER_FILE = "src/main/resources/bankingsystem/ooadassignment/customers.txt";

    @FXML
    private void registerCustomer(ActionEvent event) {
        try {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String password = passwordField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            System.out.println("\n=== CUSTOMER REGISTRATION ===");
            System.out.println("Name: " + name);
            System.out.println("Surname: " + surname);

            // Validate required fields
            if (name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in First Name, Surname, and Password.");
                return;
            }

            // Validate email format (basic check)
            if (!email.isEmpty() && !email.contains("@")) {
                showAlert("Error", "Please enter a valid email address.");
                return;
            }

            // Validate phone format (basic check)
            if (!phone.isEmpty() && !phone.matches("\\d{10}")) {
                showAlert("Error", "Please enter a valid 10-digit phone number.");
                return;
            }

            // Generate Customer ID
            String customerID = generateCustomerID();
            System.out.println("Generated Customer ID: " + customerID);

            // Append to customers.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMER_FILE, true))) {
                bw.newLine(); // Add a new line before writing
                String customerData = customerID + "," + name + "," + surname + "," +
                        password + "," + address + "," + phone + "," + email;
                bw.write(customerData);
                System.out.println("Written to file: " + customerData);
            }

            System.out.println("Registration successful!");
            showAlert("Success", "Account created successfully!\nYour Customer ID is: " + customerID);

            // Create customer object
            customer c = new customer(customerID, name, surname, password, address, phone, email);

            // Redirect to Customer Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/bankingsystem/ooadassignment/customerdashboard.fxml"
            ));
            Parent root = loader.load();
            customerdashboard controller = loader.getController();
            controller.setCustomer(c);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to register customer: " + e.getMessage());
        }
    }

    private String generateCustomerID() throws IOException {
        int maxNumber = 0;

        File file = new File(CUSTOMER_FILE);

        // If file doesn't exist, start with C001
        if (!file.exists()) {
            System.out.println("Customer file doesn't exist, starting with C001");
            return "C001";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines and header
                if (line.trim().isEmpty() || line.trim().toLowerCase().startsWith("customerid")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String customerID = parts[0].trim();

                    // Extract number from customer ID (e.g., "C001" -> 1)
                    if (customerID.startsWith("C")) {
                        try {
                            int num = Integer.parseInt(customerID.substring(1));
                            maxNumber = Math.max(maxNumber, num);
                            System.out.println("Found customer ID: " + customerID + " (number: " + num + ")");
                        } catch (NumberFormatException e) {
                            System.out.println("Couldn't parse customer ID: " + customerID);
                        }
                    }
                }
            }
        }

        System.out.println("Max customer number found: " + maxNumber);
        return "C" + String.format("%03d", maxNumber + 1);
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load main menu.");
        }
    }

    private void showAlert(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}