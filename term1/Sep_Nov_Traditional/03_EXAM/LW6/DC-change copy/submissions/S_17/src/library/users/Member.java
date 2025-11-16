package library.users;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    Member(String name, String memberId) {}
    Member(String name) {
        //ID GENERATOR - MEM+rand num
    }

    public boolean canBorrow(){
        if(borrowedCount < ) {
            return true;
        }
    }

    public void incremenntBorrowed() {
        this.borrowedCount++;
    }

    public void decrementBorrowed() {
        this.borrowedCount++;
    }

    validateNotBlank("name", this.name);
    
}