package library.core;

abstract public class LibraryItem {
    private String title;
    private String itemID;

    public LibraryItem(String title) {
        this.title = title;
        itemID = "TEMP";
    }

    public LibraryItem(String title, String itemID) {
        this.title = title;
        this.itemID = itemID;
    }

    public String getTitle() {
        return title;
    }

    public String getItemID() {
        return itemID;
    }

    abstract public String getItemType();

    public String getDisplayInfo(){
        return getItemType() + getTitle() + "ID" +  getItemID();
    }

    protected boolean validateNotBlank(String text, String fieldname){
        if(text == null || text.isEmpty()){
            throw new IllegalArgumentException(fieldname + " is blank");
        }

        return true;
    }
}
