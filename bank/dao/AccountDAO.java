package bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import bank.DatabaseManager;
import bankapp.Account;
import bankapp.Customer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import bankapp.ChequeAccount;

public class AccountDAO {
    public void create(Account a) throws SQLException {
        String sql = "INSERT INTO accounts(account_number, customer_id, type, balance, branch, initial_deposit) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getCustomer().getCustomerId());
            ps.setString(3, a.getClass().getSimpleName());
            ps.setDouble(4, a.getBalance());
            ps.setString(5, null);
            ps.setDouble(6, a.getBalance());
            ps.executeUpdate();
        }
    }

    public Account findByNumber(String accNo) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    String customerId = rs.getString("customer_id");
                    Customer c = new CustomerDAO().findById(customerId);
                    double balance = rs.getDouble("balance");
                    Account acct = null;
                    if ("SavingsAccount".equals(type)) acct = new SavingsAccount(accNo, c);
                    else if ("InvestmentAccount".equals(type)) acct = new InvestmentAccount(accNo, c, rs.getDouble("initial_deposit"));
                    else if ("ChequeAccount".equals(type)) acct = new ChequeAccount(accNo, c, rs.getString("branch"));
                    if (acct != null) {
                        // set balance from DB
                        acct.balance = rs.getDouble("balance");
                        // Load transactions from database
                        TransactionDAO tdao = new TransactionDAO();
                        java.util.List<Transaction> transactions = tdao.getTransactionsByAccount(accNo);
                        for (Transaction t : transactions) {
                            acct.addTransaction(t);
                        }
                    }
                    return acct;
                }
            }
        }
        return null;
    }

    public List<Account> listByCustomer(String customerId) throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT account_number FROM accounts WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Account a = findByNumber(rs.getString(1));
                    if (a != null) list.add(a);
                }
            }
        }
        return list;
    }

    public void updateBalance(String accountNumber, double newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            ps.executeUpdate();
        }
    }
}
