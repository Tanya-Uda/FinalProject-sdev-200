import java.time.LocalDate;

public class TransactionTest {
    public static void main(String[] args) {
        Transaction transaction = new Transaction(1, 101, 201, LocalDate.now());
        
        System.out.println("Due Date: " + transaction.getDueDate());
        transaction.returnBook(LocalDate.now().plusDays(16)); // Simulate late return
        System.out.println("Is Overdue: " + transaction.isOverdue());
    }
}