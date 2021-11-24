package database.entities;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
    private static ConnectionPool connectionPool;
    private final Connection connection;

    private ConnectionPool() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/book_store", "root", "root");
    }

    public static synchronized ConnectionPool getInstance() {
        if (connectionPool == null) {
            try {
                connectionPool = new ConnectionPool();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed to connect to MySQL");
                alert.setHeaderText("Failed to connect to MySQL server. Check if the server is up");
                alert.showAndWait();
            }
        }
        return connectionPool;
    }

    public Connection getConnection() {
        return connection;
    }
}
