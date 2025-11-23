package bankapp;

public class ChequeAccount extends Account implements Withdrawable {
    private String employerName;

    public ChequeAccount(String accountNumber, Customer owner, String employerName) {
        super(accountNumber, owner);
        // Enforce that only employed IndividualCustomer or CompanyCustomer can open ChequeAccount
        if (owner instanceof IndividualCustomer) {
            IndividualCustomer ic = (IndividualCustomer) owner;
            if (!ic.isEmployed()) {
                throw new IllegalArgumentException("Individual customer must be employed to open a ChequeAccount.");
            }
            // Prefer employerName from customer if available
            this.employerName = (ic.getEmployerName() != null && !ic.getEmployerName().isEmpty()) ? ic.getEmployerName() : employerName;
        } else if (owner instanceof CompanyCustomer) {
            // Company customers can open cheque accounts
            this.employerName = null; // Not applicable for companies
        } else {
            throw new IllegalArgumentException("ChequeAccount can only be opened by an employed individual customer or a company customer.");
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
        // Cheque Account does not earn interest
        System.out.println("Cheque Account does not earn interest.");
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
}

