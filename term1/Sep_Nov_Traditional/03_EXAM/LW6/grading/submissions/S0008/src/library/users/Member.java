package library.users;

import java.util.UUID;

public class Member {
    private String name;
    private String memberID;
    private int borrowedCount;

    public Member(String name){
        if(name != null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }else{
            this.name = name;
        }

        this.memberID = "MEM-" + UUID.randomUUID().toString();
        this.borrowedCount = 0;
    }

    public Member(String name, String memberId){
        if(name != null || name.isEmpty()){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }else{
            this.name = name;
        }
        this.memberID = memberId;
        this.borrowedCount = 0;
    }

    boolean canBorrow(){
        return true;
    }

    void incrementBorrowed(){
        this.borrowedCount++;
    }

    void decrementBorrowed(){
        this.borrowedCount--;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }
}
