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
            // simple listing via AccountDAO would be better; for now show accounts for first customer
            if (!customers.isEmpty()) {
                Customer first = customers.get(0);
                for (Account a : first.getAccounts()) accountListView.getItems().add(a.getAccountNumber() + " - " + a.getClass().getSimpleName());
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
