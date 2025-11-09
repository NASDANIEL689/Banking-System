package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the login view
public class LoginController implements Initializable {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Hyperlink registerLink;
    
    private LoginService loginService = new LoginService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize controller - add default user for demo
        // In a real application, this would be loaded from a database
    }

    // Handle login button click
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username == null || username.trim().isEmpty()) {
            showAlert("Error", "Please enter a username");
            return;
        }
        
        if (password == null || password.trim().isEmpty()) {
            showAlert("Error", "Please enter a password");
            return;
        }
        
        boolean success = loginService.login(username, password);
        
        if (success) {
            showAlert("Success", "Login successful!");
            NavigationHelper.navigateTo(NavigationHelper.getStage(loginButton), "MainMenuView.fxml");
        } else {
            showAlert("Error", "Invalid username or password");
        }
    }
    
    // Handle register link click
    @FXML
    private void handleRegister() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(registerLink), "SignUpView.fxml");
    }
    
    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Keep the original methods for backward compatibility
    public boolean login(String username, String password) {
        return loginService.login(username, password);
    }

    public void logout(String username) {
        loginService.logout(username);
    }
}
