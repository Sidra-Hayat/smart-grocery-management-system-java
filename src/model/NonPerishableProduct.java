package model;

import java.time.LocalDate;

public class NonPerishableProduct extends Product {
    private int shelfLifeInDays;

    public NonPerishableProduct(String id, String name, double price, int quantity) {
        super(id, name, price, quantity, LocalDate.now().plusYears(5)); // Default long expiry
        this.shelfLifeInDays = 365; // default shelf life
    }

    public NonPerishableProduct(String id, String name, double price, int quantity, int shelfLifeInDays ,String barcode) {
        super(id, name, price, quantity, LocalDate.now().plusDays(shelfLifeInDays));
        this.shelfLifeInDays = shelfLifeInDays;
    }
    public static NonPerishableProduct fromString(String line) {
        String[] parts = line.split("\\|");
        String id = parts[0];
        String name = parts[1];
        double price = Double.parseDouble(parts[2]);
        int quantity = Integer.parseInt(parts[3]);
        String barcode = parts.length > 5 ? parts[5] : null;
        NonPerishableProduct np = new NonPerishableProduct(id, name, price, quantity);
        np.setBarcode(barcode);
        return np;
    }



    public int getShelfLifeInDays() {
        return shelfLifeInDays;
    }

    public void setShelfLifeInDays(int shelfLifeInDays) {
        this.shelfLifeInDays = shelfLifeInDays;
    }

    @Override
    public String getBarcode() {
        return super.getBarcode(); // fixed infinite recursion
    }
}
