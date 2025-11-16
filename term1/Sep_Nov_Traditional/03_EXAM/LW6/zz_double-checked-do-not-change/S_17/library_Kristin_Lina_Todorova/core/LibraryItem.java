package library.core;

public abstract class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title) {
        this.itemId = "TEMP";
    }

    public LibraryItem(String title, String itemId){}

    public String getTitle() {
        return this.title;
    }

    public String getItemId() {
        return this.itemId;
    }

    public String getItemType();

    public String getdisplayInfo() {
        return getItemType() + ":" + this.title + "id:" + this.id;
    }

    public String validateNotBlank(String text, String fieldName) {
        if(text == null) {
            throw "IllegalArgumentException";
        }
    }
}