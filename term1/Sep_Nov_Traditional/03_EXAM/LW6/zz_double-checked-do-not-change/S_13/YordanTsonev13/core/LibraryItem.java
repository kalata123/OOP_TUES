package library.core;

public class LibraryItem {
    private String title;
    private String itemId;
    public LibraryItem(String title) {
        this.itemId = "TEMP";
    }
    public LibraryItem(String title, String itemId) {
        this.title = title;
        this.itemId = itemId;
    }
    public String getTitle(String title)
    {
       return title;
    }
    public String getItemId(String itemId) {
        return itemId;
    }

    public String getDisplayInfo()
    {
        return String.format("[TYPE]: %s\n[ID]: %s", title, itemId);
    }
    protected void validateNotBlank(String text, String fieldName)
    {
        if(text == null || text.isBlank())
        {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }
}

