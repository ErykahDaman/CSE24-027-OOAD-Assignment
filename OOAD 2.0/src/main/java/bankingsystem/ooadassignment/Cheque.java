package bankingsystem.ooadassignment;


import java.util.Date;

public class Cheque extends account implements withdrawable {

    // attributes (from UML)
    private String employerName;
    private String employerAddress;

    // constructor
    public Cheque(String accountNumber, double balance, String branch, Date openDate,
                  String employerName, String employerAddress) {
        super(accountNumber, balance, branch, openDate);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    // withdraw method
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

    // getters
    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }
}
