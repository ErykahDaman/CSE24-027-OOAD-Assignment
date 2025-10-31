package bankingsystem.ooadassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChequeController {

    @FXML
    private Label accountNumberLabel, branchLabel, balanceLabel, overdraftLabel;

    @FXML
    private TextField depositAmountField, withdrawAmountField;

    private Cheque chequeAccount;

    // Initialize with an existing Cheque account object
    public void setChequeAccount(Cheque cheque) {
        this.chequeAccount = cheque;
        updateChequeDetails();
    }

    // Update labels with account data
    private void updateChequeDetails() {
        if (chequeAccount != null) {
            accountNumberLabel.setText(chequeAccount.getAccountNumber());
            branchLabel.setText(chequeAccount.getBranch());
            balanceLabel.setText(String.format("%.2f", chequeAccount.getBalance()));

        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(depositAmountField.getText());
            if (amount <= 0) {
                showAlert("Invalid Input", "Deposit amount must be greater than zero.");
                return;
            }

            chequeAccount.deposit(amount);
            updateChequeDetails();
            showAlert("Success", "Deposited P" + amount + " successfully.");
            depositAmountField.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid deposit amount.");
        }
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(withdrawAmountField.getText());
            if (amount <= 0) {
                showAlert("Invalid Input", "Withdrawal amount must be greater than zero.");
                return;
            }

            boolean success = chequeAccount.withdraw(amount);
            if (success) {
                updateChequeDetails();
                showAlert("Success", "Withdrew P" + amount + " successfully.");
            } else {
                showAlert("Error", "Insufficient funds (including overdraft limit).");
            }
            withdrawAmountField.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid withdrawal amount.");
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Utility Alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
