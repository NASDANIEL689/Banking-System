package bankapp;

public class CompanyCustomer extends Customer {
    private String companyName;
    private String regNumber;
    private String contactPerson;

    public CompanyCustomer(String customerID, String companyName, String regNumber, String contactPerson,
                           String address, String phone, String email, String username, String password) {
        super(customerID, address, phone, email, username, password);
        this.companyName = companyName;
        this.regNumber = regNumber;
        this.contactPerson = contactPerson;
    }

    public String getCompanyName() { return companyName; }
    public String getRegNumber() { return regNumber; }
    public String getContactPerson() { return contactPerson; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setRegNumber(String regNumber) { this.regNumber = regNumber; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    @Override
    public void displayInfo() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Type: Company Customer");
        System.out.println("Company Name: " + companyName);
        System.out.println("Registration Number: " + regNumber);
        System.out.println("Contact Person: " + contactPerson);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Number of Accounts: " + accounts.size());
    }
}


