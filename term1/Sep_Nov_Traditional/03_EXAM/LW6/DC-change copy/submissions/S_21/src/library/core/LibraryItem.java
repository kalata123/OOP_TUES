package library.core;

abstract public class LibraryItem {

    private String title;
    private String itemId;

    public LibraryItem(String title) {
        this.title = title;
        this.itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId) {
        this.title = title;
        this.itemId = itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    abstract public String getItemType();

    public String getDisplayedInfo() {
        return String.format("[%s: %s] (ID: [%s])", getItemType(), getTitle(), getItemId());
    }

    protected void helper(String text, String fieldName) {
        if (text == null || text.isBlank() || fieldName == null || fieldName.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}