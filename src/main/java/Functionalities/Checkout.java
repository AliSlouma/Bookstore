package Functionalities;

import database.entities.Book;
import database.entities.ConnectionPool;
import database.entities.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Checkout {
    private static Connection connection = ConnectionPool.getInstance().getConnection();

    private static void buyBook(Book book, User currentUser) throws SQLException {
        String buyQuery = "CALL BuyBook(\"" + book.getISBN() + "\",\"" + currentUser.getUserName() + "\");";
        Statement statement = connection.prepareStatement(buyQuery);
        statement.execute(buyQuery);
    }

    public static void checkoutCart(User currentUser) {
        for (Book book : currentUser.getShoppingCart().getBooks()) {
            try {
                buyBook(book, currentUser);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
