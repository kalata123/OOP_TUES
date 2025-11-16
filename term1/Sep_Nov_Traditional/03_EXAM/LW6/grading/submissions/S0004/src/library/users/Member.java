package library.users;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    Member(String name) {
        this.name = name;
        this.memberId = "MEM-";
    }

    boolean CanBorrow() {}

    void IncreaseBorrowedCount() {
        borrowedCount++;
    }
    void DecreaseBorrowedCount() {
        borrowedCount--;
    }
}