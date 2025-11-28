package bankingsystem.ooadassignment;


import java.util.Date;

public abstract class account {
    private String accountNumber;
    protected double balance;
    private String branch;


    public account(String accountNumber, double balance, String branch) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;

    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }


    public void setBranch(String branch) { this.branch = branch; }


    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount + ", New Balance: " + balance);
        } else {
            System.out.println("Deposit must be positive.");
        }
    }

    public abstract boolean withdraw(double amount);


    public String getAccountType() {
        return "AccountType";
    }

    public void setBalance(double newBalance) {
    }


}
