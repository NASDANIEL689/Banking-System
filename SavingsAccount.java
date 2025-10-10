public class SavingsAccount extends Account {
    private static final double INTEREST_RATE = 0.0005; // 0.05%

    public SavingsAccount(String accountNumber, Customer customer) {
        super(accountNumber, customer);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals are not allowed for Savings Accounts.");
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest of BWP " + interest + " added to Savings Account.");
    }
}

