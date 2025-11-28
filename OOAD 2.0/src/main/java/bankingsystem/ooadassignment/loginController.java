package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class loginController {

    @FXML
    private TextField idField;

    @FXML
    private PasswordField passwordField;

    private final String CUSTOMER_FILE = "src/main/resources/bankingsystem/ooadassignment/customers.txt";
    private final String TELLER_FILE   = "src/main/resources/bankingsystem/ooadassignment/tellers.txt";

    @FXML
    private void loginCustomer(ActionEvent event) {
        String id = idField.getText().trim();
        String password = passwordField.getText().trim();

        if (id.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter ID and password.");
            return;
        }

        System.out.println("\n=== LOGIN ATTEMPT ===");
        System.out.println("ID: " + id);
        System.out.println("Password: " + password);

        try {
            // ---------------------------- CUSTOMER LOGIN ----------------------------
            System.out.println("\n--- Checking Customer File ---");
            File custFile = new File(CUSTOMER_FILE);
            System.out.println("Customer file exists: " + custFile.exists());
            System.out.println("Customer file path: " + custFile.getAbsolutePath());

            BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE));
            String line;
            int lineNum = 0;

            while ((line = br.readLine()) != null) {
                lineNum++;
                System.out.println("Customer Line " + lineNum + ": " + line);

                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");

                if (parts[0].equalsIgnoreCase("customerID")) {
                    System.out.println("Skipping header line");
                    continue;
                }
                if (parts.length < 7) {
                    System.out.println("Line has only " + parts.length + " parts, need 7");
                    continue;
                }

                String userID = parts[0].trim();
                String userPass = parts[3].trim();

                System.out.println("Checking: ID=" + userID + ", Pass=" + userPass);

                if (userID.equals(id) && userPass.equals(password)) {
                    System.out.println("CUSTOMER LOGIN SUCCESS!");

                    customer c = new customer(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            parts[4].trim(),
                            parts[5].trim(),
                            parts[6].trim()
                    );

                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/bankingsystem/ooadassignment/customerdashboard.fxml"
                    ));

                    Parent root = loader.load();
                    customerdashboard controller = loader.getController();
                    controller.setCustomer(c);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    br.close();
                    return;
                }
            }
            br.close();
            System.out.println("No customer match found");

            // ---------------------------- TELLER LOGIN ----------------------------
            System.out.println("\n--- Checking Teller File ---");
            File tellerFile = new File(TELLER_FILE);
            System.out.println("Teller file exists: " + tellerFile.exists());
            System.out.println("Teller file path: " + tellerFile.getAbsolutePath());

            if (!tellerFile.exists()) {
                System.out.println("ERROR: Teller file not found!");
                showAlert("Error", "Teller file not found at: " + tellerFile.getAbsolutePath());
                return;
            }

            BufferedReader br2 = new BufferedReader(new FileReader(TELLER_FILE));
            lineNum = 0;

            while ((line = br2.readLine()) != null) {
                lineNum++;
                System.out.println("Teller Line " + lineNum + ": " + line);

                if (line.trim().isEmpty()) {
                    System.out.println("Empty line, skipping");
                    continue;
                }

                String[] parts = line.split(",");
                System.out.println("Split into " + parts.length + " parts");

                if (parts[0].equalsIgnoreCase("tellerID")) {
                    System.out.println("Skipping header line");
                    continue;
                }
                if (parts.length < 4) {
                    System.out.println("Line has only " + parts.length + " parts, need 4");
                    continue;
                }

                String tellerID = parts[0].trim();
                String tellerPass = parts[3].trim();

                System.out.println("Checking: ID=" + tellerID + ", Pass=" + tellerPass);

                if (tellerID.equals(id) && tellerPass.equals(password)) {
                    System.out.println("TELLER LOGIN SUCCESS!");

                    teller t = new teller(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim()
                    );

                    FXMLLoader loader = new FXMLLoader(getClass().getResource(
                            "/bankingsystem/ooadassignment/tellerdashboard.fxml"
                    ));

                    Parent root = loader.load();
                    tellerdashboardcontroller controller = loader.getController();
                    controller.setTeller(t);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    br2.close();
                    return;
                }
            }
            br2.close();
            System.out.println("No teller match found");

            // ---------------------------- FAILED LOGIN ----------------------------
            System.out.println("LOGIN FAILED - No match in either file");
            showAlert("Error", "Invalid ID or password.");

        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load user files: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void goToRegister(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/bankingsystem/ooadassignment/CustomerRegistration.fxml"
            ));
            Stage stage = new Stage();
            stage.setTitle("Customer Registration");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open registration page.");
        }
    }

    @FXML
    private void backToMain(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/bankingsystem/ooadassignment/MainMenu.fxml"
            ));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load main menu.");
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