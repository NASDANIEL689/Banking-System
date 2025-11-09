package bankapp;

import java.util.ArrayList;
import java.util.List;

public abstract class Account implements IAccount, ITransaction {
    protected String accountNumber;
    protected double balance;
    protected Customer customer;
    protected List<Transaction> transactions = new ArrayList<>();

    // Constructor
    public Account(String accountNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = 0.0;
    }

    // Deposit funds
    public void deposit(double amount) {
        deposit(amount, "deposit");
    }

    @Override
    public void deposit(double amount, String narration) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: BWP " + amount);
            recordTransaction("DEPOSIT: " + narration, amount);
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

    // Transaction recording
    protected void recordTransaction(String type, double amount) {
        String txId = accountNumber + "-" + System.currentTimeMillis();
        Transaction t = new Transaction(txId, accountNumber, amount, type);
        addTransaction(t);
    }

    @Override
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }

    // Display basic info
    public void displayAccountInfo() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + (customer != null ? customer.getFullName() : "(no owner)"));
        System.out.println("Balance: BWP " + balance);
    }

    @Override
    public void applyMonthlyInterest() {
        calculateInterest();
    }
}
