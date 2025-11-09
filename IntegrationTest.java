public class IntegrationTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Integration test: start");
        DatabaseManager.initSchema();
        BankService bank = new BankService();

        // Register login user and verify
        bank.registerUser("testuser","pass");
        boolean ok = bank.getLoginService().login("testuser","pass");
        System.out.println("Login successful: " + ok);
        if (!ok) throw new RuntimeException("Login failed");

        // Create customer and persist
        PersonalCustomer p = new PersonalCustomer("INT-C1","Test User","Addr","000","t@example.com","PID-INT-1");
        p.setEmployment("ACME");
        bank.createPersonalCustomer(p);
        System.out.println("Created customer: " + p.getCustomerId());

        // Open savings account
        SavingsAccount s = new SavingsAccount("INT-S1", p);
        bank.openSavingsAccount(s);
        System.out.println("Opened savings account: " + s.getAccountNumber());

        // Deposit and persist
        bank.deposit(s, 1000);
        System.out.println("Deposited 1000 to account. Balance: " + s.getBalance());

        // Read back from DB
        Account aFromDb = bank.findAccount("INT-S1");
        System.out.println("Account loaded from DB: " + (aFromDb != null ? aFromDb.getAccountNumber() + ", balance=" + aFromDb.getBalance() : "null"));

        System.out.println("Integration test: done");
    }
}
