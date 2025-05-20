package org.example.assignment3.buisnessLogic;

import org.example.assignment3.dataAccess.ProductDAO;
import org.example.assignment3.model.Product;

import java.util.List;

public class ProductBLL {
    private ProductDAO productDAO;

    public ProductBLL() {
        productDAO = new ProductDAO();
    }

    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    public int insertProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        return productDAO.insert(product);
    }

    public boolean updateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be positive");
        }
        if (product.getStock() < 0) {
            throw new IllegalArgumentException("Product stock cannot be negative");
        }
        return productDAO.update(product);
    }

    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }
}