package view;

import manager.NotificationManager;
import model.Notification;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UrgentNotificationsUI extends JDialog {

    private final Color urgentRed       = Color.decode("#D32F2F");
    private final Color hoverYellow     = Color.decode("#FDD835");
    private final Color lightBackground = Color.decode("#FFF8E1");
    private final Color darkText        = Color.decode("#212121");

    public UrgentNotificationsUI(JFrame parent, NotificationManager notificationManager) {
        // FIX: pass null as parent so it never hides behind a maximized window
        super((Frame) null, "Urgent Notifications", false);

        setSize(480, 380);
        setLocationRelativeTo(null);      // center on screen
        setAlwaysOnTop(true);             // FIX: always visible above other windows
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(lightBackground);

        // ── Title ──────────────────────────────────────────────────────────
        JLabel titleLabel = new JLabel("⚠  Urgent Notifications", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(12, 8, 4, 8));
        titleLabel.setForeground(urgentRed);

        // ── Build notification list ────────────────────────────────────────
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Notification> all = notificationManager.getNotifications();

        for (Notification n : all) {
            String msgLower = n.getMessage().toLowerCase();
            if (msgLower.contains("low stock") || msgLower.contains("alert")
                    || msgLower.contains("critical")) {
                String time = n.getTimestamp()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm"));
                listModel.addElement("⚠  " + time + "  —  " + n.getMessage());
            }
        }

        if (listModel.isEmpty()) {
            listModel.addElement("✅  No urgent notifications at this time.");
        }

        JList<String> notificationList = new JList<>(listModel);
        notificationList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationList.setBackground(Color.WHITE);
        notificationList.setForeground(listModel.size() == 1
                && listModel.get(0).startsWith("✅")
                ? new Color(30, 130, 76)   // green for "no alerts"
                : urgentRed);
        notificationList.setFixedCellHeight(32);
        notificationList.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        // ── Count label ───────────────────────────────────────────────────
        int count = listModel.size() == 1 && listModel.get(0).startsWith("✅") ? 0 : listModel.size();
        JLabel countLabel = new JLabel(
                count == 0 ? "All stock levels are fine."
                        : count + " low-stock alert(s) found.",
                JLabel.CENTER);
        countLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        countLabel.setForeground(count == 0 ? new Color(30, 130, 76) : urgentRed);
        countLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 0, 8));

        // ── Close button ──────────────────────────────────────────────────
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBackground(urgentRed);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        closeBtn.addActionListener(e -> dispose());

        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                closeBtn.setBackground(hoverYellow);
                closeBtn.setForeground(darkText);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                closeBtn.setBackground(urgentRed);
                closeBtn.setForeground(Color.WHITE);
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(lightBackground);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, 0));
        btnPanel.add(closeBtn);

        // ── Assemble ──────────────────────────────────────────────────────
        JPanel centerPanel = new JPanel(new BorderLayout(0, 6));
        centerPanel.setBackground(lightBackground);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        centerPanel.add(scrollPane,  BorderLayout.CENTER);
        centerPanel.add(countLabel,  BorderLayout.SOUTH);

        add(titleLabel,   BorderLayout.NORTH);
        add(centerPanel,  BorderLayout.CENTER);
        add(btnPanel,     BorderLayout.SOUTH);

        // FIX: force to front after showing
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                toFront();
                requestFocus();
            }
        });
    }
}