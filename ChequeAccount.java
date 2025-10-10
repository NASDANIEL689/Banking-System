public class ChequeAccount extends Account {
    private String employerName;

    public ChequeAccount(String accountNumber, Customer customer, String employerName) {
        super(accountNumber, customer);
        this.employerName = employerName;
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
        System.out.println("Cheque Account does not earn interest.");
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
}

