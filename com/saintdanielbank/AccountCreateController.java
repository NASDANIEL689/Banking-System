package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import bank.BankService;
import bankapp.Customer;
import bankapp.Account;
import bankapp.IndividualCustomer;
import bankapp.CompanyCustomer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import bankapp.ChequeAccount;

public class AccountCreateController {
    @FXML public ChoiceBox<String> accountTypeChoice;
    @FXML public TextField accountNumberField;
    @FXML public VBox savingsFields;
    @FXML public TextField savingsDepositField;
    @FXML public VBox investmentFields;
    @FXML public TextField investmentDepositField;
    @FXML public VBox chequeFields;
    @FXML public Label employerLabel;
    @FXML public TextField employerField;
    @FXML public TextField chequeDepositField;

    private final BankService bankService;
    private Customer currentUser;

    public AccountCreateController(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        // Get current logged-in user
        currentUser = bankService.getLoginService().getCurrentUser();
        if (currentUser == null) {
            showError("You must be logged in to create an account");
            navigateToDashboard();
            return;
        }
        
        accountTypeChoice.getItems().addAll("Savings", "Investment", "Cheque");
        accountTypeChoice.setValue("Savings");
        
        // Show/hide fields based on account type
        accountTypeChoice.setOnAction(e -> updateFieldsVisibility());
        
        // Update employer label based on customer type
        if (currentUser instanceof IndividualCustomer) {
            employerLabel.setText("Employer Name (required for Individual customers):");
        } else if (currentUser instanceof CompanyCustomer) {
            employerLabel.setText("Employer Name (not required for Company customers):");
            employerField.setDisable(true);
        }
        
        updateFieldsVisibility();
    }

    private void updateFieldsVisibility() {
        String type = accountTypeChoice.getValue();
        savingsFields.setVisible("Savings".equals(type));
        investmentFields.setVisible("Investment".equals(type));
        chequeFields.setVisible("Cheque".equals(type));
    }

    @FXML
    public void onCreate() {
        try {
            String accNo = accountNumberField.getText().trim();
            if (accNo.isEmpty()) {
                showError("Please enter an account number");
                return;
            }
            
            String type = accountTypeChoice.getValue();
            
            if ("Savings".equals(type)) {
                double deposit = 0;
                if (!savingsDepositField.getText().trim().isEmpty()) {
                    deposit = Double.parseDouble(savingsDepositField.getText().trim());
                    if (deposit < 0) {
                        showError("Deposit amount cannot be negative");
                        return;
                    }
                }
                SavingsAccount s = new SavingsAccount(accNo, currentUser);
                bankService.openSavingsAccount(s);
                if (deposit > 0) {
                    bankService.deposit(s, deposit);
                }
                
            } else if ("Investment".equals(type)) {
                if (investmentDepositField.getText().trim().isEmpty()) {
                    showError("Investment account requires an initial deposit");
                    return;
                }
                double deposit = Double.parseDouble(investmentDepositField.getText().trim());
                if (deposit < 500) {
                    showError("Investment account requires minimum opening balance of P500");
                    return;
                }
                InvestmentAccount ia = new InvestmentAccount(accNo, currentUser, deposit);
                bankService.openInvestmentAccount(ia);
                
            } else if ("Cheque".equals(type)) {
                String employerName = null;
                if (currentUser instanceof IndividualCustomer) {
                    IndividualCustomer ic = (IndividualCustomer) currentUser;
                    if (!ic.isEmployed()) {
                        if (employerField.getText().trim().isEmpty()) {
                            showError("Individual customers must provide employer name for Cheque accounts");
                            return;
                        }
                        ic.setEmployment(employerField.getText().trim());
                        // Update customer in database - we'll need to update, not create
                        try {
                            bank.dao.CustomerDAO cdao = new bank.dao.CustomerDAO();
                            cdao.update(ic);
                        } catch (Exception ex) {
                            System.err.println("Warning: Could not update customer employment: " + ex.getMessage());
                        }
                    }
                    employerName = ((IndividualCustomer) currentUser).getEmployerName();
                }
                // Company customers don't need employer name
                
                ChequeAccount ca = new ChequeAccount(accNo, currentUser, employerName);
                bankService.openChequeAccount(ca);
                
                double deposit = 0;
                if (!chequeDepositField.getText().trim().isEmpty()) {
                    deposit = Double.parseDouble(chequeDepositField.getText().trim());
                    if (deposit > 0) {
                        bankService.deposit(ca, deposit);
                    }
                }
            }
            
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText(null);
            success.setContentText("Account created successfully!");
            success.showAndWait();
            
            navigateToDashboard();
        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for deposit amounts");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error creating account: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onCancel() {
        navigateToDashboard();
    }

    private void navigateToDashboard() {
        try {
            Stage s = (Stage) accountNumberField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            loader.setControllerFactory(cl -> new DashboardController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) {
            showError("Error navigating to dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }
}

