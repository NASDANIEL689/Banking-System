package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import bank.BankService;
import bankapp.Account;

public class AccountDetailController {
    @FXML public Label accountNumberLabel;
    @FXML public Label ownerLabel;
    @FXML public Label balanceLabel;
    @FXML public TextField amountField;

    private final BankService bankService;
    private Account account;

    public AccountDetailController(BankService bankService) { this.bankService = bankService; }

    public void setAccount(Account a) {
        this.account = a;
        accountNumberLabel.setText(a.getAccountNumber());
        ownerLabel.setText(a.getCustomer().getFullName());
        balanceLabel.setText(String.valueOf(a.getBalance()));
    }

    @FXML
    public void onDeposit() {
        try {
            double amt = Double.parseDouble(amountField.getText());
            bankService.deposit(account, amt);
            balanceLabel.setText(String.valueOf(account.getBalance()));
        } catch (Exception e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
    }

    @FXML
    public void onWithdraw() {
        try {
            double amt = Double.parseDouble(amountField.getText());
            // Use BankService to persist to database
            bankService.withdraw(account, amt);
            balanceLabel.setText(String.valueOf(account.getBalance()));
            amountField.clear();
        } catch (Exception e) { 
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); 
        }
    }
}
