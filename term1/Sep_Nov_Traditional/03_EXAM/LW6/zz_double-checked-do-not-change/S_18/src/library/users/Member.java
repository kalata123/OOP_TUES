package library.users;

import library.util.LibrarySettings;

import java.util.Random;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public Member(String name) {
        Random rand = new Random();
        int num = rand.nextInt(100, 1000);
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is null or empty");
        }
        this.name = name;
        this.borrowedCount = 0;
        this.memberId = "MEM" + num;
    }

    public boolean canBorrow() {
        if(borrowedCount + 1 < LibrarySettings.MAX_BORROW_LIMIT) {
            return true;
        }
        return false;
    }

    public void incrementBorrowCount() {
        borrowedCount++;
    }

    public void  decrementBorrowCount() {
        borrowedCount--;
    }

}
