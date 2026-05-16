package view;

import manager.CashierManager;
import model.Product;
import manager.InventoryManager;
import model.Cashier;
import manager.ReportManager;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ProductManagerUI extends JFrame {
    private InventoryManager inventoryManager;
    private Cashier cashier;
    private ReportManager reportManager;
    private CashierManager cashierManager;

    private JTextField nameField, priceField, qtyField, expiryField, barcodeField;

    public ProductManagerUI(InventoryManager inventoryManager, CashierManager cashierManager) {
        this.inventoryManager = inventoryManager;
        this.cashierManager = cashierManager;


        setTitle("Product Manager - Grocery Store");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(110, 205, 165));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(152, 211, 186));

        JLabel nameLabel = new JLabel("Product Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel qtyLabel = new JLabel("Quantity:");
        JLabel expiryLabel = new JLabel("Days to Expiry:");
        JLabel barcodeLabel = new JLabel("Barcode:");

        nameField = new JTextField();
        priceField = new JTextField();
        qtyField = new JTextField();
        expiryField = new JTextField();
        barcodeField = new JTextField();

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(priceLabel);
        formPanel.add(priceField);
        formPanel.add(qtyLabel);
        formPanel.add(qtyField);
        formPanel.add(expiryLabel);
        formPanel.add(expiryField);
        formPanel.add(barcodeLabel);
        formPanel.add(barcodeField);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(110, 205, 165));

        JButton addButton = styledButton("Add Product");
        JButton viewButton = styledButton("View All Products");
        JButton recommendButton = styledButton("AI Recommend");
        JButton closeButton = styledButton("Close");

        addButton.addActionListener(e -> addProduct());
        viewButton.addActionListener(e -> viewAllProducts());
        recommendButton.addActionListener(e -> recommendProduct());
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(recommendButton);
        buttonPanel.add(closeButton);

        return buttonPanel;
    }

    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int qty = Integer.parseInt(qtyField.getText().trim());
            long daysToExpiry = Long.parseLong(expiryField.getText().trim());
            String barcode = barcodeField.getText().trim();
            LocalDate expiryDate = LocalDate.now().plusDays(daysToExpiry);

            String id = generateProductId();
            Product p = new Product(id, name, price, qty, expiryDate);
            p.setBarcode(barcode);

            inventoryManager.addProduct(p);
            JOptionPane.showMessageDialog(this, "Product added successfully!");

            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage());
        }
    }

    private void viewAllProducts() {
        StringBuilder sb = new StringBuilder();
        for (Product p : inventoryManager.getAllProducts()) {
            sb.append(p.getName())
                    .append(" - Rs").append(p.getPrice())
                    .append(" - Qty: ").append(p.getQuantity())
                    .append(" - Expiry: ").append(p.getDaysToExpiry())
                    .append(" days")
                    .append(" - Barcode: ").append(p.getBarcode())
                    .append("\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(this, scrollPane, "All Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void recommendProduct() {
        Product p = reportManager.getRecommendedProduct();
        if (p != null) {
            JOptionPane.showMessageDialog(this, "Recommended Product: " + p.getName());
        } else {
            JOptionPane.showMessageDialog(this, "No recommendation available.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        qtyField.setText("");
        expiryField.setText("");
        barcodeField.setText("");
    }

    private JButton styledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(10, 104, 65));  // Professional Blue
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        return button;
    }

    private String generateProductId() {
        return "P" + System.currentTimeMillis();
    }

    public ProductManagerUI(InventoryManager inventoryManager) {
        this(inventoryManager, null);
    }
}
