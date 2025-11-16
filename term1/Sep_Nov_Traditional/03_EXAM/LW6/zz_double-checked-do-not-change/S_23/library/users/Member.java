package library.users;

import library.util.LibrarySettings;

import java.util.Random;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
    }

    public Member(String name) {
        this.name = name;
        this.memberId = "MEM-" + new Random().nextInt(1000);
    }


    public boolean canBorrow(){
        return borrowedCount < LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed(){
        borrowedCount++;
    }
    void decrementBorrowed(){
        borrowedCount--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.isEmpty())
        {
            throw new IllegalArgumentException("Name cant be empty");
        }
        this.name = name;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        if(memberId.isEmpty())
        {
            throw new IllegalArgumentException("Memberid cant be empty");
        }
        this.memberId = memberId;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        if(borrowedCount < 0 || borrowedCount > LibrarySettings.MAX_BORROW_LIMIT)
        {
            throw new IllegalArgumentException("Borrowed count cant be this number");
        }
        this.borrowedCount = borrowedCount;
    }
}
