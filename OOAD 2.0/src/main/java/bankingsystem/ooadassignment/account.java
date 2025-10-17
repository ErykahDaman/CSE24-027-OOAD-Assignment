package bankingsystem.ooadassignment;

import java.util.Date;

public abstract class account {
    // -------------------------
    // Attributes (from UML)
    // -------------------------
    private String accountNumber;
    protected double balance;
    private String branch;
    private Date openDate;

    // -------------------------
    // Constructor
    // -------------------------
    public account(String accountNumber, double balance, String branch, Date openDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.openDate = openDate;
    }

    // -------------------------
    // Getters and Setters
    // -------------------------
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getBranch() {
        return branch;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    // -------------------------
    // Core Methods (from UML)
    // -------------------------

    // Deposit funds
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
            System.out.println("New balance: " + balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    // Check balance
    public void checkBalance() {
        System.out.println("Your current balance is: " + balance);
    }

    // Abstract withdraw method (forces subclasses to define their own rules)
    public abstract boolean withdraw(double amount);
}


