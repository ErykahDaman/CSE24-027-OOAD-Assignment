package bankingsystem.ooadassignment;


public class Cheque extends account implements withdrawable {

    public Cheque(String accountNumber, double balance, String branch) {
        super(accountNumber, balance, branch);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount + ", Remaining Balance: " + balance);
            return true;
        } else {
            System.out.println("Insufficient funds or invalid amount.");
            return false;
        }
    }

}
