package library.users;
import java.util.Random;
import library.util.LibrarySettings;

import library.util.LibrarySettings.MAX_BORROW_LIMIT;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        if(name.isEmpty())
        {
            System.out.println("Name is Empty");
            return;
        }
        this.name = name;
        this.memberId = memberId;
        borrowedCount = 0;
    }

    public Member(String name) {
        this.name = name;
        if(name.isEmpty())
        {
            System.out.println("Name is Empty");
            return;
        }
        this.memberId = "MEM-"  + new Random().nextInt(10000);
        borrowedCount = 0;
    }

    boolean CanBorrow(){
        if(borrowedCount < MAX_BORROW_LIMIT)
        {
            return true;
        }
        return false;
    }

    void incrementBorrowed()
    {
        borrowedCount++;
    }

    void decrementBorrowed()
    {
        borrowedCount--;
    }
}
