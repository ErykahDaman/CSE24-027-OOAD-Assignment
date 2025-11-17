package bankingsystem.bankscenarioassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.*;

public class CustomerDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label customerIDLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private ListView<String> accountsListView;

    private customer loggedInCustomer;

    // Set logged-in customer and display info + read-only accounts
    public void setCustomer(customer c) {
        this.loggedInCustomer = c;
        customerIDLabel.setText(c.getCustomerID());
        nameLabel.setText(c.getFirstname() + " " + c.getSurname());
        emailLabel.setText(c.getEmail());
        welcomeLabel.setText("Welcome, " + c.getFirstname() + "!");
        loadAccounts();
    }

    // Load accounts in read-only mode
    private void loadAccounts() {
        accountsListView.getItems().clear();
        try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(loggedInCustomer.getCustomerID())) {
                    accountsListView.getItems().add(parts[0] + " - " + parts[2] + " - Balance: P" + parts[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Open interactive Account Dashboard
    @FXML
    private void openAccountsDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountDashboard.fxml"));
            Parent root = loader.load();

            AccountDashboardController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Logout to main menu
    @FXML
    private void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
