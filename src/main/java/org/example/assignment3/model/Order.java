package org.example.assignment3.model;

import java.time.LocalDateTime;

public class Order {
    private int id;
    private int clientId;
    private int productId;
    private int quantity;
    private LocalDateTime orderDate;

    public Order() {
    }

    public Order(int id, int clientId, int productId, int quantity, LocalDateTime orderDate) {
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    public Order(int clientId, int productId, int quantity) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", clientId=" + clientId + ", productId=" + productId + "]";
    }
}