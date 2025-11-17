package bankingsystem.bankscenarioassignment;


import java.util.ArrayList;
import java.util.List;

public class customer {
    // -------------------------
    // Attributes (from UML)
    // -------------------------
    private String customerID;
    private String firstname;
    private String surname;
    private String password;
    private String address;
    private String phone;
    private String email;
    private List<Account> accounts; // one-to-many relationship

    // -------------------------
    // Constructor
    // -------------------------
    public customer(String c010, String customerID, String firstname, String surname,
                    String address, String phone, String email) {
        this.customerID = customerID;
        this.firstname = firstname;
        this.surname = surname;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    public customer(String name, int age, String customerID, String contact) {
    }

    // -------------------------
    // Getters and Setters
    // -------------------------
    public List<Account> getAccounts() {
        return accounts;
    }
    public String getCustomerID() {
        return customerID;
    }

    public String getfirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // -------------------------
    // Methods (from UML)
    // -------------------------
    public void addAccount(Account acc) {
        if (acc != null) {
            accounts.add(acc);
            System.out.println("Account " + acc.getAccountNumber() +
                    " added for " + firstname + " " + surname);
        } else {
            System.out.println("Cannot add a null account.");
        }
    }

    public void viewAccounts() {
        if (accounts.isEmpty()) {
            System.out.println(firstname + " " + surname + " has no accounts yet.");
        } else {
            System.out.println("Accounts for " + firstname + " " + surname + ":");
            for (Account acc : accounts) {
                System.out.println("- " + acc.getAccountNumber() +
                        " | Balance: " + acc.getBalance());
            }
        }
    }

    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Name: " + firstname + " " + surname);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
    }

    public String getFirstname() {
        return null;
    }
}
