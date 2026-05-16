package view;

import manager.LoginManager;
import model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ApproveCustomersUI extends JDialog {

    // Theme Colors (same as AdminUI)
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color hoverRed = Color.decode("#E53935");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color darkText = Color.decode("#212121");
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    private final LoginManager loginManager;
    private JList<String> customerList;
    private DefaultListModel<String> listModel;

    public ApproveCustomersUI(LoginManager loginManager) {
        this.loginManager = loginManager;
        setTitle("Approve Customers");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        initializeUI();
    }

    private void initializeUI() {
        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerList.setBackground(lightBackground);
        customerList.setForeground(darkText);
        customerList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(customerList);
        scrollPane.setBorder(BorderFactory.createLineBorder(primaryGreen, 2));

        JLabel titleLabel = new JLabel("Unapproved Customers:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(primaryGreen);

        // Buttons styled with theme
        JButton approveButton = styledButton("Approve Selected", false);
        JButton closeButton = styledButton("Close", true);

        approveButton.addActionListener(e -> {
            try {
                String selected = customerList.getSelectedValue();
                if (selected != null && selected.contains(" - ")) {
                    String[] parts = selected.split(" - ");
                    if (parts.length == 2) {
                        String username = parts[1].trim();
                        boolean approved = loginManager.approveCustomer(username);
                        if (approved) {
                            JOptionPane.showMessageDialog(this, "Customer approved successfully!");
                            refreshCustomerList();
                        } else {
                            JOptionPane.showMessageDialog(this, "Error approving customer.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid customer entry format.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a customer to approve.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage());
            }
        });

        closeButton.addActionListener(e -> dispose());

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(approveButton);
        buttonPanel.add(closeButton);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(lightBackground);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshCustomerList();
    }

    private void refreshCustomerList() {
        listModel.clear();
        List<Customer> unapproved = loginManager.getUnapprovedCustomers();
        for (Customer c : unapproved) {
            listModel.addElement(c.getName() + " - " + c.getUsername());
        }
    }

    // Unified button styling (same theme as AdminUI)
    private JButton styledButton(String text, boolean isRed) {
        JButton btn = new JButton(text);
        btn.setFont(buttonFont);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setBackground(primaryGreen);
        btn.setForeground(Color.WHITE);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (isRed) {
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
