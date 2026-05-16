package main;

import manager.*;
import view.LoginUI;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // 1️⃣ Initialize Managers
        InventoryManager inventoryManager = new InventoryManager();
        CashierManager cashierManager = new CashierManager();
        NotificationManager notificationManager = new NotificationManager(inventoryManager);
        ReportManager reportManager = new ReportManager(inventoryManager, cashierManager);
        LoginManager loginManager = new LoginManager(cashierManager); // ✅ Pass CashierManager

        // Link InventoryManager with NotificationManager
        inventoryManager.setNotificationManager(notificationManager);

        // Load existing data
        inventoryManager.loadProducts();
        // Cashiers and other users are in users.txt
        cashierManager.loadCashiersFromFile();

        // Generate initial low-stock notifications
        notificationManager.generateLowStockNotifications(inventoryManager.getAllProducts());

        // Optionally show urgent notifications at startup
        if (!notificationManager.getNotifications().isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                view.UrgentNotificationsUI urgentUI = new view.UrgentNotificationsUI(null, notificationManager);
                urgentUI.setVisible(true);
            });
        }

        // Start the Login GUI
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI(loginManager, inventoryManager, cashierManager, notificationManager, reportManager);
            loginUI.setVisible(true);
        });
    }
}
