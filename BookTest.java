public class BookTest {
    public static void main(String[] args) {
        Book book = new Book(1, "1984", "George Orwell", "Fiction", 5, 5, "2023-04-01");

        assert book.isAvailable();
        book.issueCopy();
        assert book.getAvailableCopies() == 2;
        book.returnCopy();
        assert book.getAvailableCopies() == 3;

        System.out.println("Title: " + book.getTitle());
        book.borrowBook();
        System.out.println("Available copies after borrowing: " + book.getAvailableCopies());
        book.returnBook();
        System.out.println("Available copies after returning: " + book.getAvailableCopies());


    }
}
