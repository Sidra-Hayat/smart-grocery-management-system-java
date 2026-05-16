package view;

import manager.ReportManager;
import manager.InventoryManager;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TopSellingReportUI extends JFrame {

    // Theme Colors
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color darkText = Color.decode("#212121");
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public TopSellingReportUI(ReportManager reportManager, InventoryManager inventoryManager) {
        setTitle("Top-Selling Products Report");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(lightBackground);
        setLayout(new BorderLayout(10, 10));

        // Title Label
        JLabel titleLabel = new JLabel("Top-Selling Products", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(primaryGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table columns
        String[] columns = {"Product ID", "Product Name", "Category", "Price", "Quantity Sold", "Stock Left"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        // Fetch top-selling products
        List<Map.Entry<String, Integer>> topProducts = reportManager.getTopSellingProducts();

        if (topProducts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products sold yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Sort by Quantity Sold descending
            topProducts.sort(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()));

            for (Map.Entry<String, Integer> entry : topProducts) {
                String productId = entry.getKey();
                int qtySold = entry.getValue();

                Product product = inventoryManager.getProductById(productId);
                if (product != null) {
                    Object[] rowData = {
                            product.getId(),
                            product.getName(),
                           // product.getCategory(),
                            product.getPrice(),
                            qtySold,
                            product.getQuantity()
                    };
                    tableModel.addRow(rowData);
                }
            }
        }

        // JTable setup
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(darkText);
        table.setBackground(Color.WHITE);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(primaryGreen);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(lightBackground);
        add(scrollPane, BorderLayout.CENTER);

        // Close button
        JButton closeBtn = styledButton("Close");
        closeBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(lightBackground);
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    // Styled button consistent with AdminUI
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
