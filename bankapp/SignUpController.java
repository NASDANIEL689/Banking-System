package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the sign up view
public class SignUpController implements Initializable {
    
    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField phoneField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private ComboBox<String> accountTypeComboBox;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private CheckBox termsCheckbox;
    
    @FXML
    private Button signUpButton;
    
    @FXML
    private Hyperlink signInLink;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate account type combo
        accountTypeComboBox.getItems().addAll("Personal", "Business", "Savings", "Investment");
    }
    
    @FXML
    private void handleSignUp() {
        // Validate fields
        if (firstNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your first name");
            return;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your last name");
            return;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your email");
            return;
        }
        
        if (phoneField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your phone number");
            return;
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please choose a username");
            return;
        }
        
        if (passwordField.getText().isEmpty()) {
            showAlert("Error", "Please enter a password");
            return;
        }
        
        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            showAlert("Error", "Passwords do not match");
            return;
        }
        
        if (addressField.getText().trim().isEmpty()) {
            showAlert("Error", "Please enter your address");
            return;
        }
        
        if (!termsCheckbox.isSelected()) {
            showAlert("Error", "Please agree to the Terms and Conditions");
            return;
        }
        
        // Register user (demo - would save to database)
        showAlert("Success", "Account created successfully! Please login.");
        NavigationHelper.navigateTo(NavigationHelper.getStage(signUpButton), "LoginView.fxml");
    }
    
    @FXML
    private void handleSignIn() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(signInLink), "LoginView.fxml");
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

