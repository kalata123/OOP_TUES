package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {
        super(title);
        this.author = author;
    }


    public Book(String title, String itemId, String author) {
        super(title, itemId);
        validateNotBlank(author, "author");
        this.author = author;
    }

    @Override
    public String getItemType() {
        return "Book";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

