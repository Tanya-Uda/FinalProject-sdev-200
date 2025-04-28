package application;

import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {

    private List<Book> books;
    private List<Member> members;
    private List<Transaction> transactions;

    public LibrarySystem() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    // Add a new book
    public void addBook(Book book) {
        books.add(book);
    }

    // Add a new member
    public void addMember(Member member) {
        members.add(member);
    }

    // Issue a book to a member
    public void issueBook(int bookId, int memberId) {
        Book book = findBookById(bookId);
        Member member = findMemberById(memberId);
        if (book != null && member != null && book.getAvailableCopies() > 0) {
            // Create a new transaction
            String issueDate = "2025-04-28";  // Example issue date
            String dueDate = "2025-05-12";    // Example due date
            Transaction transaction = new Transaction(transactions.size() + 1, bookId, memberId, issueDate, dueDate);
            transactions.add(transaction);

            // Update book available copies
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
    }

    // Return a book
    public void returnBook(int transactionId) {
        Transaction transaction = findTransactionById(transactionId);
        if (transaction != null) {
            transaction.setReturnDate("2025-04-28");  // Example return date
            Book book = findBookById(transaction.getBookId());
            book.setAvailableCopies(book.getAvailableCopies() + 1);
        }
    }

    // Find a book by ID
    public Book findBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    // Find a member by ID
    public Member findMemberById(int id) {
        return members.stream().filter(member -> member.getId() == id).findFirst().orElse(null);
    }

    // Find a transaction by ID
    public Transaction findTransactionById(int id) {
        return transactions.stream().filter(transaction -> transaction.getTransactionId() == id).findFirst().orElse(null);
    }

    // Get list of books
    public List<Book> getBooks() {
        return books;
    }

    // Get list of members
    public List<Member> getMembers() {
        return members;
    }

    // Get list of transactions
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
