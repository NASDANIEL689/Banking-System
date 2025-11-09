package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

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
    private TableView<?> historyTable;
    
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate filter combos
        typeFilterComboBox.getItems().addAll("All Types", "Deposit", "Withdrawal", "Transfer", "Interest");
        typeFilterComboBox.setValue("All Types");
        
        statusFilterComboBox.getItems().addAll("All Status", "Completed", "Pending", "Failed");
        statusFilterComboBox.setValue("All Status");
        
        // Initialize summary labels
        totalTransactionsLabel.setText("0");
        totalDepositsLabel.setText("$0.00");
        totalWithdrawalsLabel.setText("$0.00");
        netBalanceLabel.setText("$0.00");
        filteredCountLabel.setText("(0 transactions)");
        pageInfoLabel.setText("Page 1 of 1");
    }
    
    @FXML
    private void handleApplyFilters() {
        showAlert("Info", "Filters applied. (Demo mode)");
    }
    
    @FXML
    private void handleClearFilters() {
        typeFilterComboBox.setValue("All Types");
        statusFilterComboBox.setValue("All Status");
        minAmountField.clear();
        maxAmountField.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }
    
    @FXML
    private void handlePreviousPage() {
        showAlert("Info", "Previous page");
    }
    
    @FXML
    private void handleNextPage() {
        showAlert("Info", "Next page");
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

