package view;

import manager.*;
import model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class CustomerUI extends JFrame {
    private final InventoryManager inventoryManager;
    private final LoginManager loginManager;
    private final Customer customer;
    private final OrderHistoryManager orderHistoryManager;
    private final CashierManager cashierManager;
    private final NotificationManager notificationManager;


    private JButton viewProductsButton;
    private JButton placeOrderButton;
    private JButton registerButton;
    private JButton logoutButton;
    private JButton viewOrderHistoryButton;
    private ReportManager reportManager;

    public CustomerUI(InventoryManager inventoryManager, LoginManager loginManager,
                      Customer customer, OrderHistoryManager orderHistoryManager,
                      CashierManager cashierManager, NotificationManager notificationManager,
                      ReportManager reportManager) {
        this.inventoryManager = inventoryManager;
        this.loginManager = loginManager;
        this.customer = customer;
        this.orderHistoryManager = orderHistoryManager;
        this.cashierManager = cashierManager;
        this.notificationManager = notificationManager;
        this.reportManager = reportManager;
        initializeUI();
    }



    private void initializeUI() {
        setTitle("Customer Dashboard - Welcome " + customer.getName());
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load and scale the background image dynamically
        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/icons/customer.gif"));
        Image bgImage = bgIcon.getImage();

        // Custom panel that paints the background scaled to frame size
        JPanel backgroundPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setOpaque(false);
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(28, 30, 28, 30);

        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/customer11.jpg"));
        Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel welcomeLabel = new JLabel("  Welcome, " + customer.getName() + " (Customer)", scaledIcon, JLabel.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(1, 17, 28));

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        backgroundPanel.add(welcomeLabel, gbc);

        viewProductsButton = createStyledButton("View Products", new Color(15, 177, 109) , "grocery.jpg");
        placeOrderButton = createStyledButton("Place Order",  new Color(15, 177, 109) , "trolli.jpg");
        registerButton = createStyledButton("Register", new Color(15, 177, 109), "user_registeration.png");
        logoutButton = createStyledButton("Logout", new Color(118, 227, 178), "logout11.png");
        viewOrderHistoryButton = createStyledButton("Order History", new Color(15, 177, 109), "history2.png");

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        backgroundPanel.add(viewProductsButton, gbc);
        gbc.gridx = 1;
        backgroundPanel.add(placeOrderButton, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        backgroundPanel.add(viewOrderHistoryButton, gbc);
        gbc.gridx = 1;
        backgroundPanel.add(registerButton, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        backgroundPanel.add(logoutButton, gbc);

        // Add the background panel to the frame
        setContentPane(backgroundPanel);

        // Button actions
        viewProductsButton.addActionListener(this::onViewProducts);
        placeOrderButton.addActionListener(this::onPlaceOrder);
        registerButton.addActionListener(e -> openRegisterUI());
        logoutButton.addActionListener(e -> onLogout());
        viewOrderHistoryButton.addActionListener(this::onViewOrderHistory);
    }


    private JButton createStyledButton(String text, Color bgColor, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(240, 90)); // Slightly larger
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/icons/" + iconPath));
            Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        Color hoverColor = bgColor.brighter();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }



    private void onViewProducts(ActionEvent e) {
        ViewInventoryUI viewInventoryUI = new ViewInventoryUI(inventoryManager);
        viewInventoryUI.setVisible(true);
    }

    private void onPlaceOrder(ActionEvent e) {
        view.PlaceOrderUI placeOrderUI = new view.PlaceOrderUI(this, inventoryManager, customer, orderHistoryManager);
        placeOrderUI.setVisible(true);
    }

    private void openRegisterUI() {
        RegisterUI registerUI = new RegisterUI(loginManager);
        registerUI.setVisible(true);
    }

    private void onLogout() {
        new File("session.txt").delete(); // Delete saved session
        dispose();
        LoginUI loginUI = new LoginUI(loginManager,inventoryManager,cashierManager,notificationManager,reportManager);
        loginUI.setVisible(true);
    }


    private void onViewOrderHistory(ActionEvent e) {
        OrderHistoryUI orderHistoryUI = new OrderHistoryUI(orderHistoryManager, customer);
        orderHistoryUI.setVisible(true);
    }

}