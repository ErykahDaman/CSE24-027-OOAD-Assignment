package bankingsystem.bankscenarioassignment;

import java.util.Date;

public class Cheque extends Account implements withdrawable {

    public Cheque(String accountNumber, double balance, String branch, Date openDate) {
        super(accountNumber, balance, branch, openDate);
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


