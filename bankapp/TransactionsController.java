package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

// Controller for the transactions view
public class TransactionsController implements Initializable {
    
    @FXML
    private Button backButton;
    
    @FXML
    private ComboBox<String> typeFilterComboBox;
    
    @FXML
    private DatePicker dateFromPicker;
    
    @FXML
    private DatePicker dateToPicker;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private Button applyFiltersButton;
    
    @FXML
    private Button clearFiltersButton;
    
    @FXML
    private TableView<Transaction> transactionsTable;
    
    @FXML
    private TableColumn<Transaction, String> dateColumn;
    
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    
    @FXML
    private Label transactionCountLabel;
    
    @FXML
    private Button previousPageButton;
    
    @FXML
    private Button nextPageButton;
    
    @FXML
    private Label pageInfoLabel;
    
    @FXML
    private Button exportButton;
    
    @FXML
    private Button printButton;
    
    @FXML
    private Button refreshButton;
    
    private bank.BankService bankService = bank.BankService.getInstance();
    private Account currentAccount;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate filter combo
        typeFilterComboBox.getItems().addAll("All Types", "Deposit", "Withdrawal", "Transfer", "Interest");
        typeFilterComboBox.setValue("All Types");
        
        // Initialize table columns if they exist
        if (transactionsTable != null) {
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
        
        loadTransactions();
    }
    
    // Load transactions from database
    private void loadTransactions() {
        try {
            // Load all transactions from all accounts
            List<Customer> customers = bankService.listCustomers();
            transactionsTable.getItems().clear();
            int totalCount = 0;
            
            for (Customer c : customers) {
                List<Account> accounts = bankService.listAccountsByCustomer(c.getCustomerId());
                for (Account a : accounts) {
                    List<Transaction> transactions = bankService.getTransactionsByAccount(a.getAccountNumber());
                    for (Transaction t : transactions) {
                        transactionsTable.getItems().add(t);
                        totalCount++;
                    }
                }
            }
            
            transactionCountLabel.setText("(" + totalCount + " transactions)");
            pageInfoLabel.setText("Page 1 of 1");
        } catch (Exception e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            transactionCountLabel.setText("(0 transactions)");
        }
    }
    
    public void setCurrentAccount(Account account) {
        this.currentAccount = account;
        loadTransactions();
    }
    
    @FXML
    private void handleApplyFilters() {
        // Apply filters to the table
        String filterType = typeFilterComboBox.getValue();
        if (filterType != null && !filterType.equals("All Types")) {
            // Filter by type
            transactionsTable.getItems().removeIf(t -> !t.getType().toUpperCase().contains(filterType.toUpperCase()));
        }
        transactionCountLabel.setText("(" + transactionsTable.getItems().size() + " transactions)");
    }
    
    @FXML
    private void handleClearFilters() {
        typeFilterComboBox.setValue("All Types");
        dateFromPicker.setValue(null);
        dateToPicker.setValue(null);
        searchField.clear();
        loadTransactions(); // Reload all transactions
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
    private void handleExport() {
        showAlert("Info", "Export to CSV feature coming soon!");
    }
    
    @FXML
    private void handlePrint() {
        showAlert("Info", "Print statement feature coming soon!");
    }
    
    @FXML
    private void handleRefresh() {
        loadTransactions();
        showAlert("Info", "Transactions refreshed");
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

