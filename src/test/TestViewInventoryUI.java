//
//
//import manager.InventoryManager;
//import model.Product;
//import view.ViewInventoryUI;
//
//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestViewInventoryUI {
//    public static void main(String[] args) {
//        // Sample InventoryManager with dummy products
//        InventoryManager inventoryManager = new InventoryManager() {
//            @Override
//            public List<Product> getAllProducts() {
//                List<Product> products = new ArrayList<>();
//                products.add(new Product("P101", "Apples", 1.99, 50));
//                products.add(new Product("P102", "Bananas", 0.99, 100));
//                products.add(new Product("P103", "Oranges", 2.49, 75));
//                products.add(new Product("P104", "Milk", 3.59, 30));
//                return products;
//            }
//        };
//
//        SwingUtilities.invokeLater(() -> {
//            ViewInventoryUI view = new ViewInventoryUI(inventoryManager);
//            view.setVisible(true);
//        });
//    }
//}
