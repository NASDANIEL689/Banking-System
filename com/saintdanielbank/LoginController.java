package com.saintdanielbank;

import java.lang.reflect.Method;
import java.util.Objects;
import bank.BankService;

public class LoginController {
    // These will be injected by FXMLLoader (as java objects) without importing JavaFX types to keep compilation in headless env
    public Object usernameField;
    public Object passwordField;
    public Object loginButton;
    public Object registerLink;

    private final BankService bankService;

    public LoginController(BankService bankService) {
        this.bankService = Objects.requireNonNull(bankService);
    }

    // Called by FXML when login button is pressed
    public void onLogin() {
        try {
            String username = getTextFromField(usernameField);
            String password = getTextFromField(passwordField);
            System.out.println("DEBUG: Attempting login with username='" + username + "', password length=" + (password != null ? password.length() : 0));
            boolean ok = bankService.getLoginService().login(username, password);
            System.out.println("Login attempted for " + username + ", success=" + ok);
            if (!ok) {
                // Show error message to user
                try {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid username or password. Try: username='demo', password='demo'");
                    alert.showAndWait();
                } catch (Exception e) {
                    System.err.println("Could not show alert: " + e.getMessage());
                }
            } else {
                // Navigate to dashboard on successful login
                try {
                    javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/dashboard.fxml"));
                    loader.setControllerFactory(c -> new com.saintdanielbank.DashboardController(bankService));
                    javafx.scene.Parent root = loader.load();
                    javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) loginButton).getScene().getWindow();
                    stage.setScene(new javafx.scene.Scene(root));
                    stage.setTitle("Saint Daniel Bank - Dashboard");
                } catch (Exception e) {
                    System.err.println("Error navigating to dashboard: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRegister() {
        try {
            // Navigate to register choice screen
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/RegisterChoiceView.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) registerLink).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.setTitle("Saint Daniel Bank - Register");
        } catch (Exception e) {
            System.err.println("Error navigating to registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getTextFromField(Object field) throws Exception {
        if (field == null) return "";
        // Use reflection to call getText() on JavaFX TextField/PasswordField if present
        Method m = field.getClass().getMethod("getText");
        Object val = m.invoke(field);
        return val != null ? val.toString() : "";
    }
}
