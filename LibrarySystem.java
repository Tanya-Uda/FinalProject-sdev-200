import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    @SuppressWarnings("FieldMayBeFinal")
    private List<Book> books = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private List<Member> members = new ArrayList<>();
    @SuppressWarnings("FieldMayBeFinal")
    private List<Transaction> transactions = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public boolean issueBook(int bookId, int memberId) {
        for (Book book : books) {
            if (book.getId() == bookId && book.getAvailableCopies() > 0) {
                book.borrowBook();
                transactions.add(new Transaction(transactions.size() + 1, bookId, memberId, java.time.LocalDate.now()));
                return true; // Successfully issued
            }
        }
        return false; // Book not available
    }

    public boolean returnBook(int transactionId) {
        for (Transaction transaction : transactions) {
            if (transactionId == transaction.getTransactionId() && transaction.getDueDate() == null) {
                for (Book book : books) {
                    if (book.getId() == transaction.getBookId()) {
                        book.returnBook();
                        transaction.returnBook(java.time.LocalDate.now());
                        return true; // Successfully returned
                    }
                }
            }
        }
        return false; // Invalid transaction
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public List<Member> getAllMembers() {
        return members;
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }
}