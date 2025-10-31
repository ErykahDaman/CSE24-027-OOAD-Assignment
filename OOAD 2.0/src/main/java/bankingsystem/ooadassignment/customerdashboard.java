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
import java.util.Date;
import java.util.List;

public class customerdashboard {

    @FXML private Label welcomeLabel;
    @FXML private Label customerIDLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private ListView<String> accountsListView;

    private customer loggedInCustomer;
    private List<account> customerAccounts = new ArrayList<>();

    public void setCustomer(customer c) {
        this.loggedInCustomer = c;
        customerIDLabel.setText(c.getCustomerID());
        nameLabel.setText(c.getFirstname() + " " + c.getSurname());
        emailLabel.setText(c.getEmail());
        welcomeLabel.setText("Welcome, " + c.getFirstname() + "!");

        loadAccounts();
    }

    private void loadAccounts() {
        customerAccounts.clear();
        accountsListView.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // Format: accountNumber,customerID,type,balance,branch
                if (parts[1].equals(loggedInCustomer.getCustomerID())) {
                    account acc;
                    if (parts[2].equalsIgnoreCase("Savings")) {
                        acc = new Savings(parts[0], Double.parseDouble(parts[3]), parts[4], new Date(), 3.0);
                    } else {
                        acc = new Cheque(parts[0], Double.parseDouble(parts[3]), parts[4], new Date());
                    }
                    customerAccounts.add(acc);
                    accountsListView.getItems().add(acc.getAccountNumber() + " - " + parts[2] + " - Balance: P" + parts[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open selected account for deposit/withdraw
        accountsListView.setOnMouseClicked(event -> {
            int index = accountsListView.getSelectionModel().getSelectedIndex();
            if (index >= 0) {
                openAccountDashboard(customerAccounts.get(index));
            }
        });
    }

    @FXML
    private void createAccount(ActionEvent event) {
        try {
            String newAccountNumber = "A" + System.currentTimeMillis();
            String type = "Savings"; // default
            double balance = 0;
            String branch = "Main";

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt", true))) {
                bw.write(newAccountNumber + "," + loggedInCustomer.getCustomerID() + "," + type + "," + balance + "," + branch);
                bw.newLine();
            }

            loadAccounts();
            showAlert("Account Created", "New " + type + " account created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not create account.");
        }
    }

    private void openAccountDashboard(account acc) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bankingsystem/ooadassignment/AccountDashboard.fxml"));
            Parent root = loader.load();
            AccountDashboardController controller = loader.getController();
            controller.setAccount(acc, loggedInCustomer);

            Stage stage = (Stage) accountsListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}
