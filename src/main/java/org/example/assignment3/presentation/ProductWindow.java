package org.example.assignment3.presentation;

import org.example.assignment3.buisnessLogic.ProductBLL;
import org.example.assignment3.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductWindow extends JFrame {
    private ProductBLL productBLL;
    private JTable productsTable;
    private JTextField idField, nameField, priceField, stockField;

    public ProductWindow() {
        productBLL = new ProductBLL();

        setTitle("Product Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        formPanel.add(stockField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> addProduct());

        JButton editButton = new JButton("Edit Product");
        editButton.addActionListener(e -> editProduct());

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> deleteProduct());

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);

        refreshTable();
        JScrollPane tableScrollPane = new JScrollPane(productsTable);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshTable() {
        List<Product> products = productBLL.findAllProducts();

        JTable newTable = TableGenerator.createTable(products);
        productsTable = newTable;

        productsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productsTable.getSelectedRow() != -1) {
                populateFields(productsTable.getSelectedRow());
            }
        });

        Container parent = getContentPane();
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component panelComp : panel.getComponents()) {
                    if (panelComp instanceof JScrollPane) {
                        ((JScrollPane) panelComp).setViewportView(productsTable);
                        break;
                    }
                }
            }
        }

        revalidate();
        repaint();
    }
    private void populateFields(int row) {
        idField.setText(productsTable.getValueAt(row, 0).toString());
        nameField.setText(productsTable.getValueAt(row, 1).toString());
        priceField.setText(productsTable.getValueAt(row, 2).toString());
        stockField.setText(productsTable.getValueAt(row, 3).toString());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
    }

    private void addProduct() {
        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            Product product = new Product(name, price, stock);
            int id = productBLL.insertProduct(product);

            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Product added successfully!");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editProduct() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a product to edit!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());

            Product product = new Product(id, name, price, stock);
            boolean success = productBLL.updateProduct(product);

            if (success) {
                JOptionPane.showMessageDialog(this, "Product updated successfully!");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update product!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a product to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idField.getText());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this product?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = productBLL.deleteProduct(id);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Product deleted successfully!");
                    refreshTable();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete product!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}