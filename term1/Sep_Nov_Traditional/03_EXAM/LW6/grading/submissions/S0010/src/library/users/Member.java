package library.users;

import library.util.LibrarySettings;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId){
        if(name.isEmpty() || memberId.isEmpty() || name == null || memberId == null){
            throw new IllegalArgumentException("Name and / or memberId cannot be empty");
        }

        this.memberId = memberId;
        this.name = name;

    }

    public Member(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        this.name = name;

        int randomNum = (int)(Math.random() * 1001);
        this.memberId = "MEM-" + randomNum;
    }

    public String getMemberId() {
        return memberId;
    }
    public void setMemberId(String memberId) {
        if (memberId.isEmpty()) {
            throw new IllegalArgumentException("memberId cannot be empty");
        }
        this.memberId = memberId;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }
    public void setBorrowedCount(int borrowedCount) {
        if(borrowedCount < 0){
            throw new IllegalArgumentException("borrowedCount cannot be negative");
        }
        this.borrowedCount = borrowedCount;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    public boolean canBorrow(){
        return this.borrowedCount <= LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed(){
        this.borrowedCount++;
    }

    public void decrementBorrowed(){
        this.borrowedCount--;
    }
}

