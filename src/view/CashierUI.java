package view;

import manager.CashierManager;
import manager.InventoryManager;
import manager.LoginManager;
import manager.NotificationManager;
import manager.ReportManager;
import model.Cashier;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CashierUI extends JFrame {

    private final InventoryManager inventoryManager;
    private final Cashier cashier;
    private final NotificationManager notificationManager;
    private final LoginManager loginManager;
    private final CashierManager cashierManager;
    private final ReportManager reportManager;

    private JTable productTable;
    private DefaultTableModel tableModel;
    private JLabel totalSalesLabel;
    private JTextField searchField;

    // Theme - matches Admin/Customer green theme
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color headerGreen  = Color.decode("#1B5E20");
    private final Color hoverYellow  = Color.decode("#FDD835");
    private final Color hoverRed     = Color.decode("#E53935");
    private final Color lightBg      = Color.decode("#F1F8E9");
    private final Color rowAlt       = Color.decode("#DCEDC8");
    private final Color darkText     = Color.decode("#212121");
    private final Font  titleFont    = new Font("Segoe UI", Font.BOLD, 22);
    private final Font  buttonFont   = new Font("Segoe UI", Font.BOLD, 14);
    private final Font  tableFont    = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font  headerFont   = new Font("Segoe UI", Font.BOLD, 14);

    // ── Constructor: now receives all managers so logout can open LoginUI ──
    public CashierUI(Cashier cashier, InventoryManager inventoryManager,
                     NotificationManager notificationManager,
                     LoginManager loginManager, CashierManager cashierManager,
                     ReportManager reportManager) {
        this.cashier             = cashier;
        this.inventoryManager    = inventoryManager;
        this.notificationManager = notificationManager;
        this.loginManager        = loginManager;
        this.cashierManager      = cashierManager;
        this.reportManager       = reportManager;
        initializeUI();
    }

    // ── Backward-compatible constructor (used in Main.java today) ──────────
    public CashierUI(Cashier cashier, InventoryManager inventoryManager,
                     NotificationManager notificationManager) {
        this(cashier, inventoryManager, notificationManager, null, null, null);
    }

    private void initializeUI() {
        setTitle("Cashier Panel - " + cashier.getName());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(lightBg);
        setLayout(new BorderLayout());

        add(buildTopPanel(),    BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        loadProducts();
        setVisible(true);
    }

    // ── TOP: green header bar ──────────────────────────────────────────────
    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(primaryGreen);
        top.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("🛒  Cashier Panel");
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);

        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy  HH:mm"));
        JLabel info = new JLabel("Welcome, " + cashier.getName() + "   |   " + date);
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        info.setForeground(Color.WHITE);

        top.add(title, BorderLayout.WEST);
        top.add(info,  BorderLayout.EAST);
        return top;
    }

    // ── CENTER: info label + search + table ───────────────────────────────
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setBackground(lightBg);
        center.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        // Info banner - explains what cashier sees
        JLabel infoLabel = new JLabel(
                "⚠  Only products ordered by customers are shown below. Select a row and click Sell Product.");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        infoLabel.setForeground(new Color(91, 100, 60));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchBar.setBackground(lightBg);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(buttonFont);
        searchLabel.setForeground(darkText);

        searchField = new JTextField(25);
        searchField.setFont(tableFont);
        searchField.setPreferredSize(new Dimension(250, 32));

        JButton searchBtn = styledButton("Search", false);
        searchBtn.setPreferredSize(new Dimension(110, 32));
        searchBtn.addActionListener(e -> filterProducts());
        searchField.addActionListener(e -> filterProducts());

        JButton refreshBtn = styledButton("Refresh", false);
        refreshBtn.setPreferredSize(new Dimension(110, 32));
        refreshBtn.addActionListener(e -> { searchField.setText(""); loadProducts(); });

        searchBar.add(searchLabel);
        searchBar.add(searchField);
        searchBar.add(searchBtn);
        searchBar.add(refreshBtn);

        JPanel northSection = new JPanel(new BorderLayout());
        northSection.setBackground(lightBg);
        northSection.add(infoLabel, BorderLayout.NORTH);
        northSection.add(searchBar, BorderLayout.SOUTH);

        center.add(northSection,  BorderLayout.NORTH);
        center.add(buildTable(),  BorderLayout.CENTER);
        return center;
    }

    private JScrollPane buildTable() {
        String[] cols = {"Product ID", "Product Name", "Category", "Price (Rs)", "Ordered Qty"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        productTable = new JTable(tableModel);
        productTable.setFont(tableFont);
        productTable.setRowHeight(28);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setShowGrid(false);
        productTable.setIntercellSpacing(new Dimension(0, 0));

        // Alternating row colours
        productTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, focus, row, col);
                if (sel) {
                    c.setBackground(new Color(200, 230, 201));
                    c.setForeground(darkText);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : rowAlt);
                    c.setForeground(darkText);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return c;
            }
        });

        JTableHeader header = productTable.getTableHeader();
        header.setFont(headerFont);
        header.setBackground(headerGreen);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);

        productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(260);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(productTable);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        return scroll;
    }

    // ── BOTTOM: daily total + buttons ─────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(lightBg);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 20, 14, 20));

        totalSalesLabel = new JLabel("Daily Total Sales:  Rs 0.00");
        totalSalesLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalSalesLabel.setForeground(primaryGreen);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setBackground(lightBg);

        JButton sellBtn    = styledButton("✔  Sell Product",    false);
        JButton viewInvBtn = styledButton("📋  View Inventory", false);
        JButton logoutBtn  = styledButton("🚪  Logout",         true);

        sellBtn.setPreferredSize(new Dimension(160, 40));
        viewInvBtn.setPreferredSize(new Dimension(170, 40));
        logoutBtn.setPreferredSize(new Dimension(130, 40));

        sellBtn.addActionListener(e -> sellSelectedProduct());
        viewInvBtn.addActionListener(e -> new ViewInventoryUI(inventoryManager).setVisible(true));
        logoutBtn.addActionListener(e -> handleLogout());

        btnPanel.add(sellBtn);
        btnPanel.add(viewInvBtn);
        btnPanel.add(logoutBtn);

        bottom.add(totalSalesLabel, BorderLayout.WEST);
        bottom.add(btnPanel,        BorderLayout.EAST);
        return bottom;
    }

    // ── LOGOUT: close this window, open LoginUI ────────────────────────────
    private void handleLogout() {
        int ok = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION) return;

        dispose();

        // If managers were passed in, go back to LoginUI
        if (loginManager != null && cashierManager != null && reportManager != null) {
            SwingUtilities.invokeLater(() ->
                    new LoginUI(loginManager, inventoryManager, cashierManager,
                            notificationManager, reportManager).setVisible(true)
            );
        }
        // else (old constructor): just close, no redirect needed
    }

    // ── DATA: only products ordered by customers ───────────────────────────
    private void loadProducts() {
        tableModel.setRowCount(0);
        // getOrderedProducts() returns only products that customers placed orders for
        List<Product> products = inventoryManager.getOrderedProducts();

        if (products.isEmpty()) {
            // Show a helpful empty-state message via a disabled row
            tableModel.addRow(new Object[]{
                    "-", "No customer orders pending.", "-", "-", "-"
            });
            return;
        }

        for (Product p : products) {
            String category = (p.getDaysToExpiry() >= 0) ? "Perishable" : "Non-Perishable";
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    category,
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity()
            });
        }
    }

    private void filterProducts() {
        String query = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        for (Product p : inventoryManager.getOrderedProducts()) {
            if (p.getName().toLowerCase().contains(query)
                    || p.getId().toLowerCase().contains(query)) {
                String category = (p.getDaysToExpiry() >= 0) ? "Perishable" : "Non-Perishable";
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getName(),
                        category,
                        String.format("%.2f", p.getPrice()),
                        p.getQuantity()
                });
            }
        }
    }

    // ── SELL ───────────────────────────────────────────────────────────────
    private void sellSelectedProduct() {
        int row = productTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a product from the table first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Guard: ignore the empty-state placeholder row
        if (tableModel.getValueAt(row, 0).toString().equals("-")) return;

        String id      = tableModel.getValueAt(row, 0).toString();
        String name    = tableModel.getValueAt(row, 1).toString();
        int    inStock = Integer.parseInt(tableModel.getValueAt(row, 4).toString());

        Product product = inventoryManager.getProductById(id);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Product not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this,
                "Product : " + name + "\nOrdered Qty : " + inStock
                        + "\n\nEnter quantity to sell:",
                "Sell Product", JOptionPane.QUESTION_MESSAGE);
        if (qtyStr == null || qtyStr.trim().isEmpty()) return;

        int qty;
        try {
            qty = Integer.parseInt(qtyStr.trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid positive number.",
                    "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (qty > inStock) {
            JOptionPane.showMessageDialog(this,
                    "Cannot sell more than ordered!\nOrdered: " + inStock + "   Requested: " + qty,
                    "Quantity Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = inventoryManager.sellProduct(id, qty);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Sale failed. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double saleAmount = product.getPrice() * qty;
        cashier.addSale(saleAmount);
        cashier.recordProductSale(id, qty);

        // Save sales to file immediately so data persists on rerun
        if (cashierManager != null) cashierManager.saveCashiersToFile();

        totalSalesLabel.setText(String.format("Daily Total Sales:  Rs %.2f", cashier.getTotalSales()));

        if (searchField.getText().trim().isEmpty()) loadProducts();
        else filterProducts();

        showReceipt(name, qty, product.getPrice(), saleAmount);
    }

    // ── RECEIPT ────────────────────────────────────────────────────────────
    private void showReceipt(String productName, int qty, double unitPrice, double total) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss"));

        String receipt =
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "       LARA GROCERY STORE\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "Date & Time : " + time + "\n" +
                        "Cashier     : " + cashier.getName() + "\n" +
                        "─────────────────────────────────\n" +
                        "Product     : " + productName + "\n" +
                        "Qty Sold    : " + qty + "\n" +
                        "Unit Price  : Rs " + String.format("%.2f", unitPrice) + "\n" +
                        "─────────────────────────────────\n" +
                        String.format("TOTAL       : Rs %.2f\n", total) +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                        "       Thank you! Come again.\n" +
                        "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";

        JTextArea textArea = new JTextArea(receipt);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        textArea.setBackground(new Color(255, 255, 245));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JOptionPane.showMessageDialog(this, textArea, "Sale Receipt", JOptionPane.PLAIN_MESSAGE);
    }

    // ── STYLED BUTTON ──────────────────────────────────────────────────────
    private JButton styledButton(String text, boolean isLogout) {
        JButton btn = new JButton(text);
        btn.setFont(buttonFont);
        btn.setBackground(primaryGreen);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(isLogout ? hoverRed : hoverYellow);
                btn.setForeground(isLogout ? Color.WHITE : darkText);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(primaryGreen);
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }
}