
public class Member {
    @SuppressWarnings("FieldMayBeFinal")
    private int id;
    @SuppressWarnings("FieldMayBeFinal")
    private String firstName;
    @SuppressWarnings("FieldMayBeFinal")
    private String lastName;
    @SuppressWarnings({"FieldMayBeFinal", "unused"})
    private String birthDate;
    @SuppressWarnings({"unused", "FieldMayBeFinal"})
    private String phoneNumber;
    @SuppressWarnings("FieldMayBeFinal")
    private String email;

    public Member(int id, String firstName, String lastName, String birthDate, String phoneNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getId() { return id; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
}