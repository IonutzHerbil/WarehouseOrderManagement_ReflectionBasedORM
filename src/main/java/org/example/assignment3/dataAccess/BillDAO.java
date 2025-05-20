package org.example.assignment3.dataAccess;

import org.example.assignment3.model.Bill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BillDAO {
    private static final Logger LOGGER = Logger.getLogger(BillDAO.class.getName());

    public int insert(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "INSERT INTO log(order_id, client_id, product_id, total_price, quantity, issue_date) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, bill.orderId());
            statement.setInt(2, bill.clientId());
            statement.setInt(3, bill.productId());
            statement.setDouble(4, bill.totalPrice());
            statement.setInt(5, bill.quantity());
            statement.setTimestamp(6, Timestamp.valueOf(bill.issue_date()));

            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting bill", e);
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return -1;
    }
}