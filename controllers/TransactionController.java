import java.util.ArrayList;
import java.util.List;

public class TransactionController {
    public List<Transaction> getAllTransactions(List<Account> accounts) {
        List<Transaction> all = new ArrayList<>();
        for (Account a : accounts) {
            all.addAll(a.getTransactions());
        }
        return all;
    }
}
