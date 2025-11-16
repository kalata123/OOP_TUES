package library.core;

abstract public class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title) {
        validateNotBlank(title, "title");
        this.title = title;
        this.itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId) {
        validateNotBlank(title, "title");
        validateNotBlank(itemId, "itemId");
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

    public String getDisplayInfo() {
        return getItemType() + ": " + getTitle() + " (ID: " + getItemId() + ")";
    }

    protected void validateNotBlank(String text, String fieldName) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
}
