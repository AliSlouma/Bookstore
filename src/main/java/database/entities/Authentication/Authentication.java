package database.entities.Authentication;

import database.entities.ConnectionPool;
import database.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Authentication {
    private String userName, password;

    public Authentication(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean isAuthenticated() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement statement =
                    connection.prepareStatement(String.format("select count(*) as count from users where user_name = '%s' and password = '%s';", userName, password));
            statement.execute();
            ResultSet set = statement.getResultSet();
            if (!set.next())
                return false;
            return set.getInt(set.findColumn("count")) > 0;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public User getUser() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        try {
            PreparedStatement statement =
                    connection.prepareStatement(String.format("select user_name, last_name, first_name, shipping_address, manager" +
                            " from users where user_name = '%s' and password = '%s';", userName, password));
            statement.execute();
            ResultSet set = statement.getResultSet();
            if (!set.next())
                return null;
            return new User(userName, set.getString(set.findColumn("first_name")),
                    set.getString(set.findColumn("last_name")),
                    set.getString(set.findColumn("shipping_address")),
                    set.getString(set.findColumn("manager")));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
