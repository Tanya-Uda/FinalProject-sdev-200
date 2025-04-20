public class LibrarySystemTest {
    public static void main(String[] args) {
        LibrarySystem system = new LibrarySystem();
        system.addBook(new Book(1, "The Hobbit", "Tolkien", "Fantasy", 1, 1, "2022-03-21"));
        system.addBook(new Book(2, "To Kill a Mockingbird", "Harper Lee", "Classic", 3, 3, "2025-04-05"));

        system.addMember(new Member(1, "John", "Doe", "2000-1-1", "555-9876", "john@mail.com"));
        system.addMember(new Member(2, "Bob", "Jones", "1985-05-20", "555-5678", "bob.jones@mail.com"));


        boolean issued = system.issueBook(1, 1);
        System.out.println("Issue Successful: " + issued);

        boolean returned = system.returnBook(1);
        System.out.println("Return Successful: " + returned);
        
        System.out.println("Books in Library:");
        for (Book book : system.getAllBooks()) {
            System.out.println("- " + book.getTitle() + " (Available: " + book.getAvailableCopies() + ")");
        }

        // Display all transactions
        System.out.println("Transactions:");
        for (Transaction transaction : system.getAllTransactions()) {
            System.out.println("- Transaction ID: " + transaction.getTransactionId() + ", Status: " +
                    (transaction.isOverdue() ? "Overdue" : "On Time"));
        }
    }
}
