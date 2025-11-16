package library.core;

public abstract class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title){
        validateNotBlank(title, "Title");
        this.title = title;
        this.itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId) {
        validateNotBlank(title, "Title");
        this.title = title;
        validateNotBlank(itemId, "ItemId");
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }
    public String getItemId() {
        return itemId;
    }

    String getDisplayInfo(){
        return this.getItemType() + ": " + this.getTitle() + "(ID: " + this.getItemId() + ")";
    }

    protected void validateNotBlank(String text, String fieldName) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
    }

    protected abstract String getItemType();
}
