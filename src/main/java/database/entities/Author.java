package database.entities;

public class Author implements ISimpleString {
    private String ISBN;
    private String authorName;

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    @Override
    public String getSimpleString() {
        return authorName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "ISBN='" + ISBN + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Author)) return false;
        Author a = (Author) o;
        return a.getAuthorName().equals(((Author) o).getAuthorName());
    }
}
