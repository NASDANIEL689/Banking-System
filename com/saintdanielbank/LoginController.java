package com.saintdanielbank;

import java.lang.reflect.Method;

import java.util.Objects;

import BankService;

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
            boolean ok = bankService.getLoginService().login(username, password);
            System.out.println("Login attempted for " + username + ", success=" + ok);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onRegister() {
        System.out.println("Register link clicked - implement registration flow (not wired here)");
    }

    private String getTextFromField(Object field) throws Exception {
        if (field == null) return "";
        // Use reflection to call getText() on JavaFX TextField/PasswordField if present
        Method m = field.getClass().getMethod("getText");
        Object val = m.invoke(field);
        return val != null ? val.toString() : "";
    }
}
