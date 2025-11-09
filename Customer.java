package bankapp;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    protected String customerId;
    protected String fullName; // Can be person or business name
    protected String address;
    protected String phoneNumber;
    protected String email;
    protected List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String fullName, String address, String phoneNumber, String email) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }

    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }

    // Manage accounts
    public void addAccount(Account account) { accounts.add(account); }
    public List<Account> getAccounts() { return accounts; }

    // Display customer details
    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Full Name: " + fullName);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Email: " + email);
        System.out.println("Number of Accounts: " + accounts.size());
    }
}
