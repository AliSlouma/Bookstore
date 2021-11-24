package database.entities;

import UserData.ShoppingCart;

public class User {
    final private String userName, firstName, lastName, shippingAddress;
    final private ShoppingCart shoppingCart;
    final private boolean isManager;

    public User(String userName, String firstName, String lastName, String shippingAddress, String isManager) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.shippingAddress = shippingAddress;
        this.shoppingCart = new ShoppingCart();
        this.isManager = isManager.equals("1");
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public boolean isManager() {
        return isManager;
    }
}
