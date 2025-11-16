package library.core;
import library.util.LibrarySettings;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;
    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
    }
    public Member(String name) {
        this.memberId = "MEM-"+75785;
    }
    public String getName() {
        return name;
    }

    protected void validateNotBlank(String text, String fieldName)
    {
        if(text == null || text.isBlank())
        {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    public void setName(String name) {
        validateNotBlank("name", name);
        this.name = name;
    }
    public boolean canBorrow()
    {
        if(itemCount < MAX_BORROW_LIMIT)
        {
            return true;
        }
        return false;
    }
    public void incrementBorrowed()
    {
        borrowedCount++;
    }
    public void decrementBorrowed()
    {
        borrowedCount--;
    }

}
