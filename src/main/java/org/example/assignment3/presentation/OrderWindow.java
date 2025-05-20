package org.example.assignment3.presentation;

import org.example.assignment3.buisnessLogic.ClientBLL;
import org.example.assignment3.buisnessLogic.OrderBLL;
import org.example.assignment3.buisnessLogic.ProductBLL;
import org.example.assignment3.model.Client;
import org.example.assignment3.model.Order;
import org.example.assignment3.model.Product;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderWindow extends JFrame {
    private OrderBLL orderBLL;
    private ClientBLL clientBLL;
    private ProductBLL productBLL;

    private JComboBox<Client> clientComboBox;
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private JTable ordersTable;

    public OrderWindow() {
        orderBLL = new OrderBLL();
        clientBLL = new ClientBLL();
        productBLL = new ProductBLL();

        setTitle("Order Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Client:"));
        clientComboBox = new JComboBox<>();
        loadClients();
        formPanel.add(clientComboBox);

        formPanel.add(new JLabel("Product:"));
        productComboBox = new JComboBox<>();
        loadProducts();
        formPanel.add(productComboBox);

        formPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        formPanel.add(quantityField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton createOrderButton = new JButton("Create Order");
        createOrderButton.addActionListener(e -> createOrder());

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());

        buttonsPanel.add(createOrderButton);
        buttonsPanel.add(clearButton);

        refreshTable();
        JScrollPane tableScrollPane = new JScrollPane(ordersTable);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadClients() {
        clientComboBox.removeAllItems();

        List<Client> clients = clientBLL.findAllClients();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }
    }

    private void loadProducts() {
        productComboBox.removeAllItems();

        List<Product> products = productBLL.findAllProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    private void refreshTable() {
        List<Order> orders = orderBLL.findAllOrders();
        JTable newTable = TableGenerator.createTable(orders);
        ordersTable = newTable;
        Container parent = getContentPane();
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component panelComp : panel.getComponents()) {
                    if (panelComp instanceof JScrollPane) {
                        ((JScrollPane) panelComp).setViewportView(ordersTable);
                        break;
                    }
                }
            }
        }

        revalidate();
        repaint();
    }

    private void clearFields() {
        if (clientComboBox.getItemCount() > 0) {
            clientComboBox.setSelectedIndex(0);
        }

        if (productComboBox.getItemCount() > 0) {
            productComboBox.setSelectedIndex(0);
        }

        quantityField.setText("");
    }

    private void createOrder() {
        try {
            if (clientComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select a client!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (productComboBox.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Please select a product!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Client client = (Client) clientComboBox.getSelectedItem();
            Product product = (Product) productComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int orderId = orderBLL.createOrder(client.getId(), product.getId(), quantity);

            if (orderId > 0) {
                JOptionPane.showMessageDialog(this, "Order created successfully! Order ID: " + orderId);
                refreshTable();
                loadProducts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create order!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}