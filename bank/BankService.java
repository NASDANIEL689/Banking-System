package bank;

import java.sql.SQLException;
import java.util.List;
import bank.dao.CustomerDAO;
import bank.dao.AccountDAO;
import bank.dao.TransactionDAO;
import bankapp.LoginService;
import bankapp.Customer;
import bankapp.Account;
import bankapp.PersonalCustomer;
import bankapp.BusinessCustomer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import bankapp.ChequeAccount;
import bankapp.Transaction;

public class BankService {
    // Static instance for global access
    private static BankService instance = new BankService();
    
    public static BankService getInstance() {
        return instance;
    }
    
    private LoginService loginService = new LoginService();
    private CustomerDAO customerDAO = new CustomerDAO();
    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    public LoginService getLoginService() { return loginService; }

    public void registerUser(String username, String password) {
        loginService.registerUser(username, password);
    }

    public void createPersonalCustomer(PersonalCustomer pc) throws SQLException {
        customerDAO.create(pc);
    }

    public void createBusinessCustomer(BusinessCustomer bc) throws SQLException {
        customerDAO.create(bc);
    }

    public void openSavingsAccount(SavingsAccount sa) throws SQLException {
        accountDAO.create(sa);
    }

    public void openInvestmentAccount(InvestmentAccount ia) throws SQLException {
        accountDAO.create(ia);
    }

    public void openChequeAccount(ChequeAccount ca) throws SQLException {
        accountDAO.create(ca);
    }

    public void deposit(Account a, double amount) throws SQLException {
        a.deposit(amount);
        // persist account balance
        accountDAO.updateBalance(a.getAccountNumber(), a.getBalance());
        // persist new transactions (only the ones not yet saved)
        List<Transaction> allTransactions = a.getTransactions();
        // Get existing transactions from database to avoid duplicates
        List<Transaction> existingTransactions = transactionDAO.getTransactionsByAccount(a.getAccountNumber());
        for (Transaction t : allTransactions) {
            // Check if transaction already exists in database
            boolean exists = false;
            for (Transaction existing : existingTransactions) {
                if (existing.getTransactionId().equals(t.getTransactionId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                transactionDAO.addTransaction(t, a.getBalance());
            }
        }
    }

    public void withdraw(Account a, double amount) throws SQLException {
        a.withdraw(amount);
        // persist account balance
        accountDAO.updateBalance(a.getAccountNumber(), a.getBalance());
        // persist new transactions
        List<Transaction> allTransactions = a.getTransactions();
        List<Transaction> existingTransactions = transactionDAO.getTransactionsByAccount(a.getAccountNumber());
        for (Transaction t : allTransactions) {
            boolean exists = false;
            for (Transaction existing : existingTransactions) {
                if (existing.getTransactionId().equals(t.getTransactionId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                transactionDAO.addTransaction(t, a.getBalance());
            }
        }
    }

    public void transfer(Account fromAccount, Account toAccount, double amount) throws SQLException {
        fromAccount.withdraw(amount);
        toAccount.deposit(amount, "Transfer from " + fromAccount.getAccountNumber());
        // persist both account balances
        accountDAO.updateBalance(fromAccount.getAccountNumber(), fromAccount.getBalance());
        accountDAO.updateBalance(toAccount.getAccountNumber(), toAccount.getBalance());
        // persist transactions for both accounts
        List<Transaction> fromTransactions = fromAccount.getTransactions();
        List<Transaction> existingFromTransactions = transactionDAO.getTransactionsByAccount(fromAccount.getAccountNumber());
        for (Transaction t : fromTransactions) {
            boolean exists = false;
            for (Transaction existing : existingFromTransactions) {
                if (existing.getTransactionId().equals(t.getTransactionId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                transactionDAO.addTransaction(t, fromAccount.getBalance());
            }
        }
        List<Transaction> toTransactions = toAccount.getTransactions();
        List<Transaction> existingToTransactions = transactionDAO.getTransactionsByAccount(toAccount.getAccountNumber());
        for (Transaction t : toTransactions) {
            boolean exists = false;
            for (Transaction existing : existingToTransactions) {
                if (existing.getTransactionId().equals(t.getTransactionId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                transactionDAO.addTransaction(t, toAccount.getBalance());
            }
        }
    }

    public Customer findCustomer(String id) throws SQLException { 
        return customerDAO.findById(id); 
    }
    
    public Account findAccount(String accNo) throws SQLException { 
        return accountDAO.findByNumber(accNo); 
    }
    
    public List<Customer> listCustomers() throws SQLException { 
        return customerDAO.listAll(); 
    }

    public List<Account> listAccountsByCustomer(String customerId) throws SQLException {
        return accountDAO.listByCustomer(customerId);
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) throws SQLException {
        return transactionDAO.getTransactionsByAccount(accountNumber);
    }
}
