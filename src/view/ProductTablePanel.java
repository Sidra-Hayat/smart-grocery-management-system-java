package view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductTablePanel extends JPanel {
    private JTable productTable;
    private DefaultTableModel tableModel;

    // Colors matching your dark theme
    private final Color backgroundColor = new Color(15, 177, 109);         // Dark slate background
    private final Color tableBackgroundColor = new Color(110, 205, 165);    // Darker blue-gray for table cells
    private final Color textColor = new Color(1, 17, 28);            // Soft white text for contrast
    private final Color headerBackground = new Color(39, 174, 96);       // Bright green header
    private final Color headerTextColor = Color.WHITE;
    private final Color selectionBackground = new Color(41, 128, 185);   // Bright blue for selection
    private final Color selectionTextColor = Color.WHITE;
    private final Color gridColor = new Color(70, 85, 95);               // Subtle grid lines

    public ProductTablePanel() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);

        String[] columnNames = {"ID", "Name", "Price", "Quantity", "Type", "Expiry Date", "Shelf Life"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);

        // Table styling
        productTable.setBackground(tableBackgroundColor);
        productTable.setForeground(textColor);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.setRowHeight(26);
        productTable.setSelectionBackground(selectionBackground);
        productTable.setSelectionForeground(selectionTextColor);
        productTable.setGridColor(gridColor);
        productTable.setShowGrid(true);
        productTable.setFillsViewportHeight(true);
        productTable.setAutoCreateRowSorter(true);  // Allow sorting on columns

        // Header styling
        productTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(headerBackground);
                label.setForeground(headerTextColor);
                label.setFont(new Font("Segoe UI", Font.BOLD, 16));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                return label;
            }
        });


        // Set header renderer for all columns
        productTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                label.setBackground(headerBackground);
                label.setForeground(headerTextColor);
                label.setFont(new Font("Segoe UI", Font.BOLD, 16));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                return label;
            }
        });

        // Center-align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.getViewport().setBackground(tableBackgroundColor);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addProductRow(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTable getProductTable() {
        return productTable;
    }
}
