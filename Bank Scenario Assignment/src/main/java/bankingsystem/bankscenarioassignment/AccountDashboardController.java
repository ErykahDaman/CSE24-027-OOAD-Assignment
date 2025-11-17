package bankingsystem.bankscenarioassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountDashboardController {

    @FXML private ListView<String> accountsListView;
    @FXML private ComboBox<String> accountTypeComboBox;
    @FXML private TextField depositField;
    @FXML private TextField withdrawField;
    @FXML private Label selectedAccountLabel;

    private customer loggedInCustomer;
    private List<Account> customerAccounts = new ArrayList<>();

    public void setCustomer(customer c) {
        this.loggedInCustomer = c;
        accountTypeComboBox.getItems().addAll("Savings", "Cheque");
        loadAccounts();

        // Update label when user selects an account
        accountsListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            int index = newVal.intValue();
            if (index >= 0 && index < customerAccounts.size()) {
                Account acc = customerAccounts.get(index);
                selectedAccountLabel.setText("Selected: " + acc.getAccountNumber() + " - " + acc.getAccType() + " - Balance: P" + acc.getBalance());
            } else {
                selectedAccountLabel.setText("Select an account to see details.");
            }
        });
    }

    private void loadAccounts() {
        customerAccounts.clear();
        accountsListView.getItems().clear();

        try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[1].equals(loggedInCustomer.getCustomerID())) {
                    Account acc;
                    if (parts[2].equalsIgnoreCase("Savings")) {
                        acc = new Savings(parts[0], Double.parseDouble(parts[3]), parts[4], new Date(), 3.0);
                    } else {
                        acc = new Cheque(parts[0], Double.parseDouble(parts[3]), parts[4], new Date());
                    }
                    customerAccounts.add(acc);
                    accountsListView.getItems().add(acc.getAccountNumber() + " - " + acc.getAccType() + " - Balance: P" + acc.getBalance());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createAccount(ActionEvent event) {
        String type = accountTypeComboBox.getValue();
        if (type == null) { showAlert("Select Type", "Please select an account type!"); return; }

        String newAccountNumber = "A" + System.currentTimeMillis();
        double balance = 0;
        String branch = "Main";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt", true))) {
            bw.write(newAccountNumber + "," + loggedInCustomer.getCustomerID() + "," + type + "," + balance + "," + branch);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not create account.");
            return;
        }

        showAlert("Success", type + " account created!");
        loadAccounts();
        selectedAccountLabel.setText("Select an account to see details.");
    }

    @FXML
    private void deposit(ActionEvent event) {
        int index = accountsListView.getSelectionModel().getSelectedIndex();
        if (index < 0) { showAlert("Select Account", "Please select an account first!"); return; }

        Account acc = customerAccounts.get(index);
        double amount;
        try {
            amount = Double.parseDouble(depositField.getText());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) { showAlert("Invalid Amount", "Enter a valid deposit amount!"); return; }

        acc.deposit(amount);
        updateAccountInFile(acc);
        loadAccounts();
        depositField.clear();
        selectedAccountLabel.setText("Select an account to see details.");
        showAlert("Success", "Deposited P" + amount + " to " + acc.getAccountNumber());
    }

    @FXML
    private void withdraw(ActionEvent event) {
        int index = accountsListView.getSelectionModel().getSelectedIndex();
        if (index < 0) { showAlert("Select Account", "Please select an account first!"); return; }

        Account acc = customerAccounts.get(index);
        double amount;
        try {
            amount = Double.parseDouble(withdrawField.getText());
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) { showAlert("Invalid Amount", "Enter a valid withdraw amount!"); return; }

        if (!acc.withdraw(amount)) { showAlert("Error", "Insufficient funds!"); return; }

        updateAccountInFile(acc);
        loadAccounts();
        withdrawField.clear();
        selectedAccountLabel.setText("Select an account to see details.");
        showAlert("Success", "Withdrew P" + amount + " from " + acc.getAccountNumber());
    }

    @FXML
    private void deleteAccount(ActionEvent event) {
        int index = accountsListView.getSelectionModel().getSelectedIndex();
        if (index < 0) { showAlert("Select Account", "Please select an account to delete!"); return; }

        Account acc = customerAccounts.get(index);
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith(acc.getAccountNumber() + ",")) {
                        lines.add(line);
                    }
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt"))) {
                for (String l : lines) { bw.write(l); bw.newLine(); }
            }

            showAlert("Success", "Account deleted!");
            loadAccounts();
            selectedAccountLabel.setText("Select an account to see details.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not delete account.");
        }
    }

    @FXML
    private void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerDashboard.fxml"));
            Parent root = loader.load();
            CustomerDashboardController controller = loader.getController();
            controller.setCustomer(loggedInCustomer);
            Stage stage = (Stage)((Scene)accountsListView.getScene()).getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void updateAccountInFile(Account acc) {
        try {
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader("accounts.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith(acc.getAccountNumber() + ",")) {
                        String[] parts = line.split(",");
                        parts[3] = String.valueOf(acc.getBalance());
                        line = String.join(",", parts);
                    }
                    lines.add(line);
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt"))) {
                for (String l : lines) { bw.write(l); bw.newLine(); }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
