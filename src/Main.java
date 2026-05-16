import manager.*;
import model.*;
import view.CustomerUI;
import view.CashierUI;
import view.UrgentNotificationsUI;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryManager inventoryManager = new InventoryManager();
    private static final CashierManager cashierManager = new CashierManager();
    private static final   LoginManager loginManager = new LoginManager(cashierManager);
    private static final OrderHistoryManager orderHistoryManager = new OrderHistoryManager();
    private static final ReportManager reportManager = new ReportManager(inventoryManager, cashierManager);
    private static NotificationManager notificationManager;
    private static final boolean APPROVED = true;

    public static void main(String[] args) {
        // 1) Create and link NotificationManager BEFORE modifying products
        notificationManager = new NotificationManager(inventoryManager);
        inventoryManager.loadProducts();
        inventoryManager.setNotificationManager(notificationManager);


        // 2) Load previously saved products and cashiers
        inventoryManager.loadProducts();
        cashierManager.loadCashiersFromFile();
        // 3) Seed data if inventory is empty
        seedDataIfEmpty();

        // 4) Generate initial notifications for any loaded/seeded low-stock items
        notificationManager.generateInitialNotifications();



        // 5) Show urgent notifications dialog at startup if any exist
        if (!notificationManager.getNotifications().isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                UrgentNotificationsUI urgentUI = new UrgentNotificationsUI(null, notificationManager);
                urgentUI.setVisible(true);
            });
        }

        // 6) Try to load cashier from file
        Cashier cashier = cashierManager.getCashierByUsername("ali");
        if (cashier == null) {
            cashier = new Cashier("C001", "Ali Raza", "ali", "ali123", 30000);
            cashierManager.addCashier(cashier);
            loginManager.registerUser(cashier);
            System.out.println("✅ Cashier registered successfully.");
        }


        // 7) Show cashier GUI
        Cashier finalCashier = cashier;
        SwingUtilities.invokeLater(() -> {
            // Call the constructor with only Cashier and InventoryManager
            new CashierUI(finalCashier, inventoryManager,notificationManager).setVisible(true);
        });

        // 8) Login flow for console mode
        System.out.println("=== Grocery Store Management System ===");
        Person user = loginFlow();

        if (user instanceof Admin) {
            adminMenu((Admin) user);
        } else if (user instanceof Customer) {
            Customer customer = (Customer) user;
            SwingUtilities.invokeLater(() -> {
                new CustomerUI(inventoryManager, loginManager, customer, orderHistoryManager, cashierManager, notificationManager, reportManager).setVisible(true);
            });
        } else {
            System.out.println("Access denied.");
        }
    }

    private static void seedDataIfEmpty() {
        if (inventoryManager.getAllProducts().isEmpty()) {
            if (loginManager.login("lara Admin", "lara123") == null &&
                    loginManager.login("rija", "rija123") == null) {
                Admin admin = new Admin("9293", "lara", "lara Admin", "lara123", 50000);
                Customer customer = new Customer("rija", "rija Customer", "rija", "rija123", 18,  APPROVED );
                Cashier cashier = new Cashier("102", "Ali Raza", "ali", "ali123");
                loginManager.registerUser(cashier);
                loginManager.registerUser(admin);
                loginManager.registerUser(customer);
            }

            Product product = new Product("P001", "Milk", 16.0, 50, LocalDate.now().plusDays(7));
            inventoryManager.addProduct(product);
            // Perishable Items with correct constructor: id, name, price, quantity, expiryDate, barcodeinventoryManager.addProduct(new PerishableProduct("P001", "Milk", 160, 50, LocalDate.now().plusDays(7), "890123456001"));
            inventoryManager.addProduct(new PerishableProduct("P002", "Eggs", 26, 40, LocalDate.now().plusDays(10), "890123456002"));
            inventoryManager.addProduct(new PerishableProduct("P003", "Bread", 70, 60, LocalDate.now().plusDays(3), "890123456003"));
            inventoryManager.addProduct(new PerishableProduct("P004", "Butter", 250, 25, LocalDate.now().plusDays(30), "890123456004"));
            inventoryManager.addProduct(new PerishableProduct("P005", "Yogurt", 170, 40, LocalDate.now().plusDays(10), "890123456005"));
            inventoryManager.addProduct(new PerishableProduct("P006", "Fresh Juice", 160, 30, LocalDate.now().plusDays(5), "890123456006"));
            inventoryManager.addProduct(new PerishableProduct("P007", "Bananas", 180, 70, LocalDate.now().plusDays(4), "890123456007"));
            inventoryManager.addProduct(new PerishableProduct("P008", "Apples", 150, 1050, LocalDate.now().plusDays(10), "890123456008"));
            inventoryManager.addProduct(new PerishableProduct("P009", "Chicken", 550, 35, LocalDate.now().plusDays(3), "890123456009"));
            inventoryManager.addProduct(new PerishableProduct("P010", "Fish", 500, 20, LocalDate.now().plusDays(2), "890123456010"));
            inventoryManager.addProduct(new PerishableProduct("P011", "Lettuce", 50, 40, LocalDate.now().plusDays(5), "890123456011"));
            inventoryManager.addProduct(new PerishableProduct("P012", "Tomatoes", 120, 50, LocalDate.now().plusDays(6), "890123456012"));
            inventoryManager.addProduct(new PerishableProduct("P013", "Cucumbers", 150, 45, LocalDate.now().plusDays(7), "890123456013"));
            inventoryManager.addProduct(new PerishableProduct("P014", "Cheese", 300, 20, LocalDate.now().plusDays(25), "890123456014"));
            inventoryManager.addProduct(new PerishableProduct("P015", "Cream", 280, 18, LocalDate.now().plusDays(15), "890123456015"));

            // Non-Perishable Items with barcode: id, name, price, quantity, shelfLifeMonths, barcode
            inventoryManager.addProduct(new NonPerishableProduct("NP001", "Rice", 300, 100, 12, "890223456001"));
            inventoryManager.addProduct(new NonPerishableProduct("NP002", "Sugar", 270, 80, 24, "890223456002"));
            inventoryManager.addProduct(new NonPerishableProduct("NP003", "Salt", 40, 90, 36, "890223456003"));
            inventoryManager.addProduct(new NonPerishableProduct("NP004", "Flour", 300, 70, 18, "890223456004"));
            inventoryManager.addProduct(new NonPerishableProduct("NP005", "Cooking Oil", 550, 60, 24, "890223456005"));
            inventoryManager.addProduct(new NonPerishableProduct("NP006", "Tea", 400, 50, 24, "890223456006"));
            inventoryManager.addProduct(new NonPerishableProduct("NP007", "Coffee", 400, 30, 18, "890223456007"));
            inventoryManager.addProduct(new NonPerishableProduct("NP008", "Soap", 70, 120, 36, "890223456008"));
            inventoryManager.addProduct(new NonPerishableProduct("NP009", "Shampoo", 600, 40, 30, "890223456009"));
            inventoryManager.addProduct(new NonPerishableProduct("NP010", "Toothpaste", 90, 70, 24, "890223456010"));
            inventoryManager.addProduct(new NonPerishableProduct("NP011", "Detergent", 150, 60, 36, "890223456011"));
            inventoryManager.addProduct(new NonPerishableProduct("NP012", "Biscuits", 60, 90, 18, "890223456012"));
            inventoryManager.addProduct(new NonPerishableProduct("NP013", "Chips", 50, 100, 12, "890223456013"));
            inventoryManager.addProduct(new NonPerishableProduct("NP014", "Noodles", 80, 75, 18, "890223456014"));
            inventoryManager.addProduct(new NonPerishableProduct("NP015", "Ketchup", 120, 55, 24, "890223456015"));
            inventoryManager.addProduct(new NonPerishableProduct("NP016", "Mayonnaise", 140, 45, 18, "890223456016"));
            inventoryManager.addProduct(new NonPerishableProduct("NP017", "Tissue Box", 100, 85, 48, "890223456017"));
            inventoryManager.addProduct(new NonPerishableProduct("NP018", "Paper Towels", 130, 60, 48, "890223456018"));
            inventoryManager.addProduct(new NonPerishableProduct("NP019", "Hand Sanitizer", 150, 50, 24, "890223456019"));
            inventoryManager.addProduct(new NonPerishableProduct("NP020", "Face Mask Pack", 300, 40, 36, "890223456020"));

            // Save after seeding
            inventoryManager.saveProducts();

            System.out.println("✅ Inventory seeded with products.");
        }
    }

    private static Person loginFlow() {
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        Person user = loginManager.login(username, password);
        if (user != null) {
            System.out.println("Welcome, " + user.getName() + "!");
        } else {
            System.out.println("Invalid credentials.");
        }
        return user;
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Product");
            System.out.println("2. View Inventory");
            System.out.println("3. Add Customer");
            System.out.println("0. Logout");
            System.out.print("Choose option: ");

            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (option) {
                case 1 -> {
                    addProduct();
                    inventoryManager.saveProducts();
                    notificationManager.generateLowStockNotifications(inventoryManager.getAllProducts());

                }
                case 2 -> viewInventory();
                case 3 -> addCustomer();
                case 0 -> {
                    System.out.println("Logged out successfully...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addCustomer() {
        System.out.println("--- Add New Customer ---");

        System.out.print("Customer ID: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Initial Loyalty Points: ");
        int points;
        try {
            points = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid points input. Setting points to 0.");
            points = 0;
        }

        Customer customer = new Customer(id, name, username, password, points,  APPROVED );
        if (loginManager.registerUser(customer)) {
            System.out.println("Customer added successfully.");
        } else {
            System.out.println("Failed to add customer. Username may already exist.");
        }
    }

    private static void customerMenu(Customer customer) {
        Order order = new Order("ORD" + System.currentTimeMillis(), customer);

        while (true) {
            System.out.println("\n--- Customer Menu ---");
            System.out.println("1. View Products");
            System.out.println("2. Add Item to Order");
            System.out.println("3. View Total");
            System.out.println("0. Checkout and Exit");
            System.out.print("Choose option: ");
            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (option) {
                case 1 -> viewInventory();
                case 2 -> addItemToOrder(order);
                case 3 -> System.out.println("Total: RS" + order.calculateTotal());
                case 0 -> {
                    System.out.println("Thank you! Order total: RS" + order.calculateTotal());
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void addProduct() {
        System.out.println("--- Add Product ---");
        System.out.print("Product ID: ");
        String id = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price input. Aborting product addition.");
            return;
        }

        System.out.print("Quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity input. Aborting product addition.");
            return;
        }

        System.out.print("Perishable? (yes/no): ");
        String isPerishable = scanner.nextLine();

        Product product;
        if (isPerishable.equalsIgnoreCase("yes")) {
            System.out.print("Expiry days from now: ");
            int days;
            try {
                days = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid days input. Aborting product addition.");
                return;
            }
            // For new product added by admin, barcode can be set as empty or generated somehow
            product = new PerishableProduct(id, name, price, qty, LocalDate.now().plusDays(days), "");
        } else {
            System.out.print("Shelf life (months): ");
            int shelfLife;
            try {
                shelfLife = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid shelf life input. Aborting product addition.");
                return;
            }
            product = new NonPerishableProduct(id, name, price, qty, shelfLife, "");
        }

        inventoryManager.addProduct(product);
        System.out.println("Product added.");
    }

    private static void viewInventory() {
        System.out.println("\n--- Inventory ---");

        List<Product> products = new ArrayList<>(inventoryManager.getAllProducts());
        notificationManager.generateLowStockNotifications(products);

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product p : products) {
            System.out.println(p);
        }
    }



    private static void addItemToOrder(Order order) {
        System.out.print("Enter Product Barcode: ");
        String barcode = scanner.nextLine();
        Product product = inventoryManager.getProductByBarcode(barcode);

        if (product == null) {
            System.out.println("Product not found.");
            return;
        }

        System.out.print("Enter quantity: ");
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity input.");
            return;
        }

        if (qty > product.getQuantity()) {
            System.out.println("Not enough stock.");
            return;
        }

        // Reduce product quantity and update inventory
        product.setQuantity(product.getQuantity() - qty);
        inventoryManager.updateProduct(product);

        // Add to customer's order
        order.addItem(new OrderItem(product, qty));

        // Check stock notifications
        notificationManager.checkStock(product);

        System.out.println("Item added to order.");
    }
}
