public class ChequeAccount extends Account implements Withdrawable {
    private String employerName;

    public ChequeAccount(String accountNumber, Customer customer, String employerName) {
        super(accountNumber, customer);
        // Enforce that only employed IndividualCustomer or CompanyCustomer can open ChequeAccount
        if (customer instanceof IndividualCustomer) {
            IndividualCustomer pc = (IndividualCustomer) customer;
            if (!pc.isEmployed()) {
                throw new IllegalArgumentException("Individual customer must be employed to open a ChequeAccount.");
            }
            this.employerName = (pc.getEmployerName() != null && !pc.getEmployerName().isEmpty()) ? pc.getEmployerName() : employerName;
        } else if (customer instanceof CompanyCustomer) {
            this.employerName = employerName; // company may pass company name
        } else {
            throw new IllegalArgumentException("ChequeAccount can only be opened by an employed individual or a company customer.");
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
        System.out.println("Cheque Account does not earn interest.");
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
}

