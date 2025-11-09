package bankapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Main JavaFX Application class
public class BankApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the login view FXML file
            Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            
            // Create the scene
            Scene scene = new Scene(root);
            
            // Set the stage properties
            primaryStage.setTitle("Saint Daniel Bank - Banking System");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            
            // Show the stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading LoginView.fxml: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

