public class InvestmentAccount extends Account implements Withdrawable {
    private static final double INTEREST_RATE = 0.05; // 5%

    public InvestmentAccount(String accountNumber, Customer customer, double initialDeposit) {
        super(accountNumber, customer);
        if (initialDeposit >= 500) {
            this.balance = initialDeposit;
            System.out.println("Investment account opened with BWP " + initialDeposit);
        } else {
            System.out.println("Initial deposit must be at least BWP 500. Account not activated.");
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: BWP " + amount);
        } else {
            System.out.println("Invalid or insufficient funds for withdrawal.");
        }
    }

    @Override
    public void calculateInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        System.out.println("Interest of BWP " + interest + " added to Investment Account.");
    }
}
