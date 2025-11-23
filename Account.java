package bankapp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Account implements IAccount, ITransaction {
    protected String accountNumber;
    protected double balance;
    protected Customer owner;
    protected LocalDate openedDate;
    protected List<Transaction> transactions = new ArrayList<>();

    // Constructor
    public Account(String accountNumber, Customer owner) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = 0.0;
        this.openedDate = LocalDate.now();
    }

    // Constructor with explicit open date
    public Account(String accountNumber, Customer owner, LocalDate openedDate) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = 0.0;
        this.openedDate = openedDate;
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
            recordTransaction("deposit", amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    // Withdraw funds (abstract - implemented differently by subclasses)
    public abstract void withdraw(double amount);

    // Apply monthly interest (abstract - implemented differently by subclasses)
    public abstract void applyMonthlyInterest();

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Customer getOwner() {
        return owner;
    }

    public Customer getCustomer() {
        return owner; // For backward compatibility
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
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
        System.out.println("Account Holder: " + (owner != null ? owner.getCustomerID() : "(no owner)"));
        System.out.println("Balance: BWP " + balance);
        System.out.println("Opened Date: " + openedDate);
    }

}
