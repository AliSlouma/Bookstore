package Functionalities;

import database.entities.BookOrder;
import database.entities.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Orderer {
    private String[] orderAttributes = {"order_num", "ISBN", "date_out", "quantity_ordered"};

    enum Type {INTEGER, STRING, DATE}

    private Type[] types = {Orderer.Type.INTEGER, Orderer.Type.STRING, Orderer.Type.DATE, Orderer.Type.INTEGER};

    private Connection connection = ConnectionPool.getInstance().getConnection();

    private String createDeleteQuery(List<String> attributes) {
        StringBuilder deleteQuery = new StringBuilder("delete from book_orders ");
        boolean whereFlag = false;
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).isEmpty()) continue;
            if (!whereFlag) {
                deleteQuery.append("where ");
                whereFlag = true;
            } else {
                deleteQuery.append(" AND ");
            }
            deleteQuery.append(orderAttributes[i]).append("=");
            if (types[i] == Orderer.Type.DATE) {
                deleteQuery.append("DATE(\'");
                deleteQuery.append(attributes.get(i));
                deleteQuery.append("\')");
            } else if (types[i] == Orderer.Type.STRING)
                deleteQuery.append("\"").append(attributes.get(i)).append("\"");
            else
                deleteQuery.append(attributes.get(i));
        }
        deleteQuery.append(";");
        return deleteQuery.toString();
    }

    private ArrayList<BookOrder> readResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<BookOrder> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            ArrayList<String> newAttributes = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                newAttributes.add(resultSet.getString(i));
            }
            BookOrder resultBook = new BookOrder(newAttributes);
            result.add(resultBook);
        }
        return result;
    }

    public List<BookOrder> getOrders() {
        String query = "select * from book_orders ;";
        try {
            Statement statement = connection.prepareStatement(query);
            statement.execute(query);
            return readResultSet(statement.getResultSet());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean confirmOrder(BookOrder bookOrder) {
        String deleteQuery = createDeleteQuery(bookOrder.getAttributes());
        try {
            Statement statement = connection.prepareStatement(deleteQuery);
            statement.execute(deleteQuery);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public void orderBooks(BookOrder bookOrder) {
        List<String> attributes = bookOrder.getAttributes();
        StringBuilder insertQuery = new StringBuilder("CALL InsertBookOrder(");
        for (int i = 0; i < attributes.size(); i++) {
            if (types[i] != Orderer.Type.INTEGER)
                insertQuery.append("\"").append(attributes.get(i)).append("\"");
            else
                insertQuery.append(attributes.get(i));
            if (i + 1 != attributes.size()) insertQuery.append(",");
        }
        insertQuery.append(");");
        String query = insertQuery.toString();
        try {
            Statement statement = connection.prepareStatement(query);
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
