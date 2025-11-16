package library.users;

import library.core.LibraryItem;

public class Member
{
    private String name;
    private String memberID;
    private int borrowedCount;

    public Member(String name, String memberId)
    {
        this.name = name;
        this.memberID = memberId;
    }

    public Member(String name)
    {
        this.name = name;
        this.memberID = "MEM-" + (Math.random() * 101);
    }

    public boolean canBorrow()
    {
        if(this.borrowedCount < 1000) {return true;}
        else {return false;}
    }

    public void incrementBorrowed() {this.borrowedCount++;}
    public void decrementBorrowed() {this.borrowedCount--;}

    //validateNotBlank(this.name, "Name: ")
}
