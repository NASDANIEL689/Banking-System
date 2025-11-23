package com.saintdanielbank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import bank.BankService;
import bankapp.Customer;
import bankapp.Account;
import bankapp.IndividualCustomer;
import bankapp.CompanyCustomer;
import bankapp.SavingsAccount;
import bankapp.InvestmentAccount;
import bankapp.ChequeAccount;
import bankapp.Withdrawable;

public class DashboardController {
    @FXML
    public Label welcomeLabel;
    @FXML
    public Label totalBalanceLabel;
    @FXML
    public VBox accountsContainer;

    private final BankService bankService;
    private Customer currentUser;

    public DashboardController(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    public void initialize() {
        try {
            // Get current logged-in user
            currentUser = bankService.getLoginService().getCurrentUser();
            if (currentUser == null) {
                // If no user, go back to login
                navigateToLogin();
                return;
            }
            
            // Set welcome message
            String username = currentUser.getUsername();
            String displayName = currentUser instanceof IndividualCustomer ? 
                ((IndividualCustomer)currentUser).getFirstname() + " " + ((IndividualCustomer)currentUser).getSurname() :
                currentUser instanceof CompanyCustomer ? 
                ((CompanyCustomer)currentUser).getCompanyName() : username;
            welcomeLabel.setText("Welcome, " + displayName);
            
            // Load user's accounts
            loadUserAccounts();
        } catch (Exception e) {
            showError("Error initializing dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUserAccounts() {
        try {
            accountsContainer.getChildren().clear();
            
            List<Account> accounts = bankService.listAccountsByCustomer(currentUser.getCustomerID());
            updateTotalBalanceLabel(accounts);
            
            if (accounts.isEmpty()) {
                Label noAccountsLabel = new Label("You don't have any accounts yet. Click 'Create New Account' to get started.");
                noAccountsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");
                accountsContainer.getChildren().add(noAccountsLabel);
                return;
            }
            
            for (Account account : accounts) {
                VBox accountCard = createAccountCard(account);
                accountsContainer.getChildren().add(accountCard);
            }
        } catch (Exception e) {
            showError("Error loading accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createAccountCard(Account account) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        // Account Number
        Label accountNumberLabel = new Label("Account Number: " + account.getAccountNumber());
        accountNumberLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Account Type
        String accountType = account.getClass().getSimpleName().replace("Account", "");
        Label accountTypeLabel = new Label("Account Type: " + accountType);
        accountTypeLabel.setStyle("-fx-font-size: 14px;");
        
        // Balance
        Label balanceLabel = new Label("Balance: P" + String.format("%.2f", account.getBalance()));
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1E3C72;");
        
        // Buttons
        HBox buttonBox = new HBox(10);
        Button depositButton = new Button("Deposit");
        depositButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        depositButton.setOnAction(e -> onDeposit(account));
        
        Button withdrawButton = new Button("Withdraw");
        // Enable withdraw only if account implements Withdrawable
        boolean canWithdraw = account instanceof Withdrawable;
        withdrawButton.setDisable(!canWithdraw);
        if (canWithdraw) {
            withdrawButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        } else {
            withdrawButton.setStyle("-fx-background-color: #ccc; -fx-text-fill: #666;");
            withdrawButton.setTooltip(new Tooltip("Savings account does not allow withdrawals."));
        }
        withdrawButton.setOnAction(e -> onWithdraw(account));
        
        Button viewStatementButton = new Button("View Statement");
        viewStatementButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        viewStatementButton.setOnAction(e -> onViewStatement(account));
        
        buttonBox.getChildren().addAll(depositButton, withdrawButton, viewStatementButton);
        
        card.getChildren().addAll(accountNumberLabel, accountTypeLabel, balanceLabel, buttonBox);
        
        return card;
    }

    @FXML
    public void onCreateAccount() {
        try {
            Stage s = (Stage) accountsContainer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/account_create.fxml"));
            loader.setControllerFactory(c -> new AccountCreateController(bankService));
            Parent p = loader.load();
            s.getScene().setRoot(p);
        } catch (Exception e) {
            showError("Error opening account creation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void onDeposit(Account account) {
        try {
            Stage stage = (Stage) accountsContainer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deposit.fxml"));
            DepositController controller = new DepositController(bankService, account);
            loader.setController(controller);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Deposit - " + account.getAccountNumber());
        } catch (Exception e) {
            showError("Error opening deposit screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void onWithdraw(Account account) {
        if (!(account instanceof Withdrawable)) {
            showError("Savings account does not allow withdrawals.");
            return;
        }
        try {
            Stage stage = (Stage) accountsContainer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/withdraw.fxml"));
            WithdrawController controller = new WithdrawController(bankService, account);
            loader.setController(controller);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Withdraw - " + account.getAccountNumber());
        } catch (Exception e) {
            showError("Error opening withdrawal screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void onViewStatement(Account account) {
        try {
            Stage stage = (Stage) accountsContainer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/statement.fxml"));
            StatementController controller = new StatementController(bankService, account);
            loader.setController(controller);
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Statement - " + account.getAccountNumber());
        } catch (Exception e) {
            showError("Error opening statement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogout() {
        try {
            bankService.getLoginService().logout(null);
            navigateToLogin();
        } catch (Exception e) {
            showError("Error during logout: " + e.getMessage());
        }
    }

    private void navigateToLogin() {
        try {
            Stage stage = (Stage) accountsContainer.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register_choice.fxml"));
            loader.setController(new LoginController(bankService));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
            stage.setTitle("Saint Daniel Bank - Login");
        } catch (Exception e) {
            System.err.println("Error navigating to login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.showAndWait();
    }

    private void updateTotalBalanceLabel(List<Account> accounts) {
        double total = 0.0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        if (totalBalanceLabel != null) {
            totalBalanceLabel.setText("Total Balance: P" + String.format("%.2f", total));
        }
    }

    @FXML
    public void onTransferBetweenAccounts() {
        try {
            List<Account> accounts = bankService.listAccountsByCustomer(currentUser.getCustomerID());
            if (accounts.size() < 2) {
                showError("You need at least two accounts to transfer between.");
                return;
            }

            Account fromAccountSelection = promptForAccount("Select Source Account", accounts, null);
            if (fromAccountSelection == null) {
                return;
            }

            Account toAccountSelection = promptForAccount("Select Destination Account", accounts, fromAccountSelection);
            if (toAccountSelection == null) {
                return;
            }

            TextInputDialog amountDialog = new TextInputDialog();
            amountDialog.setTitle("Transfer Amount");
            amountDialog.setHeaderText("Enter amount to transfer from " + fromAccountSelection.getAccountNumber() + " to " + toAccountSelection.getAccountNumber());
            amountDialog.setContentText("Amount (P):");
            Optional<String> amountResult = amountDialog.showAndWait();
            if (!amountResult.isPresent()) {
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountResult.get().trim());
            } catch (NumberFormatException ex) {
                showError("Please enter a valid number for the amount.");
                return;
            }

            if (amount <= 0) {
                showError("Amount must be greater than zero.");
                return;
            }

            if (amount > fromAccountSelection.getBalance()) {
                showError("Insufficient funds in the source account.");
                return;
            }

            bankService.transfer(fromAccountSelection, toAccountSelection, amount);

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setTitle("Success");
            success.setHeaderText(null);
            success.setContentText("Transferred P" + String.format("%.2f", amount) + " successfully.");
            success.showAndWait();

            loadUserAccounts();
        } catch (Exception e) {
            showError("Error during transfer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Account promptForAccount(String title, List<Account> accounts, Account excludeAccount) {
        List<String> options = new ArrayList<>();
        Map<String, Account> accountMap = new HashMap<>();

        for (Account account : accounts) {
            if (excludeAccount != null && account.getAccountNumber().equals(excludeAccount.getAccountNumber())) {
                continue;
            }
            String display = account.getAccountNumber() + " (" + account.getClass().getSimpleName().replace("Account", "") + ") - P" + String.format("%.2f", account.getBalance());
            options.add(display);
            accountMap.put(display, account);
        }

        if (options.isEmpty()) {
            return null;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Choose account:");
        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return null;
        }
        return accountMap.get(result.get());
    }
}
