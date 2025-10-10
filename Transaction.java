import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");

    private final String transactionId;
    private final String accountNumber;
    private final double amount;
    private final String type;
    private final LocalDateTime dateTime;

    public Transaction(String transactionId, String accountNumber, double amount, String type) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.dateTime = LocalDateTime.now();
    }

    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public LocalDateTime getDateTime() { return dateTime; }

    public void displayTransaction() {
        System.out.println("------------------------------------------------");
        System.out.println(this);
        System.out.println("------------------------------------------------");
    }

    @Override
    public String toString() {
        return transactionId + " | " + accountNumber + " | " + type + " | BWP " + amount + " | " + dateTime.format(FORMATTER);
    }
}
