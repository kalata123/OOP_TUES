package library.core;

abstract public LibraryItem {
    private String title;
    private String itemId;

    LibraryItem(String title){
        this.title = title;
        this.itemId = "TEMP";
    }

    LibraryItem(String title, String itemId) {
        this.title = title;
        this.itemId = itemId;
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
    abstract String getItemType();

    public String GetDisplayInfo(){
        return system.out.printIn("[TYPE]: [TITLE] (ID: [ID])")
    }
    protected void validateNotBlank(String text, String fieldName)throws IllegalArgumentException{
        if (text.isEmpty() || text == null){
            throw new IllegalArgumentException(fieldName + " is null or empty");
        }
    }
}