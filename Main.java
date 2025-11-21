public class Main {
    public static void main(String[] args) {
        System.out.println("Banking System demo (console)");

        CustomerController custCtrl = new CustomerController();
        AccountController accCtrl = new AccountController();

        // Create a personal customer and set employment
        IndividualCustomer p = new IndividualCustomer("C001", "Alice Brown", "12 Main St", "777-1111", "alice@example.com", "PID123");
        p.setEmployment("ABC Corp");
        custCtrl.addCustomer(p);

        // Open accounts
        SavingsAccount s = (SavingsAccount) accCtrl.createSavings("S001", p);
        InvestmentAccount i = (InvestmentAccount) accCtrl.createInvestment("I001", p, 600);
        ChequeAccount c = (ChequeAccount) accCtrl.createCheque("Q001", p);

        // Do some operations
        s.deposit(1000);
        i.deposit(200);
        c.deposit(500);

        // Attempt withdrawal from savings (should be prevented by SavingsAccount)
        s.withdraw(100); // Savings withdraw prints message per implementation

        // Withdraw from investment and cheque
        i.withdraw(100);
        c.withdraw(200);

        // Apply monthly interest
        s.applyMonthlyInterest();
        i.applyMonthlyInterest();

        // Print balances
        System.out.println("Savings balance: " + s.getBalance());
        System.out.println("Investment balance: " + i.getBalance());
        System.out.println("Cheque balance: " + c.getBalance());

        // Print transactions
        System.out.println("Transactions for Cheque Account:");
        for (Transaction t : c.getTransactions()) System.out.println(t);
    }
}
