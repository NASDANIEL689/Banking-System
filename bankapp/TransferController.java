package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the transfer view
public class TransferController implements Initializable {
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label fromAccountBalanceLabel;
    
    @FXML
    private Label fromAccountNumberLabel;
    
    @FXML
    private Label toAccountBalanceLabel;
    
    @FXML
    private Label toAccountNumberLabel;
    
    @FXML
    private ComboBox<String> fromAccountComboBox;
    
    @FXML
    private ComboBox<String> toAccountComboBox;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private Label insufficientFundsLabel;
    
    @FXML
    private Button quick50Button;
    
    @FXML
    private Button quick100Button;
    
    @FXML
    private Button quick200Button;
    
    @FXML
    private Button quick500Button;
    
    @FXML
    private Button transferButton;
    
    @FXML
    private Button addExternalAccountButton;
    
    @FXML
    private TableView<?> recentTransfersTable;
    
    private Account fromAccount;
    private Account toAccount;
    private bank.BankService bankService = bank.BankService.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        insufficientFundsLabel.setVisible(false);
        
        // Load accounts from database
        try {
            java.util.List<Customer> customers = bankService.listCustomers();
            for (Customer c : customers) {
                java.util.List<Account> accounts = bankService.listAccountsByCustomer(c.getCustomerId());
                for (Account a : accounts) {
                    String displayText = a.getAccountNumber() + " - " + a.getClass().getSimpleName() + " ($" + String.format("%.2f", a.getBalance()) + ")";
                    fromAccountComboBox.getItems().add(displayText);
                    toAccountComboBox.getItems().add(displayText);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
        
        // Add listener to update account info when selection changes
        fromAccountComboBox.setOnAction(e -> updateFromAccount());
        toAccountComboBox.setOnAction(e -> updateToAccount());
        
        updateAccountInfo();
    }
    
    // Update from account when selection changes
    private void updateFromAccount() {
        String selected = fromAccountComboBox.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String accountNumber = selected.split(" - ")[0];
            try {
                fromAccount = bankService.findAccount(accountNumber);
                updateAccountInfo();
            } catch (Exception e) {
                System.err.println("Error loading account: " + e.getMessage());
            }
        }
    }
    
    // Update to account when selection changes
    private void updateToAccount() {
        String selected = toAccountComboBox.getSelectionModel().getSelectedItem();
        if (selected != null && !selected.equals("External Account")) {
            String accountNumber = selected.split(" - ")[0];
            try {
                toAccount = bankService.findAccount(accountNumber);
                updateAccountInfo();
            } catch (Exception e) {
                System.err.println("Error loading account: " + e.getMessage());
            }
        }
    }
    
    private void updateAccountInfo() {
        if (fromAccount != null) {
            fromAccountBalanceLabel.setText(String.format("$%.2f", fromAccount.getBalance()));
            fromAccountNumberLabel.setText(fromAccount.getAccountNumber());
        } else {
            fromAccountBalanceLabel.setText("$0.00");
            fromAccountNumberLabel.setText("****1234");
        }
        
        if (toAccount != null) {
            toAccountBalanceLabel.setText(String.format("$%.2f", toAccount.getBalance()));
            toAccountNumberLabel.setText(toAccount.getAccountNumber());
        } else {
            toAccountBalanceLabel.setText("$0.00");
            toAccountNumberLabel.setText("****5678");
        }
    }
    
    @FXML
    private void handleQuickAmount(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        String amount = buttonText.replace("$", "");
        amountField.setText(amount);
    }
    
    @FXML
    private void handleTransfer() {
        try {
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showAlert("Error", "Please enter an amount");
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than zero");
                return;
            }
            
            if (fromAccountComboBox.getSelectionModel().getSelectedItem() == null) {
                showAlert("Error", "Please select a source account");
                return;
            }
            
            if (toAccountComboBox.getSelectionModel().getSelectedItem() == null) {
                showAlert("Error", "Please select a destination account");
                return;
            }
            
            if (fromAccount == null) {
                showAlert("Error", "Please select a source account");
                return;
            }
            
            if (toAccount == null) {
                showAlert("Error", "Please select a destination account");
                return;
            }
            
            if (fromAccount.getAccountNumber().equals(toAccount.getAccountNumber())) {
                showAlert("Error", "Source and destination accounts cannot be the same");
                return;
            }
            
            if (amount > fromAccount.getBalance()) {
                showAlert("Error", "Insufficient funds in source account");
                return;
            }
            
            // Process transfer using BankService to persist to database
            try {
                bankService.transfer(fromAccount, toAccount, amount);
                showAlert("Success", "Transfer of $" + String.format("%.2f", amount) + " completed successfully!");
                
                amountField.clear();
                descriptionField.clear();
                updateAccountInfo();
            } catch (Exception e) {
                showAlert("Error", "Failed to process transfer: " + e.getMessage());
            }
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }
    
    @FXML
    private void handleAddExternalAccount() {
        showAlert("Info", "Add External Account feature coming soon!");
    }
    
    @FXML
    private void handleBack() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(backButton), "MainMenuView.fxml");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

