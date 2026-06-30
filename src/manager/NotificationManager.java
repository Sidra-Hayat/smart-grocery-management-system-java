package manager;

import model.Notification;
import model.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class NotificationManager {

    public static final int LOW_STOCK_THRESHOLD = 10;

    private final List<Notification> notifications = new ArrayList<>();
    private final InventoryManager inventoryManager;

    public NotificationManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    // called after products are loaded/seeded
    public void generateInitialNotifications() {
        generateLowStockNotifications(inventoryManager.getAllProducts());
    }

    public void generateLowStockNotifications(Collection<Product> products) {
        for (Product product : products) {
            if (product.getQuantity() < LOW_STOCK_THRESHOLD) {
                String message = "Low stock: " + product.getName() + " (" + product.getQuantity() + " left)";

                Optional<Notification> existing = notifications.stream()
                        .filter(n -> n.getType() == Notification.Type.LOW_STOCK
                                && n.getMessage().startsWith("Low stock: " + product.getName()))
                        .findFirst();

                if (existing.isPresent()) {
                    Notification notif = existing.get();
                    if (!notif.getMessage().equals(message)) {
                        notif.setMessage(message);
                    }
                } else {
                    notifications.add(new Notification(message, Notification.Type.LOW_STOCK));
                }
            } else {
                notifications.removeIf(n -> n.getType() == Notification.Type.LOW_STOCK
                        && n.getMessage().startsWith("Low stock: " + product.getName()));
            }
        }
    }

    public void checkStock(Product product) {
        if (product != null) {
            List<Product> single = new ArrayList<>();
            single.add(product);
            generateLowStockNotifications(single);
        }
    }
}