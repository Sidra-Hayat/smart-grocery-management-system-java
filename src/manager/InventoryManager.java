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

    // Track which products have been ordered by customers and HOW MANY were ordered
    // Key = productId, Value = quantity the customer ordered
    private Map<String, Integer> orderedProductQty;

    public InventoryManager() {
        products = new ArrayList<>();
        orderedProductQty = new HashMap<>();
        loadProducts();
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public void addProduct(Product product) {
        products.add(product);
        if (notificationManager != null) notificationManager.checkStock(product);
        saveProducts();
    }

    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                if (notificationManager != null) notificationManager.checkStock(product);
                saveProducts();
                return;
            }
        }
    }

    public List<Product> getAllProducts() { return products; }

    public Product getProductByBarcode(String barcode) {
        for (Product p : products) {
            if (p.getBarcode() != null && p.getBarcode().equals(barcode)) return p;
        }
        return null;
    }

    public Product getProductById(String id) {
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public boolean sellProduct(String productId, int quantity) {
        Product product = getProductById(productId);
        if (product != null && product.getQuantity() >= quantity) {
            product.setQuantity(product.getQuantity() - quantity);
            updateProduct(product);

            // Reduce ordered qty; if fully fulfilled, remove from ordered map so
            // the cashier table stops showing this product
            int remaining = orderedProductQty.getOrDefault(productId, 0) - quantity;
            if (remaining <= 0) {
                orderedProductQty.remove(productId);
            } else {
                orderedProductQty.put(productId, remaining);
            }
            return true;
        }
        return false;
    }

    // ── Ordered Products ────────────────────────────────────────────────────
    // Now accepts the quantity the customer actually ordered
    public void markProductAsOrdered(String productId, int orderedQty) {
        // If same product ordered again, add to existing qty
        orderedProductQty.merge(productId, orderedQty, Integer::sum);
    }

    // Returns virtual Product objects where getQuantity() == the ORDERED qty (not full stock)
    public List<Product> getOrderedProducts() {
        List<Product> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : orderedProductQty.entrySet()) {
            Product real = getProductById(entry.getKey());
            if (real != null && real.getQuantity() > 0) {
                // Clone the product and set quantity = what the customer ordered
                Product clone;
                if (real instanceof PerishableProduct) {
                    clone = new PerishableProduct(real.getId(), real.getName(),
                            real.getPrice(), entry.getValue(),
                            ((PerishableProduct) real).getExpiryDate(), real.getBarcode());
                } else {
                    clone = new NonPerishableProduct(real.getId(), real.getName(),
                            real.getPrice(), entry.getValue());
                    clone.setBarcode(real.getBarcode());
                }
                result.add(clone);
            }
        }
        return result;
    }

    // Remove from ordered map once cashier has processed the sale
    public void removeFromOrdered(String productId) {
        orderedProductQty.remove(productId);
    }

    // ── File Save ───────────────────────────────────────────────────────────
    // Format: id|name|category|price|quantity|expiry|barcode
    //   category = "Perishable" or "NonPerishable"
    //   expiry   = ISO date for perishable, "null" for non-perishable
    public void saveProducts() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("products.txt"))) {
            for (Product p : products) {
                String category;
                String expiry;
                if (p instanceof PerishableProduct) {
                    category = "Perishable";
                    expiry = ((PerishableProduct) p).getExpiryDate() != null
                            ? ((PerishableProduct) p).getExpiryDate().toString()
                            : "null";
                } else {
                    category = "NonPerishable";
                    expiry = "null";
                }
                String barcode = (p.getBarcode() != null) ? p.getBarcode() : "";
                // Write exactly 7 pipe-separated fields
                bw.write(p.getId() + "|" + p.getName() + "|" + category + "|"
                        + p.getPrice() + "|" + p.getQuantity() + "|" + expiry + "|" + barcode);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── File Load ───────────────────────────────────────────────────────────
    // Reads same format: id|name|category|price|quantity|expiry|barcode
    public void loadProducts() {
        products.clear();
        File file = new File("products.txt");
        if (!file.exists()) return; // first run — seed data will fill it

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|", -1); // -1 keeps trailing empty fields
                if (parts.length < 7) continue;         // skip malformed lines

                String id       = parts[0];
                String name     = parts[1];
                // parts[2] = category (used below)
                double price;
                int quantity;
                try {
                    price    = Double.parseDouble(parts[3]);
                    quantity = Integer.parseInt(parts[4]);
                } catch (NumberFormatException e) {
                    continue; // skip bad line
                }
                String expiryStr = parts[5];
                String barcode   = parts[6];

                Product product;
                if (!expiryStr.equals("null") && !expiryStr.isEmpty()) {
                    LocalDate expiryDate = LocalDate.parse(expiryStr);
                    product = new PerishableProduct(id, name, price, quantity, expiryDate, barcode);
                } else {
                    product = new NonPerishableProduct(id, name, price, quantity);
                    product.setBarcode(barcode);
                }
                products.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}