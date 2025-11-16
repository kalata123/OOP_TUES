package library.users;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        validateNotBlank(name);
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public Member(String name) {
        validateNotBlank(name);
        this.name = name;
        this.memberId = "MEM-" + (int)(Math.random() * 10000);
        this.borrowedCount = 0;
    }

    private void validateNotBlank(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    public boolean canBorrow() {
        return borrowedCount < library.util.LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        borrowedCount++;
    }

    public void decrementBorrowed() {
        borrowedCount--;
    }

    public String getName() {
        return name;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }
}