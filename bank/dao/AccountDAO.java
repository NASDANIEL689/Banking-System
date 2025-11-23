package bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import bank.DatabaseManager;
import bankapp.Account;
import bankapp.Customer;
import bankapp.IndividualCustomer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import bankapp.ChequeAccount;

public class AccountDAO {
    public void create(Account a) throws SQLException {
        String sql = "INSERT INTO accounts(\"accountNumber\", \"customerID\", \"accountType\", balance, branch, \"openDate\") VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getOwner().getCustomerID());
            ps.setString(3, a.getClass().getSimpleName());
            ps.setDouble(4, a.getBalance());
            ps.setString(5, null); // branch can be null
            ps.setDate(6, java.sql.Date.valueOf(a.getOpenedDate()));
            ps.executeUpdate();
        }
    }

    public Account findByNumber(String accNo) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE \"accountNumber\" = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("accountType");
                    String customerID = rs.getString("customerID");
                    Customer c = new CustomerDAO().findById(customerID);
                    if (c == null) return null;
                    
                    double balance = rs.getDouble("balance");
                    LocalDate openDate = rs.getDate("openDate").toLocalDate();
                    
                    Account acct = null;
                    if ("SavingsAccount".equals(type)) {
                        acct = new SavingsAccount(accNo, c);
                    } else if ("InvestmentAccount".equals(type)) {
                        // For investment accounts, we need initial deposit - use balance if available
                        acct = new InvestmentAccount(accNo, c, Math.max(balance, 500.0));
                    } else if ("ChequeAccount".equals(type)) {
                        String employerName = null;
                        if (c instanceof IndividualCustomer) {
                            IndividualCustomer ic = (IndividualCustomer) c;
                            employerName = ic.getEmployerName();
                        }
                        acct = new ChequeAccount(accNo, c, employerName);
                    }
                    
                    if (acct != null) {
                        // Set balance and open date from DB
                        acct.setBalance(balance);
                        acct.setOpenedDate(openDate);
                        // Load transactions from database
                        TransactionDAO tdao = new TransactionDAO();
                        java.util.List<bankapp.Transaction> transactions = tdao.getTransactionsByAccount(accNo);
                        for (bankapp.Transaction t : transactions) {
                            acct.addTransaction(t);
                        }
                    }
                    return acct;
                }
            }
        }
        return null;
    }

    public List<Account> listByCustomer(String customerID) throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT \"accountNumber\" FROM accounts WHERE \"customerID\" = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerID);
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
        String sql = "UPDATE accounts SET balance = ? WHERE \"accountNumber\" = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newBalance);
            ps.setString(2, accountNumber);
            ps.executeUpdate();
        }
    }
}
