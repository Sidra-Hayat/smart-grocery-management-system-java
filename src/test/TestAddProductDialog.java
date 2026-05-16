//import view.AddProductDialog;
//
//import javax.swing.*;
//
//public class TestAddProductDialog {
//    public static void main(String[] args) {
//        // Ensure Swing components are created on the Event Dispatch Thread
//        SwingUtilities.invokeLater(() -> {
//            JFrame dummyParent = new JFrame(); // Dummy frame to act as a parent
//            dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            dummyParent.setSize(300, 200);
//            dummyParent.setLocationRelativeTo(null);
//            dummyParent.setVisible(false); // Hide parent window
//
//            AddProductDialog dialog = new AddProductDialog(dummyParent);
//            dialog.setVisible(true);
//
//            // After closing the dialog, print the entered values if submitted
//            if (dialog.isSubmitted()) {
//                System.out.println("Product ID: " + dialog.getProductId());
//                System.out.println("Product Name: " + dialog.getProductName());
//                System.out.println("Price: " + dialog.getPrice());
//                System.out.println("Quantity: " + dialog.getQuantity());
//                System.out.println("Type: " + dialog.getProductType());
//                System.out.println("Expiry/Shelf Life: " + dialog.getExpiryOrShelfLife());
//            } else {
//                System.out.println("Product addition was canceled.");
//            }
//
//            System.exit(0); // Exit the program after dialog closes
//        });
//    }
//}
