import java.time.LocalDate;


@SuppressWarnings("unused")
public class Transaction {
    @SuppressWarnings({ "FieldMayBeFinal"})
    private int transactionId;
    @SuppressWarnings({ "FieldMayBeFinal" })
    private int bookId;
    @SuppressWarnings({ "FieldMayBeFinal" })
    private int memberId;
    @SuppressWarnings({ "FieldMayBeFinal" })
    private LocalDate issueDate;
    @SuppressWarnings("FieldMayBeFinal")
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isOverdue;
    private String status; // "Issued", "Returned"

    public Transaction(int transactionId, int bookId, int memberId, LocalDate issueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = issueDate.plusDays(14);
        this.returnDate = null;
        this.isOverdue = false;
    }

    public void returnBook(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.isOverdue = returnDate.isAfter(dueDate);
    }

    public boolean isOverdue() { return isOverdue; }
    public LocalDate getDueDate() { return dueDate; }

    public int getTransactionId() {
        return transactionId;
    }

    int getBookId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}