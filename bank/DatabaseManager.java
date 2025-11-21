package bank;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    // Use file-based database instead of in-memory
    private static final String JDBC_URL = "jdbc:h2:file:data/bankdb;AUTO_SERVER=TRUE";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASS = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
    }

    public static void initSchema() throws Exception {
        Connection conn = getConnection();
        String schema = new String(Files.readAllBytes(Paths.get("schema.sql")));
        try (Statement st = conn.createStatement()) {
            for (String s : schema.split(";")) {
                String t = s.trim();
                if (!t.isEmpty()) st.execute(t);
            }
        }
    }

    public static void close() {
        // No-op: connections are closed by callers
    }
}
