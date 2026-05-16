package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cashier extends Person implements Serializable {
    private double salary;

    private double totalSales;
    private Map<String, Integer> productSales; // productId -> quantity

    // Primary constructor (with salary)
    public Cashier(String id, String name, String username, String password, double salary) {
        super(id, name, username, password,"Customer");
        this.salary = salary;
        this.totalSales = 0;
        this.productSales = new HashMap<>();
    }

    // Convenience constructor (no salary provided -> default 0.0)
    public Cashier(String id, String name, String username, String password) {
        this(id, name, username, password, 0.0);
    }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public void addSale(double amount) { totalSales += amount; }

    public void recordProductSale(String productId, int quantity) {
        productSales.put(productId, productSales.getOrDefault(productId, 0) + quantity);
    }

    public double getTotalSales() { return totalSales; }

    public Map<String, Integer> getProductSales() { return productSales; }

    @Override
    public String getRole() {
        // Keep consistent casing with your other roles ("Admin", "Customer")
        return "Cashier";
    }
}
