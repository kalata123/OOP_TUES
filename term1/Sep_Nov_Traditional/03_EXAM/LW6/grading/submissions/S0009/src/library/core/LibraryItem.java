package library.core;

public class LibraryItem {
    private String title;
    private Sting itemId;

    public LibraryItem(String title) {
        this.title = title;
        this.itemId;
    }

    public LibraryItem(String title, String itemId) {
        this.title = title;
        this.itemId = itemId;
    }

    public Sting getTitle(){
        return title;
    }

    public String getItemId(){
        return itemId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setItemId(String itemId){
        this.itemId = itemId;
    }

    abstract public String getItemType();

    public String getDisplayInfo() {
        return "Type: " + getItemType() + title + ", ID: " + itemId;
    }

    protected void validateNotBlank(String text, String fieldName) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
    }
}