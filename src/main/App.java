package main;

import manager.*;
import model.*;
import view.LoginUI;

import javax.swing.*;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) {

        InventoryManager     inventoryManager     = new InventoryManager();
        CashierManager       cashierManager       = new CashierManager();
        NotificationManager  notificationManager  = new NotificationManager(inventoryManager);
        ReportManager        reportManager        = new ReportManager(inventoryManager, cashierManager);
        LoginManager         loginManager         = new LoginManager(cashierManager);
        // FIX: pass inventoryManager so order history can look up products on load
        OrderHistoryManager  orderHistoryManager  = new OrderHistoryManager(inventoryManager);

        inventoryManager.setNotificationManager(notificationManager);

        // Load saved data
        inventoryManager.loadProducts();
        cashierManager.loadCashiersFromFile();

        // Seed default users + products if running for first time
        seedDataIfEmpty(inventoryManager, cashierManager, loginManager);

        // Build notification list silently — no popup at startup
        notificationManager.generateLowStockNotifications(inventoryManager.getAllProducts());

        // Open Login screen
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI(loginManager, inventoryManager,
                    cashierManager, notificationManager, reportManager);
            loginUI.setVisible(true);
        });
    }

    private static void seedDataIfEmpty(InventoryManager inventoryManager,
                                        CashierManager cashierManager,
                                        LoginManager loginManager) {
        if (!inventoryManager.getAllProducts().isEmpty()) return; // already seeded

        // Default users
        Admin    admin    = new Admin("A001", "Lara", "admin", "admin123", 50000);
        Customer customer = new Customer("C001", "Rija", "rija", "rija123", 18, true);
        Cashier  cashier  = new Cashier("CA001", "Ali Raza", "ali", "ali123", 30000);

        loginManager.registerUser(admin);
        loginManager.registerUser(customer);
        loginManager.registerUser(cashier);
        cashierManager.addCashier(cashier);

        // Perishable products
        inventoryManager.addProduct(new PerishableProduct("P001", "Milk",        160, 50,   LocalDate.now().plusDays(7)));
        inventoryManager.addProduct(new PerishableProduct("P002", "Eggs",         26, 40,   LocalDate.now().plusDays(10)));
        inventoryManager.addProduct(new PerishableProduct("P003", "Bread",        70, 60,   LocalDate.now().plusDays(3)));
        inventoryManager.addProduct(new PerishableProduct("P004", "Butter",      250, 25,   LocalDate.now().plusDays(30)));
        inventoryManager.addProduct(new PerishableProduct("P005", "Yogurt",      170, 40,   LocalDate.now().plusDays(10)));
        inventoryManager.addProduct(new PerishableProduct("P006", "Fresh Juice", 160, 30,   LocalDate.now().plusDays(5)));
        inventoryManager.addProduct(new PerishableProduct("P007", "Bananas",     180, 70,   LocalDate.now().plusDays(4)));
        inventoryManager.addProduct(new PerishableProduct("P008", "Apples",      150, 100,  LocalDate.now().plusDays(10)));
        inventoryManager.addProduct(new PerishableProduct("P009", "Chicken",     550, 35,   LocalDate.now().plusDays(3)));
        inventoryManager.addProduct(new PerishableProduct("P010", "Fish",        500, 20,   LocalDate.now().plusDays(2)));
        inventoryManager.addProduct(new PerishableProduct("P011", "Lettuce",      50, 40,   LocalDate.now().plusDays(5)));
        inventoryManager.addProduct(new PerishableProduct("P012", "Tomatoes",    120, 50,   LocalDate.now().plusDays(6)));
        inventoryManager.addProduct(new PerishableProduct("P013", "Cucumbers",   150, 45,   LocalDate.now().plusDays(7)));
        inventoryManager.addProduct(new PerishableProduct("P014", "Cheese",      300, 20,   LocalDate.now().plusDays(25)));
        inventoryManager.addProduct(new PerishableProduct("P015", "Cream",       280, 18,   LocalDate.now().plusDays(15)));

        // Non-perishable products
        inventoryManager.addProduct(new NonPerishableProduct("NP001", "Rice",            300, 100));
        inventoryManager.addProduct(new NonPerishableProduct("NP002", "Sugar",           270,  80));
        inventoryManager.addProduct(new NonPerishableProduct("NP003", "Salt",             40,  90));
        inventoryManager.addProduct(new NonPerishableProduct("NP004", "Flour",           300,  70));
        inventoryManager.addProduct(new NonPerishableProduct("NP005", "Cooking Oil",     550,  60));
        inventoryManager.addProduct(new NonPerishableProduct("NP006", "Tea",             400,  50));
        inventoryManager.addProduct(new NonPerishableProduct("NP007", "Coffee",          400,  30));
        inventoryManager.addProduct(new NonPerishableProduct("NP008", "Soap",             70, 120));
        inventoryManager.addProduct(new NonPerishableProduct("NP009", "Shampoo",         600,  40));
        inventoryManager.addProduct(new NonPerishableProduct("NP010", "Toothpaste",       90,  70));
        inventoryManager.addProduct(new NonPerishableProduct("NP011", "Detergent",       150,  60));
        inventoryManager.addProduct(new NonPerishableProduct("NP012", "Biscuits",         60,  90));
        inventoryManager.addProduct(new NonPerishableProduct("NP013", "Chips",            50, 100));
        inventoryManager.addProduct(new NonPerishableProduct("NP014", "Noodles",          80,  75));
        inventoryManager.addProduct(new NonPerishableProduct("NP015", "Ketchup",         120,  55));
        inventoryManager.addProduct(new NonPerishableProduct("NP016", "Mayonnaise",      140,  45));
        inventoryManager.addProduct(new NonPerishableProduct("NP017", "Tissue Box",      100,  85));
        inventoryManager.addProduct(new NonPerishableProduct("NP018", "Paper Towels",    130,  60));
        inventoryManager.addProduct(new NonPerishableProduct("NP019", "Hand Sanitizer",  150,  50));
        inventoryManager.addProduct(new NonPerishableProduct("NP020", "Face Mask Pack",  300,  40));

        inventoryManager.saveProducts();
    }
}