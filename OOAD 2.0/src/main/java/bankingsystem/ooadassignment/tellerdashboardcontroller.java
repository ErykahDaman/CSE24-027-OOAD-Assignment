package bankingsystem.ooadassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class tellerdashboardcontroller {

    @FXML private Label welcomeLabel;
    @FXML private ListView<String> customerListView;

    private teller loggedInTeller;
    private List<customer> customers = new ArrayList<>();

    private final String CUSTOMER_FILE = "src/main/resources/bankingsystem/ooadassignment/customers.txt";
    private final String TELLER_FILE = "src/main/resources/bankingsystem/ooadassignment/tellers.txt";

    // Set teller object and initialize dashboard
    public void setTeller(teller t) {
        this.loggedInTeller = t;
        welcomeLabel.setText("Welcome, " + t.getFirstname() + "!");
        loadCustomers();
    }

    // Alternative: Set teller using ID
    public void settellerID(int tellerID) {
        String id = "T" + String.format("%03d", tellerID);

        try (BufferedReader br = new BufferedReader(new FileReader(TELLER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("tellerID")) continue;

                String[] p = line.split(",");
                if (p.length >= 4 && p[0].trim().equals(id)) {
                    loggedInTeller = new teller(
                            p[0].trim(),
                            p[1].trim(),
                            p[2].trim(),
                            p[3].trim()
                    );
                    welcomeLabel.setText("Welcome, " + p[1].trim() + "!");
                    loadCustomers();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load teller information: " + e.getMessage());
        }
    }

    // Load all customers from file
    private void loadCustomers() {
        customers.clear();
        customerListView.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("customerID")) continue;

                String[] parts = line.split(",");
                if (parts.length < 7) continue;

                customer c = new customer(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        parts[6].trim()
                );

                customers.add(c);
                customerListView.getItems().add(
                        c.getCustomerID() + " - " + c.getFirstname() + " " + c.getSurname()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load customers: " + e.getMessage());
        }
    }

    // Add new customer
    @FXML
    private void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/bankingsystem/ooadassignment/CustomerRegistration.fxml"
            ));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Customer");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Reload customers after registration window closes
            loadCustomers();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to open registration page.");
        }
    }

    // Delete selected customer
    @FXML
    private void deleteCustomer(ActionEvent event) {
        int index = customerListView.getSelectionModel().getSelectedIndex();

        if (index < 0) {
            showAlert("Warning", "Please select a customer to delete.");
            return;
        }

        customer selectedCustomer = customers.get(index);

        // Confirm deletion
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete Customer");
        confirm.setContentText("Are you sure you want to delete " +
                selectedCustomer.getFirstname() + " " + selectedCustomer.getSurname() +
                " (ID: " + selectedCustomer.getCustomerID() + ")?");

        if (confirm.showAndWait().get() != ButtonType.OK) {
            return;
        }

        try {
            String customerIDToDelete = selectedCustomer.getCustomerID().trim();
            System.out.println("\n=== DELETE CUSTOMER OPERATION ===");
            System.out.println("Customer ID to delete: [" + customerIDToDelete + "]");
            System.out.println("Customer file path: " + CUSTOMER_FILE);

            File file = new File(CUSTOMER_FILE);
            System.out.println("File exists: " + file.exists());
            System.out.println("File can read: " + file.canRead());
            System.out.println("File can write: " + file.canWrite());

            List<String> linesToKeep = new ArrayList<>();
            boolean customerFound = false;
            int lineNumber = 0;

            // Read all lines
            try (BufferedReader br = new BufferedReader(new FileReader(CUSTOMER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lineNumber++;
                    System.out.println("Line " + lineNumber + ": [" + line + "]");

                    // Always keep empty lines
                    if (line.trim().isEmpty()) {
                        System.out.println("  -> Keeping (empty line)");
                        linesToKeep.add(line);
                        continue;
                    }

                    // Always keep header line
                    if (line.trim().toLowerCase().startsWith("customerid")) {
                        System.out.println("  -> Keeping (header line)");
                        linesToKeep.add(line);
                        continue;
                    }

                    // Check if this line contains the customer to delete
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        String lineCustomerID = parts[0].trim();
                        System.out.println("  -> Line customer ID: [" + lineCustomerID + "]");

                        if (lineCustomerID.equals(customerIDToDelete)) {
                            customerFound = true;
                            System.out.println("  -> DELETING THIS LINE!");
                            continue; // Skip this line (delete it)
                        }
                    }

                    // Keep all other lines
                    System.out.println("  -> Keeping (different customer)");
                    linesToKeep.add(line);
                }
            }

            System.out.println("\nTotal lines read: " + lineNumber);
            System.out.println("Lines to keep: " + linesToKeep.size());
            System.out.println("Customer found: " + customerFound);

            if (!customerFound) {
                System.out.println("ERROR: Customer not found in file!");
                showAlert("Error", "Customer ID " + customerIDToDelete + " not found in file!");
                return;
            }

            // Write back all remaining lines
            System.out.println("\nWriting back to file...");
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CUSTOMER_FILE))) {
                for (int i = 0; i < linesToKeep.size(); i++) {
                    bw.write(linesToKeep.get(i));
                    bw.newLine();
                    System.out.println("Wrote line " + (i+1) + ": [" + linesToKeep.get(i) + "]");
                }
            }

            System.out.println("File write complete!");
            System.out.println("=== DELETE OPERATION COMPLETE ===\n");

            loadCustomers();
            showAlert("Success", "Customer " + selectedCustomer.getFirstname() + " " +
                    selectedCustomer.getSurname() + " deleted successfully!");

        } catch (IOException e) {
            System.err.println("IOException during delete: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to delete customer: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during delete: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Unexpected error: " + e.getMessage());
        }
    }

    // Logout and return to main menu
    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/bankingsystem/ooadassignment/MainMenu.fxml"
            ));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to main menu.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}