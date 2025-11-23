package bankapp;

import bank.BankService;
import bank.dao.CustomerDAO;
import bankapp.IndividualCustomer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;

// Controller for the sign up view
public class SignUpController implements Initializable {
    private final BankService bankService = BankService.getInstance();
    private final CustomerDAO customerDAO = new CustomerDAO();
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;

    @FXML
    private TextField nationalIdField;

    @FXML
    private TextField initialDepositField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private ComboBox<String> accountTypeComboBox;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private CheckBox termsCheckbox;
    
    @FXML
    private Button signUpButton;
    
    @FXML
    private Hyperlink signInLink;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate account type combo with supported account types
        accountTypeComboBox.getItems().clear();
        accountTypeComboBox.getItems().add("Savings Account");
        accountTypeComboBox.getItems().add("Investment Account");
        accountTypeComboBox.getSelectionModel().selectFirst();
    }
    
    @FXML
    private void handleSignUp() {
        // Validate fields
        if (firstNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your first name");
            return;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your last name");
            return;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your email");
            return;
        }

        if (nationalIdField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your national ID or registration number");
            return;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your phone number");
            return;
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please choose a username");
            return;
        }
        
        if (passwordField.getText().isEmpty()) {
            showAlert("Error", "Please enter a password");
            return;
        }
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "Passwords do not match");
            return;
        }
        
        if (addressField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your address");
            return;
        }

        if (accountTypeComboBox.getValue() == null) {
            showAlert("Error", "Please select an account type");
            return;
        }

        if (initialDepositField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your initial deposit amount");
            return;
        }

        double initialDeposit;
        try {
            initialDeposit = Double.parseDouble(initialDepositField.getText().trim());
        } catch (NumberFormatException ex) {
            showAlert("Error", "Initial deposit must be a valid number");
            return;
        }

        if (initialDeposit <= 0) {
            showAlert("Error", "Initial deposit must be greater than zero");
            return;
        }
        
        if (!termsCheckbox.isSelected()) {
            showAlert("Error", "Please agree to the Terms and Conditions");
            return;
        }

        String username = usernameField.getText().trim();

        try {
            // Ensure username is still unique before saving
            if (customerDAO.findByUsername(username) != null) {
                showAlert("Error", "Username already exists. Please choose a different one.");
                return;
            }

            // Create the customer record and persist it
            IndividualCustomer customer = new IndividualCustomer(
                generateCustomerId(),
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                nationalIdField.getText().trim(),
                addressField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim(),
                username,
                passwordField.getText()
            );

            bankService.createIndividualCustomer(customer);
            try {
                createInitialAccount(customer, accountTypeComboBox.getValue(), initialDeposit);
            } catch (Exception accountError) {
                // Roll back customer if account creation fails so rule is not violated
                try {
                    customerDAO.delete(customer.getCustomerID());
                } catch (SQLException deleteError) {
                    System.err.println("WARNING: Failed to roll back customer after account error: " + deleteError.getMessage());
                }
                showAlert("Error", accountError.getMessage());
                return;
            }

            showAlert("Success", "Account created successfully! Please login.");
            NavigationHelper.navigateTo(NavigationHelper.getStage(signUpButton), "LoginView.fxml");
        } catch (SQLException e) {
            showAlert("Error", "Could not save your account: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleSignIn() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(signInLink), "LoginView.fxml");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String generateCustomerId() {
        // Simple unique id for new customers
        return "CUST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateAccountNumber() {
        // Account number uses ACC- prefix so it is easy to spot
        return "ACC-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }

    private void createInitialAccount(IndividualCustomer customer, String selectedAccountType, double initialDeposit) throws SQLException {
        String accountNumber = generateAccountNumber();
        String type = selectedAccountType != null ? selectedAccountType.toLowerCase() : "";

        if (type.contains("savings")) {
            SavingsAccount account = new SavingsAccount(accountNumber, customer);
            bankService.openSavingsAccount(account);
            bankService.deposit(account, initialDeposit);
        } else if (type.contains("investment")) {
            if (initialDeposit < 500.0) {
                throw new IllegalArgumentException("Investment accounts require a minimum opening balance of BWP 500.");
            }
            InvestmentAccount account = new InvestmentAccount(accountNumber, customer);
            bankService.openInvestmentAccount(account);
            bankService.deposit(account, initialDeposit);
        } else {
            throw new IllegalArgumentException("Unsupported account type. Please pick Savings or Investment.");
        }
    }
}

