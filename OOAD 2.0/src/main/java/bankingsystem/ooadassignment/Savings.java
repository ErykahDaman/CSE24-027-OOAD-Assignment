package bankingsystem.ooadassignment;


import java.util.Date;

public class Savings extends account implements interest {
    private double interestRate;

    public Savings(String accountNumber, double balance, String branch, Date openDate, double interestRate) {
        super(accountNumber, balance, branch, openDate);
        this.interestRate = interestRate;
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Withdrawals not allowed from Savings account.");
        return false;
    }

    @Override
    public double calculateInterest() {
        return balance * (interestRate / 100);
    }

    @Override
    public void applyInterest() {
        double interest = calculateInterest();
        balance += interest;
        System.out.println("Interest applied: " + interest + ", New Balance: " + balance);
    }

    public double getInterestRate() { return interestRate; }
}
