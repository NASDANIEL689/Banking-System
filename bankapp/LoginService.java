package bankapp;

import java.sql.SQLException;
import bank.dao.CustomerDAO;

public class LoginService implements ILoginService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private Customer currentUser = null;

    @Override
    public boolean login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                System.err.println("DEBUG: Username is null or empty");
                return false;
            }
            Customer customer = customerDAO.findByUsername(username.trim());
            if (customer == null) {
                System.err.println("DEBUG: Customer not found for username: '" + username + "'");
                return false;
            }
            if (customer.getPassword() == null || !customer.getPassword().equals(password)) {
                System.err.println("DEBUG: Password mismatch for username: '" + username + "'");
                return false;
            }
            currentUser = customer;
            System.out.println("DEBUG: Login successful for username: '" + username + "'");
            return true;
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void logout(String username) {
        currentUser = null;
    }

    public Customer getCurrentUser() {
        return currentUser;
    }

    public void registerUser(String username, String password) {
        // This method is kept for backward compatibility but should not be used directly
        // Registration should be done through CustomerDAO.create() with full customer details
    }
}
