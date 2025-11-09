package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

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
    private TableView<?> transactionsTable;
    
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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate filter combo
        typeFilterComboBox.getItems().addAll("All Types", "Deposit", "Withdrawal", "Transfer", "Interest");
        typeFilterComboBox.setValue("All Types");
        
        transactionCountLabel.setText("(0 transactions)");
        pageInfoLabel.setText("Page 1 of 1");
    }
    
    @FXML
    private void handleApplyFilters() {
        showAlert("Info", "Filters applied. (Demo mode)");
    }
    
    @FXML
    private void handleClearFilters() {
        typeFilterComboBox.setValue("All Types");
        dateFromPicker.setValue(null);
        dateToPicker.setValue(null);
        searchField.clear();
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
    private void handleExport() {
        showAlert("Info", "Export to CSV feature coming soon!");
    }
    
    @FXML
    private void handlePrint() {
        showAlert("Info", "Print statement feature coming soon!");
    }
    
    @FXML
    private void handleRefresh() {
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

