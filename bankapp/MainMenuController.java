package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

// Controller for the main menu view
public class MainMenuController implements Initializable {
    
    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Label balanceLabel;
    
    @FXML
    private Label accountNumberLabel;
    
    @FXML
    private Button depositButton;
    
    @FXML
    private Button withdrawButton;
    
    @FXML
    private Button transferButton;
    
    @FXML
    private Button payBillsButton;
    
    @FXML
    private Button viewTransactionsButton;
    
    @FXML
    private Button transactionHistoryButton;
    
    @FXML
    private Button accountSettingsButton;
    
    @FXML
    private Button helpButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button viewAllTransactionsButton;
    
    @FXML
    private TableView<Transaction> recentTransactionsTable;
    
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    
    // Current account (would be set from login)
    private Account currentAccount;
    private bank.BankService bankService = bank.BankService.getInstance();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the view
        updateAccountInfo();
        loadRecentTransactions();
    }
    
    // Load recent transactions from database
    private void loadRecentTransactions() {
        if (currentAccount != null && recentTransactionsTable != null) {
            try {
                List<Transaction> transactions = bankService.getTransactionsByAccount(currentAccount.getAccountNumber());
                // Limit to recent 5 transactions
                recentTransactionsTable.getItems().clear();
                int count = 0;
                for (Transaction t : transactions) {
                    if (count < 5) {
                        recentTransactionsTable.getItems().add(t);
                        count++;
                    } else {
                        break;
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
            balanceLabel.setText(String.format("$%.2f", currentAccount.getBalance()));
            accountNumberLabel.setText("Account: " + currentAccount.getAccountNumber());
        } else {
            // Default values for demo
            balanceLabel.setText("$0.00");
            accountNumberLabel.setText("Account: ****1234");
        }
    }
    
    // Set the current account
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
        updateAccountInfo();
    }
    
    // Navigation handlers
    @FXML
    private void handleDeposit() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(depositButton), "DepositView.fxml");
    }
    
    @FXML
    private void handleWithdraw() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(withdrawButton), "WithdrawView.fxml");
    }
    
    @FXML
    private void handleTransfer() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(transferButton), "TransferView.fxml");
    }
    
    @FXML
    private void handlePayBills() {
        showAlert("Info", "Pay Bills feature coming soon!");
    }
    
    @FXML
    private void handleViewTransactions() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(viewTransactionsButton), "TransactionsView.fxml");
    }
    
    @FXML
    private void handleTransactionHistory() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(transactionHistoryButton), "TransactionHistoryView.fxml");
    }
    
    @FXML
    private void handleAccountSettings() {
        showAlert("Info", "Account Settings feature coming soon!");
    }
    
    @FXML
    private void handleHelp() {
        showAlert("Help", "For assistance, please contact customer support at support@saintdanielbank.com");
    }
    
    @FXML
    private void handleLogout() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(logoutButton), "LoginView.fxml");
    }
    
    @FXML
    private void handleViewAllTransactions() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(viewAllTransactionsButton), "TransactionsView.fxml");
    }
    
    // Helper method to show alerts
    private void showAlert(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

