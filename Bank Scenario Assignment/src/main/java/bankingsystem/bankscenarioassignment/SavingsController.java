package bankingsystem.bankscenarioassignment;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SavingsController {

    @FXML
    private Label accountNumberLabel;
    @FXML
    private Label accountHolderLabel;
    @FXML
    private Label balanceLabel;

    @FXML
    private TextField depositField;
    @FXML
    private TextField withdrawField; // will just show message if used

    private Savings currentAccount;
    private final String accountsFilePath = "C:\\Users\\parke\\IdeaProjects\\Bank Scenario Assignment\\src\\main\\resources\\bankingsystem\\bankscenarioassignment\\accounts.txt";

    // Set account from dashboard
    public void setAccount(Savings account) {
        this.currentAccount = account;
        loadAccountDetails();
    }

    private void loadAccountDetails() {
        if (currentAccount != null) {
            accountNumberLabel.setText(currentAccount.getAccountNumber());
            accountHolderLabel.setText(currentAccount.getBranch()); // optional: can show customer name if stored
            balanceLabel.setText(String.format("%.2f", currentAccount.getBalance()));
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        if (currentAccount == null) return;

        try {
            double amount = Double.parseDouble(depositField.getText());
            currentAccount.deposit(amount);
            balanceLabel.setText(String.format("%.2f", currentAccount.getBalance()));
            saveAccountChanges();
        } catch (NumberFormatException e) {
            System.out.println("Invalid deposit amount.");
        }
        depositField.clear();
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        if (currentAccount == null) return;

        System.out.println("Withdrawals are not allowed for Savings accounts.");
        withdrawField.clear();
    }

    @FXML
    private void applyInterest(ActionEvent event) {
        if (currentAccount == null) return;

        currentAccount.applyInterest();
        balanceLabel.setText(String.format("%.2f", currentAccount.getBalance()));
        saveAccountChanges();
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
                        // accountNumber,type,balance,branch,interestRate
                        line = currentAccount.getAccountNumber() + ",Savings," +
                                currentAccount.getBalance() + "," + currentAccount.getBranch() + "," +
                                currentAccount.getInterestRate();
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
    private void backToDashboard(ActionEvent event) {
        System.out.println("Back button pressed. Implement scene switch to customer dashboard.");
    }
}
