package bankingsystem.ooadassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.*;
import java.util.List;

public class AccountDashboardController {

    @FXML private Label accountNumberLabel;
    @FXML private Label balanceLabel;
    @FXML private TextField amountField;

    private account account;
    private customer customer;

    public void setAccount(account a, customer c) {
        this.account = a;
        this.customer = c;
        accountNumberLabel.setText(a.getAccountNumber());
        balanceLabel.setText("P" + a.getBalance());
    }

    @FXML
    private void deposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            account.deposit(amount);
            updateAccountsFile();
            balanceLabel.setText("P" + account.getBalance());
            showAlert("Success", "Deposit successful!");
        } catch (NumberFormatException e) {
            showAlert("Error", "Enter a valid amount.");
        }
    }

    @FXML
    private void withdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (account.withdraw(amount)) {
                updateAccountsFile();
                balanceLabel.setText("P" + account.getBalance());
                showAlert("Success", "Withdrawal successful!");
            } else {
                showAlert("Error", "Insufficient funds or not allowed.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Enter a valid amount.");
        }
    }

    private void updateAccountsFile() {
        // Read all accounts
        try {
            List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get("accounts.txt"));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("accounts.txt"))) {
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(account.getAccountNumber())) {
                        // update balance
                        bw.write(parts[0] + "," + parts[1] + "," + parts[2] + "," + account.getBalance() + "," + parts[4]);
                    } else {
                        bw.write(line);
                    }
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title); alert.setHeaderText(null); alert.setContentText(msg);
        alert.showAndWait();
    }
}
