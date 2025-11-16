package library.users;
import library.util.LibrarySettings;
import library.transactions.BorrowingSystem;
public class Member
{
    private String name;
    private String memberID;
    private int borrowedCount;
    public Member(String name, String memberId)
    {

        if(name.isEmpty())
        {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        if(memberId.isEmpty())
        {
            throw new IllegalArgumentException("Member ID cannot be empty");
        }
        this.memberID = memberId;
        this.borrowedCount = 0;
    }
    public Member(String name)
    {
        if(name.isEmpty())
        {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.memberID = LibrarySettings.generateItemId("MEM-");
        this.borrowedCount = 0;
    }

    public boolean canBorrow(){
       if(this.borrowedCount > LibrarySettings.MAX_BORROW_LIMIT)
       {
           return false;
       }
       return true;
    }
    void incrementBorrowed()
    {
        this.borrowedCount++;
    }
    void decrementBorrowed()
    {
        this.borrowedCount--;
    }

    public String  getName()
    {
        return name;
    }
    public String getMemberID()
    {
        return memberID;
    }






}
