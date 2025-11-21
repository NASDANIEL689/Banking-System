package bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import bank.DatabaseManager;
import bankapp.Transaction;

public class TransactionDAO {
    public void addTransaction(Transaction t, double balanceAfter) throws SQLException {
        String sql = "INSERT INTO transactions(transaction_id, account_number, type, amount, timestamp, balance_after) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTransactionId());
            ps.setString(2, t.getAccountNumber());
            ps.setString(3, t.getType());
            ps.setDouble(4, t.getAmount());
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(t.getDateTime()));
            ps.setDouble(6, balanceAfter);
            ps.executeUpdate();
        }
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT transaction_id, account_number, type, amount, timestamp FROM transactions WHERE account_number = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Convert SQL timestamp to LocalDateTime
                    java.sql.Timestamp ts = rs.getTimestamp("timestamp");
                    java.time.LocalDateTime dateTime = ts != null ? ts.toLocalDateTime() : java.time.LocalDateTime.now();
                    Transaction t = new Transaction(
                        rs.getString("transaction_id"),
                        rs.getString("account_number"),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        dateTime
                    );
                    list.add(t);
                }
            }
        }
        return list;
    }
}
