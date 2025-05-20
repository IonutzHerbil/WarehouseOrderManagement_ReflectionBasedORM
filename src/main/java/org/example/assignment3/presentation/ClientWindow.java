package org.example.assignment3.presentation;

import org.example.assignment3.buisnessLogic.ClientBLL;
import org.example.assignment3.model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClientWindow extends JFrame {
    private ClientBLL clientBLL;
    private JTable clientsTable;
    private JTextField nameField, addressField, emailField, phoneField;
    private JTextField idField;

    public ClientWindow() {
        clientBLL = new ClientBLL();

        setTitle("Client Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Client");
        addButton.addActionListener(e -> addClient());

        JButton editButton = new JButton("Edit Client");
        editButton.addActionListener(e -> editClient());

        JButton deleteButton = new JButton("Delete Client");
        deleteButton.addActionListener(e -> deleteClient());

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());

        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(clearButton);

        refreshTable();
        JScrollPane tableScrollPane = new JScrollPane(clientsTable);

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void refreshTable() {
        List<Client> clients = clientBLL.findAllClients();

        JTable newTable = TableGenerator.createTable(clients);
        clientsTable = newTable;

        clientsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && clientsTable.getSelectedRow() != -1) {
                populateFields(clientsTable.getSelectedRow());
            }
        });

        Container parent = getContentPane();
        for (Component comp : parent.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                for (Component panelComp : panel.getComponents()) {
                    if (panelComp instanceof JScrollPane) {
                        ((JScrollPane) panelComp).setViewportView(clientsTable);
                        break;
                    }
                }
            }
        }

        revalidate();
        repaint();
    }

    private void populateFields(int row) {
        idField.setText(clientsTable.getValueAt(row, 0).toString());
        nameField.setText(clientsTable.getValueAt(row, 1).toString());
        addressField.setText(clientsTable.getValueAt(row, 2).toString());
        emailField.setText(clientsTable.getValueAt(row, 3).toString());
        phoneField.setText(clientsTable.getValueAt(row, 4).toString());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        addressField.setText("");
        emailField.setText("");
        phoneField.setText("");
    }

    private void addClient() {
        try {
            String name = nameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();

            Client client = new Client(name, address, email, phone);
            int id = clientBLL.insertClient(client);

            if (id > 0) {
                JOptionPane.showMessageDialog(this, "Client added successfully!");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add client!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editClient() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a client to edit!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String address = addressField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();

            Client client = new Client(id, name, address, email, phone);
            boolean success = clientBLL.updateClient(client);

            if (success) {
                JOptionPane.showMessageDialog(this, "Client updated successfully!");
                refreshTable();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update client!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteClient() {
        try {
            if (idField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a client to delete!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idField.getText());

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this client?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = clientBLL.deleteClient(id);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Client deleted successfully!");
                    refreshTable();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete client!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}