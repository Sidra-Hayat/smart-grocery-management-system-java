package view;

import model.Customer;
import manager.LoginManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ManageCustomersUI extends JFrame {

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

    public ManageCustomersUI(LoginManager loginManager) {
        this.loginManager = loginManager;
        setTitle("Manage Customers");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initializeUI();
    }

    private void initializeUI() {
        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(lightBackground);

        // Title Label
        JLabel titleLabel = new JLabel("Registered Customers:");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(primaryGreen);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // List
        listModel = new DefaultListModel<>();
        customerList = new JList<>(listModel);
        customerList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customerList.setBackground(lightBackground);
        customerList.setForeground(darkText);
        JScrollPane scrollPane = new JScrollPane(customerList);
        scrollPane.setBorder(BorderFactory.createLineBorder(primaryGreen, 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton deleteButton = styledButton("Delete Selected Customer", true);
        deleteButton.addActionListener(e -> deleteSelectedCustomer());

        JButton refreshButton = styledButton("Refresh List", false);
        refreshButton.addActionListener(e -> refreshCustomerList());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(deleteButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshCustomerList();
    }

    private void refreshCustomerList() {
        listModel.clear();
        List<Customer> customers = loginManager.getAllCustomers(); // Ensure this method exists
        for (Customer customer : customers) {
            listModel.addElement(customer.getUsername() + " (" + customer.getName() + ")");
        }
    }

    private void deleteSelectedCustomer() {
        int selectedIndex = customerList.getSelectedIndex();
        if (selectedIndex != -1) {
            String selected = listModel.get(selectedIndex);
            String username = selected.split(" ")[0];
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete customer: " + username + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = loginManager.deleteCustomer(username); // Ensure this method exists
                if (success) {
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
                    refreshCustomerList();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to delete.");
        }
    }

    // Consistent button style (same as AdminUI and ApproveCustomersUI)
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
