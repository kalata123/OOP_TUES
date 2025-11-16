package library.core;

abstract public class LibraryItem {
    private String title;
    private String itemId;

    public LibraryItem(String title, String itemId) {
        validateNotBlank(title, "title");
        this.title = title;
        this.itemId = itemId;
    }

    public LibraryItem(String title) {
        validateNotBlank(title, "title");
        this.title = title;
        this.itemId = "TEMP";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    abstract public String getItemType();

    public String getDisplayInfo(){
        return "[ " + this.getItemType() + " ]: "
                + this.getTitle() + " ( ID: " + this.getItemId() + " )";
    }

    protected void validateNotBlank(String text, String fieldName){
        if(text == null || text.isEmpty()){
            throw new IllegalArgumentException("Field " + fieldName + " is blank");
        }
    }
}
