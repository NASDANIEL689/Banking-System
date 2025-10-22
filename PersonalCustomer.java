public class PersonalCustomer extends Customer {
    private String personalIdNumber;
    private boolean employed = false;
    private String employerName;

    public PersonalCustomer(String customerId, String fullName, String address, String phoneNumber, String email, String personalIdNumber) {
        super(customerId, fullName, address, phoneNumber, email);
        this.personalIdNumber = personalIdNumber;
    }

    public String getPersonalIdNumber() { return personalIdNumber; }

    public boolean isEmployed() { return employed; }
    public String getEmployerName() { return employerName; }

    public void setEmployment(String employerName) {
        this.employed = true;
        this.employerName = employerName;
    }
}
