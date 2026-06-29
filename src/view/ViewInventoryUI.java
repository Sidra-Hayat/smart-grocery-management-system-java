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
    private List<Product> allProducts;

    // Professional Theme
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color darkGreen = Color.decode("#1B5E20");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color alternateRow = Color.decode("#DCEDC8");
    private final Color darkText = Color.decode("#212121");
    private final Color hoverYellow = Color.decode("#FDD835");

    public ViewInventoryUI(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        initializeUI();
        loadProducts();
    }

    private void initializeUI() {

        setTitle("Available Products");
        setSize(800, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(lightBackground);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //======================
        // Title
        //======================

        JLabel title = new JLabel("Available Products", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(primaryGreen);

        //======================
        // Search Panel
        //======================

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(lightBackground);

        JLabel lblSearch = new JLabel("Search Product:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(darkGreen);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(220, 35));

        JButton searchButton = createButton("Search", primaryGreen);
        JButton clearButton = createButton("Clear", Color.GRAY);

        searchButton.addActionListener(e -> filterProducts());
        searchField.addActionListener(e -> filterProducts());

        clearButton.addActionListener(e -> {
            searchField.setText("");
            updateTable(allProducts);
        });

        searchPanel.add(lblSearch);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(lightBackground);
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        panel.add(northPanel, BorderLayout.NORTH);

        //======================
        // Table
        //======================

        String[] columns = {
                "Product ID",
                "Product Name",
                "Price (Rs)",
                "Available Quantity"
        };

        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel) {

            @Override
            public Component prepareRenderer(TableCellRenderer renderer,
                                             int row,
                                             int column) {

                Component c = super.prepareRenderer(renderer, row, column);

                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : alternateRow);
                    c.setForeground(darkText);
                }

                return c;
            }
        };

        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(30);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));
        productTable.setSelectionBackground(primaryGreen);
        productTable.setSelectionForeground(Color.WHITE);

        productTable.getTableHeader().setBackground(primaryGreen);
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.getViewport().setBackground(lightBackground);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        panel.add(scrollPane, BorderLayout.CENTER);

        //======================
        // Bottom Panel
        //======================

        JButton closeButton = createButton("Close", primaryGreen);
        closeButton.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(lightBackground);
        bottomPanel.add(closeButton);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);
    }

    public void loadProducts() {
        allProducts = inventoryManager.getAllProducts();
        updateTable(allProducts);
    }

    private void updateTable(List<Product> products) {

        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0);

        if (products == null)
            return;

        for (Product p : products) {

            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    String.format("Rs %.2f", p.getPrice()),
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

        List<Product> filtered = allProducts.stream()
                .filter(p ->
                        p.getName().toLowerCase().contains(query)
                                || p.getId().toLowerCase().contains(query))
                .toList();

        updateTable(filtered);
    }

    private JButton createButton(String text, Color color) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverYellow);
                button.setForeground(darkText);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}