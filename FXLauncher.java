import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import bank.BankService;
import bank.DatabaseManager;
import com.saintdanielbank.LoginController;

public class FXLauncher extends Application {
    private static BankService bankService = new BankService();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // DB schema already initialized in main()
        
        // Load login screen (register_choice.fxml is actually the login screen)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register_choice.fxml"));
        // Set controller directly since we need to inject BankService
        loader.setController(new com.saintdanielbank.LoginController(bankService));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Saint Daniel Bank - Login");
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            // Initialize database schema first
            DatabaseManager.initSchema();
            
            // Check if demo user already exists
            bankapp.Customer existingDemo = bankService.findCustomer("DEMO-001");
            if (existingDemo == null) {
                // Create a demo individual customer for testing
                bankapp.IndividualCustomer demoCustomer = new bankapp.IndividualCustomer(
                    "DEMO-001",
                    "Demo",
                    "User",
                    "DEMO-NID-001",
                    "123 Demo Street",
                    "123-456-7890",
                    "demo@example.com",
                    "demo",
                    "demo"
                );
                demoCustomer.setEmployment("Demo Corp");
                bankService.createIndividualCustomer(demoCustomer);
                
                // Create a demo savings account
                bankapp.SavingsAccount demoAccount = new bankapp.SavingsAccount("DEMO-ACC-001", demoCustomer);
                demoAccount.deposit(1000.0);
                bankService.openSavingsAccount(demoAccount);
                bankService.deposit(demoAccount, 1000.0);
                
                System.out.println("Demo user created: username='demo', password='demo'");
            } else {
                System.out.println("Demo user already exists: username='demo', password='demo'");
            }
        } catch (Exception e) {
            System.err.println("Warning: Could not create demo user: " + e.getMessage());
            // Continue anyway - user can register
        }
        launch(args);
    }
}
