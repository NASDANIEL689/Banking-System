package com.saintdanielbank;

package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

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
                SavingsAccount s = new SavingsAccount(accNo, c);
                bankService.openSavingsAccount(s);
            } else if ("Investment".equals(type)) {
                InvestmentAccount ia = new InvestmentAccount(accNo, c, initial);
                bankService.openSavingsAccount(ia); // reuse openSavingsAccount for persistence path
            } else if ("Cheque".equals(type)) {
                String emp = null;
                if (c instanceof IndividualCustomer) emp = ((IndividualCustomer)c).getEmployerName();
                ChequeAccount ca = new ChequeAccount(accNo, c, emp);
                bankService.openSavingsAccount(ca);
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
