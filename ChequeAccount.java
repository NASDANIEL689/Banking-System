public class ChequeAccount extends Account {
    private String employerName;

    public ChequeAccount(String accountNumber, Customer customer, String employerName) {
        super(accountNumber, customer);
        // Enforce that only employed PersonalCustomer can open ChequeAccount
        if (!(customer instanceof PersonalCustomer)) {
            throw new IllegalArgumentException("ChequeAccount can only be opened by a personal customer who is employed.");
        }
        PersonalCustomer pc = (PersonalCustomer) customer;
        if (!pc.isEmployed()) {
            throw new IllegalArgumentException("Personal customer must be employed to open a ChequeAccount.");
        }
        // Prefer employerName from customer if available
        this.employerName = (pc.getEmployerName() != null && !pc.getEmployerName().isEmpty()) ? pc.getEmployerName() : employerName;
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

