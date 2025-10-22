import java.util.ArrayList;
import java.util.List;

public class AccountController {
    private List<Account> accounts = new ArrayList<>();

    public Account createSavings(String accountNumber, Customer customer) {
        SavingsAccount a = new SavingsAccount(accountNumber, customer);
        customer.addAccount(a);
        accounts.add(a);
        return a;
    }

    public Account createInvestment(String accountNumber, Customer customer, double initialDeposit) {
        InvestmentAccount a = new InvestmentAccount(accountNumber, customer, initialDeposit);
        if (a.getBalance() >= 500) {
            customer.addAccount(a);
            accounts.add(a);
        }
        return a;
    }

    public Account createCheque(String accountNumber, Customer customer) {
        // attempt to use employer info from PersonalCustomer
        String employer = null;
        if (customer instanceof PersonalCustomer) {
            PersonalCustomer pc = (PersonalCustomer) customer;
            if (!pc.isEmployed()) throw new IllegalArgumentException("Personal customer must be employed to open a ChequeAccount");
            employer = pc.getEmployerName();
        } else {
            throw new IllegalArgumentException("ChequeAccount can only be opened for personal customers");
        }
        ChequeAccount a = new ChequeAccount(accountNumber, customer, employer);
        customer.addAccount(a);
        accounts.add(a);
        return a;
    }

    public List<Account> getAllAccounts() { return accounts; }
}
