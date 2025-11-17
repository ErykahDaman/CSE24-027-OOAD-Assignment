package bankingsystem.bankscenarioassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TellerDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> customersListView;

    private teller loggedInTeller;
    private List<customer> customers = new ArrayList<>();

    private final String CUSTOMER_FILE = "src/main/resources/bankingsystem/bankscenarioassignment/customers.txt";

    // ----------------------------
    // Initialize Teller
    // ----------------------------
    public void setTeller(teller t) {
        this.loggedInTeller = t;
        welcomeLabel.setText("Welcome, " + t.getFirstname() + "!");
        loadCustomers();
    }

    // ----------------------------
    // Load all customers into ListView
    // ----------------------------
    private void loadCustomers() {
        customers.clear();
        customersListView.getItems().clear();

        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) {
            showAlert("Error", "Customers file not found!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue; // skip invalid lines
                customer c = new customer(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                customers.add(c);
                customersListView.getItems().add(c.getCustomerID() + " - " + c.getFirstname() + " " + c.getSurname());
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load customers.");
        }
    }

    // ----------------------------
    // Delete selected customer
    // ----------------------------
    @FXML
    private void deleteCustomer(ActionEvent event) {
        int index = customersListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Select Customer", "Please select a customer to delete.");
            return;
        }

        customer c = customers.get(index);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to delete " + c.getFirstname() + " " + c.getSurname() + "?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;

        try {
            List<String> remainingLines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(c.getCustomerID() + ",")) remainingLines.add(line);
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
                for (String l : remainingLines) {
                    bw.write(l);
                    bw.newLine();
                }
            }

            loadCustomers();
            showAlert("Success", "Customer deleted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete customer.");
        }
    }

    // ----------------------------
    // View selected customer's accounts
    // ----------------------------
    @FXML
    private void viewCustomerAccounts(ActionEvent event) {
        int index = customersListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Select Customer", "Please select a customer to view accounts.");
            return;
        }

        customer c = customers.get(index);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/bankingsystem/bankscenarioassignment/Accounts.fxml"
            ));
            Parent root = loader.load();

            AccountDashboardController controller = loader.getController();
            controller.setCustomer(c); // pass the customer

            Stage stage = (Stage) customersListView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Accounts page.");
        }
    }

    // ----------------------------
    // Go to Customer Registration
    // ----------------------------
    @FXML
    private void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankingsystem/bankscenarioassignment/CustomerRegistration.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) customersListView.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Customer Registration page.");
        }
    }

    // ----------------------------
    // Logout
    // ----------------------------
    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/bankscenarioassignment/MainMenu.fxml"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout.");
        }
    }

    // ----------------------------
    // Show alerts
    // ----------------------------
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
