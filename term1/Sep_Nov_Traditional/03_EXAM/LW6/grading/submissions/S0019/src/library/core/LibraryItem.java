package library.core;

public abstract class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title)
    {
        if(validateNotBlank(title, "Title")) this.title = title;
        this.itemId = "TEMP";
    }
    public LibraryItem(String title, String itemID)
    {
        if(validateNotBlank(title, "Title")) this.title = title;
        if(validateNotBlank(itemID, "Item ID")) this.itemId = itemID;
    }
    public String getTitle()
    {
        return this.title;
    }
    public String getItemId()
    {
        return this.itemId;
    }
    public abstract String getItemType();
    public String getDisplayInfo()
    {
        return this.getItemType() + ": " + this.getTitle() + " (ID" + this.getItemId() + ")";
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

