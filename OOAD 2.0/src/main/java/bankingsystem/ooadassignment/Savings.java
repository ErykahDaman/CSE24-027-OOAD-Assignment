package bankingsystem.ooadassignment;


public class Savings extends account implements interest {
    private double interestRate;

    public Savings(String accountNumber, double balance, String branch, double interestRate) {
        super(accountNumber, balance, branch);
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
