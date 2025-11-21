public class IndividualCustomer extends Customer {
    private String firstName;
    private String surname;
    private String nationalId;
    private boolean employed = false;
    private String employerName;

    public IndividualCustomer(String customerId, String firstName, String surname, String address, String phoneNumber, String email, String nationalId) {
        super(customerId, firstName + " " + surname, address, phoneNumber, email);
        this.firstName = firstName;
        this.surname = surname;
        this.nationalId = nationalId;
    }

    // Convenience constructor to preserve older code that passed full name as single string
    public IndividualCustomer(String customerId, String fullName, String address, String phoneNumber, String email, String nationalId) {
        super(customerId, fullName, address, phoneNumber, email);
        String[] parts = fullName != null ? fullName.split(" ", 2) : new String[]{"",""};
        this.firstName = parts.length > 0 ? parts[0] : "";
        this.surname = parts.length > 1 ? parts[1] : "";
        this.nationalId = nationalId;
    }

    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getNationalId() { return nationalId; }

    // Backwards-compatible alias expected by DAO
    public String getPersonalIdNumber() { return nationalId; }

    public boolean isEmployed() { return employed; }
    public String getEmployerName() { return employerName; }
    public void setEmployment(String employerName) { this.employed = true; this.employerName = employerName; }
}
