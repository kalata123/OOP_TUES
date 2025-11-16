package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {
        super(title);
        helper(title, author);
        this.author = author;
    }

    public Book(String title, String author, String itemId) {
        super(title, itemId);
        helper(title, author); // TODO -- fix
        this.author = author;
    }

    @Override
    public String getItemType() {
        return "Book";
    }
}