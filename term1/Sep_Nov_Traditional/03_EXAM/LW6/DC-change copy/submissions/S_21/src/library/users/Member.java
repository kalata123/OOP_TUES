package library.users;

import java.util.Random;

import static library.util.LibrarySettings.MAX_BORROW_LIMIT;

public class Member {

    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        if (name == null || name.isBlank() || memberId == null || memberId.isBlank()) {
            throw new IllegalArgumentException("Name and memberId cannot be null or blank");
        }
        this.name = name;
        this.memberId = memberId;
    }

    public Member(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        this.name = name;
        Random rand = new Random();
        this.memberId = "MEM-" + rand.nextInt(10000);
    }

    public boolean canBorrow() {
        return borrowedCount < MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        borrowedCount++;
    }

    public void decrementBorrowed() {
        borrowedCount--;
    }
}
