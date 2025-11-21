public class CompanyCustomer extends Customer {
    private String companyName;
    private String registrationNumber;
    private String contactPerson;

    public CompanyCustomer(String customerId, String companyName, String address, String phoneNumber, String email, String registrationNumber, String contactPerson) {
        super(customerId, companyName, address, phoneNumber, email);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.contactPerson = contactPerson;
    }

    // Convenience constructor to match older BusinessCustomer usage
    public CompanyCustomer(String customerId, String companyName, String address, String phoneNumber, String email, String registrationNumber) {
        super(customerId, companyName, address, phoneNumber, email);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.contactPerson = null;
    }

    public String getCompanyName() { return companyName; }
    public String getRegistrationNumber() { return registrationNumber; }

    // Backwards-compatible alias expected by DAO
    public String getBusinessRegistrationNumber() { return registrationNumber; }
    public String getContactPerson() { return contactPerson; }
}
