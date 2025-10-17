package bankingsystem.ooadassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SavingsPageController {

    @FXML
    private Label accountNumberLabel, accountHolderLabel, balanceLabel;

    @FXML
    private TextField depositField, withdrawField;
@FXML
    private Savings savingsAccount;

    // Called when navigating from dashboard
    public void setSavingsAccount(Savings account) {
        this.savingsAccount = account;
        updateAccountDetails();
    }

    private void updateAccountDetails() {
        if (savingsAccount != null) {
            accountNumberLabel.setText(savingsAccount.getAccountNumber());

            balanceLabel.setText(String.format("%.2f", savingsAccount.getBalance()));
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        try {
            double amount = Double.parseDouble(depositField.getText());
            if (amount <= 0) {
                showAlert("Invalid Input", "Deposit amount must be greater than zero.");
                return;
            }
            savingsAccount.deposit(amount);
            updateAccountDetails();
            showAlert("Success", "Deposit of P" + amount + " successful!");
            depositField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for deposit.");
        }
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        try {
            double amount = Double.parseDouble(withdrawField.getText());
            if (amount <= 0) {
                showAlert("Invalid Input", "Withdrawal amount must be greater than zero.");
                return;
            }
            if (amount > savingsAccount.getBalance()) {
                showAlert("Insufficient Funds", "You don't have enough balance to withdraw P" + amount);
                return;
            }
            savingsAccount.withdraw(amount);
            updateAccountDetails();
            showAlert("Success", "Withdrawal of P" + amount + " successful!");
            withdrawField.clear();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for withdrawal.");
        }
    }

    @FXML
    private void backToDashboard(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountDashboard.fxml"));
        Parent root = loader.load();

        AccountDashboardController dashboardController = loader.getController();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
