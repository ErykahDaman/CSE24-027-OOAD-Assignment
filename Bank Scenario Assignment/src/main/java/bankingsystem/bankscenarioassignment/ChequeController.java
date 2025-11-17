package bankingsystem.bankscenarioassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChequeController {

    @FXML
    private Label accountNumberLabel;
    @FXML
    private Label branchLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private Label overdraftLabel;

    @FXML
    private TextField depositAmountField;
    @FXML
    private TextField withdrawAmountField;

    private Cheque currentAccount;
    private final String accountsFilePath = "C:\\Users\\parke\\IdeaProjects\\Bank Scenario Assignment\\src\\main\\resources\\bankingsystem\\bankscenarioassignment\\accounts.txt";

    // Call this method from dashboard to set the account being viewed
    public void setAccount(Cheque account) {
        this.currentAccount = account;
        loadAccountDetails();
    }

    private void loadAccountDetails() {
        if (currentAccount != null) {
            accountNumberLabel.setText(currentAccount.getAccountNumber());
            branchLabel.setText(currentAccount.getBranch());
            balanceLabel.setText(String.format("%.2f", currentAccount.getBalance()));
            overdraftLabel.setText("0.00"); // Assuming no overdraft for now
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        if (currentAccount == null) return;

        try {
            double amount = Double.parseDouble(depositAmountField.getText());
            currentAccount.deposit(amount);
            balanceLabel.setText(String.format("%.2f", currentAccount.getBalance()));
            saveAccountChanges();
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount.");
        }
        depositAmountField.clear();
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        if (currentAccount == null) return;

        try {
            double amount = Double.parseDouble(withdrawAmountField.getText());
            if (currentAccount.withdraw(amount)) {
                balanceLabel.setText(String.format("%.2f", currentAccount.getBalance()));
                saveAccountChanges();
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid withdraw amount.");
        }
        withdrawAmountField.clear();
    }

    // Save updated account to accounts.txt
    private void saveAccountChanges() {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(accountsFilePath);
            if (!file.exists()) file.createNewFile();

            // Read all accounts, update this one
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(currentAccount.getAccountNumber())) {
                        // accountNumber, type, balance, branch, extra
                        line = currentAccount.getAccountNumber() + ",Cheque," +
                                currentAccount.getBalance() + "," + currentAccount.getBranch() + ",0";
                    }
                    lines.add(line);
                }
            }

            // Write all lines back
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (String l : lines) {
                    bw.write(l);
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToMainMenu(ActionEvent event) {
        // Implement scene switch back to customer dashboard
        System.out.println("Back button pressed. Implement scene switch.");
    }
}
