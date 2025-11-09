package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the sign in view
public class SignInController implements Initializable {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private CheckBox rememberMeCheckbox;
    
    @FXML
    private Button signInButton;
    
    @FXML
    private Hyperlink forgotPasswordLink;
    
    @FXML
    private Hyperlink signUpLink;
    
    private LoginService loginService = new LoginService();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize if needed
    }
    
    @FXML
    private void handleSignIn() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username == null || username.trim().isEmpty()) {
            showAlert("Error", "Please enter a username");
            return;
        }
        
        if (password == null || password.isEmpty()) {
            showAlert("Error", "Please enter a password");
            return;
        }
        
        boolean success = loginService.login(username, password);
        
        if (success) {
            showAlert("Success", "Login successful!");
            NavigationHelper.navigateTo(NavigationHelper.getStage(signInButton), "MainMenuView.fxml");
        } else {
            showAlert("Error", "Invalid username or password");
        }
    }
    
    @FXML
    private void handleForgotPassword() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(forgotPasswordLink), "ForgotPasswordView.fxml");
    }
    
    @FXML
    private void handleSignUp() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(signUpLink), "SignUpView.fxml");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

