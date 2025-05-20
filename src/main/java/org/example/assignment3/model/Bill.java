package org.example.assignment3.model;

import java.time.LocalDateTime;

public record Bill(int id, int orderId, int clientId, int productId, double totalPrice, int quantity, LocalDateTime issue_date) {
    public static Bill fromOrder(Order order, double productPrice) {
        return new Bill(
                0,
                order.getId(),
                order.getClientId(),
                order.getProductId(),
                productPrice * order.getQuantity(),
                order.getQuantity(),
                LocalDateTime.now()
        );
    }
}