package model;

import java.util.HashMap;
import java.util.Map;

public class TopSellingReport {
    private Map<String, Integer> productSales = new HashMap<>();

    public void recordSale(String productName, int quantity) {
        productSales.put(productName, productSales.getOrDefault(productName, 0) + quantity);
    }

    public Map<String, Integer> getTopSellingProducts() {
        return productSales;
    }
}
