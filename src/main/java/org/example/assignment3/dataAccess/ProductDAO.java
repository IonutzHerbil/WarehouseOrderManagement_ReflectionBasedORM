package org.example.assignment3.dataAccess;

import org.example.assignment3.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
;
public class ProductDAO extends AbstractDAO<Product> {

    public boolean updateStock(int productId, int newStock) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement("UPDATE product SET stock = ? WHERE id = ?");
            statement.setInt(1, newStock);
            statement.setInt(2, productId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }
}