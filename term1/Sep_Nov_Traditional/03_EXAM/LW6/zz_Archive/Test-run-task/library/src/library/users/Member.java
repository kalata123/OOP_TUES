// src/library/users/Member.java
package library.users;

import library.util.LibrarySettings;
import library.transactions.LibraryException;

import java.util.concurrent.ThreadLocalRandom;

public class Member {
    private final String name;
    private final String memberId;
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
        int code = ThreadLocalRandom.current().nextInt(1000, 10000);
        this.memberId = "MEM-" + code;
        this.borrowedCount = 0;
    }

    public boolean canBorrow() {
        return borrowedCount < LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        if (borrowedCount >= LibrarySettings.MAX_BORROW_LIMIT) {
            throw new LibraryException("Borrow limit reached: " + LibrarySettings.MAX_BORROW_LIMIT);
        }
        borrowedCount++;
    }

    public void decrementBorrowed() {
        if (borrowedCount <= 0) {
            throw new LibraryException("Borrowed count cannot go negative");
        }
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

    private void validateNotBlank(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}
