import java.util.ArrayList;
import java.util.List;

public class CustomerController {
    private List<Customer> customers = new ArrayList<>();

    public void addCustomer(Customer c) { customers.add(c); }
    public List<Customer> getCustomers() { return customers; }
    public Customer findById(String id) {
        return customers.stream().filter(c -> c.getCustomerId().equals(id)).findFirst().orElse(null);
    }
}
