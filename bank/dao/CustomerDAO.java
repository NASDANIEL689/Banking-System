package bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import bank.DatabaseManager;
import bankapp.Customer;
import bankapp.PersonalCustomer;
import bankapp.BusinessCustomer;

public class CustomerDAO {
    public void create(Customer c) throws SQLException {
        String sql = "INSERT INTO customers(customer_id, type, full_name, address, phone, email, personal_id, business_registration, employed, employer_name) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerId());
            ps.setString(2, c instanceof PersonalCustomer ? "PERSONAL" : "BUSINESS");
            ps.setString(3, c.getFullName());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getPhoneNumber());
            ps.setString(6, c.getEmail());
            // optional subtype fields
            if (c instanceof PersonalCustomer) {
                PersonalCustomer p = (PersonalCustomer) c;
                ps.setString(7, p.getPersonalIdNumber());
                ps.setNull(8, Types.VARCHAR);
                ps.setBoolean(9, p.isEmployed());
                ps.setString(10, p.getEmployerName());
            } else if (c instanceof BusinessCustomer) {
                BusinessCustomer b = (BusinessCustomer) c;
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, b.getBusinessRegistrationNumber());
                ps.setBoolean(9, false);
                ps.setNull(10, Types.VARCHAR);
            } else {
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
                ps.setBoolean(9, false);
                ps.setNull(10, Types.VARCHAR);
            }
            ps.executeUpdate();
        }
    }

    public Customer findById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String type = rs.getString("type");
                    if ("PERSONAL".equalsIgnoreCase(type)) {
                        PersonalCustomer p = new PersonalCustomer(
                                rs.getString("customer_id"),
                                rs.getString("full_name"),
                                rs.getString("address"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                rs.getString("personal_id")
                        );
                        boolean employed = rs.getBoolean("employed");
                        if (employed) p.setEmployment(rs.getString("employer_name"));
                        return p;
                    } else {
                        BusinessCustomer b = new BusinessCustomer(
                                rs.getString("customer_id"),
                                rs.getString("full_name"),
                                rs.getString("address"),
                                rs.getString("phone"),
                                rs.getString("email"),
                                rs.getString("business_registration")
                        );
                        return b;
                    }
                }
            }
        }
        return null;
    }

    public List<Customer> listAll() throws SQLException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id FROM customers";
        try (Connection conn = DatabaseManager.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(findById(rs.getString(1)));
        }
        return list;
    }

    public void update(Customer c) throws SQLException {
        String sql = "UPDATE customers SET full_name=?, address=?, phone=?, email=?, personal_id=?, business_registration=?, employed=?, employer_name=? WHERE customer_id=?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getAddress());
            ps.setString(3, c.getPhoneNumber());
            ps.setString(4, c.getEmail());
            if (c instanceof PersonalCustomer) {
                PersonalCustomer p = (PersonalCustomer) c;
                ps.setString(5, p.getPersonalIdNumber());
                ps.setNull(6, Types.VARCHAR);
                ps.setBoolean(7, p.isEmployed());
                ps.setString(8, p.getEmployerName());
            } else if (c instanceof BusinessCustomer) {
                BusinessCustomer b = (BusinessCustomer) c;
                ps.setNull(5, Types.VARCHAR);
                ps.setString(6, b.getBusinessRegistrationNumber());
                ps.setBoolean(7, false);
                ps.setNull(8, Types.VARCHAR);
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setBoolean(7, false);
                ps.setNull(8, Types.VARCHAR);
            }
            ps.setString(9, c.getCustomerId());
            ps.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection conn = DatabaseManager.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }
}
