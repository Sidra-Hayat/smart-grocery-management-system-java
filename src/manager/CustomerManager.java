
package manager;

import model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerManager {
    private final List<Customer> customers = new ArrayList<>();

    // Load customers from file or initialize list in constructor if needed

    public Customer getCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    // Add methods for adding customers, loading, saving, etc. as needed
}
