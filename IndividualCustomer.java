package bankapp;

public class IndividualCustomer extends Customer {
    private String firstname;
    private String surname;
    private String nationalID;
    private boolean employed = false;
    private String employerName;

    public IndividualCustomer(String customerID, String firstname, String surname, String nationalID, 
                             String address, String phone, String email, String username, String password) {
        super(customerID, address, phone, email, username, password);
        this.firstname = firstname;
        this.surname = surname;
        this.nationalID = nationalID;
    }

    public String getFirstname() { return firstname; }
    public String getSurname() { return surname; }
    public String getNationalID() { return nationalID; }
    public boolean isEmployed() { return employed; }
    public String getEmployerName() { return employerName; }

    public void setFirstname(String firstname) { this.firstname = firstname; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setNationalID(String nationalID) { this.nationalID = nationalID; }
    
    public void setEmployment(String employerName) {
        this.employed = true;
        this.employerName = employerName;
    }

    @Override
    public void displayInfo() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Type: Individual Customer");
        System.out.println("Name: " + firstname + " " + surname);
        System.out.println("National ID: " + nationalID);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Employed: " + (employed ? "Yes - " + employerName : "No"));
        System.out.println("Number of Accounts: " + accounts.size());
    }
}


