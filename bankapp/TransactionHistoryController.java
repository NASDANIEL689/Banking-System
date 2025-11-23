package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

// Controller for the transaction history view
public class TransactionHistoryController implements Initializable {
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label totalTransactionsLabel;
    
    @FXML
    private Label totalDepositsLabel;
    
    @FXML
    private Label totalWithdrawalsLabel;
    
    @FXML
    private Label netBalanceLabel;
    
    @FXML
    private ComboBox<String> typeFilterComboBox;
    
    @FXML
    private TextField minAmountField;
    
    @FXML
    private TextField maxAmountField;
    
    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private ComboBox<String> statusFilterComboBox;
    
    @FXML
    private Button applyFiltersButton;
    
    @FXML
    private TableView<Transaction> historyTable;
    
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    
    @FXML
    private Label filteredCountLabel;
    
    @FXML
    private Button previousPageButton;
    
    @FXML
    private Button nextPageButton;
    
    @FXML
    private Label pageInfoLabel;
    
    @FXML
    private Button exportPdfButton;
    
    @FXML
    private Button exportExcelButton;
    
    @FXML
    private Button printButton;
    
    @FXML
    private Button clearFiltersButton;
    
    private bank.BankService bankService = bank.BankService.getInstance();
    private Account currentAccount;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate filter combos
        typeFilterComboBox.getItems().addAll("All Types", "Deposit", "Withdrawal", "Transfer", "Interest");
        typeFilterComboBox.setValue("All Types");
        
        statusFilterComboBox.getItems().addAll("All Status", "Completed", "Pending", "Failed");
        statusFilterComboBox.setValue("All Status");
        
        // Initialize table columns if they exist
        if (historyTable != null) {
            if (dateColumn != null) {
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
            }
            if (typeColumn != null) {
                typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            }
            if (amountColumn != null) {
                amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            }
        }
        
        loadTransactionHistory();
    }
    
    // Load transaction history from database
    private void loadTransactionHistory() {
        try {
            List<Customer> customers = bankService.listCustomers();
            historyTable.getItems().clear();
            
            int totalCount = 0;
            double totalDeposits = 0.0;
            double totalWithdrawals = 0.0;
            
            for (Customer c : customers) {
                List<Account> accounts = bankService.listAccountsByCustomer(c.getCustomerID());
                for (Account a : accounts) {
                    List<Transaction> transactions = bankService.getTransactionsByAccount(a.getAccountNumber());
                    for (Transaction t : transactions) {
                        historyTable.getItems().add(t);
                        totalCount++;
                        if (t.getType().contains("DEPOSIT")) {
                            totalDeposits += t.getAmount();
                        } else if (t.getType().contains("WITHDRAW")) {
                            totalWithdrawals += t.getAmount();
                        }
                    }
                }
            }
            
            totalTransactionsLabel.setText(String.valueOf(totalCount));
            totalDepositsLabel.setText("$" + String.format("%.2f", totalDeposits));
            totalWithdrawalsLabel.setText("$" + String.format("%.2f", totalWithdrawals));
            netBalanceLabel.setText("$" + String.format("%.2f", totalDeposits - totalWithdrawals));
            filteredCountLabel.setText("(" + totalCount + " transactions)");
            pageInfoLabel.setText("Page 1 of 1");
        } catch (Exception e) {
            System.err.println("Error loading transaction history: " + e.getMessage());
            totalTransactionsLabel.setText("0");
            totalDepositsLabel.setText("$0.00");
            totalWithdrawalsLabel.setText("$0.00");
            netBalanceLabel.setText("$0.00");
            filteredCountLabel.setText("(0 transactions)");
        }
    }
    
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
        loadTransactionHistory();
    }
    
    @FXML
    private void handleApplyFilters() {
        // Apply filters to the table
        String filterType = typeFilterComboBox.getValue();
        if (filterType != null && !filterType.equals("All Types")) {
            historyTable.getItems().removeIf(t -> !t.getType().toUpperCase().contains(filterType.toUpperCase()));
        }
        filteredCountLabel.setText("(" + historyTable.getItems().size() + " transactions)");
    }
    
    @FXML
    private void handleClearFilters() {
        typeFilterComboBox.setValue("All Types");
        statusFilterComboBox.setValue("All Status");
        minAmountField.clear();
        maxAmountField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        loadTransactionHistory(); // Reload all transactions
    }
    
    @FXML
    private void handlePreviousPage() {
        showAlert("Info", "Pagination not yet implemented");
    }
    
    @FXML
    private void handleNextPage() {
        showAlert("Info", "Pagination not yet implemented");
    }
    
    @FXML
    private void handleExportPdf() {
        showAlert("Info", "Export to PDF feature coming soon!");
    }
    
    @FXML
    private void handleExportExcel() {
        showAlert("Info", "Export to Excel feature coming soon!");
    }
    
    @FXML
    private void handlePrint() {
        showAlert("Info", "Print statement feature coming soon!");
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

