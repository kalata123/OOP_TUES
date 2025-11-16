package library.users;

import library.core.LibraryItem;
import library.materials.Book;
import library.util.LibrarySettings;
import library.transactions.BorrowingSystem;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        validateNotBlank(name, "Name");
        validateNotBlank(memberId, "Member ID");
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public Member(String name, String memberId, int borrowedCount) {
        validateNotBlank(name, "Name");
        validateNotBlank(memberId, "Member ID");
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = borrowedCount;
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

    public void setName(String name) {
        validateNotBlank(name, "Name");
        this.name = name;
    }

    public void setMemberId(String memberId) {
        validateNotBlank(memberId, "Member ID");
        this.memberId = memberId;
    }

    public void setBorrowedCount(int borrowedCount) {
        if (borrowedCount < 0) {
            throw new IllegalArgumentException("Borrowed count cannot be negative.");
        }
        this.borrowedCount = borrowedCount;
    }

    public boolean canBorrow() {
        return borrowedCount < LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        if (borrowedCount >= LibrarySettings.MAX_BORROW_LIMIT) {
            throw new IllegalStateException("Cannot borrow more items, limit reached.");
        }
        borrowedCount++;
    }

    public void decrementBorrowed(){
        if (borrowedCount <= 0) {
            throw new IllegalStateException("No borrowed items to return.");
        }
        borrowedCount--;
    }
}