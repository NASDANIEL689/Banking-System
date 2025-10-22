public class PersonalAccount extends Account {
    private String personalId;

    public PersonalAccount(String accountNumber, Customer customer, double initialDeposit, String personalId) {
        super(accountNumber, customer);
        this.personalId = personalId;
        this.balance = initialDeposit;
        System.out.println("Personal account opened with BWP " + initialDeposit);
    }

    public String getPersonalId() {
        return personalId;
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
        // Personal accounts may have specific interest logic
        double interest = balance * 0.03; // Example: 3% interest
        balance += interest;
        System.out.println("Interest of BWP " + interest + " added to Personal Account.");
    }
}
