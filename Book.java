package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class Book {
    private IntegerProperty bookId;
    private StringProperty title;
    private StringProperty author;
    private StringProperty genre;
    private IntegerProperty availableCopies;  // New property for available copies

    // Constructor
    public Book(int bookId, String title, String author, String genre, int availableCopies) {
        this.bookId = new SimpleIntegerProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.genre = new SimpleStringProperty(genre);
        this.availableCopies = new SimpleIntegerProperty(availableCopies);  // Initialize available copies
    }

    // Property getter methods
    public IntegerProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty authorProperty() {
        return author;
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public IntegerProperty availableCopiesProperty() {
        return availableCopies;  // Property getter for available copies
    }

    // Getters for the properties (for non-binding access)
    public int getBookId() {
        return bookId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getGenre() {
        return genre.get();
    }

    public int getAvailableCopies() {
        return availableCopies.get();  // Getter for available copies
    }

    // Setters for the properties (to allow modification)
    public void setBookId(int bookId) {
        this.bookId.set(bookId);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies.set(availableCopies);  // Setter for available copies
    }
}
