package view;

import manager.LoginManager;
import model.Customer;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JFrame {
    private final LoginManager loginManager;

    private JTextField idField;
    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton cancelButton;

    private final Color backgroundColor = new Color(15, 177, 109);      // dark blue-gray
    private final Color fieldBackground = new Color(255, 255, 255);   // light gray
    private final Color textColor = new Color(15, 177, 109);            // dark text
    private final Color labelColor = Color.BLACK;
    private final Color registerBtnColor = new Color(10, 104, 65);    // green
    private final Color cancelBtnColor = new Color(192, 57, 43);      // red

    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
    private final Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 15);

    public RegisterUI(LoginManager loginManager) {
        this.loginManager = loginManager;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Customer Registration");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(backgroundColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel idLabel = createStyledLabel("Customer ID:");
        JLabel nameLabel = createStyledLabel("Name:");
        JLabel usernameLabel = createStyledLabel("Username:");
        JLabel passwordLabel = createStyledLabel("Password:");

        idField = createStyledTextField();
        nameField = createStyledTextField();
        usernameField = createStyledTextField();
        passwordField = new JPasswordField(20);
        passwordField.setFont(fieldFont);
        passwordField.setBackground(fieldBackground);
        passwordField.setForeground(textColor);

        registerButton = createStyledButton("Register", registerBtnColor);
        cancelButton = createStyledButton("Cancel", cancelBtnColor);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(registerButton, gbc);
        gbc.gridx = 1;
        panel.add(cancelButton, gbc);

        add(panel);

        registerButton.addActionListener(e -> handleRegister());
        cancelButton.addActionListener(e -> dispose());
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(labelColor);
        label.setFont(labelFont);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(fieldFont);
        field.setBackground(fieldBackground);
        field.setForeground(textColor);
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void handleRegister() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (id.isEmpty() || name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (loginManager.isUsernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "Username already taken. Choose another.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int points = 0;
        Customer newCustomer = new Customer(id, name, username, password, points, false);
        loginManager.registerUser(newCustomer);

        JOptionPane.showMessageDialog(this, "Registration submitted for approval. Please wait.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

}
