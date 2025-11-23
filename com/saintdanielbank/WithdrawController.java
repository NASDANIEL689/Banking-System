package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import bank.BankService;
import bankapp.Account;
import bankapp.Withdrawable;
import bankapp.SavingsAccount;

public class WithdrawController {
    @FXML
    private Label accountNumberLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private TextField amountField;

    private final BankService bankService;
    private final Account account;

    public WithdrawController(BankService bankService, Account account) {
        this.bankService = bankService;
        this.account = account;
    }

    @FXML
    public void initialize() {
        accountNumberLabel.setText(account.getAccountNumber());
        balanceLabel.setText("P" + String.format("%.2f", account.getBalance()));
        
        // Check if withdrawals are allowed
        if (account instanceof SavingsAccount) {
            showError("Savings account does not allow withdrawals.");
            amountField.setDisable(true);
        }
    }

    @FXML
    public void onConfirmWithdraw() {
        try {
            if (account instanceof SavingsAccount) {
                showError("Savings account does not allow withdrawals.");
                return;
            }
            
            if (!(account instanceof Withdrawable)) {
                showError("This account type does not allow withdrawals.");
                return;
            }
            
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                showError("Please enter an amount");
                return;
            }
            
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                showError("Amount must be greater than zero");
                return;
            }
            
            if (amount > account.getBalance()) {
                showError("Insufficient funds. Available balance: P" + String.format("%.2f", account.getBalance()));
                return;
            }
            
            bankService.withdraw(account, amount);
            
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText(null);
            success.setContentText("Withdrawal of P" + String.format("%.2f", amount) + " successful!");
            success.showAndWait();
            
            navigateToDashboard();
        } catch (NumberFormatException e) {
            showError("Please enter a valid number");
        } catch (Exception e) {
            showError("Error processing withdrawal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel() {
        navigateToDashboard();
    }

    private void navigateToDashboard() {
        try {
            Stage stage = (Stage) amountField.getScene().getWindow();
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
}


