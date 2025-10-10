package Banking_system;

import java.util.ArrayList;
import java.util.List;

public class customer {
    // -------------------------
    // Attributes (from UML)
    // -------------------------
    private String customerID;
    private String firstname;
    private String surname;
    private String address;
    private String phone;
    private String email;
    private List<account> accounts; // one-to-many relationship

    // -------------------------
    // Constructor
    // -------------------------
    public customer(String customerID, String firstname, String surname,
                    String address, String phone, String email) {
        this.customerID = customerID;
        this.firstname = firstname;
        this.surname = surname;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    // -------------------------
    // Getters and Setters
    // -------------------------
    public String getCustomerID() {
        return customerID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
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
    public void addAccount(account acc) {
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
            for (account acc : accounts) {
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
}
