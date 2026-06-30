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

    private final Color backgroundColor  = new Color(135, 211, 177);
    private final Color headerColor      = new Color(37, 154, 105);
    private final Color headerTextColor  = Color.WHITE;

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
        setSize(700, 450);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Table ──────────────────────────────────────────────────────────
        String[] columns = {"Product ID", "Name", "Price", "Available Qty", "Order Qty"};
        model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return column == 4; }
            @Override public Class<?> getColumnClass(int col) {
                return col == 4 ? Integer.class : String.class;
            }
        };

        productTable = new JTable(model);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(26);
        productTable.getTableHeader().setBackground(headerColor);
        productTable.getTableHeader().setForeground(headerTextColor);
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(productTable);

        // ── Search ─────────────────────────────────────────────────────────
        searchField  = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> filterProducts());
        searchField.addActionListener(e -> filterProducts());

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(30, 38, 44));
        searchPanel.add(new JLabel("Search Product:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane,  BorderLayout.CENTER);

        // ── Buttons ────────────────────────────────────────────────────────
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        placeOrderButton = new JButton("Place Order");
        cancelButton     = new JButton("Cancel");
        buttonsPanel.add(placeOrderButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        add(panel);

        allProducts = inventoryManager.getAllProducts();
        updateTable(allProducts);

        placeOrderButton.addActionListener(e -> onPlaceOrder());
        cancelButton.addActionListener(e -> dispose());
    }

    private void updateTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            if (p.getQuantity() > 0) { // only show products that have stock
                model.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        String.format("Rs%.2f", p.getPrice()),
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
            inventoryManager.markProductAsOrdered(item.getProduct().getId(), item.getQuantity()); // FIX: pass ordered qty // ← cashier can now see it
        }

        orderHistoryManager.addOrder(customer, currentOrder);
        JOptionPane.showMessageDialog(this,
                "Order placed successfully!\nThe cashier will process your order.");
        dispose();
    }
}