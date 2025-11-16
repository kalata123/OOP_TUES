package library.users;

import library.transactions.LibraryException;
import library.util.LibrarySettings;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        validateNotBlank(name, "member name");
        this.name = name;
        validateNotBlank(memberId, "member ID");
        this.memberId = memberId;
    }

    public Member(String name){
        validateNotBlank(name, "member name");
        this.name = name;
        this.memberId = "MEM-" + (int) (Math.random() * 1001);
    }

    boolean canBorrow(){
        return this.borrowedCount < LibrarySettings.MAX_BORROW_LIMIT;
    }

    void incrementBorrowedCount(){
        if(canBorrow()){
            this.borrowedCount++;
        }else{
            throw new LibraryException("Cannot borrow member");
        }
    }

    void decrementBorrowedCount(){
        this.borrowedCount--;
        if (this.borrowedCount < 0) this.borrowedCount = 0;
    }

    protected void validateNotBlank(String text, String fieldName) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }
}
