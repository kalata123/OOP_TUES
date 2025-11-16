package library.core;

public abstract class LibraryItem
{
    private String title;
    private String itemId;

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

    public String getTitle() {return this.title;}
    public String getItemId() {return this.itemId;}

    abstract String getItemType();

    public String getDisplayInfo()
    {
        return "[TYPE]: [" + this.getTitle() + "] " + "(ID: [" + this.getItemId() + "])";
    }

    protected void validateNotBlank(String text, String fieldName)
    {
        if (text == null || text.isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " is null or empty");
        }
    }
}
