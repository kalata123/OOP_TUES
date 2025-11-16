package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {
        super(title);
        this.author = author;
        validateNotBlank(title, author);
    }

    public Book(String title, String author, String itemId) {
        super(title, itemId);
        this.author = author;
        validateNotBlank(title, author);
    }

    public String getAuthor() {
        return author;
    }

    public String getItemType() {
        return "Book";
    }


}
