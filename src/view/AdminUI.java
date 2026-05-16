package view;

import javax.swing.*;
import java.awt.*;
import manager.*;
import model.Cashier;
import model.Product;

public class AdminUI extends JFrame {

    // Theme Colors
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color hoverRed = Color.decode("#E53935");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color darkText = Color.decode("#212121");
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    private LoginManager loginManager;
    private ReportManager reportManager;
    private NotificationManager notificationManager;

    public AdminUI(InventoryManager inventoryManager, CashierManager cashierManager,
                   NotificationManager notificationManager, LoginManager loginManager) {
        this.reportManager = new ReportManager(inventoryManager, cashierManager);
        this.notificationManager = notificationManager;
        this.loginManager = loginManager;

        setTitle("Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 768));

        // Background Image
        JLabel backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new GridBagLayout());
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/icons/admin.png"));
        Image bgImage = backgroundIcon.getImage();
        Image scaledBg = bgImage.getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH);
        backgroundLabel.setIcon(new ImageIcon(scaledBg));

        // Transparent button panel
        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(300, 400));

        // Buttons
        JButton manageProducts = styledButton("Manage Products", false);
        manageProducts.addActionListener(e -> new ProductManagerUI(inventoryManager).setVisible(true));

        JButton approveCustomers = styledButton("Approve Customers", false);
        approveCustomers.addActionListener(e -> new ApproveCustomersUI(loginManager).setVisible(true));

        JButton manageCustomers = styledButton("Manage Customers", false);
        manageCustomers.addActionListener(e -> new ManageCustomersUI(loginManager).setVisible(true));

        JButton manageCashiersBtn = styledButton("Manage Cashiers", false);
        manageCashiersBtn.addActionListener(e -> {
            ManageCashiersUI ui = new ManageCashiersUI(cashierManager, loginManager);
            ui.setVisible(true);
        });

        JButton viewReports = styledButton("View Reports", false);
        viewReports.addActionListener(e -> new TopSellingReportUI(reportManager, inventoryManager).setVisible(true));

        JButton notifications = styledButton("Notifications", false);
        notifications.addActionListener(e -> new UrgentNotificationsUI(this, notificationManager).setVisible(true));

        JButton logoutButton = styledButton("Logout", true);
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginUI(loginManager, inventoryManager, cashierManager, notificationManager, reportManager).setVisible(true);
            }
        });

        // Add buttons to panel
        buttonPanel.add(manageProducts);
        buttonPanel.add(approveCustomers);
        buttonPanel.add(manageCustomers);
        buttonPanel.add(manageCashiersBtn);
        buttonPanel.add(viewReports);
        buttonPanel.add(notifications);
        buttonPanel.add(logoutButton);

        backgroundLabel.add(buttonPanel, new GridBagConstraints());
        setContentPane(backgroundLabel);
        setVisible(true);
    }

    // Enhanced styledButton: supports red hover for logout button
    private JButton styledButton(String text, boolean isLogout) {
        JButton btn = new JButton(text);
        btn.setBackground(primaryGreen);
        btn.setForeground(Color.WHITE);
        btn.setFont(buttonFont);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isLogout) {
                    btn.setBackground(hoverRed);
                } else {
                    btn.setBackground(hoverYellow);
                }
                btn.setForeground(darkText);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(primaryGreen);
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }
}
