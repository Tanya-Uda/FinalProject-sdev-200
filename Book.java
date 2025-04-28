package application;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    private String dateAdded;

    public Book(int id, String title, String author, String genre, int totalCopies, int availableCopies, String dateAdded) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.dateAdded = dateAdded;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public String getDateAdded() { return dateAdded; }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }
}
