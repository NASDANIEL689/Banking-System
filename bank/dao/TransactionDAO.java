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
    public void addTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions(\"txnID\", \"accountNumber\", date, amount, type) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTransactionId());
            ps.setString(2, t.getAccountNumber());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(t.getDateTime()));
            ps.setDouble(4, t.getAmount());
            ps.setString(5, t.getType());
            ps.executeUpdate();
        }
    }

    public List<Transaction> getTransactionsByAccount(String accountNumber) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT \"txnID\", \"accountNumber\", type, amount, date FROM transactions WHERE \"accountNumber\" = ? ORDER BY date DESC";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Convert SQL timestamp to LocalDateTime
                    java.sql.Timestamp ts = rs.getTimestamp("date");
                    java.time.LocalDateTime dateTime = ts != null ? ts.toLocalDateTime() : java.time.LocalDateTime.now();
                    Transaction t = new Transaction(
                        rs.getString("txnID"),
                        rs.getString("accountNumber"),
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
