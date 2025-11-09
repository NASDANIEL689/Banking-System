package bankapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

// Helper class for navigating between different views
public class NavigationHelper {
    
    // Navigate to a specific FXML view
    public static void navigateTo(Stage stage, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(NavigationHelper.class.getResource("../" + fxmlFile));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Navigate to a specific FXML view with a specific size
    public static void navigateTo(Stage stage, String fxmlFile, double width, double height) {
        try {
            Parent root = FXMLLoader.load(NavigationHelper.class.getResource("../" + fxmlFile));
            Scene scene = new Scene(root, width, height);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Get the current stage from a node (used in controllers)
    public static Stage getStage(javafx.scene.Node node) {
        return (Stage) node.getScene().getWindow();
    }
}

