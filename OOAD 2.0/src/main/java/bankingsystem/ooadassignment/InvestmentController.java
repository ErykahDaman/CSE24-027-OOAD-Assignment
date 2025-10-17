package bankingsystem.ooadassignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InvestmentController {

    @FXML
    private Label accountNumberLabel, branchLabel, balanceLabel, interestLabel;

    @FXML
    private TextField investAmountField, withdrawAmountField;

    private Investment investmentAccount;

    // Inject an existing Investment object (from login or main menu)
    public void setInvestmentAccount(Investment investment) {
        this.investmentAccount = investment;
        updateInvestmentDetails();
    }

    // Update UI with account details
    private void updateInvestmentDetails() {
        if (investmentAccount != null) {
            accountNumberLabel.setText(investmentAccount.getAccountNumber());
            branchLabel.setText(investmentAccount.getBranch());
            balanceLabel.setText(String.format("%.2f", investmentAccount.getBalance()));
            interestLabel.setText(String.format("%.2f", investmentAccount.getInterestRate()));
        }
    }

    @FXML
    private void handleInvest(ActionEvent event) {
        try {
            double amount = Double.parseDouble(investAmountField.getText());
            if (amount <= 0) {
                showAlert("Invalid Input", "Investment amount must be greater than zero.");
                return;
            }



        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount to invest.");
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

            boolean success = investmentAccount.withdraw(amount);
            if (success) {
                updateInvestmentDetails();
                showAlert("Success", "Withdrew P" + amount + " successfully.");
            } else {
                showAlert("Error", "Insufficient funds or invalid withdrawal amount.");
            }
            withdrawAmountField.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount to withdraw.");
        }
    }

    @FXML
    private void applyInterest(ActionEvent event) {
        investmentAccount.applyInterest();
        updateInvestmentDetails();
        showAlert("Interest Applied", "Interest has been added to your balance.");
    }

    @FXML
    private void backToMainMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MainMenu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Utility method to display alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
