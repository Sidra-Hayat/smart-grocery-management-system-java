package view;

import manager.CashierManager;
import manager.LoginManager;
import model.Cashier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManageCashiersUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private final CashierManager cashierManager;
    private final LoginManager loginManager;

    // Theme Colors
    private final Color primaryGreen = Color.decode("#2E7D32");
    private final Color hoverYellow = Color.decode("#FDD835");
    private final Color lightBackground = Color.decode("#F1F8E9");
    private final Color darkText = Color.decode("#212121");

    public ManageCashiersUI(CashierManager cashierManager, LoginManager loginManager) {
        this.cashierManager = cashierManager;
        this.loginManager = loginManager;

        setTitle("Manage Cashiers");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background
        getContentPane().setBackground(lightBackground);
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Manage Cashiers", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(primaryGreen);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Username", "Salary"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(darkText);
        table.setBackground(Color.WHITE);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(primaryGreen);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(lightBackground);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JButton addBtn = createStyledButton("Add");
        JButton editBtn = createStyledButton("Edit");
        JButton deleteBtn = createStyledButton("Delete");
        JButton refreshBtn = createStyledButton("Refresh");

        addBtn.addActionListener(e -> addCashier());
        editBtn.addActionListener(e -> editCashier());
        deleteBtn.addActionListener(e -> deleteCashier());
        refreshBtn.addActionListener(e -> loadCashiers());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(lightBackground);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        cashierManager.loadCashiersFromFile();
        loadCashiers();
    }

    /** Utility method for consistent button style **/
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(primaryGreen);
        button.setForeground(darkText);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverYellow);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryGreen);
            }
        });
        return button;
    }

    private void loadCashiers() {
        tableModel.setRowCount(0);
        for (Cashier c : cashierManager.getAllCashiers()) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getUsername(), c.getSalary()});
        }
    }

    private void addCashier() {
        String name = JOptionPane.showInputDialog(this, "Enter Cashier Name:");
        if (name == null || name.trim().isEmpty()) return;

        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        if (username == null || username.trim().isEmpty()) return;

        String password = JOptionPane.showInputDialog(this, "Enter Password:");
        if (password == null || password.trim().isEmpty()) return;

        String salaryStr = JOptionPane.showInputDialog(this, "Enter Salary:");
        if (salaryStr == null) return;

        double salary;
        try {
            salary = Double.parseDouble(salaryStr.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary!");
            return;
        }

        boolean taken = cashierManager.getAllCashiers().stream()
                .anyMatch(c -> c.getUsername().equalsIgnoreCase(username));
        if (taken) {
            JOptionPane.showMessageDialog(this, "Username already exists!");
            return;
        }

        String id = "C" + System.currentTimeMillis();
        Cashier newCashier = new Cashier(id, name.trim(), username.trim(), password, salary);

        cashierManager.addCashier(newCashier);
        loginManager.registerUser(newCashier);

        JOptionPane.showMessageDialog(this, "Cashier added successfully!");
        loadCashiers();
    }

    private void editCashier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a cashier to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        Cashier cashier = cashierManager.getCashierById(id);
        if (cashier == null) return;

        String name = JOptionPane.showInputDialog(this, "Edit Name:", cashier.getName());
        if (name != null && !name.trim().isEmpty()) cashier.setName(name.trim());

        String password = JOptionPane.showInputDialog(this, "Edit Password:", cashier.getPassword());
        if (password != null && !password.isEmpty()) cashier.setPassword(password);

        String salaryStr = JOptionPane.showInputDialog(this, "Edit Salary:", String.valueOf(cashier.getSalary()));
        if (salaryStr != null && !salaryStr.trim().isEmpty()) {
            try {
                cashier.setSalary(Double.parseDouble(salaryStr.trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid salary!");
            }
        }

        cashierManager.updateCashier(cashier);
        loginManager.updateUser(cashier);
        JOptionPane.showMessageDialog(this, "Cashier updated successfully!");
        loadCashiers();
    }

    private void deleteCashier() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a cashier to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        cashierManager.removeCashierById(id);
        loginManager.deleteUserById(id);
        JOptionPane.showMessageDialog(this, "Cashier deleted successfully!");
        loadCashiers();
    }
}
