import java.sql.SQLException;
import java.util.List;

public class BankService {
    private LoginService loginService = new LoginService();
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public LoginService getLoginService() { return loginService; }

    public void registerUser(String username, String password) {
        loginService.registerUser(username, password);
    }

    public void createIndividualCustomer(IndividualCustomer pc) throws SQLException {
        customerDAO.create(pc);
    }

    public void openSavingsAccount(SavingsAccount sa) throws SQLException {
        accountDAO.create(sa);
    }

    public void deposit(Account a, double amount) throws SQLException {
        a.deposit(amount);
        // persist account balance
        accountDAO.updateBalance(a.getAccountNumber(), a.getBalance());
        // persist transactions
        for (Transaction t : a.getTransactions()) {
            transactionDAO.addTransaction(t);
        }
    }

    public Customer findCustomer(String id) throws SQLException { return customerDAO.findById(id); }
    public Account findAccount(String accNo) throws SQLException { return accountDAO.findByNumber(accNo); }
    public List<Customer> listCustomers() throws SQLException { return customerDAO.listAll(); }
}
