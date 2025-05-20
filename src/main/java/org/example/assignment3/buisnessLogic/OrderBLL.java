package org.example.assignment3.buisnessLogic;

import org.example.assignment3.dataAccess.BillDAO;
import org.example.assignment3.dataAccess.OrderDAO;
import org.example.assignment3.dataAccess.ProductDAO;
import org.example.assignment3.model.Bill;
import org.example.assignment3.model.Order;
import org.example.assignment3.model.Product;

import java.util.List;

public class OrderBLL {
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private BillDAO billDAO;

    public OrderBLL() {
        orderDAO = new OrderDAO();
        productDAO = new ProductDAO();
        billDAO = new BillDAO();
    }

    public List<Order> findAllOrders() {
        return orderDAO.findAll();
    }

    public int createOrder(int clientId, int productId, int quantity) {
        Product product = productDAO.findById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Not enough stock. Available: " + product.getStock());
        }

        Order order = new Order(clientId, productId, quantity);
        int orderId = orderDAO.insert(order);

        if (orderId > 0) {
            productDAO.updateStock(productId, product.getStock() - quantity);
            order.setId(orderId);
            Bill bill = Bill.fromOrder(order, product.getPrice());
            billDAO.insert(bill);
        }

        return orderId;
    }
}