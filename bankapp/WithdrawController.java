package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the withdraw view
public class WithdrawController implements Initializable {
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label availableBalanceLabel;
    
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
    private Button withdrawButton;
    
    @FXML
    private TableView<?> recentWithdrawalsTable;
    
    // Current account
    private Account currentAccount;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up radio button group
        ToggleGroup methodGroup = new ToggleGroup();
        cashRadioButton.setToggleGroup(methodGroup);
        checkRadioButton.setToggleGroup(methodGroup);
        transferRadioButton.setToggleGroup(methodGroup);
        cashRadioButton.setSelected(true);
        
        // Hide insufficient funds label initially
        insufficientFundsLabel.setVisible(false);
        
        updateAccountInfo();
    }
    
    private void updateAccountInfo() {
        if (currentAccount != null) {
            availableBalanceLabel.setText(String.format("$%.2f", currentAccount.getBalance()));
            accountNumberLabel.setText("Account: " + currentAccount.getAccountNumber());
        } else {
            availableBalanceLabel.setText("$0.00");
            accountNumberLabel.setText("Account: ****1234");
        }
    }
    
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
        updateAccountInfo();
    }
    
    @FXML
    private void handleQuickAmount(javafx.event.ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonText = clickedButton.getText();
        String amount = buttonText.replace("$", "");
        amountField.setText(amount);
        checkInsufficientFunds();
    }
    
    @FXML
    private void handleWithdraw() {
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
            
            if (currentAccount != null) {
                double currentBalance = currentAccount.getBalance();
                if (amount > currentBalance) {
                    showAlert("Error", "Insufficient funds. Available balance: $" + String.format("%.2f", currentBalance));
                    return;
                }
                
                // Get withdrawal method
                String method = "Cash";
                if (checkRadioButton.isSelected()) {
                    method = "Check";
                } else if (transferRadioButton.isSelected()) {
                    method = "Bank Transfer";
                }
                
                // Process withdrawal
                currentAccount.withdraw(amount);
                showAlert("Success", "Withdrawal of $" + String.format("%.2f", amount) + " processed successfully!");
                
                // Clear fields
                amountField.clear();
                descriptionField.clear();
                insufficientFundsLabel.setVisible(false);
                
                // Update balance
                updateAccountInfo();
            } else {
                showAlert("Info", "Withdrawal would be processed. (Demo mode - no account connected)");
            }
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }
    
    private void checkInsufficientFunds() {
        try {
            String amountText = amountField.getText().trim();
            if (!amountText.isEmpty() && currentAccount != null) {
                double amount = Double.parseDouble(amountText);
                if (amount > currentAccount.getBalance()) {
                    insufficientFundsLabel.setText("Insufficient funds");
                    insufficientFundsLabel.setVisible(true);
                } else {
                    insufficientFundsLabel.setVisible(false);
                }
            }
        } catch (NumberFormatException e) {
            insufficientFundsLabel.setVisible(false);
        }
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

