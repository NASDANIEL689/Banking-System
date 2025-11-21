package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import bankapp.PersonalCustomer;

public class CustomerFormController {
    @FXML public TextField customerIdField;
    @FXML public TextField fullNameField;
    @FXML public TextArea addressField;
    @FXML public TextField phoneField;
    @FXML public TextField emailField;
    @FXML public ChoiceBox<String> typeChoice;
    @FXML public TextField personalIdField;
    @FXML public CheckBox employedCheckBox;
    @FXML public TextField employerField;

    private final BankService bankService;

    public CustomerFormController(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        typeChoice.getItems().addAll("Personal","Business");
        typeChoice.setValue("Personal");
    }

    @FXML
    public void onSave() {
        try {
            String id = customerIdField.getText();
            String name = fullNameField.getText();
            PersonalCustomer p = new PersonalCustomer(id, name, addressField.getText(), phoneField.getText(), emailField.getText(), personalIdField.getText());
            if (employedCheckBox.isSelected()) p.setEmployment(employerField.getText());
            bankService.createPersonalCustomer(p);
            // go back to dashboard
            Stage s = (Stage) customerIdField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            loader.setControllerFactory(c -> new DashboardController(bankService));
            Parent proot = loader.load();
            s.getScene().setRoot(proot);
        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onCancel() {
        try {
            Stage s = (Stage) customerIdField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            loader.setControllerFactory(c -> new DashboardController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) { new Alert(AlertType.ERROR,e.getMessage()).showAndWait(); }
    }
}
