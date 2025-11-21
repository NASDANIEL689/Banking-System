package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import bankapp.Customer;
import bankapp.Account;
import bankapp.PersonalCustomer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import bankapp.ChequeAccount;
import bank.BankService;
import bankapp.Transaction;
import bank.dao.TransactionDAO;

public class AccountCreateController {
    @FXML public TextField customerIdField;
    @FXML public TextField accountNumberField;
    @FXML public ChoiceBox<String> accountTypeChoice;
    @FXML public TextField initialDepositField;

    private final BankService bankService;

    public AccountCreateController(BankService bankService) { this.bankService = bankService; }

    @FXML
    public void initialize() { accountTypeChoice.getItems().addAll("Savings","Investment","Cheque"); accountTypeChoice.setValue("Savings"); }

    @FXML
    public void onCreate() {
        try {
            String custId = customerIdField.getText();
            Customer c = bankService.findCustomer(custId);
            if (c == null) throw new IllegalArgumentException("Customer not found: " + custId);
            String accNo = accountNumberField.getText();
            String type = accountTypeChoice.getValue();
            double initial = Double.parseDouble(initialDepositField.getText());
            if ("Savings".equals(type)) {
                SavingsAccount s = new SavingsAccount(accNo, (PersonalCustomer)c);
                bankService.openSavingsAccount(s);
                if (initial > 0) {
                    bankService.deposit(s, initial);
                }
            } else if ("Investment".equals(type)) {
                InvestmentAccount ia = new InvestmentAccount(accNo, c, initial);
                bankService.openInvestmentAccount(ia);
                // InvestmentAccount constructor already deposits initial amount
                // Save the transaction that was created
                if (initial > 0 && !ia.getTransactions().isEmpty()) {
                    try {
                        TransactionDAO tdao = new TransactionDAO();
                        Transaction initialTx = ia.getTransactions().get(0);
                        tdao.addTransaction(initialTx, ia.getBalance());
                    } catch (Exception ex) {
                        System.err.println("Error saving initial deposit transaction: " + ex.getMessage());
                    }
                }
            } else if ("Cheque".equals(type)) {
                ChequeAccount ca = new ChequeAccount(accNo, c, ((PersonalCustomer)c).getEmployerName());
                bankService.openChequeAccount(ca);
                if (initial > 0) {
                    bankService.deposit(ca, initial);
                }
            }
            // navigate back to dashboard
            Stage s = (Stage) accountNumberField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            loader.setControllerFactory(cl -> new DashboardController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML public void onCancel() {
        try {
            Stage s = (Stage) accountNumberField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            loader.setControllerFactory(cl -> new DashboardController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait(); }
    }
}
