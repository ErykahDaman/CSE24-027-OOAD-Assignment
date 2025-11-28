package bankingsystem.ooadassignment;

public class Investment extends account implements interest, withdrawable {
    private double interestRate;

    public Investment(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
        this.interestRate = interestRate;

        if (balance < 500) {
            System.out.println("Minimum balance for Investment Account is 500.");
            this.balance = 500;
        }
    }

    // withdraw allowed
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
            System.out.println("Remaining balance: " + balance);
            return true;
        } else {
            System.out.println("Invalid or insufficient funds.");
            return false;
        }
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
    public String getType() { return "Investment"; }
}
