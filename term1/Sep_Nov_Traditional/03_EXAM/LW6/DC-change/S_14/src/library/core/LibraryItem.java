package library.core;

public abstract class LibraryItem
{
    private String title;
    private String itemID;

    public  LibraryItem(String title)
    {
        try{
            validateNotBlank(title, "Title");
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        this.title = title;
        this.itemID = "Temp";
    }

    public LibraryItem(String title, String itemID)
    {
        try{
            validateNotBlank(title, "Title");
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        this.title = title;
        try{
            validateNotBlank(itemID, "ItemID");
        }
        catch(IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        this.itemID = itemID;
    }

    public String getTitle()
    {
        return this.title;
    }
    public String getItemID()
    {
        return this.itemID;
    }

    abstract public String getItemType();

    public String getDisplayInfo()
    {
        return "[" + getItemType() + "]: [" + getItemID() + "] (ID: [" + getItemID() + "])";
    }

    public void validateNotBlank(String text, String fieldName)
    {
        if(text.isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " is empty");
        }
    }




}
