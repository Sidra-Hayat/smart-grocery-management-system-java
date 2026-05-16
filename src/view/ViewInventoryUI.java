package view;

import manager.InventoryManager;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class ViewInventoryUI extends JFrame {
    private final InventoryManager inventoryManager;
    private JTable productTable;
    private JTextField searchField;
    private JButton searchButton;
    private List<Product> allProducts;  // store full product list for filtering


    // Professional UI Theme Colors
    private final Color backgroundColor = new Color(135, 211, 177);              // deep navy-blue
    private final Color rowBackground = new Color(255, 255, 255);                // slightly lighter rows
    private final Color tableTextColor = new Color(25, 1, 1);            // light gray text
    private final Color headerColor = new Color(37, 154, 105);                // vivid blue
    private final Color headerTextColor = Color.WHITE;
    private boolean modal;

    public ViewInventoryUI(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
        initializeUI();
        loadProducts();
    }

    private void initializeUI() {
        setTitle("Inventory");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);

        String[] columns = {"Product ID", "Name", "Price", "Quantity"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        productTable = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                comp.setBackground(row % 2 == 0 ? rowBackground : backgroundColor);
                comp.setForeground(tableTextColor);
                return comp;
            }
        };

        // Styling
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(28);
        productTable.setGridColor(new Color(144, 166, 181));
        productTable.setShowHorizontalLines(true);
        productTable.setSelectionBackground(headerColor.darker());
        productTable.setSelectionForeground(Color.WHITE);
        productTable.setForeground(tableTextColor);
        productTable.setBackground(backgroundColor);

        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(headerColor);
        productTable.getTableHeader().setForeground(headerTextColor);
        productTable.getTableHeader().setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(backgroundColor);

        panel.add(scrollPane, BorderLayout.CENTER);
        add(panel);

        // Create search bar components
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        // Add action listener to search button
        searchButton.addActionListener(e -> filterProducts());

        // Optional: Press Enter in text field triggers search
        searchField.addActionListener(e -> filterProducts());

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(backgroundColor);  // keep color theme consistent
        searchPanel.add(new JLabel("Search Product:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);  // Add search panel at the top
        panel.add(scrollPane, BorderLayout.CENTER);

        add(panel);

        loadProducts();  // loads full product list
    }
  public void loadProducts() {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0); // clear existing

        List<Product> allProducts = inventoryManager.getAllProducts();
        // store full list for filtering
        updateTable(allProducts);
    }
    private void updateTable(List<Product> products) {
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        model.setRowCount(0); // clear existing rows

        for (Product p : products) {
            Object[] row = {
                    p.getId(),
                    p.getName(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity()
            };
            model.addRow(row);
        }
    }
    private void filterProducts() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            updateTable(allProducts);  // no search term, show all
            return;
        }

        List<Product> filtered = allProducts.stream()
                .filter(p -> p.getName().toLowerCase().contains(query))
                .toList();

        updateTable(filtered);
    }



    public void setModal(boolean modal) {
        this.modal = modal;
    }

   
}
