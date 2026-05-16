package view;

import javax.swing.*;
import java.awt.*;

public class AddProductDialog extends JDialog {
    private JTextField idField = new JTextField(5);
    private JTextField nameField = new JTextField(10);
    private JTextField priceField = new JTextField(5);
    private JTextField quantityField = new JTextField(5);
    private JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Perishable", "NonPerishable"});
    private JTextField expiryOrShelfField = new JTextField(10);

    private JLabel expiryOrShelfLabel;

    private boolean submitted = false;

    // Colors matching LoginUI / ProductManagerUI
    private final Color backgroundColor = new Color(34, 49, 63);          // dark blue-gray
    private final Color fieldBackgroundColor = new Color(236, 240, 241);  // light gray fields
    private final Color fieldTextColor = Color.BLACK;
    private final Color labelTextColor = Color.WHITE;

    private final Color okButtonColor = new Color(39, 174, 96);   // green
    private final Color cancelButtonColor = new Color(231, 76, 60); // red-ish

    private final Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
    private final Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

    public AddProductDialog(JFrame parent) {
        super(parent, "Add Product", true);

        setLayout(new GridBagLayout());
        getContentPane().setBackground(backgroundColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0 - Product ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(createLabel("Product ID:"), gbc);

        gbc.gridx = 1;
        add(createField(idField), gbc);

        // Row 1 - Product Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(createLabel("Product Name:"), gbc);

        gbc.gridx = 1;
        add(createField(nameField), gbc);

        // Row 2 - Price
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(createLabel("Price:"), gbc);

        gbc.gridx = 1;
        add(createField(priceField), gbc);

        // Row 3 - Quantity
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(createLabel("Quantity:"), gbc);

        gbc.gridx = 1;
        add(createField(quantityField), gbc);

        // Row 4 - Type ComboBox
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(createLabel("Type:"), gbc);

        gbc.gridx = 1;
        add(createComboBox(typeComboBox), gbc);

        // Row 5 - Expiry/Shelf Life Label and Field
        gbc.gridx = 0;
        gbc.gridy = 5;
        expiryOrShelfLabel = createLabel("Expiry Date or Shelf Life (months):");
        add(expiryOrShelfLabel, gbc);

        gbc.gridx = 1;
        add(createField(expiryOrShelfField), gbc);

        // Buttons row (Row 6)
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton okButton = new JButton("Add");
        styleButton(okButton, okButtonColor);

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, cancelButtonColor);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        // Dynamic label update based on product type
        typeComboBox.addActionListener(e -> {
            if ("Perishable".equals(typeComboBox.getSelectedItem())) {
                expiryOrShelfLabel.setText("Expiry Date (YYYY-MM-DD):");
            } else {
                expiryOrShelfLabel.setText("Shelf Life (months):");
            }
        });

        okButton.addActionListener(e -> {
            if (validateInputs()) {
                submitted = true;
                setVisible(false);
            }
        });

        cancelButton.addActionListener(e -> {
            submitted = false;
            setVisible(false);
        });

        pack();
        setLocationRelativeTo(parent);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(labelTextColor);
        label.setFont(labelFont);
        return label;
    }

    private JTextField createField(JTextField field) {
        field.setBackground(fieldBackgroundColor);
        field.setForeground(fieldTextColor);
        field.setFont(fieldFont);
        return field;
    }

    private JComboBox<String> createComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(fieldBackgroundColor);
        comboBox.setForeground(fieldTextColor);
        comboBox.setFont(fieldFont);
        return comboBox;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(buttonFont);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
    }

    private boolean validateInputs() {
        if (getProductId().isEmpty() || getProductName().isEmpty() || getPrice().isEmpty() || getQuantity().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(getPrice());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            Integer.parseInt(getQuantity());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if ("Perishable".equals(getProductType())) {
            String expiry = getExpiryOrShelfLife();
            if (expiry.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter expiry date.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            // Optionally, validate date format here
        } else {
            String shelfLife = getExpiryOrShelfLife();
            if (shelfLife.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter shelf life.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            try {
                Integer.parseInt(shelfLife);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid shelf life value.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public String getProductId() {
        return idField.getText().trim();
    }

    public String getProductName() {
        return nameField.getText().trim();
    }

    public String getPrice() {
        return priceField.getText().trim();
    }

    public String getQuantity() {
        return quantityField.getText().trim();
    }

    public String getProductType() {
        return (String) typeComboBox.getSelectedItem();
    }

    public String getExpiryOrShelfLife() {
        return expiryOrShelfField.getText().trim();
    }
}
