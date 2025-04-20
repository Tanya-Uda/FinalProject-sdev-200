public class MemberTest {
    public static void main(String[] args) {
        Member member = new Member(1, "Alice", "Smith", "1995-01-15", "555-1234", "alice@example.com");

        assert member.getFullName().equals("Alice Smith");
        assert member.getEmail().equals("alice@example.com");

        System.out.println("Member Name: " + member.getFullName());
        System.out.println("Email: " + member.getEmail());

    }
}