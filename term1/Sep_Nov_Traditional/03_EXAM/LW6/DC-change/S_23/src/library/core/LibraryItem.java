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

    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public abstract String getItemType();

    public String getDisplayInfo(){
        return getItemType() + ": " + title + " (" + itemId + ": ID)";
    }

    protected void validateNotBlank(String text, String fieldName) throws IllegalArgumentException{
        if(text.isEmpty() || text == null){
            throw new IllegalArgumentException("text cant be null or empty!");
        }
    }
}
