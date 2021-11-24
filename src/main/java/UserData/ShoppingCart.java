package UserData;

import database.entities.Book;

import java.util.ArrayList;

public class ShoppingCart {
    private final ArrayList<Book> books;
    private int totalPrice;

    public ShoppingCart() {
        this.books = new ArrayList<>();
        this.totalPrice = 0;
    }


    public void addBook(Book book) {
        books.add(book);
        totalPrice += book.getSellingPrice();
    }

    public void removeBook(int index) {
        totalPrice -= books.get(index).getSellingPrice();
        books.remove(index);
    }

    public void clearCart() {
        books.clear();
        totalPrice = 0;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

}
