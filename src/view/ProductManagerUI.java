package view;

import manager.CashierManager;
import manager.InventoryManager;
import manager.ReportManager;
import model.NonPerishableProduct;
import model.PerishableProduct;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class ProductManagerUI extends JFrame {

    private final InventoryManager inventoryManager;
    private final CashierManager   cashierManager;
    private ReportManager reportManager;

    // Barcode field REMOVED
    private JTextField nameField, priceField, qtyField, expiryField;

    private final Color panelColor  = new Color(110, 205, 165);
    private final Color formColor   = new Color(152, 211, 186);
    private final Color btnColor    = new Color(10, 104, 65);

    public ProductManagerUI(InventoryManager inventoryManager, CashierManager cashierManager) {
        this.inventoryManager = inventoryManager;
        this.cashierManager   = cashierManager;

        setTitle("Product Manager - Grocery Store");
        setSize(420, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(panelColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(createFormPanel(),   BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        add(mainPanel);
    }

    public ProductManagerUI(InventoryManager inventoryManager) {
        this(inventoryManager, null);
    }

    private JPanel createFormPanel() {
        // 4 rows only — barcode removed
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(formColor);

        nameField   = new JTextField();
        priceField  = new JTextField();
        qtyField    = new JTextField();
        expiryField = new JTextField();

        formPanel.add(new JLabel("Product Name:"));  formPanel.add(nameField);
        formPanel.add(new JLabel("Price (Rs):"));     formPanel.add(priceField);
        formPanel.add(new JLabel("Quantity:"));       formPanel.add(qtyField);
        formPanel.add(new JLabel("Days to Expiry:")); formPanel.add(expiryField);

        // Hint label below form
        JLabel hint = new JLabel("  Leave 'Days to Expiry' empty for non-perishable products.");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(60, 80, 60));

        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.setBackground(formColor);
        wrapper.add(formPanel, BorderLayout.CENTER);
        wrapper.add(hint,      BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(panelColor);

        JButton addBtn       = styledButton("Add Product");
        JButton viewBtn      = styledButton("View All Products");
        JButton recommendBtn = styledButton("AI Recommend");
        JButton closeBtn     = styledButton("Close");

        addBtn.addActionListener(e -> addProduct());
        viewBtn.addActionListener(e -> viewAllProducts());
        recommendBtn.addActionListener(e -> recommendProduct());
        closeBtn.addActionListener(e -> dispose());

        btnPanel.add(addBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(recommendBtn);
        btnPanel.add(closeBtn);
        return btnPanel;
    }

    private void addProduct() {
        try {
            String name  = nameField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            int    qty   = Integer.parseInt(qtyField.getText().trim());
            String expiryText = expiryField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Product name cannot be empty.");
                return;
            }

            String id = "P" + System.currentTimeMillis();
            Product p;

            if (expiryText.isEmpty()) {
                // Non-perishable — no expiry needed
                p = new NonPerishableProduct(id, name, price, qty);
            } else {
                long daysToExpiry = Long.parseLong(expiryText);
                LocalDate expiryDate = LocalDate.now().plusDays(daysToExpiry);
                // Barcode auto-generated — admin does not type it
                String autoBarcode = "BC" + System.currentTimeMillis();
                p = new PerishableProduct(id, name, price, qty, expiryDate, autoBarcode);
            }

            inventoryManager.addProduct(p);
            JOptionPane.showMessageDialog(this, "Product '" + name + "' added successfully!");
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numbers for Price, Quantity, and Days to Expiry.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void viewAllProducts() {
        new ViewInventoryUI(inventoryManager).setVisible(true);
    }

    private void recommendProduct() {
        if (reportManager == null) {
            reportManager = new ReportManager(inventoryManager, cashierManager);
        }
        Product p = reportManager.getRecommendedProduct();
        if (p != null) {
            JOptionPane.showMessageDialog(this,
                    "AI Recommendation:\n\nSell '" + p.getName()
                            + "' soon — it expires in " + p.getDaysToExpiry() + " day(s).");
        } else {
            JOptionPane.showMessageDialog(this, "No recommendation available right now.");
        }
    }

    private void clearFields() {
        nameField.setText("");
        priceField.setText("");
        qtyField.setText("");
        expiryField.setText("");
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(btnColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}