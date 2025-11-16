package library.core;

public abstract class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title){
        this.title = title;
        itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId) {
        this.title = title;
        this.itemId = itemId;
    }

    public String getTitle(){
        return title;
    }
    public String getItemId(){
        return itemId;
    }

    abstract public String getItemType();

    public String getDisplayInfo(){
        return getTitle() + " (" + getItemId() + ")";
    }

    protected void validateNotBlank(String text, String fieldName) {
        if (text == null || text.isEmpty()){
            throw new IllegalArgumentException(fieldName + " is null or empty");
        }
    }
}