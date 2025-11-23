package bankapp;

public class InvestmentAccount extends Account implements Withdrawable {
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    private static final double MINIMUM_OPENING_BALANCE = 500.0;

    public InvestmentAccount(String accountNumber, Customer owner) {
        super(accountNumber, owner);
    }

    public InvestmentAccount(String accountNumber, Customer owner, double initialDeposit) {
        this(accountNumber, owner);
        setOpeningDeposit(initialDeposit);
    }

    public void setOpeningDeposit(double initialDeposit) {
        if (initialDeposit >= MINIMUM_OPENING_BALANCE) {
            this.balance = initialDeposit;
            if (initialDeposit > 0) {
                recordTransaction("deposit", initialDeposit);
            }
            System.out.println("Investment account opened with BWP " + initialDeposit);
        } else {
            throw new IllegalArgumentException("Investment account requires minimum opening balance of P500. Provided: P" + initialDeposit);
        }
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            recordTransaction("withdrawal", amount);
            System.out.println("Withdrawn: BWP " + amount);
        } else {
            System.out.println("Invalid or insufficient funds for withdrawal.");
        }
    }

    @Override
    public void applyMonthlyInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        recordTransaction("interest", interest);
        System.out.println("Interest of BWP " + interest + " added to Investment Account.");
    }
}
