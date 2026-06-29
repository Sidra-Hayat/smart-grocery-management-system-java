package manager;

import model.Customer;
import model.Order;
import model.OrderItem;
import model.Product;

import java.io.*;
import java.util.*;

/**
 * Stores order history in memory AND persists it to orders.txt so data
 * survives a rerun.
 *
 * File format (pipe-separated):
 *   orderId|customerId|customerName|productId|productName|price|orderedQty
 *   One line per OrderItem.
 */
public class OrderHistoryManager {

    private static final String FILE_NAME = "orders.txt";

    // Key = customer username (stable across reruns), Value = list of orders
    private final Map<String, List<Order>> orderHistory = new HashMap<>();

    // We need InventoryManager to look up Product objects when loading from file
    private final manager.InventoryManager inventoryManager;

    public OrderHistoryManager(manager.InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        loadFromFile();
    }

    // ── Add & persist ───────────────────────────────────────────────────────
    public void addOrder(Customer customer, Order order) {
        orderHistory.computeIfAbsent(customer.getUsername(), k -> new ArrayList<>()).add(order);
        saveToFile();
    }

    // ── Getters ─────────────────────────────────────────────────────────────
    public List<Order> getOrdersByCustomer(Customer customer) {
        return orderHistory.getOrDefault(customer.getUsername(), new ArrayList<>());
    }

    public Map<String, List<Order>> getAllOrderHistory() {
        return orderHistory;
    }

    // ── File Save ────────────────────────────────────────────────────────────
    // Format: orderId|customerId|customerUsername|productId|productName|price|qty
    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, List<Order>> entry : orderHistory.entrySet()) {
                for (Order order : entry.getValue()) {
                    for (OrderItem item : order.getItems()) {
                        bw.write(
                                order.getOrderId() + "|" +
                                        order.getCustomer().getId() + "|" +
                                        order.getCustomer().getUsername() + "|" +
                                        item.getProduct().getId() + "|" +
                                        item.getProduct().getName() + "|" +
                                        item.getProduct().getPrice() + "|" +
                                        item.getQuantity()
                        );
                        bw.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── File Load ────────────────────────────────────────────────────────────
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        // Rebuild orders grouped by orderId first, then by customer
        Map<String, Order> orderMap = new LinkedHashMap<>(); // orderId -> Order

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|", -1);
                if (parts.length < 7) continue;

                String orderId         = parts[0];
                String customerId      = parts[1];
                String customerUsername = parts[2];
                String productId       = parts[3];
                String productName     = parts[4];
                double price;
                int    qty;
                try {
                    price = Double.parseDouble(parts[5]);
                    qty   = Integer.parseInt(parts[6]);
                } catch (NumberFormatException e) {
                    continue;
                }

                // Get or create the Order
                Order order = orderMap.get(orderId);
                if (order == null) {
                    // Build a lightweight Customer stub so we can group orders
                    Customer stub = new Customer(customerId, customerUsername,
                            customerUsername, "", 0, true);
                    order = new Order(orderId, stub);
                    orderMap.put(orderId, order);
                }

                // Try to find the real Product; fall back to a stub
                Product product = inventoryManager != null
                        ? inventoryManager.getProductById(productId) : null;
                if (product == null) {
                    product = new model.NonPerishableProduct(productId, productName, price, qty);
                }
                order.addItem(new OrderItem(product, qty));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Now group by customer username
        for (Order order : orderMap.values()) {
            String username = order.getCustomer().getUsername();
            orderHistory.computeIfAbsent(username, k -> new ArrayList<>()).add(order);
        }
    }
}