public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected Customer customer;

    // Constructor
    public Account(String accountNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = 0.0;
    }

    // Deposit funds
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: BWP " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw funds (abstract - implemented differently by subclasses)
    public abstract void withdraw(double amount);

    // Calculate interest (abstract - implemented differently by subclasses)
    public abstract void calculateInterest();

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getCustomer() {
        return customer;
    }

    // Display basic info
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + customer.getFullName());
        System.out.println("Balance: BWP " + balance);
    }
}
