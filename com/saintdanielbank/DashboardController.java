package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import bank.BankService;
import bankapp.Customer;
import bankapp.Account;

public class DashboardController {
    @FXML
    public ListView<String> customerListView;
    @FXML
    public ListView<String> accountListView;

    private final BankService bankService;

    public DashboardController(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        refreshLists();
    }

    public void refreshLists() {
        try {
            customerListView.getItems().clear();
            List<Customer> customers = bankService.listCustomers();
            for (Customer c : customers) customerListView.getItems().add(c.getCustomerId() + " - " + c.getFullName());

            accountListView.getItems().clear();
            // Load accounts from database for all customers
            for (Customer c : customers) {
                try {
                    List<Account> accounts = bankService.listAccountsByCustomer(c.getCustomerId());
                    for (Account a : accounts) {
                        accountListView.getItems().add(a.getAccountNumber() + " - " + a.getClass().getSimpleName() + " ($" + String.format("%.2f", a.getBalance()) + ")");
                    }
                } catch (Exception e) {
                    System.err.println("Error loading accounts for customer " + c.getCustomerId() + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    public void onNewCustomer() {
        try {
            Stage s = (Stage) customerListView.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer_form.fxml"));
            loader.setControllerFactory(c -> new CustomerFormController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    public void onCreateAccount() {
        try {
            Stage s = (Stage) accountListView.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/account_create.fxml"));
            loader.setControllerFactory(c -> new AccountCreateController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    public void onRefresh() { refreshLists(); }

    @FXML
    public void onLogout() {
        // simple: exit app
        System.exit(0);
    }

    private void showError(String msg) {
        Alert a = new Alert(AlertType.ERROR, msg);
        a.showAndWait();
    }
}
