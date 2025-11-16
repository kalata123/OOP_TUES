// src/library/materials/Book.java
package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private final String author;

    public Book(String title, String author) {
        super(title);
        validateNotBlank(author, "author");
        this.author = author;
    }

    public Book(String title, String author, String itemId) {
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
}
