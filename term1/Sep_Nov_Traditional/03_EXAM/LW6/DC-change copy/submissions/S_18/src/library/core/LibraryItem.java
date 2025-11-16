package library.core;

public abstract class LibraryItem {
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

    public String getTitle() {
        return title;
    }

    public String getItemId() {
        return itemId;
    }

    public abstract String getItemType();

    public String getDisplay() {
        return getItemType() + ": " + title + " (ID: " + itemId + ")";
    }

    protected void validateNotBlank(String text, String fieldName) {
        if(text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is blank");
        }
        if(fieldName == null || fieldName.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is blank");
        }
    }


}
