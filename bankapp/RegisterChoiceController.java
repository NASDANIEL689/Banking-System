package bankapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

// Controller for the register choice view
public class RegisterChoiceController implements Initializable {
    
    @FXML
    private Button personalAccountButton;
    
    @FXML
    private Button businessAccountButton;
    
    @FXML
    private Button backButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize if needed
    }
    
    @FXML
    private void handlePersonalAccount() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(personalAccountButton), "SignUpView.fxml");
    }
    
    @FXML
    private void handleBusinessAccount() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(businessAccountButton), "SignUpView.fxml");
    }
    
    @FXML
    private void handleBack() {
        NavigationHelper.navigateTo(NavigationHelper.getStage(backButton), "LoginView.fxml");
    }
}

