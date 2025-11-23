package bankapp;

public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly

    public SavingsAccount(String accountNumber, Customer owner) {
        super(accountNumber, owner);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Savings account does not allow withdrawals.");
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        recordTransaction("interest", interest);
        System.out.println("Interest of BWP " + interest + " added to Savings Account.");
    }
}

