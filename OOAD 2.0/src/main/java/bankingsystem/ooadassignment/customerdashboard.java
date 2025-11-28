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
import java.util.Optional;

public class customerdashboard {

    @FXML private Label welcomeLabel;
    @FXML private Label customerIDLabel;
    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private ListView<String> accountsListView;
    @FXML private ChoiceBox<String> accountTypeChoice;
    @FXML private Button createAccountBtn;
    @FXML private Button deleteAccountBtn;
    @FXML private Button depositBtn;
    @FXML private Button withdrawBtn;
    @FXML private Button logoutBtn;

    private customer loggedInCustomer;
    private List<account> customerAccounts = new ArrayList<>();

    private static final String ACCOUNTS_FILE = "accounts.txt";

    @FXML
    private void initialize() {
        accountTypeChoice.getItems().addAll("Savings", "Cheque", "Investment");

        createAccountBtn.setOnAction(this::createAccount);
        deleteAccountBtn.setOnAction(this::deleteAccount);
        depositBtn.setOnAction(e -> handleDepositWithdraw(true));
        withdrawBtn.setOnAction(e -> handleDepositWithdraw(false));
        logoutBtn.setOnAction(this::logout);
    }

    // =====================================================================
    // ACCOUNT FACTORY METHODS
    // =====================================================================

    private Savings createSavingsAccount(String accNumber, double balance, String branch) {
        return new Savings(accNumber, balance, branch, 3.0);
    }

    private Cheque createChequeAccount(String accNumber, double balance, String branch) {
        return new Cheque(accNumber, balance, branch);
    }

    private Investment createInvestmentAccount(String accNumber, double balance, String branch) {
        return new Investment(accNumber, balance, branch);
    }

    private account createAccountInstance(String type, String accNumber, double balance, String branch)
            throws IllegalArgumentException {
        return switch (type) {
            case "Savings" -> createSavingsAccount(accNumber, balance, branch);
            case "Cheque" -> createChequeAccount(accNumber, balance, branch);
            case "Investment" -> createInvestmentAccount(accNumber, balance, branch);
            default -> throw new IllegalArgumentException("Unknown account type: " + type);
        };
    }

    // =====================================================================
    // SET CUSTOMER AND LOAD THEIR ACCOUNTS
    // =====================================================================
    public void setCustomer(customer c) {
        this.loggedInCustomer = c;

        welcomeLabel.setText("Welcome, " + c.getFirstname() + "!");
        customerIDLabel.setText(c.getCustomerID());
        nameLabel.setText(c.getFirstname() + " " + c.getSurname());
        emailLabel.setText(c.getEmail());

        loadCustomerAccounts();
    }

    // =====================================================================
    // LOAD CUSTOMER ACCOUNTS FROM FILE
    // =====================================================================
    public void loadCustomerAccounts() {
        if (loggedInCustomer == null) {
            System.out.println("ERROR: No customer logged in");
            return;
        }

        accountsListView.getItems().clear();
        customerAccounts.clear();

        System.out.println("\n=== LOADING CUSTOMER ACCOUNTS ===");
        System.out.println("Customer ID: " + loggedInCustomer.getCustomerID());
        System.out.println("Accounts file: " + ACCOUNTS_FILE);

        File accountsFile = new File(ACCOUNTS_FILE);
        if (!accountsFile.exists()) {
            System.out.println("Accounts file not found. No accounts to load.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            int lineNum = 0;
            int loadedCount = 0;

            while ((line = reader.readLine()) != null) {
                lineNum++;

                // Print each line for debugging
                System.out.println("Line " + lineNum + ": [" + line + "]");

                if (line.trim().isEmpty()) {
                    System.out.println("  -> Empty line, skipping");
                    continue;
                }

                // Skip header line
                if (line.trim().toLowerCase().startsWith("accountnumber")) {
                    System.out.println("  -> Header line, skipping");
                    continue;
                }

                String[] parts = line.split(",");
                System.out.println("  -> Split into " + parts.length + " parts");

                if (parts.length >= 4) {
                    String accNumber = parts[0].trim();
                    String custID = parts[1].trim();
                    String type = parts[2].trim();
                    double balance = Double.parseDouble(parts[3].trim()); // ✅ FIX: parts[3] not parts[2]!
                    String branch = parts.length > 4 ? parts[4].trim() : "Main";

                    System.out.println("  -> Account: " + accNumber);
                    System.out.println("  -> Customer: " + custID);
                    System.out.println("  -> Type: " + type);
                    System.out.println("  -> Balance: " + balance);

                    // Only load accounts for the logged-in customer
                    if (custID.equals(loggedInCustomer.getCustomerID())) {
                        System.out.println("  -> ✓ MATCH! Loading this account");
                        try {
                            account acc = createAccountInstance(type, accNumber, balance, branch);
                            customerAccounts.add(acc);

                            String displayText = String.format("%s | %s | Balance: P%.2f",
                                    accNumber, type, balance);
                            accountsListView.getItems().add(displayText);
                            loadedCount++;
                        } catch (IllegalArgumentException e) {
                            System.out.println("  -> ✗ ERROR: Unknown account type: " + type);
                        }
                    } else {
                        System.out.println("  -> Different customer (" + custID + " vs " +
                                loggedInCustomer.getCustomerID() + "), skipping");
                    }
                } else {
                    System.out.println("  -> ✗ Invalid line format (expected at least 4 parts)");
                }
            }
            System.out.println("\n=== LOADING COMPLETE ===");
            System.out.println("Total accounts loaded: " + loadedCount);

        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Could not load customer accounts: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing account data: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Invalid data format in accounts file");
        }
    }

    // =====================================================================
    // CREATE NEW ACCOUNT
    // =====================================================================
    private void createAccount(ActionEvent event) {
        String type = accountTypeChoice.getValue();

        if (type == null || type.isEmpty()) {
            showAlert("Error", "Please select an account type.");
            return;
        }

        System.out.println("\n=== CREATE ACCOUNT ===");
        System.out.println("Customer: " + loggedInCustomer.getCustomerID());
        System.out.println("Type: " + type);

        try {
            String newAccNumber = generateAccountNumber();
            System.out.println("Generated account number: " + newAccNumber);

            File accountsFile = new File(ACCOUNTS_FILE);

            // Create file with header if it doesn't exist
            if (!accountsFile.exists()) {
                System.out.println("Creating new accounts file with header");
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACCOUNTS_FILE))) {
                    bw.write("AccountNumber,CustomerID,AccountType,Balance,Branch,OpenDate");
                    bw.newLine();
                }
            }

            // Append new account to file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true))) {
                String accountData = newAccNumber + "," +
                        loggedInCustomer.getCustomerID() + "," +
                        type + ",0.0,Main," + System.currentTimeMillis();
                bw.write(accountData);
                bw.newLine();
                System.out.println("Written to file: " + accountData);
            }

            showAlert("Success", type + " account created successfully!\nAccount Number: " + newAccNumber);
            loadCustomerAccounts();

        } catch (IOException e) {
            System.err.println("Error creating account: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to create account: " + e.getMessage());
        }
    }

    // =====================================================================
    // GENERATE UNIQUE ACCOUNT NUMBER
    // =====================================================================
    private String generateAccountNumber() throws IOException {
        int maxNumber = 0;
        File file = new File(ACCOUNTS_FILE);

        if (!file.exists()) {
            return "ACC001";
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().toLowerCase().startsWith("accountnumber")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length > 0) {
                    String accNum = parts[0].trim();
                    if (accNum.startsWith("ACC")) {
                        try {
                            int num = Integer.parseInt(accNum.substring(3));
                            maxNumber = Math.max(maxNumber, num);
                        } catch (NumberFormatException e) {
                            // Skip if can't parse
                        }
                    }
                }
            }
        }

        return "ACC" + String.format("%03d", maxNumber + 1);
    }

    // =====================================================================
    // DELETE ACCOUNT
    // =====================================================================
    private void deleteAccount(ActionEvent event) {
        int index = accountsListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            showAlert("Error", "Select an account to delete.");
            return;
        }

        account acc = customerAccounts.get(index);

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText("Delete Account");
        confirmAlert.setContentText("Are you sure you want to delete account " + acc.getAccountNumber() + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        List<String> allLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(acc.getAccountNumber() + ",")) {
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to read accounts file");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (String l : allLines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update accounts file");
            return;
        }

        showAlert("Success", "Account deleted successfully!");
        loadCustomerAccounts();
    }

    // =====================================================================
    // DEPOSIT AND WITHDRAW
    // =====================================================================
    private void handleDepositWithdraw(boolean isDeposit) {
        int index = accountsListView.getSelectionModel().getSelectedIndex();

        if (index < 0) {
            showAlert("Error", "Select an account first.");
            return;
        }

        account selectedAcc = customerAccounts.get(index);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(isDeposit ? "Deposit" : "Withdraw");
        dialog.setHeaderText((isDeposit ? "Deposit into " : "Withdraw from ") + selectedAcc.getAccountNumber());
        dialog.setContentText("Enter amount:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            try {
                double amount = Double.parseDouble(result.get());

                if (amount <= 0) {
                    showAlert("Error", "Amount must be positive.");
                    return;
                }

                if (!isDeposit && amount > selectedAcc.getBalance()) {
                    showAlert("Error", "Insufficient funds. Current balance: P" +
                            String.format("%.2f", selectedAcc.getBalance()));
                    return;
                }

                double newBalance = isDeposit ?
                        selectedAcc.getBalance() + amount :
                        selectedAcc.getBalance() - amount;

                selectedAcc.setBalance(newBalance);
                updateAccountFile(selectedAcc);

                showAlert("Success",
                        String.format("%s successful! New balance: P%.2f",
                                isDeposit ? "Deposit" : "Withdrawal", newBalance));

                loadCustomerAccounts();

            } catch (NumberFormatException e) {
                showAlert("Error", "Invalid number format.");
            }
        }
    }

    // =====================================================================
    // UPDATE ACCOUNT IN FILE
    // =====================================================================
    private void updateAccountFile(account updated) {
        File input = new File(ACCOUNTS_FILE);
        File temp = new File("accounts_temp.txt");

        try {
            BufferedReader br = new BufferedReader(new FileReader(input));
            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));

            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Write header as-is
                if (isFirstLine || line.trim().toLowerCase().startsWith("accountnumber")) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length >= 4 && parts[0].trim().equals(updated.getAccountNumber())) {
                    // Write updated account data
                    String openDate = parts.length > 5 ? parts[5].trim() : String.valueOf(System.currentTimeMillis());
                    String branch = parts.length > 4 ? parts[4].trim() : "Main";

                    bw.write(updated.getAccountNumber() + "," +
                            loggedInCustomer.getCustomerID() + "," +
                            updated.getAccountType() + "," +
                            updated.getBalance() + "," +
                            branch + "," +
                            openDate);
                    bw.newLine();
                } else {
                    // Write line as-is
                    bw.write(line);
                    bw.newLine();
                }
            }

            br.close();
            bw.close();

            // Replace original with temp
            if (input.delete()) {
                if (!temp.renameTo(input)) {
                    throw new IOException("Could not rename temp file");
                }
            } else {
                throw new IOException("Could not delete original file");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update account: " + e.getMessage());
        }
    }

    // =====================================================================
    // LOGOUT
    // =====================================================================
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/bankingsystem/ooadassignment/MainMenu.fxml")
            );

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to main menu");
        }
    }

    // =====================================================================
    // SHOW ALERT HELPER
    // =====================================================================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}