package manager;

import model.NonPerishableProduct;
import model.PerishableProduct;
import model.Product;
import java.time.LocalDate;
import java.io.*;
import java.util.*;

public class InventoryManager {

    private List<Product> products;
    private NotificationManager notificationManager;

    // Track which products have been ordered by customers
    private List<String> orderedProductIds;

    public InventoryManager() {
        products = new ArrayList<>();
        orderedProductIds = new ArrayList<>();
        loadProducts(); // Load stock from file
    }

    // ✅ Link NotificationManager
    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    // Add a product to inventory
    public void addProduct(Product product) {
        products.add(product);
        if (notificationManager != null) {
            notificationManager.checkStock(product);
        }
        saveProducts();
    }

    // Update a product in inventory
    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                if (notificationManager != null) {
                    notificationManager.checkStock(product);
                }
                saveProducts();
                return;
            }
        }
    }

    // Get all products
    public List<Product> getAllProducts() {
        return products;
    }

    // Find product by barcode
    public Product getProductByBarcode(String barcode) {
        for (Product p : products) {
            if (p.getBarcode().equals(barcode)) {
                return p;
            }
        }
        return null;
    }

    public Product getProductById(String id) {
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null; // Product not found
    }

    // Sell a product (reduces stock permanently)
    public boolean sellProduct(String productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);

            // Remove from ordered list if sold out
            if (product.getQuantity() == 0) {
                orderedProductIds.remove(productId);
            }

            updateProduct(product); // Triggers notification and saves to file
            return true;
        }
        return false; // Not enough stock or product doesn't exist
    }

    // ---------------- Ordered Products ----------------

    // Mark product as ordered by customer
    public void markProductAsOrdered(String productId) {
        if (!orderedProductIds.contains(productId)) {
            orderedProductIds.add(productId);
        }
    }

    // Get only products that have been ordered (for Cashier UI)
    public List<Product> getOrderedProducts() {
        List<Product> orderedProducts = new ArrayList<>();
        for (String id : orderedProductIds) {
            Product p = getProductById(id);
            if (p != null && p.getQuantity() > 0) {
                orderedProducts.add(p);
            }
        }
        return orderedProducts;
    }

    // ---------------- File Handling ----------------

    // Load products from file

    public void loadProducts() {
        products.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader("products.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length < 7) continue;

                String id = parts[0];
                String name = parts[1];

                // parts[2] is category → ignore if your Product doesn't use it
                double price = Double.parseDouble(parts[3]);
                int quantity = Integer.parseInt(parts[4]);

                String expiryStr = parts[5];
                String barcode = parts[6];

                Product product;

                if (!expiryStr.equals("null")) {
                    LocalDate expiryDate = LocalDate.parse(expiryStr);
                    product = new PerishableProduct(id, name, price, quantity, expiryDate, barcode);
                } else {
                    product = new NonPerishableProduct(id, name, price, quantity);
                }

                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Save products to file
    public void saveProducts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product p : products) {
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
