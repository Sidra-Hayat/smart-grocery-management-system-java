package model;

import java.time.LocalDate;

public class PerishableProduct extends Product {
    private LocalDate expiryDate;
    private String barcode;

    // Only one constructor
    public PerishableProduct(String id, String name, double price, int quantity, LocalDate expiryDate, String barcode) {
        super(id, name, price, quantity);
        this.expiryDate = expiryDate;
        this.barcode = barcode; // can be empty string if not available
    }
    public static PerishableProduct fromString(String line) {
        String[] parts = line.split("\\|");
        String id = parts[0];
        String name = parts[1];
        double price = Double.parseDouble(parts[2]);
        int quantity = Integer.parseInt(parts[3]);
        LocalDate expiry = LocalDate.parse(parts[4]); // adjust format if needed
        String barcode = parts.length > 5 ? parts[5] : null;
        return new PerishableProduct(id, name, price, quantity, expiry, barcode);
    }




    public LocalDate getExpiryDate() { return expiryDate; }
    public String getBarcode() { return barcode; }

    @Override
    public String toString() {
        return super.toString() + "|" + expiryDate + "|" + barcode;
    }
}
