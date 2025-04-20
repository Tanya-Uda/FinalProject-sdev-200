
public class Book {
    @SuppressWarnings("FieldMayBeFinal")
    private int id;
    @SuppressWarnings("FieldMayBeFinal")
    private String title;
    @SuppressWarnings("FieldMayBeFinal")
    private String author;
    @SuppressWarnings("FieldMayBeFinal")
    private String genre;
    @SuppressWarnings("FieldMayBeFinal")
    private int totalCopies;
    @SuppressWarnings("FieldMayBeFinal")
    private int availableCopies;
    @SuppressWarnings({ "FieldMayBeFinal", "unused" })
    private String dateAdded;

    public Book(int id, String title, String author, String genre, int totalCopies, int availableCopies, String dateAdded) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.dateAdded = dateAdded;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void borrowBook() { if (availableCopies > 0) availableCopies--; }
    public void returnBook() { if (availableCopies < totalCopies) availableCopies++; }

    

    public void issueCopy() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }
}