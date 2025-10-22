public class BusinessAccount extends Account {
    private String businessRegistrationNumber;

    public BusinessAccount(String accountNumber, Customer customer, double initialDeposit, String businessRegistrationNumber) {
        super(accountNumber, customer);
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.balance = initialDeposit;
        System.out.println("Business account opened with BWP " + initialDeposit);
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
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
        // Business accounts may have different interest logic
        double interest = balance * 0.04; // Example: 4% interest
        balance += interest;
        System.out.println("Interest of BWP " + interest + " added to Business Account.");
    }
}
