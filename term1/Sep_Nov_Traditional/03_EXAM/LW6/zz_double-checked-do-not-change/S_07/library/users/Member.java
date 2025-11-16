package library.users;
import library.util.LibrarySettings;

public class Member {
    //name (String), memberId (String), borrowedCount (int)
    private String name;
    private String memberId;
    private int borrowedCount = 0;


    //Validate that name is not blank
    public Member(String name, String memberId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or blank");
        }
        this.name = name;
        this.memberId = memberId;
    }

    public Member(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be null or blank");
        }
        this.name = name;
        int randomNum = (int)(Math.random() * 101);
        this.memberId = "MEM-" + Integer.toString(randomNum);
    }

    public boolean canBorrow() {
        return borrowedCount < LibrarySettings.MAX_BORROW_LIMIT;
    }

    public void incrementBorrowed() {
        this.borrowedCount++;
    }

    public void decrementBorrowed() {
        this.borrowedCount--;
    }
}



