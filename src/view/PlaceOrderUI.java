package view;

import manager.InventoryManager;
import manager.OrderHistoryManager;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderUI extends JFrame {

    private JTextField searchField;
    private JButton searchButton;
    private List<Product> allProducts;
    private DefaultTableModel model;

    private final Customer customer;
    private final InventoryManager inventoryManager;
    private final OrderHistoryManager orderHistoryManager;
    private Order currentOrder;

    private JTable productTable;
    private JButton placeOrderButton;
    private JButton cancelButton;

    // ── Theme — matches CustomerUI / OrderHistoryUI / ViewInventoryUI ──────
    private final Color primaryGreen    = Color.decode("#2E7D32");
    private final Color darkGreen       = Color.decode("#1B5E20");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color alternateRow    = Color.decode("#DCEDC8");
    private final Color darkText        = Color.decode("#212121");
    private final Color hoverYellow     = Color.decode("#FDD835");

    public PlaceOrderUI(JFrame parent, InventoryManager inventoryManager,
                        Customer customer, OrderHistoryManager orderHistoryManager) {
        this.inventoryManager    = inventoryManager;
        this.customer            = customer;
        this.orderHistoryManager = orderHistoryManager;
        this.currentOrder        = new Order("ORD" + System.currentTimeMillis(), customer);
        initializeUI(parent);
    }

    private void initializeUI(JFrame parent) {
        setTitle("Place Order - " + customer.getName());
        setSize(760, 520);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(lightBackground);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Title + Search (north) ──────────────────────────────────────
        JLabel title = new JLabel("Place Order", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(primaryGreen);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(lightBackground);

        JLabel lblSearch = new JLabel("Search Product:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(darkGreen);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(220, 35));

        searchButton = createButton("Search", primaryGreen);
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

        // ── Table ──────────────────────────────────────────────────────
        String[] columns = {"Product ID", "Product Name", "Price (Rs)", "Available Qty", "Order Qty"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 4; }
            @Override public Class<?> getColumnClass(int col) {
                return col == 4 ? Integer.class : String.class;
            }
        };

        productTable = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
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

        // ── Buttons (south) ───────────────────────────────────────────
        placeOrderButton = createButton("Place Order", primaryGreen);
        cancelButton     = createButton("Cancel", Color.GRAY);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonsPanel.setBackground(lightBackground);
        buttonsPanel.add(placeOrderButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        add(panel);

        allProducts = inventoryManager.getAllProducts();
        updateTable(allProducts);

        placeOrderButton.addActionListener(e -> onPlaceOrder());
        cancelButton.addActionListener(e -> dispose());
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

    private void updateTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            if (p.getQuantity() > 0) { // only show products that have stock
                model.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        String.format("Rs %.2f", p.getPrice()),
                        p.getQuantity(),
                        0
                });
            }
        }
    }

    private void filterProducts() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) { updateTable(allProducts); return; }
        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(query)) filtered.add(p);
        }
        updateTable(filtered);
    }

    private void onPlaceOrder() {
        if (productTable.isEditing()) productTable.getCellEditor().stopCellEditing();

        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object val = model.getValueAt(i, 4);
            int orderQty = (val instanceof Integer) ? (Integer) val : 0;
            if (orderQty <= 0) continue;

            String productId = model.getValueAt(i, 0).toString();
            Product product  = inventoryManager.getProductById(productId);

            if (product == null) continue;

            if (orderQty > product.getQuantity()) {
                JOptionPane.showMessageDialog(this,
                        "Order quantity for '" + product.getName() + "' exceeds available stock.\n"
                                + "Available: " + product.getQuantity(),
                        "Quantity Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            orderItems.add(new OrderItem(product, orderQty));
        }

        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a quantity (greater than 0) for at least one product.");
            return;
        }

        // ── KEY FIX: mark products as ordered for the cashier.
        //    Do NOT reduce stock here — the cashier does that when they sell.
        for (OrderItem item : orderItems) {
            currentOrder.addItem(item);
            inventoryManager.markProductAsOrdered(item.getProduct().getId(), item.getQuantity());
        }

        orderHistoryManager.addOrder(customer, currentOrder);
        JOptionPane.showMessageDialog(this,
                "Order placed successfully!\nThe cashier will process your order.");
        dispose();
    }
}