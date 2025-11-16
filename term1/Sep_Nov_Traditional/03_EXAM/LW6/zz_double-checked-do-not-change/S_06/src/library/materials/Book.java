package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {
        super(title);
        this.author = validateNotBlank(author, "Author");
    }

    public Book(String title, String author, String itemId) {
        super(title, itemId);
        this.author = validateNotBlank(author, "Author");
    }

    public String getItemType() {
        return "Book";
    }

}
