package bankapp;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String customerID;
    protected String address;
    protected String phone;
    protected String email;
    protected String username;
    protected String password;
    protected List<Account> accounts = new ArrayList<>();

    public Customer(String customerID, String address, String phone, String email, String username, String password) {
        this.customerID = customerID;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getCustomerID() { return customerID; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public void setCustomerID(String customerID) { this.customerID = customerID; }
    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }

    // Manage accounts
    public void addAccount(Account account) { accounts.add(account); }
    public List<Account> getAccounts() { return accounts; }

    // Abstract method to display customer details (must be implemented by subclasses)
    public abstract void displayInfo();
}
