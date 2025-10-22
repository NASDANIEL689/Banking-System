import java.util.List;

public interface ITransaction {
    void addTransaction(Transaction t);
    List<Transaction> getTransactions();
}
