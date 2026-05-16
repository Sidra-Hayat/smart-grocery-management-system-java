package manager;

import model.Product;
import model.Cashier;

import java.util.*;
import java.util.stream.Collectors;

public class ReportManager {
    private InventoryManager inventoryManager;
    private CashierManager cashierManager;

    // Updated constructor to accept both managers
    public ReportManager(InventoryManager inventoryManager, CashierManager cashierManager) {
        this.inventoryManager = inventoryManager;
        this.cashierManager = cashierManager;
    }



    public List<Map.Entry<String, Integer>> getTopSellingProducts() {
        Map<String, Integer> allSales = new HashMap<>();
        for (Cashier cashier : cashierManager.getAllCashiers()) {
            Map<String, Integer> sales = cashier.getProductSales();
            for (Map.Entry<String, Integer> entry : sales.entrySet()) {
                allSales.merge(entry.getKey(), entry.getValue(), Integer::sum);
            }
        }
        return allSales.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

    public List<Product> getLowStockAlerts(int threshold) {
        return inventoryManager.getAllProducts().stream()
                .filter(product -> product.getQuantity() <= threshold)
                .collect(Collectors.toList());
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
    public Product getRecommendedProduct() {
        Optional<Product> expiryProduct = inventoryManager.getAllProducts().stream()
                .filter(p -> p.getDaysToExpiry() >= 0)
                .sorted(Comparator.comparingLong(Product::getDaysToExpiry))
                .findFirst();

        if (expiryProduct.isPresent()) return expiryProduct.get();

        // other logic if expiryProduct not found
        return null;
    }
}
