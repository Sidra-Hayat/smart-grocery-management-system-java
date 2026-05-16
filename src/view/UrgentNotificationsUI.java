package view;

import manager.NotificationManager;
import model.Notification;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UrgentNotificationsUI extends JDialog {

    private final Color urgentRed = Color.decode("#D32F2F");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color lightBackground = Color.decode("#FFF8E1");
    private final Color darkText = Color.decode("#212121");

    public UrgentNotificationsUI(JFrame parent, NotificationManager notificationManager) {
        super(parent, "Urgent Notifications", true); // modal dialog

        setSize(420, 340);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(lightBackground);

        DefaultListModel<String> listModel = new DefaultListModel<>();

        // Filter urgent notifications (customize keywords as needed)
        List<Notification> all = notificationManager.getNotifications();
        for (Notification n : all) {
            String msgLower = n.getMessage().toLowerCase();
            if (msgLower.contains("low stock") || msgLower.contains("alert") || msgLower.contains("critical")) {
                String time = n.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                listModel.addElement("⚠ " + time + " - " + n.getMessage());
            }
        }

        if (listModel.isEmpty()) {
            listModel.addElement("No urgent notifications.");
        }

        JList<String> notificationList = new JList<>(listModel);
        notificationList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notificationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificationList.setBackground(Color.WHITE);
        notificationList.setForeground(urgentRed);

        JScrollPane scrollPane = new JScrollPane(notificationList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel titleLabel = new JLabel("⚠ Urgent Notifications", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 0, 8));
        titleLabel.setForeground(urgentRed);

        // Close button
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
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
