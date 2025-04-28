package application;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int memberId;
    private String issueDate;
    private String dueDate;
    private String returnDate;
    private boolean isOverdue;
    private String status; // "Issued" or "Returned"

    public Transaction(int transactionId, int bookId, int memberId, String issueDate, String dueDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.isOverdue = false;
        this.status = "Issued";
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public String getIssueDate() { return issueDate; }
    public String getDueDate() { return dueDate; }
    public String getReturnDate() { return returnDate; }
    public boolean isOverdue() { return isOverdue; }
    public String getStatus() { return status; }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
        this.status = "Returned";
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
