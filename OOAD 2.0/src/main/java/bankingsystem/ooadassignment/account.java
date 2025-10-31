package bankingsystem.ooadassignment;


import java.util.Date;

public abstract class account {
    private String accountNumber;
    protected double balance;
    private String branch;
    private Date openDate;

    public account(String accountNumber, double balance, String branch, Date openDate) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.openDate = openDate;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Date getOpenDate() { return openDate; }

    public void setBranch(String branch) { this.branch = branch; }
    public void setOpenDate(Date openDate) { this.openDate = openDate; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount + ", New Balance: " + balance);
        } else {
            System.out.println("Deposit must be positive.");
        }
    }

    public abstract boolean withdraw(double amount);
}
