package utils;

import javax.swing.*;

public class BarcodeScanner {

    public static String scan() {
        // Simulate a barcode scan by prompting user input
        String barcode = JOptionPane.showInputDialog(null, "Scan Barcode (Enter Product Barcode):");
        return barcode;
    }
}
