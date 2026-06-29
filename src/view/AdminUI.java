package view;

import javax.swing.*;
import java.awt.*;
import manager.*;

public class AdminUI extends JFrame {

    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color hoverYellow  = Color.decode("#FDD835");
    private final Color hoverRed     = Color.decode("#E53935");
    private final Color darkText     = Color.decode("#212121");
    private final Font  buttonFont   = new Font("Segoe UI", Font.BOLD, 14);

    public AdminUI(InventoryManager inventoryManager, CashierManager cashierManager,
                   NotificationManager notificationManager, LoginManager loginManager) {

        ReportManager reportManager = new ReportManager(inventoryManager, cashierManager);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        setTitle("Admin Panel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 768));

        // FIX: JPanel with paintComponent — NOT a JLabel — so buttons stay clickable
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            private final Image bg = loadImage();
            private Image loadImage() {
                try { return new ImageIcon(getClass().getResource("/icons/admin.png")).getImage(); }
                catch (Exception e) { return null; }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bg != null) g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                else { g.setColor(Color.decode("#E8F5E9")); g.fillRect(0, 0, getWidth(), getHeight()); }
            }
        };
        backgroundPanel.setOpaque(true);

        JPanel buttonPanel = new JPanel(new GridLayout(7, 1, 15, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(300, 420));

        JButton manageProducts   = btn("Manage Products",   false);
        JButton approveCustomers = btn("Approve Customers", false);
        JButton manageCustomers  = btn("Manage Customers",  false);
        JButton manageCashiers   = btn("Manage Cashiers",   false);
        JButton viewReports      = btn("View Reports",      false);
        JButton notifications    = btn("Notifications",     false);
        JButton logout           = btn("Logout",            true);

        manageProducts  .addActionListener(e -> new ProductManagerUI(inventoryManager).setVisible(true));
        approveCustomers.addActionListener(e -> new ApproveCustomersUI(loginManager).setVisible(true));
        manageCustomers .addActionListener(e -> new ManageCustomersUI(loginManager).setVisible(true));
        manageCashiers  .addActionListener(e -> new ManageCashiersUI(cashierManager, loginManager).setVisible(true));
        viewReports     .addActionListener(e -> new TopSellingReportUI(reportManager, inventoryManager).setVisible(true));
        notifications   .addActionListener(e -> {
            UrgentNotificationsUI d = new UrgentNotificationsUI(this, notificationManager);
            d.setVisible(true);
        });
        logout.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (ok == JOptionPane.YES_OPTION) {
                dispose();
                new LoginUI(loginManager, inventoryManager, cashierManager,
                        notificationManager, reportManager).setVisible(true);
            }
        });

        buttonPanel.add(manageProducts);
        buttonPanel.add(approveCustomers);
        buttonPanel.add(manageCustomers);
        buttonPanel.add(manageCashiers);
        buttonPanel.add(viewReports);
        buttonPanel.add(notifications);
        buttonPanel.add(logout);

        backgroundPanel.add(buttonPanel, new GridBagConstraints());
        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JButton btn(String text, boolean isLogout) {
        JButton b = new JButton(text);
        b.setFont(buttonFont);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setBackground(primaryGreen);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(isLogout ? hoverRed : hoverYellow);
                b.setForeground(darkText);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(primaryGreen);
                b.setForeground(Color.WHITE);
            }
        });
        return b;
    }
}