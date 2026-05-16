package manager;

import model.Customer;
import model.Order;
import model.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryManager {
    private int orderCounter = 1;
    private final Map<Customer, List<Order>> orderHistory = new HashMap<>();

    public void addOrder(Customer customer, Order order) {
        orderHistory.computeIfAbsent(customer, k -> new ArrayList<>()).add(order);
    }


    // Get all orders for a customer
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderHistory.getOrDefault(customer, new ArrayList<>());
    }

    // Get the entire order history
    public Map<Customer, List<Order>> getAllOrderHistory() {
        return orderHistory;
    }
}
