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
    // Professional UI Theme Colors (same as ViewInventoryUI)
    private final Color backgroundColor = new Color(135, 211, 177);
    private final Color rowBackground = new Color(255, 255, 255);
    private final Color tableTextColor = new Color(25, 1, 1);
    private final Color headerColor = new Color(37, 154, 105);
    private final Color headerTextColor = Color.WHITE;


    public PlaceOrderUI(JFrame parent, InventoryManager inventoryManager,
                        Customer customer, OrderHistoryManager orderHistoryManager) {
        this.inventoryManager = inventoryManager;
        this.customer = customer;
        this.orderHistoryManager = orderHistoryManager;
        this.currentOrder = new Order("ORD" + System.currentTimeMillis(), customer);

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

        // Table
        String[] columns = {"Product ID", "Name", "Price", "Available Qty", "Order Qty"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // only order quantity editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Integer.class;
                return String.class;
            }
        };

        productTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(productTable);

        // Search
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> filterProducts());
        searchField.addActionListener(e -> filterProducts());

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(new Color(30, 38, 44));
        searchPanel.add(new JLabel("Search Product:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        placeOrderButton = new JButton("Place Order");
        cancelButton = new JButton("Cancel");

        buttonsPanel.add(placeOrderButton);
        buttonsPanel.add(cancelButton);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        add(panel);

        // Load products
        allProducts = inventoryManager.getAllProducts();
        updateTable(allProducts);

        // Button actions
        placeOrderButton.addActionListener(e -> onPlaceOrder());
        cancelButton.addActionListener(e -> dispose());
    }

    private void updateTable(List<Product> products) {
        model.setRowCount(0);
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    String.format("Rs%.2f", p.getPrice()),
                    p.getQuantity(),
                    0
            });
        }
    }

    private void filterProducts() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            updateTable(allProducts);
            return;
        }

        List<Product> filtered = new ArrayList<>();
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(query)) {
                filtered.add(p);
            }
        }
        updateTable(filtered);
    }

    private void onPlaceOrder() {
        if (productTable.isEditing()) {
            productTable.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        List<OrderItem> orderItems = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            int orderQty;
            Object val = model.getValueAt(i, 4);
            if (val instanceof Integer) orderQty = (Integer) val;
            else orderQty = 0;

            if (orderQty > 0) {
                String productId = model.getValueAt(i, 0).toString();
                Product product = inventoryManager.getProductById(productId);
                if (product != null && orderQty <= product.getQuantity()) {
                    orderItems.add(new OrderItem(product, orderQty));
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Order quantity for product '" + model.getValueAt(i, 1) + "' exceeds stock.",
                            "Quantity Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (orderItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity for at least one product.");
            return;
        }

        // Add to current order & update inventory
        for (OrderItem item : orderItems) {
            currentOrder.addItem(item);
            inventoryManager.sellProduct(item.getProduct().getBarcode(), item.getQuantity());
        }

        orderHistoryManager.addOrder(customer, currentOrder);
        JOptionPane.showMessageDialog(this, "Order placed successfully!");
        dispose();
    }
}
