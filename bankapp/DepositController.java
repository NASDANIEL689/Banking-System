package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.List;

// Controller for the deposit view
public class DepositController implements Initializable {
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label currentBalanceLabel;
    
    @FXML
    private Label accountNumberLabel;
    
    @FXML
    private RadioButton cashRadioButton;
    
    @FXML
    private RadioButton checkRadioButton;
    
    @FXML
    private RadioButton transferRadioButton;
    
    @FXML
    private TextField amountField;
    
    @FXML
    private TextField descriptionField;
    
    @FXML
    private Button quick50Button;
    
    @FXML
    private Button quick100Button;
    
    @FXML
    private Button quick200Button;
    
    @FXML
    private Button quick500Button;
    
    @FXML
    private Button depositButton;
    
    @FXML
    private TableView<Transaction> recentDepositsTable;
    
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    
    @FXML
    private TableColumn<Transaction, String> methodColumn;
    
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    
    // Current account (would be set from previous screen)
    private Account currentAccount;
    private bank.BankService bankService = bank.BankService.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up radio button group
        ToggleGroup methodGroup = new ToggleGroup();
        cashRadioButton.setToggleGroup(methodGroup);
        checkRadioButton.setToggleGroup(methodGroup);
        transferRadioButton.setToggleGroup(methodGroup);
        cashRadioButton.setSelected(true);
        
        // Initialize table columns
        if (dateColumn != null) {
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        }
        if (amountColumn != null) {
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        }
        if (methodColumn != null) {
            methodColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        if (descriptionColumn != null) {
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        }
        
        // Update account info
        updateAccountInfo();
        loadRecentTransactions();
    }
    
    // Load recent transactions from database
    private void loadRecentTransactions() {
        if (currentAccount != null && recentDepositsTable != null) {
            try {
                List<Transaction> transactions = bankService.getTransactionsByAccount(currentAccount.getAccountNumber());
                // Filter for deposits only and limit to recent 10
                recentDepositsTable.getItems().clear();
                int count = 0;
                for (Transaction t : transactions) {
                    if (t.getType().contains("DEPOSIT") && count < 10) {
                        recentDepositsTable.getItems().add(t);
                        count++;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading transactions: " + e.getMessage());
            }
        }
    }
    
    // Update account information display
    private void updateAccountInfo() {
        if (currentAccount != null) {
            currentBalanceLabel.setText(String.format("$%.2f", currentAccount.getBalance()));
            accountNumberLabel.setText("Account: " + currentAccount.getAccountNumber());
        } else {
            // Default values for demo
            currentBalanceLabel.setText("$0.00");
            accountNumberLabel.setText("Account: ****1234");
        }
    }
    
    // Set the current account
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
        updateAccountInfo();
    }
    
    // Handle quick amount buttons
    @FXML
    private void handleQuickAmount(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        String amount = buttonText.replace("$", "");
        amountField.setText(amount);
    }
    
    // Handle deposit button
    @FXML
    private void handleDeposit() {
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
            
            // Get deposit method
            String method = "Cash";
            if (checkRadioButton.isSelected()) {
                method = "Check";
            } else if (transferRadioButton.isSelected()) {
                method = "Bank Transfer";
            }
            
            // Get description
            String description = descriptionField.getText().trim();
            if (description.isEmpty()) {
                description = "Deposit via " + method;
            }
            
            // Process deposit
            if (currentAccount != null) {
                try {
                    // Use BankService to persist to database
                    bankService.deposit(currentAccount, amount);
                    showAlert("Success", "Deposit of $" + String.format("%.2f", amount) + " processed successfully!");
                    
                    // Clear fields
                    amountField.clear();
                    descriptionField.clear();
                    
                    // Update balance and reload transactions
                    updateAccountInfo();
                    loadRecentTransactions();
                } catch (Exception e) {
                    showAlert("Error", "Failed to process deposit: " + e.getMessage());
                }
            } else {
                showAlert("Error", "No account selected. Please select an account first.");
            }
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }
    
    // Handle back button
    @FXML
    private void handleBack() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(backButton), "MainMenuView.fxml");
    }
    
    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

