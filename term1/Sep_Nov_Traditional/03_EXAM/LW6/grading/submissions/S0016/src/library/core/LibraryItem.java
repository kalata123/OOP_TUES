package library.core;

public abstract class LibraryItem{
    private String title;
    private String itemId;

    public LibraryItem(String title){ this.title = title; this.itemId = "TEMP"; }

    public LibraryItem(String title, String itemId){ this.title = title; this.itemId = itemId; }


    public String          getTitle()       { return title; }
    
    public String          getItemId()      { return itemId; }
    public abstract String getItemType();

    public String          getDisplayInfo() { return "[TYPE]: [TITLE] (ID: [ID])"; }    

    protected void validateNotBlank(String text, String fieldName) throws IllegalArgumentException 
    { if (text.isBlank() || text.isEmpty()) throw new IllegalArgumentException(fieldName + "cannot be blank or empty!"); };
} 

