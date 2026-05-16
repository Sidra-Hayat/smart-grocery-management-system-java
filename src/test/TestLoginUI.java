//import manager.InventoryManager;
//import manager.LoginManager;
//import model.Person;
//import view.LoginUI;
//
//import javax.swing.*;
//
//public class TestLoginUI {
//    public static void main(String[] args) {
//        InventoryManager inventoryManager = new InventoryManager();
//        LoginManager loginManager = new LoginManager();
////
////        // Add a dummy approved user to loginManager for testing
////        loginManager.registerUser(new Person("testuser", "testpass", "Test User",), true);
//
//        SwingUtilities.invokeLater(() -> {
//            LoginUI loginUI = new LoginUI(inventoryManager, loginManager);
//            loginUI.setLoginSuccessListener(user -> {
//                System.out.println("Logged in: " + user.getName());
//                // You can open another UI here based on the user type
//            });
//            loginUI.setVisible(true);
//        });
//    }
//}
