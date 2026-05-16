package view;

import manager.OrderHistoryManager;
import model.Customer;
import model.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderHistoryUI extends JFrame {
    private final OrderHistoryManager orderHistoryManager;
    private final Customer customer;

    private JTable ordersTable;
    private JButton closeButton;

    public OrderHistoryUI(OrderHistoryManager orderHistoryManager, Customer customer) {
        this.orderHistoryManager = orderHistoryManager;
        this.customer = customer;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Order History - " + customer.getName());
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 38, 44));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Order ID", "Product Name", "Quantity", "Price Per Unit", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };

        List<Order> orders = orderHistoryManager.getOrdersByCustomer(customer);
        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                model.addRow(new Object[]{
                        order.getOrderId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        String.format("Rs%.2f", item.getProduct().getPrice()),
                        String.format("Rs%.2f", item.getProduct().getPrice() * item.getQuantity())
                });
            }
        }

        ordersTable = new JTable(model);
        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ordersTable.setRowHeight(28);
        ordersTable.getTableHeader().setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        ordersTable.getTableHeader().setBackground(new Color(27, 140, 89)); // Purple header
        ordersTable.getTableHeader().setForeground(Color.WHITE);
        ordersTable.setSelectionBackground(new Color(25, 143, 94));
        ordersTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        closeButton = createStyledButton("Close", new Color(231, 76, 60)); // red
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(30, 38, 44));
        btnPanel.add(closeButton);

        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);

        closeButton.addActionListener(e -> dispose());
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker().darker(), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        Color hoverColor = bgColor.brighter();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker().darker(), 2),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker().darker(), 2),
                        BorderFactory.createEmptyBorder(5, 15, 5, 15)
                ));
            }
        });

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
