//import manager.InventoryManager;
//import manager.LoginManager;
//import model.Customer;
//import view.CustomerUI;
//
//import javax.swing.*;
//
//public class TestCustomerUI {
//    public static void main(String[] args) {
//        // Prepare dummy managers
//        InventoryManager inventoryManager = new InventoryManager();
//        LoginManager loginManager = new LoginManager();
//
//        // Create a test customer
//        Customer testCustomer = new Customer("john_doe", "password123", "John Doe","abc123",100);
//
//        // Run UI in Event Dispatch Thread for thread safety
//        SwingUtilities.invokeLater(() -> {
//            CustomerUI customerUI = new CustomerUI(inventoryManager, loginManager, testCustomer);
//            customerUI.setVisible(true);
//        });
//    }
//}
