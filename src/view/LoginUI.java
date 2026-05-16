package view;

import manager.*;
import model.Admin;
import model.Cashier;
import model.Customer;
import model.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginUI extends JFrame {
    private final InventoryManager inventoryManager;
    private final LoginManager loginManager;
    private final CashierManager cashierManager;
    private final NotificationManager notificationManager;
    private final ReportManager reportManager;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton registerButton;

    public LoginUI(LoginManager loginManager, InventoryManager inventoryManager, CashierManager cashierManager, NotificationManager notificationManager, ReportManager reportManager) {

        this.loginManager = loginManager;
        this.inventoryManager = inventoryManager;
        this.cashierManager = cashierManager;
        this.notificationManager = notificationManager;
        this.reportManager = reportManager;

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login - Grocery Store");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === TOP PANEL ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(3, 85, 51));
        topPanel.setPreferredSize(new Dimension(getWidth(), 100));

        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icons/lara_logo1.png"));
        Image scaledLogo = logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_AREA_AVERAGING);

        logoIcon = new ImageIcon(scaledLogo);

        JPanel logoPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(141, 223, 189));
                g2.fillOval(0, 0, getWidth(), getHeight());
            }
        };
        logoPanel.setOpaque(false);
      //  logoPanel.setPreferredSize(new Dimension(100, 100));
        logoPanel.setLayout(new GridBagLayout());
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setPreferredSize(new Dimension(90, 90));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);
        logoPanel.add(logoLabel);


        JLabel titleLabel = new JLabel("LARA GROCERY STORE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);

        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(logoPanel);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        // === CENTER PANEL (Image + Form) ===
// Custom JPanel with background image
        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            private Image bg = new ImageIcon(getClass().getResource("/icons/BG_login.jpg")).getImage();


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }

        };
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);


        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Let background panel show through
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(10, 10, 10, 10);
        fgbc.fill = GridBagConstraints.HORIZONTAL;

        // Role
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        roleLabel.setForeground(Color.WHITE);

        roleComboBox = new JComboBox<>(new String[]{"Customer", "Admin", "Cashier"});
        roleComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 18));  // Added font for combo box
        roleComboBox.setBackground(Color.WHITE);

        fgbc.gridx = 0; fgbc.gridy = 0;
        formPanel.add(roleLabel, fgbc);
        fgbc.gridx = 1;
        formPanel.add(roleComboBox, fgbc);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        userLabel.setForeground(Color.WHITE);

        usernameField = new JTextField(30);   // Increased columns
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        usernameField.setBackground(new Color(255, 255, 255, 200));
        usernameField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        usernameField.setCaretColor(Color.BLACK);
// Increased font size

        fgbc.gridx = 0; fgbc.gridy = 1;
        formPanel.add(userLabel, fgbc);
        fgbc.gridx = 1;
        formPanel.add(usernameField, fgbc);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        passLabel.setForeground(Color.WHITE);


        passwordField = new JPasswordField(30);  // Initialize first
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        passwordField.setBackground(new Color(255, 255, 255, 200));  // Style after initialization
        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        passwordField.setCaretColor(Color.BLACK);
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        });


        fgbc.gridx = 0; fgbc.gridy = 2;
        formPanel.add(passLabel, fgbc);
        fgbc.gridx = 1;
        formPanel.add(passwordField, fgbc);

        // Buttons
        ImageIcon loginIcon = new ImageIcon(getClass().getResource("/icons/login1.png"));
        loginIcon = new ImageIcon(loginIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        loginButton = createStyledButton("Login", loginIcon);

        ImageIcon regIcon = new ImageIcon(getClass().getResource("/icons/registeration.png"));
        regIcon = new ImageIcon(regIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        registerButton = createStyledButton("Register", regIcon);

        fgbc.gridx = 0; fgbc.gridy = 3;
        formPanel.add(loginButton, fgbc);
        fgbc.gridx = 1;
        formPanel.add(registerButton, fgbc);

        // Create glass-effect wrapper panel
        JPanel glassPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(57, 189, 110, 255)); // Semi-transparent white
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        formPanel.setBackground(new Color(0, 0, 0, 80)); // semi-transparent dark
        formPanel.setOpaque(true);

        glassPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createMatteBorder(2, 2, 10, 10, new Color(0, 0, 0, 80))  // drop shadow
        ));
        glassPanel.add(formPanel); // place form inside glass

// Add to center panel
        // Move form to right (column 2)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST; // aligns it to top-right
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(50, 0, 50, 80); // Top, Left, Bottom, Right
        centerPanel.add(glassPanel, gbc);

// Add an empty label to take left space (so trolley image is visible)
        JLabel emptyLabel = new JLabel();
        gbc.gridx = 0;
        gbc.weightx = 0.5;
        centerPanel.add(emptyLabel, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegisterUI());

        setVisible(true);
    }

    private JButton createStyledButton(String text, Icon icon) {
        JButton button = new JButton(text, icon);
        button.setBackground(new Color(5, 80, 50));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));        // Increased font size
        button.setPreferredSize(new Dimension(160, 50));           // Increased button size
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;

    }


    private void handleLogin() {
        String role = (String) roleComboBox.getSelectedItem();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (role.equals("Customer") || role.equals("Admin")) {
            Person user = loginManager.login(username, password);
            if (user != null) {
                if (role.equals("Admin") && user instanceof Admin) {
                    JOptionPane.showMessageDialog(this, "Welcome Admin " + user.getName());
                    this.dispose();
                    new AdminUI(inventoryManager, cashierManager, notificationManager, loginManager).setVisible(true);
                } else if (role.equals("Customer") && user instanceof Customer) {
                    JOptionPane.showMessageDialog(this, "Welcome " + user.getName());
                    this.dispose();
                    new CustomerUI(inventoryManager, loginManager, (Customer) user,
                            new OrderHistoryManager(), cashierManager, notificationManager, reportManager).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Selected role doesn't match your account type.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or account not approved yet.");
            }
        } else if (role.equals("Cashier")) {
            Cashier cashier = loginManager.loginAsCashier(username, password);
            if (cashier != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + cashier.getName());
                this.dispose();
                new CashierUI(cashier, inventoryManager,notificationManager).setVisible(true); // no NotificationManager
            } else {
                JOptionPane.showMessageDialog(this, "Invalid cashier credentials.");
            }
        }

    }

    private void openRegisterUI() {
        new RegisterUI(loginManager).setVisible(true);
    }
}
