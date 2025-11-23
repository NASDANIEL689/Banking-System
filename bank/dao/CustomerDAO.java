package bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import bank.DatabaseManager;
import bankapp.Customer;
import bankapp.IndividualCustomer;
import bankapp.CompanyCustomer;

public class CustomerDAO {
    public void create(Customer c) throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Insert into customers table
            String sql = "INSERT INTO customers(\"customerID\", \"type\", username, password, address, email, phone) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getCustomerID());
                ps.setString(2, c instanceof IndividualCustomer ? "individual" : "company");
                ps.setString(3, c.getUsername());
                ps.setString(4, c.getPassword());
                ps.setString(5, c.getAddress());
            ps.setString(6, c.getEmail());
                ps.setString(7, c.getPhone());
                ps.executeUpdate();
            }
            
            // Insert into appropriate details table
            if (c instanceof IndividualCustomer) {
                IndividualCustomer ic = (IndividualCustomer) c;
                String sql2 = "INSERT INTO IndividualDetails(\"customerID\", firstname, surname, \"nationalID\", employed, \"employerName\") VALUES (?,?,?,?,?,?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, ic.getCustomerID());
                    ps2.setString(2, ic.getFirstname());
                    ps2.setString(3, ic.getSurname());
                    ps2.setString(4, ic.getNationalID());
                    ps2.setBoolean(5, ic.isEmployed());
                    ps2.setString(6, ic.getEmployerName());
                    ps2.executeUpdate();
                }
            } else if (c instanceof CompanyCustomer) {
                CompanyCustomer cc = (CompanyCustomer) c;
                String sql2 = "INSERT INTO CompanyDetails(\"customerID\", \"companyName\", \"regNumber\", \"contactPerson\") VALUES (?,?,?,?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, cc.getCustomerID());
                    ps2.setString(2, cc.getCompanyName());
                    ps2.setString(3, cc.getRegNumber());
                    ps2.setString(4, cc.getContactPerson());
                    ps2.executeUpdate();
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public Customer findById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE \"customerID\" = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    String customerID = rs.getString("customerID");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    String address = rs.getString("address");
                    String email = rs.getString("email");
                    String phone = rs.getString("phone");
                    
                    if ("individual".equalsIgnoreCase(type)) {
                        // Load individual details
                        String sql2 = "SELECT * FROM IndividualDetails WHERE \"customerID\" = ?";
                        try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                            ps2.setString(1, id);
                            try (ResultSet rs2 = ps2.executeQuery()) {
                                if (rs2.next()) {
                                    IndividualCustomer ic = new IndividualCustomer(
                                        customerID,
                                        rs2.getString("firstname"),
                                        rs2.getString("surname"),
                                        rs2.getString("nationalID"),
                                        address, phone, email, username, password
                                    );
                                    // Load employment info
                                    boolean employed = rs2.getBoolean("employed");
                                    if (employed) {
                                        ic.setEmployment(rs2.getString("employerName"));
                                    }
                                    return ic;
                                }
                            }
                        }
                    } else if ("company".equalsIgnoreCase(type)) {
                        // Load company details
                        String sql2 = "SELECT * FROM CompanyDetails WHERE \"customerID\" = ?";
                        try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                            ps2.setString(1, id);
                            try (ResultSet rs2 = ps2.executeQuery()) {
                                if (rs2.next()) {
                                    return new CompanyCustomer(
                                        customerID,
                                        rs2.getString("companyName"),
                                        rs2.getString("regNumber"),
                                        rs2.getString("contactPerson"),
                                        address, phone, email, username, password
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Customer findByUsername(String username) throws SQLException {
        String sql = "SELECT \"customerID\" FROM customers WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return findById(rs.getString("customerID"));
                }
            }
        }
        return null;
    }

    public List<Customer> listAll() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT \"customerID\" FROM customers";
        try (Connection conn = DatabaseManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Customer c = findById(rs.getString(1));
                if (c != null) list.add(c);
            }
        }
        return list;
    }

    public void update(Customer c) throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Update customers table
            String sql = "UPDATE customers SET address=?, phone=?, email=?, username=?, password=? WHERE \"customerID\"=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, c.getAddress());
                ps.setString(2, c.getPhone());
                ps.setString(3, c.getEmail());
                ps.setString(4, c.getUsername());
                ps.setString(5, c.getPassword());
                ps.setString(6, c.getCustomerID());
                ps.executeUpdate();
            }
            
            // Update details table
            if (c instanceof IndividualCustomer) {
                IndividualCustomer ic = (IndividualCustomer) c;
                String sql2 = "UPDATE IndividualDetails SET firstname=?, surname=?, \"nationalID\"=?, employed=?, \"employerName\"=? WHERE \"customerID\"=?";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, ic.getFirstname());
                    ps2.setString(2, ic.getSurname());
                    ps2.setString(3, ic.getNationalID());
                    ps2.setBoolean(4, ic.isEmployed());
                    ps2.setString(5, ic.getEmployerName());
                    ps2.setString(6, ic.getCustomerID());
                    ps2.executeUpdate();
                }
            } else if (c instanceof CompanyCustomer) {
                CompanyCustomer cc = (CompanyCustomer) c;
                String sql2 = "UPDATE CompanyDetails SET \"companyName\"=?, \"regNumber\"=?, \"contactPerson\"=? WHERE \"customerID\"=?";
                try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                    ps2.setString(1, cc.getCompanyName());
                    ps2.setString(2, cc.getRegNumber());
                    ps2.setString(3, cc.getContactPerson());
                    ps2.setString(4, cc.getCustomerID());
                    ps2.executeUpdate();
                }
            }
            
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void delete(String id) throws SQLException {
        Connection conn = DatabaseManager.getConnection();
        try {
            conn.setAutoCommit(false);
            
            // Delete from details table first (foreign key constraint)
            String typeSql = "SELECT \"type\" FROM customers WHERE \"customerID\" = ?";
            String type = null;
            try (PreparedStatement ps = conn.prepareStatement(typeSql)) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        type = rs.getString("type");
                    }
                }
            }
            
            if ("individual".equalsIgnoreCase(type)) {
                String sql = "DELETE FROM IndividualDetails WHERE \"customerID\" = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, id);
                    ps.executeUpdate();
                }
            } else if ("company".equalsIgnoreCase(type)) {
                String sql = "DELETE FROM CompanyDetails WHERE \"customerID\" = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
                }
            }
            
            // Delete from customers table
            String sql2 = "DELETE FROM customers WHERE \"customerID\" = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)) {
                ps2.setString(1, id);
                ps2.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
