package library.materials;

import library.core.LibraryItem;

class Book extends LibraryItem{
    private String author;

    Book(String title, String author){
        super(title);
        this.author = author;
    }

    Book(String title, String author, String itemId){
        super(title);
        this .author = author;
        super(ItemId);
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    String getItemType() {
        return "Book";
    }

}