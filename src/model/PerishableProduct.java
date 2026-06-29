package model;

import java.time.LocalDate;

public class PerishableProduct extends Product {

    private LocalDate expiryDate;

    public PerishableProduct(String id, String name, double price, int quantity,
                             LocalDate expiryDate, String barcode) {
        super(id, name, price, quantity, expiryDate);
        this.expiryDate = expiryDate;
        // Barcode stored in parent Product — no duplicate field here
        setBarcode(barcode);
    }

    // Convenience constructor — auto-generates barcode, no manual entry needed
    public PerishableProduct(String id, String name, double price,
                             int quantity, LocalDate expiryDate) {
        this(id, name, price, quantity, expiryDate, "BC" + System.currentTimeMillis());
    }

    public LocalDate getExpiryDate() { return expiryDate; }

    @Override
    public String toString() {
        return super.toString();
    }
}