package bankapp;

public interface IAccount {
    void deposit(double amount);
    void deposit(double amount, String narration);
    void withdraw(double amount);
    void applyMonthlyInterest();
    double getBalance();
    String getAccountNumber();
}
