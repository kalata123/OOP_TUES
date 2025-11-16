package library.users;

import library.util.LibrarySettings;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        validateNotBlank(name, "name");
        validateNotBlank(memberId, "memberId");
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public Member(String name) {
        validateNotBlank(name, "name");
        this.name = name;
        int random = (int) (Math.random() * 1000);
        this.memberId = "MEM-" + random;
        this.borrowedCount = 0;
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

    public boolean canBorrow() {
        return borrowedCount < LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        if (borrowedCount >= LibrarySettings.MAX_BORROW_LIMIT) {
            throw new IllegalStateException("Member has reached maximum borrow limit");
        }
        borrowedCount++;
    }

    public void decrementBorrowed() {
        if (borrowedCount <= 0) {
            throw new IllegalStateException("Borrowed count cannot be negative");
        }
        borrowedCount--;
    }

    private void validateNotBlank(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }
}
