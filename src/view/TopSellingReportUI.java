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

    private final Color primaryGreen    = Color.decode("#2E7D32");
    private final Color hoverYellow     = Color.decode("#FDD835");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color darkText        = Color.decode("#212121");
    private final Font  buttonFont      = new Font("Segoe UI", Font.BOLD, 14);

    public TopSellingReportUI(ReportManager reportManager, InventoryManager inventoryManager) {
        setTitle("Top-Selling Products Report");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(lightBackground);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Top-Selling Products", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(primaryGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Product ID", "Product Name", "Category", "Price (Rs)", "Quantity Sold", "Stock Left"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Map.Entry<String, Integer>> topProducts = reportManager.getTopSellingProducts();

        if (topProducts.isEmpty()) {
            tableModel.addRow(new Object[]{
                    "-", "No sales recorded yet — cashier has not sold any products.", "-", "-", "-", "-"
            });
        } else {
            topProducts.sort(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()));

            for (Map.Entry<String, Integer> entry : topProducts) {
                String productId = entry.getKey();
                int    qtySold   = entry.getValue();

                Product product = inventoryManager.getProductById(productId);
                if (product != null) {
                    // FIX: derive category since getCategory() does not exist
                    String category = (product.getDaysToExpiry() >= 0) ? "Perishable" : "Non-Perishable";

                    // FIX: was 5 values for 6 columns — Category was commented out
                    // causing every column after it to be shifted/wrong
                    tableModel.addRow(new Object[]{
                            product.getId(),
                            product.getName(),
                            category,                                      // ← was missing
                            String.format("Rs %.2f", product.getPrice()),
                            qtySold,
                            product.getQuantity()                          // ← now shows correctly
                    });
                }
            }
        }

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(darkText);
        table.setBackground(Color.WHITE);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(primaryGreen);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 34));

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(130);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(110);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(lightBackground);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = styledButton("Close");
        closeBtn.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(lightBackground);
        btnPanel.add(closeBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(buttonFont);
        btn.setBackground(primaryGreen);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hoverYellow); btn.setForeground(darkText);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(primaryGreen); btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }
}