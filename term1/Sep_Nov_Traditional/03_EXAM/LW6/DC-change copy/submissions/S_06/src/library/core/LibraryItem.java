package library.core;

public abstract class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title) {
        if(title.isEmpty() || title.isBlank())
            throw new IllegalArgumentException("(LibraryItem Title Constructor) Title cannot be empty!");
        this.title = title;
        this.itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId) {
        if(title.isEmpty() || title.isBlank())
            throw new IllegalArgumentException("(LibraryItem Full Constructor) Title cannot be empty!");
        this.title = title;
        this.itemId = itemId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getItemId() {
        return this.itemId;
    }

    public abstract String getItemType();

    public String getDisplayInfo() {
        return this.getItemType() + ": " + this.getTitle() + " (ID: " + this.getItemId() + ")";
    }

    protected String validateNotBlank(String text, String fieldName) throws IllegalArgumentException {
        if(text.trim().isBlank() || text.trim().isEmpty()) throw new IllegalArgumentException(fieldName + " cannot be empty!");
        return text.trim();
    }
}
