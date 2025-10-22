public class BusinessCustomer extends Customer {
    private String businessRegistrationNumber;

    public BusinessCustomer(String customerId, String businessName, String address, String phoneNumber, String email, String businessRegistrationNumber) {
        super(customerId, businessName, address, phoneNumber, email);
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public String getBusinessRegistrationNumber() { return businessRegistrationNumber; }
}
