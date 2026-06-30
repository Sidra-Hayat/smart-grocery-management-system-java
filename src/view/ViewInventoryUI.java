package view;

import manager.InventoryManager;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class ViewInventoryUI extends JFrame {

    private final InventoryManager inventoryManager;
    private JTable productTable;
    private JTextField searchField;
    private List<Product> allProducts; // FIX: this is now properly set in loadProducts()

    // Theme colors
    private final Color backgroundColor = new Color(135, 211, 177);
    private final Color rowBackground   = new Color(255, 255, 255);
    private final Color tableTextColor  = new Color(25, 1, 1);
    private final Color headerColor     = new Color(37, 154, 105);
    private final Color headerTextColor = Color.WHITE;

    public ViewInventoryUI(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        initializeUI();
        loadProducts(); // called once, after UI is built
    }

    private void initializeUI() {
        setTitle("Inventory - All Products");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        // ── Table ──────────────────────────────────────────────────────────
        String[] columns = {"Product ID", "Name", "Price (Rs)", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        productTable = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? rowBackground : backgroundColor);
                    comp.setForeground(tableTextColor);
                }
                return comp;
            }
        };

        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(28);
        productTable.setGridColor(new Color(144, 166, 181));
        productTable.setShowHorizontalLines(true);
        productTable.setSelectionBackground(headerColor.darker());
        productTable.setSelectionForeground(Color.WHITE);

        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(headerColor);
        productTable.getTableHeader().setForeground(headerTextColor);
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 34));

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(backgroundColor);

        // ── Search bar ─────────────────────────────────────────────────────
        searchField = new JTextField(22);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(headerColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(180, 180, 180));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFocusPainted(false);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        searchButton.addActionListener(e -> filterProducts());
        searchField.addActionListener(e -> filterProducts());
        clearButton.addActionListener(e -> {
            searchField.setText("");
            updateTable(allProducts);
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(backgroundColor);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        // ── Assemble — only added ONCE each ────────────────────────────────
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane,  BorderLayout.CENTER);

        add(panel); // added to frame once
    }

    public void loadProducts() {
        // FIX: store into the field, not a local variable
        allProducts = inventoryManager.getAllProducts();
        updateTable(allProducts);
    }

    private void updateTable(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);
        if (products == null) return;
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity()
            });
        }
    }

    private void filterProducts() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            updateTable(allProducts);
            return;
        }
        if (allProducts == null) return;
        List<Product> filtered = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(query)
                        || p.getId().toLowerCase().contains(query))
                .toList();
        updateTable(filtered);
    }
}