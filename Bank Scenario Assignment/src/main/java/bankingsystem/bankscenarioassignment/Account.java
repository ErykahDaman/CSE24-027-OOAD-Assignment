package bankingsystem.bankscenarioassignment;
import java.util.Date;

public abstract class Account {
    private String accountNumber;
    protected double balance;
    private String branch;
    private Date openDate;

    public Account(String accountNumber, double balance, String branch, Date openDate) {
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

    public void setBalance(double v) {
    }

    public String getAccType() {
     return "AccType";
    }
}

