package view;

import manager.InventoryManager;
import manager.NotificationManager;
import model.Cashier;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CashierUI extends JFrame {

    private final InventoryManager inventoryManager;
    private final Cashier cashier;
    private final NotificationManager notificationManager;
    private JTable productTable;
    private DefaultTableModel model;
    private JButton sellButton, logoutButton;

    // Theme Colors
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color hoverRed = Color.decode("#E53935");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color darkText = Color.decode("#212121");
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public CashierUI(Cashier cashier, InventoryManager inventoryManager, NotificationManager notificationManager) {
        this.cashier = cashier;
        this.inventoryManager = inventoryManager;
        this.notificationManager = notificationManager;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Cashier Panel - " + cashier.getName());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(lightBackground);
        setLayout(new BorderLayout(10, 10));

        // Table setup
        model = new DefaultTableModel(new String[]{"ID", "Name", "Price", "Quantity"}, 0);
        productTable = new JTable(model);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Sell button
        sellButton = styledButton("Sell Product");
        sellButton.addActionListener(e -> sellSelectedProduct());

        // Logout button
        logoutButton = styledButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose(); // Close this UI
            JOptionPane.showMessageDialog(null, "Logged out successfully!");
        });
        // Set logout hover red effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(hoverRed);
                logoutButton.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(primaryGreen);
                logoutButton.setForeground(Color.WHITE);
            }
        });

        // Panel setup
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(lightBackground);
        btnPanel.add(sellButton);
        btnPanel.add(logoutButton);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        loadProducts();
        setVisible(true);
    }

    private void loadProducts() {
        model.setRowCount(0);

        // Only show products that have pending orders
        List<Product> products = inventoryManager.getOrderedProducts(); // <-- new method in InventoryManager
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    String.format("Rs %.2f", p.getPrice()),
                    p.getQuantity()
            });
        }
    }

    private void sellSelectedProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first.");
            return;
        }

        String id = productTable.getValueAt(row, 0).toString();
        Product product = inventoryManager.getProductById(id);

        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product not found.");
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity to sell:");
        if (qtyStr == null) return;

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.");
            return;
        }

        if (qty > product.getQuantity()) {
            JOptionPane.showMessageDialog(this, "Not enough stock.");
            return;
        }

        // Reduce stock permanently and record sale
        inventoryManager.sellProduct(product.getId(), qty); // <-- should persist to file/db
        cashier.addSale(product.getPrice() * qty);
        cashier.recordProductSale(product.getId(), qty);

        loadProducts();
        JOptionPane.showMessageDialog(this, "Sold successfully!");
    }

    // Styled button for theme
    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(buttonFont);
        btn.setBackground(primaryGreen);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverYellow);
                btn.setForeground(darkText);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(primaryGreen);
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }
}
