package Functionalities;

import database.entities.Author;
import database.entities.Book;
import database.entities.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookInteractor {

    private String[] bookAttributes = {"ISBN", "title", "publisher_name", "publication_year", "selling_price",
            "category", "no_of_copies", "threshold"};

    enum Type {INTEGER, STRING, DATE}

    private Type[] types = {Type.STRING, Type.STRING, Type.STRING, Type.DATE,
            Type.INTEGER, Type.STRING, Type.INTEGER, Type.INTEGER};

    private Connection connection = ConnectionPool.getInstance().getConnection();

    public BookInteractor() {
    }

    private void populateAuthors(Book book) throws SQLException {
        String authorQuery = "select * from authors where ISBN = \"" + book.getISBN() + "\";";
        Statement statement = connection.prepareStatement(authorQuery);
        statement.execute(authorQuery);
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            Author author = new Author();
            author.setAuthorName(resultSet.getString(2));
            author.setISBN(resultSet.getString(1));
            book.getAuthors().add(author);
        }
    }

    private ArrayList<Book> readResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<Book> result = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            ArrayList<String> newAttributes = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                newAttributes.add(resultSet.getString(i));
            }
            Book resultBook = new Book(newAttributes);
            populateAuthors(resultBook);
            result.add(resultBook);
        }
        return result;
    }

    private String createAuthorQuery(String author, StringBuilder searchQuery, boolean whereFlag) {
        if (whereFlag)
            searchQuery.append(" AND ");
        else
            searchQuery.append("where ");
        searchQuery.append("ISBN in ( select ISBN from authors where author_name=\"").append(author).append("\")");
        return searchQuery.toString();
    }

    private String createSearchQuery(ArrayList<String> attributes, String author) {
        StringBuilder searchQuery = new StringBuilder("select * from book ");
        boolean whereFlag = false;
        for (int i = 0; i < attributes.size(); i++) {
            if (attributes.get(i).isEmpty()) continue;
            if (!whereFlag) {
                searchQuery.append("where ");
                whereFlag = true;
            } else {
                searchQuery.append(" AND ");
            }
            searchQuery.append(bookAttributes[i]).append("=");
            if (types[i] == Type.DATE) {
                searchQuery.append("DATE(\'");
                searchQuery.append(attributes.get(i));
                searchQuery.append("\')");
            } else if (types[i] == Type.STRING)
                searchQuery.append("\"").append(attributes.get(i)).append("\"");
            else
                searchQuery.append(attributes.get(i));
        }
        if (!author.isEmpty()) createAuthorQuery(author, searchQuery, whereFlag);
        searchQuery.append(";");
        return searchQuery.toString();
    }

    public ArrayList<Book> searchByAttribute(ArrayList<String> attributes, String author) {
        String searchQuery = createSearchQuery(attributes, author);
        try {
            Statement statement = connection.prepareStatement(searchQuery);
            statement.execute(searchQuery);
            return readResultSet(statement.getResultSet());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }



    private void deleteAuthor(String ISBN) throws SQLException {
        String deleteAuthorQuery = "delete from authors where ISBN=" + "\"" + ISBN + "\";";
        connection.prepareStatement(deleteAuthorQuery).execute();
    }

    private void modifyAuthors(Book oldBook, Book newBook) throws SQLException {
        deleteAuthor(oldBook.getISBN());
        for (Author author : newBook.getAuthors())
            addAuthor(author);
    }


    public void modifyBook(Book oldBook, Book newBook) throws SQLException {
        StringBuilder updateQuery = new StringBuilder("update book set ");
        List<String> newAttributes = newBook.getAttributes();
        List<String> oldAttributes = oldBook.getAttributes();
        boolean diff = false;
        List<String> updates = new ArrayList<>();
        for (int i = 0; i < newAttributes.size(); i++) {
            if (oldAttributes.get(i).equals(newAttributes.get(i))) continue;
            diff = true;
            StringBuilder currentAttribute = new StringBuilder();
            currentAttribute.append(bookAttributes[i]).append("=");
            if (types[i] != Type.INTEGER)
                currentAttribute.append("\"").append(newAttributes.get(i)).append("\"");
            else
                currentAttribute.append(newAttributes.get(i));
            updates.add(currentAttribute.toString());
        }
        for (int i = 0; i < updates.size(); i++) {
            if (i != 0) updateQuery.append(",");
            updateQuery.append(updates.get(i));
        }
        updateQuery.append(" where ISBN=\"").append(oldAttributes.get(0)).append("\";");
        Statement statement = connection.prepareStatement(updateQuery.toString());
        if (diff)
            statement.execute(updateQuery.toString());
        if (!oldBook.getAuthors().equals(newBook.getAuthors()))
            modifyAuthors(oldBook, newBook);
    }

    private void addAuthor(Author author) throws SQLException {
        String insertQuery = "insert into authors values(" + "\"" + author.getISBN() +
                "\"" + "," + "\"" + author.getAuthorName() + "\");";
        Statement statement = connection.prepareStatement(insertQuery);
        boolean success = statement.execute(insertQuery);
    }

    public void addBook(Book book) {
        List<String> attributes = book.getAttributes();
        StringBuilder insertQuery = new StringBuilder("CALL InsertBook(");
        for (int i = 0; i < attributes.size(); i++) {
            if (types[i] != Type.INTEGER)
                insertQuery.append("\"").append(attributes.get(i)).append("\"");
            else
                insertQuery.append(attributes.get(i));
            if (i + 1 != attributes.size()) insertQuery.append(",");
        }
        insertQuery.append(");");
        try {
            Statement statement = connection.prepareStatement(insertQuery.toString());
            statement.execute(insertQuery.toString());
            for (Author author : book.getAuthors())
                addAuthor(author);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
