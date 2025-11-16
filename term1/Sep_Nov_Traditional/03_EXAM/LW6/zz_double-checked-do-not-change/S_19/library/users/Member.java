package library.users;

import library.utils.LibrarySettings;
import java.util.Random;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId)
    {
        if(validateNotBlank(name, "Name")) this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }
    public Member(String name)
    {
        if(validateNotBlank(name, "Name")) this.name = name;  
        int randomNum = (int)(Math.random() * 101);
        this.memberId = "MEM-" + randomNum;
        this.borrowedCount = 0;
    }
    public boolean canBorrow()
    {
        return this.borrowedCount < LibrarySettings.MAX_BORROWED_LIMIT;
    }
    public void incrementBorrowed()
    {
        this.borrowedCount += 1;
    }
    public void decrementBorrowed()
    {
        this.borrowedCount -= 1;
    }
        protected boolean validateNotBlank(String text, String fieldName) throws IllegalArgumentException
    {
        if (text == null || text.isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return true;
    }
}
