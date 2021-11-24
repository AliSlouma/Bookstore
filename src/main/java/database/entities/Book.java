package database.entities;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private List<Author> authors = new ArrayList<>();
    private List<String> attributes;

    public Book(List<String> attributes) {
        this.attributes = new ArrayList<>(attributes.subList(0, 8));
        this.setAuthors(attributes.subList(8, attributes.size()));
    }

    public String getISBN() {
        return attributes.get(0);
    }

    public void setISBN(String ISBN) {
        attributes.set(0, ISBN);
    }

    public String getTitle() {
        return attributes.get(1);
    }

    public void setTitle(String title) {
        attributes.set(1, title);
    }

    public String getPublisherName() {
        return attributes.get(2);
    }

    public void setPublisherName(String publisherName) {
        attributes.set(2, publisherName);
    }

    public String getPublicationYear() {
        return attributes.get(3);
    }

    public void setPublicationYear(String publicationYear) {
        attributes.set(3, publicationYear);
    }

    public int getSellingPrice() {
        return Integer.parseInt(attributes.get(4));
    }

    public void setSellingPrice(int sellingPrice) {
        attributes.set(4, String.valueOf(sellingPrice));
    }

    public String getCategory() {
        return attributes.get(5);
    }

    public void setCategory(String category) {
        attributes.set(5, category);
    }

    public int getNumberOfCopies() {
        return Integer.parseInt(attributes.get(6));
    }

    public void setNumberOfCopies(int numberOfCopies) {
        attributes.set(6, String.valueOf(numberOfCopies));
    }

    public int getThreshold() {
        return Integer.parseInt(attributes.get(7));
    }

    public void setThreshold(int threshold) {
        attributes.set(7, String.valueOf(threshold));
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public void setAuthors(List<String> newAuthors) {
        for (int i = 0; i < newAuthors.size(); i++) {
            Author newAuthor = new Author();
            newAuthor.setAuthorName(newAuthors.get(i));
            newAuthor.setISBN(getISBN());
            authors.add(newAuthor);
        }
    }

    public List<String> getAttributes() {
        return attributes;
    }
}
