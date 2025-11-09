public class DBMain {
    public static void main(String[] args) throws Exception {
        System.out.println("Initializing DB...");
        DatabaseManager.initSchema();
        System.out.println("Schema initialized.");

        CustomerDAO cdao = new CustomerDAO();
        AccountDAO adao = new AccountDAO();
        TransactionDAO tdao = new TransactionDAO();

        // create a personal customer
        PersonalCustomer p = new PersonalCustomer("DB-C1", "DB Alice", "1 DB St", "800-1000", "dbalice@example.com", "PID-DB-1");
        p.setEmployment("DBCorp");
        cdao.create(p);
        System.out.println("Created customer: " + p.getCustomerId());

        SavingsAccount s = new SavingsAccount("DB-S1", p);
        s.deposit(500);
        adao.create(s);
        System.out.println("Created savings account: " + s.getAccountNumber());

        // record transaction into DB
        for (Transaction t : s.getTransactions()) {
            tdao.addTransaction(t);
        }

        System.out.println("Listing customers from DB:");
        for (Customer c : new CustomerDAO().listAll()) {
            System.out.println("- " + c.getCustomerId() + ": " + c.getFullName());
        }

        DatabaseManager.close();
        System.out.println("Done.");
    }
}
