import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.saintdanielbank.LoginController;

public class FXLauncher extends Application {
    private static BankService bankService = new BankService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize DB schema
        DatabaseManager.initSchema();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register_choice.fxml"));
        // controller factory to inject BankService
        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) return new LoginController(bankService);
            try { return clazz.getDeclaredConstructor().newInstance(); } catch (Exception e) { throw new RuntimeException(e); }
        });
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Saint Daniel Bank - Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        // register a demo user so login works
        bankService.registerUser("demo","demo");
        launch(args);
    }
}
