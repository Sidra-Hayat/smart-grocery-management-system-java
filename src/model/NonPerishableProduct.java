package model;

import java.time.LocalDate;

public class NonPerishableProduct extends Product {

    private int shelfLifeInDays;

    public NonPerishableProduct(String id, String name, double price, int quantity) {
        super(id, name, price, quantity, LocalDate.now().plusYears(5));
        this.shelfLifeInDays = 1825; // 5 years default
        setBarcode("BC" + System.currentTimeMillis()); // auto-generated
    }

    public NonPerishableProduct(String id, String name, double price,
                                int quantity, int shelfLifeInDays) {
        super(id, name, price, quantity, LocalDate.now().plusDays(shelfLifeInDays));
        this.shelfLifeInDays = shelfLifeInDays;
        setBarcode("BC" + System.currentTimeMillis());
    }

    public int  getShelfLifeInDays() { return shelfLifeInDays; }
    public void setShelfLifeInDays(int d) { this.shelfLifeInDays = d; }

    @Override
    public String toString() { return super.toString(); }
}