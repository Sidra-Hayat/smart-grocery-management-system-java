package view;

import manager.OrderHistoryManager;
import model.Customer;
import model.Order;
import model.OrderItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderHistoryUI extends JFrame {
    private final OrderHistoryManager orderHistoryManager;
    private final Customer customer;

    private JTable ordersTable;
    private JButton closeButton;

    // ── Theme — matches CustomerUI / PlaceOrderUI / ViewInventoryUI ────────
    private final Color primaryGreen    = Color.decode("#2E7D32");
    private final Color darkGreen       = Color.decode("#1B5E20");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color alternateRow    = Color.decode("#DCEDC8");
    private final Color darkText        = Color.decode("#212121");
    private final Color hoverYellow     = Color.decode("#FDD835");

    public OrderHistoryUI(OrderHistoryManager orderHistoryManager, Customer customer) {
        this.orderHistoryManager = orderHistoryManager;
        this.customer = customer;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Order History - " + customer.getName());
        setSize(760, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(lightBackground);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ── Title ────────────────────────────────────────────────────────
        JLabel title = new JLabel("Order History", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(primaryGreen);
        panel.add(title, BorderLayout.NORTH);

        // ── Table ────────────────────────────────────────────────────────
        String[] columns = {"#", "Order Date & Time", "Product Name", "Qty", "Price/Unit", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };

        List<Order> orders = orderHistoryManager.getOrdersByCustomer(customer);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm");
        int rowNum = 1;
        for (Order order : orders) {
            // Parse timestamp from orderId (format: ORD<epochMillis>)
            String dateStr;
            try {
                long millis = Long.parseLong(order.getOrderId().replace("ORD", ""));
                LocalDateTime ldt = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(millis), ZoneId.systemDefault());
                dateStr = ldt.format(fmt);
            } catch (Exception ex) {
                dateStr = order.getOrderId();
            }
            for (OrderItem item : order.getItems()) {
                model.addRow(new Object[]{
                        rowNum++,
                        dateStr,
                        item.getProduct().getName(),
                        item.getQuantity(),
                        String.format("Rs%.2f", item.getProduct().getPrice()),
                        String.format("Rs%.2f", item.getProduct().getPrice() * item.getQuantity())
                });
            }
        }

        ordersTable = new JTable(model) {
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

        ordersTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ordersTable.setRowHeight(30);
        ordersTable.setShowGrid(false);
        ordersTable.setIntercellSpacing(new Dimension(0, 0));
        ordersTable.setSelectionBackground(primaryGreen);
        ordersTable.setSelectionForeground(Color.WHITE);

        ordersTable.getTableHeader().setBackground(primaryGreen);
        ordersTable.getTableHeader().setForeground(Color.WHITE);
        ordersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        ordersTable.getTableHeader().setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollPane = new JScrollPane(ordersTable);
        scrollPane.getViewport().setBackground(lightBackground);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // ── Close button ─────────────────────────────────────────────────
        closeButton = createButton("Close", primaryGreen);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(lightBackground);
        btnPanel.add(closeButton);
        panel.add(btnPanel, BorderLayout.SOUTH);

        add(panel);

        closeButton.addActionListener(e -> dispose());
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));

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