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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        insufficientFundsLabel.setVisible(false);
        
        // Populate account combos (demo data)
        fromAccountComboBox.getItems().addAll("Account 1234", "Account 5678");
        toAccountComboBox.getItems().addAll("Account 5678", "Account 1234", "External Account");
        
        updateAccountInfo();
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
            
            if (fromAccount != null && amount > fromAccount.getBalance()) {
                showAlert("Error", "Insufficient funds in source account");
                return;
            }
            
            // Process transfer
            if (fromAccount != null && toAccount != null) {
                fromAccount.withdraw(amount);
                toAccount.deposit(amount, "Transfer from " + fromAccount.getAccountNumber());
                showAlert("Success", "Transfer of $" + String.format("%.2f", amount) + " completed successfully!");
                
                amountField.clear();
                descriptionField.clear();
                updateAccountInfo();
            } else {
                showAlert("Info", "Transfer would be processed. (Demo mode)");
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

