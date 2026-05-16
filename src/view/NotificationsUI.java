package view;

import manager.NotificationManager;
import model.Notification;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class NotificationsUI extends JDialog {

    private final Color urgentRed = Color.decode("#D32F2F");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color lightBackground = Color.decode("#FFF8E1");
    private final Color darkText = Color.decode("#212121");

    public NotificationsUI(JFrame parent, NotificationManager notificationManager) {
        super(parent, "Urgent Alerts", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(lightBackground);

        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Only show urgent alerts (Low stock or critical messages)
        for (Notification n : notificationManager.getNotifications()) {
            String msg = n.getMessage().toLowerCase();
            if (msg.contains("low stock") || msg.contains("alert") || msg.contains("critical")) {
                String time = n.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                listModel.addElement("⚠ " + time + " - " + n.getMessage());
            }
        }

        JList<String> notificationList = new JList<>(listModel);
        notificationList.setFont(new Font("Segoe UI", Font.BOLD, 14));
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationList.setBackground(Color.WHITE);
        notificationList.setForeground(urgentRed);

        JScrollPane scrollPane = new JScrollPane(notificationList);

        JLabel titleLabel = new JLabel("⚠ Urgent Notifications", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setForeground(urgentRed);

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBackground(urgentRed);
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        closeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeBtn.setBackground(hoverYellow);
                closeBtn.setForeground(darkText);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeBtn.setBackground(urgentRed);
                closeBtn.setForeground(Color.WHITE);
            }
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(lightBackground);
        btnPanel.add(closeBtn);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
}
