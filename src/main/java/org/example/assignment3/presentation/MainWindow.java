package org.example.assignment3.presentation;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Orders Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu operationsMenu = new JMenu("Operations");

        JMenuItem clientsItem = new JMenuItem("Clients");
        clientsItem.addActionListener(e -> openClientWindow());

        JMenuItem productsItem = new JMenuItem("Products");
        productsItem.addActionListener(e -> openProductWindow());

        JMenuItem ordersItem = new JMenuItem("Orders");
        ordersItem.addActionListener(e -> openOrderWindow());

        operationsMenu.add(clientsItem);
        operationsMenu.add(productsItem);
        operationsMenu.add(ordersItem);

        menuBar.add(fileMenu);
        menuBar.add(operationsMenu);

        setJMenuBar(menuBar);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to Orders Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private void openClientWindow() {
        ClientWindow clientWindow = new ClientWindow();
        clientWindow.setVisible(true);
    }

    private void openProductWindow() {
        ProductWindow productWindow = new ProductWindow();
        productWindow.setVisible(true);
    }

    private void openOrderWindow() {
        OrderWindow orderWindow = new OrderWindow();
        orderWindow.setVisible(true);
    }
}