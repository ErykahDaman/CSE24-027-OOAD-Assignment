package bankingsystem.ooadassignment;

public class teller {
    private String tellerID;
    private String firstname;
    private String lastname;
    private String password;

    public teller(String tellerID, String firstname, String lastname, String password) {
        this.tellerID = tellerID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    // Getters
    public String getTellerID() { return tellerID; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getPassword() { return password; }

    // Setters
    public void setTellerID(String tellerID) { this.tellerID = tellerID; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public void setPassword(String password) { this.password = password; }

    public String getlastname() {
        return lastname;
    }

    public void setSurname(String lastname) {
        this.lastname = lastname;
    }
}
