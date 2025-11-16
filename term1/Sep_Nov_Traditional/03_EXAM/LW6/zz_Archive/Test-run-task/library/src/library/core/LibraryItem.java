// src/library/core/LibraryItem.java
package library.core;

public abstract class LibraryItem {
    private final String title;
    private final String itemId;

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

    public abstract String getItemType();

    public String getDisplayInfo() {
        return "[" + getItemType() + "]: " + title + " (ID: " + itemId + ")";
    }

    protected void validateNotBlank(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }
}
