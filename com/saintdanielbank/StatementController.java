package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import bank.BankService;
import bankapp.Account;
import bankapp.Transaction;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatementController {
    @FXML
    private Label accountNumberLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private TableView<TransactionRow> transactionsTable;
    @FXML
    private TableColumn<TransactionRow, String> dateColumn;
    @FXML
    private TableColumn<TransactionRow, String> typeColumn;
    @FXML
    private TableColumn<TransactionRow, Double> amountColumn;
    @FXML
    private TableColumn<TransactionRow, Double> balanceColumn;

    private final BankService bankService;
    private final Account account;
    private ObservableList<TransactionRow> transactionData = FXCollections.observableArrayList();

    public StatementController(BankService bankService, Account account) {
        this.bankService = bankService;
        this.account = account;
    }

    @FXML
    public void initialize() {
        accountNumberLabel.setText(account.getAccountNumber());
        balanceLabel.setText("P" + String.format("%.2f", account.getBalance()));
        
        // Set up table columns
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        balanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());
        
        // Format amount and balance columns
        amountColumn.setCellFactory(column -> new TableCell<TransactionRow, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText("P" + String.format("%.2f", amount));
                }
            }
        });
        
        balanceColumn.setCellFactory(column -> new TableCell<TransactionRow, Double>() {
            @Override
            protected void updateItem(Double balance, boolean empty) {
                super.updateItem(balance, empty);
                if (empty || balance == null) {
                    setText(null);
                } else {
                    setText("P" + String.format("%.2f", balance));
                }
            }
        });
        
        loadTransactions();
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = bankService.getTransactionsByAccount(account.getAccountNumber());
            transactionData.clear();
            
            // Sort transactions by date (oldest first)
            transactions.sort((t1, t2) -> t1.getDateTime().compareTo(t2.getDateTime()));
            
            double runningBalance = 0; // Start from zero
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
            
            // Process transactions in chronological order
            for (Transaction t : transactions) {
                String type = t.getType();
                double amount = t.getAmount();
                
                // Update balance based on transaction type
                if (type.equals("deposit") || type.equals("interest")) {
                    runningBalance += amount; // Add for deposits and interest
                } else if (type.equals("withdrawal")) {
                    runningBalance -= amount; // Subtract for withdrawals
                }
                
                TransactionRow row = new TransactionRow(
                    t.getDateTime().format(formatter),
                    type,
                    amount,
                    runningBalance
                );
                transactionData.add(row);
            }
            
            transactionsTable.setItems(transactionData);
        } catch (Exception e) {
            showError("Error loading transactions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onBack() {
        try {
            Stage stage = (Stage) transactionsTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            loader.setControllerFactory(c -> new DashboardController(bankService));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Saint Daniel Bank - Dashboard");
        } catch (Exception e) {
            System.err.println("Error navigating to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }

    // Inner class for table rows
    public static class TransactionRow {
        private final SimpleStringProperty date;
        private final SimpleStringProperty type;
        private final SimpleDoubleProperty amount;
        private final SimpleDoubleProperty balance;

        public TransactionRow(String date, String type, double amount, double balance) {
            this.date = new SimpleStringProperty(date);
            this.type = new SimpleStringProperty(type);
            this.amount = new SimpleDoubleProperty(amount);
            this.balance = new SimpleDoubleProperty(balance);
        }

        public SimpleStringProperty dateProperty() { return date; }
        public SimpleStringProperty typeProperty() { return type; }
        public SimpleDoubleProperty amountProperty() { return amount; }
        public SimpleDoubleProperty balanceProperty() { return balance; }
    }
}

