package library.core;

public abstract class LibraryItem {
    private String title;
    String itemId;

    public LibraryItem(String title)
    {
        this.title = title;
        this.itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId)
    {
        this.title = title;
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }
    public String getItemId() {
        return itemId;
    }

    abstract public String getItemType();

    public String getDisplayInfo()
    {
        return "Type:" + title + "ID:" + itemId;
    }

    protected void ValidateNotBlank(String text, String fieldName) throws IllegalArgumentException
    {
        if(text == null)
        {
            throw new IllegalArgumentException(fieldName + " is blank");
        }
    }
}
