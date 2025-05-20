package org.example.assignment3.dataAccess;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractDAO<T> {
    private final Class<T> type;

    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    //because I wrongly named my DB columns...
    private String camelToSnake(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));
        for (int i = 1; i < camelCase.length(); i++) {
            char current = camelCase.charAt(i);

            if (Character.isUpperCase(current)) {
                result.append('_');
                result.append(Character.toLowerCase(current));
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private String SelectQuery(String field) {
        String tableName = type.getSimpleName().toLowerCase();
        String dbField = camelToSnake(field);

        if (tableName.equals("order")) {
            return "SELECT * FROM \"order\" WHERE " + dbField + " = ?";
        }
        return "SELECT * FROM " + tableName + " WHERE " + dbField + " = ?";
    }

    private String SelectAllQuery() {
        String tableName = type.getSimpleName().toLowerCase();
        if (tableName.equals("order")) {
            return "SELECT * FROM \"order\"";
        }
        return "SELECT * FROM " + tableName;
    }

    private String InsertQuery() {
        String tableName = type.getSimpleName().toLowerCase();
        Field[] fields = type.getDeclaredFields();

        List<String> javaFieldNames = Arrays.stream(fields).filter(field -> !field.getName().equals("id")).map(field -> field.getName()).collect(Collectors.toList());

        List<String> dbFieldNames = javaFieldNames.stream().map(this::camelToSnake).collect(Collectors.toList());

        String placeholders = dbFieldNames.stream().map(name -> "?").collect(Collectors.joining(", "));

        if (tableName.equals("order")) {
            return "INSERT INTO \"order\" (" + String.join(", ", dbFieldNames) + ") VALUES (" + placeholders + ")";
        }
        return "INSERT INTO " + tableName + " (" + String.join(", ", dbFieldNames) + ") VALUES (" + placeholders + ")";
    }

    private String UpdateQuery() {
        String tableName = type.getSimpleName().toLowerCase();

        String setClause = Arrays.stream(type.getDeclaredFields()).filter(field -> !field.getName().equals("id")).map(field -> camelToSnake(field.getName()) + " = ?").collect(Collectors.joining(", "));

        if (tableName.equals("order")) {
            return "UPDATE \"order\" SET " + setClause + " WHERE id = ?";
        }
        return "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";
    }

    private String createDeleteQuery() {
        String tableName = type.getSimpleName().toLowerCase();
        if (tableName.equals("order")) {
            return "DELETE FROM \"order\" WHERE id = ?";
        }
        return "DELETE FROM " + tableName + " WHERE id = ?";
    }

    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = SelectAllQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = SelectQuery("id");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> results = createObjects(resultSet);
            return results.isEmpty() ? null : results.get(0);
        } catch (SQLException e) {
            return null;
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public int insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = InsertQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(i++, field.get(t));
                }
            }

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException | IllegalAccessException e) {
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return -1;
    }

    public boolean update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = UpdateQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            int i = 1;
            int idValue = 0;

            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    idValue = (int) field.get(t);
                } else {
                    statement.setObject(i++, field.get(t));
                }
            }

            statement.setInt(i, idValue);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IllegalAccessException e) {
            return false;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public boolean delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();

        try {
            while (resultSet.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();

                    String dbFieldName = camelToSnake(fieldName);

                    try {
                        Object value = resultSet.getObject(dbFieldName);
                        if (value != null) {
                            Class<?> fieldType = field.getType();

                            if (fieldType == double.class && value instanceof java.math.BigDecimal) {
                                field.setDouble(instance, ((java.math.BigDecimal) value).doubleValue());
                            }
                            else if (fieldType == int.class && value instanceof Number) {
                                field.setInt(instance, ((Number) value).intValue());
                            }
                            else if (fieldType == boolean.class && value instanceof Boolean) {
                                field.setBoolean(instance, (Boolean) value);
                            }
                            else if (fieldType == LocalDateTime.class && value instanceof java.sql.Timestamp) {
                                field.set(instance, ((java.sql.Timestamp) value).toLocalDateTime());
                            }
                            else {
                                field.set(instance, value);
                            }
                        }
                    } catch (SQLException e) {
                        try {
                            Object value = resultSet.getObject(fieldName);
                            if (value != null) {
                                field.set(instance, value);
                            }
                        } catch (SQLException ex) {
                        }
                    }
                }
                list.add(instance);
            }
        } catch (Exception e) {
        }

        return list;
    }
}