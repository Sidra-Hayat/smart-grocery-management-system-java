package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Product implements Serializable {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private LocalDate expiryDate;
    private String barcode;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Main constructor
    public Product(String id, String name, double price, int quantity, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    // Optional constructor
    public Product(String id, String name, double price, int quantity) {
        this(id, name, price, quantity, LocalDate.now().plusDays(30));
    }

    // Expiry calculations
    public long getDaysToExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    public double getDynamicPrice() {
        long days = getDaysToExpiry();
        if (days < 0) return 0; // Expired
        if (days <= 3) return price * 0.8; // 20% off
        return price;
    }
    public static Product fromString(String line) {
        // Assuming CSV format: id|name|price|quantity
        String[] parts = line.split("\\|");
        String id = parts[0];
        String name = parts[1];
        double price = Double.parseDouble(parts[2]);
        int quantity = Integer.parseInt(parts[3]);

        return new Product(id, name, price, quantity);
    }

    public double getDiscountedPrice() {
        long daysToExpiry = getDaysToExpiry();
        if (daysToExpiry <= 0) {
            return price * 0.5;
        } else if (daysToExpiry <= 3) {
            return price * 0.7;
        } else if (daysToExpiry <= 7) {
            return price * 0.85;
        }
        return price;
    }

    // CSV serialization
    public String toCSV() {
        // Format: type,id,name,price,quantity,expiryDate,barcode
        String type = (this instanceof PerishableProduct) ? "PERISHABLE" : "NONPERISHABLE";
        String expiry = (expiryDate != null) ? expiryDate.format(DATE_FORMAT) : "";
        String bc = (barcode != null) ? barcode : "";
        return String.join(",",
                type,
                id,
                name,
                String.valueOf(price),
                String.valueOf(quantity),
                expiry,
                bc
        );
    }

    // Parse CSV into Product object
    public static Product fromCSV(String csvLine) {
        try {
            String[] parts = csvLine.split(",");
            if (parts.length < 7) {
                System.out.println("Skipping invalid CSV line: " + csvLine);
                return null;
            }

            String type = parts[0];
            String id = parts[1];
            String name = parts[2];
            double price = Double.parseDouble(parts[3]);
            int quantity = Integer.parseInt(parts[4]);
            LocalDate expiry = parts[5].isEmpty() ? null : LocalDate.parse(parts[5], DATE_FORMAT);
            String bc = parts[6];

            if ("PERISHABLE".equalsIgnoreCase(type)) {
                return new PerishableProduct(id, name, price, quantity, expiry, bc);
            } else {
                if (expiry == null) expiry = LocalDate.now().plusYears(5);
                NonPerishableProduct np = new NonPerishableProduct(id, name, price, quantity);
                np.setExpiryDate(expiry);
                np.setBarcode(bc);
                return np;
            }
        } catch (Exception e) {
            System.out.println("Error parsing CSV line: " + csvLine);
            e.printStackTrace();
            return null;
        }
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getBarcode() { return barcode; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public void setBarcode(String barcode) { this.barcode = barcode; }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s | Price: %.2f | Qty: %d | Barcode: %s",
                id, name, price, quantity, getBarcode());
    }

}
