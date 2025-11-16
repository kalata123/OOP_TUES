package library.users;

import java.util.Random;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        setName(name);
        validateNotBlank(memberId, "Member ID");
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public Member(String name) {
        setName(name);
        this.memberId = "MEM-" + generateRandomNumber();
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
        return borrowedCount < Library.util.LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        if (borrowedCount >= Library.util.LibrarySettings.MAX_BORROW_LIMIT) {
            throw new IllegalStateException("borrow limit");
        }
        borrowedCount++;
    }

    public void decrementBorrowed() {
        if (borrowedCount <= 0) {
            throw new IllegalStateException("Nqma borrowed items koito da vurne");
        }
        borrowedCount--;
    }

    private void setName(String name) {
        validateNotBlank(name, "Name");
        this.name = name;
    }

    private String generateRandomNumber() {
        return String.valueOf((int)(Math.random() * 101));
    }

    private void validateNotBlank(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " ne moje da e null or blank");
        }
    }
}
