package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the forgot password view
public class ForgotPasswordController implements Initializable {
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private ComboBox<String> securityQuestionComboBox;
    
    @FXML
    private TextField securityAnswerField;
    
    @FXML
    private Button sendResetButton;
    
    @FXML
    private Label statusMessageLabel;
    
    @FXML
    private Hyperlink backToLoginLink;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate security questions
        securityQuestionComboBox.getItems().addAll(
            "What was the name of your first pet?",
            "What city were you born in?",
            "What was your mother's maiden name?",
            "What was the name of your elementary school?",
            "What is your favorite color?"
        );
        
        statusMessageLabel.setText("");
    }
    
    @FXML
    private void handleSendReset() {
        String email = emailField.getText().trim();
        
        if (email.isEmpty()) {
            statusMessageLabel.setText("Please enter your email address");
            statusMessageLabel.setStyle("-fx-text-fill: #F44336;");
            return;
        }
        
        // Validate email format (basic check)
        if (!email.contains("@")) {
            statusMessageLabel.setText("Please enter a valid email address");
            statusMessageLabel.setStyle("-fx-text-fill: #F44336;");
            return;
        }
        
        // Demo: Send reset link
        statusMessageLabel.setText("Password reset link has been sent to " + email);
        statusMessageLabel.setStyle("-fx-text-fill: #4CAF50;");
        
        // Clear fields
        emailField.clear();
        usernameField.clear();
        securityQuestionComboBox.setValue(null);
        securityAnswerField.clear();
    }
    
    @FXML
    private void handleBackToLogin() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(backToLoginLink), "LoginView.fxml");
    }
}

