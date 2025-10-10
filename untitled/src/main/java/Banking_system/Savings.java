package Banking_system;

import java.util.Date;

public class Savings extends account implements interest {
    private double interestRate;

    public Savings(String accountNumber, double balance, String branch, Date openDate, double interestRate) {
        super(accountNumber, balance, branch, openDate);
        this.interestRate = interestRate;
    }

    // no withdrawals allowed for savings
    @Override
    public boolean withdraw(double amount) {
        System.out.println("Withdrawals are not allowed from a Savings Account.");
        return false;
    }

    // calculate interest
    @Override
    public double calculateInterest() {
        return balance * (interestRate / 100);
    }

    // apply interest
    @Override
    public void applyInterest() {
        double interest = calculateInterest();
        balance += interest;
        System.out.println("Interest added: " + interest);
        System.out.println("New balance: " + balance);
    }

    public double getInterestRate() {
        return interestRate;
    }
}
