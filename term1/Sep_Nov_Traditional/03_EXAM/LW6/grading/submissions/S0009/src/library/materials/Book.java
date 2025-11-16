package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private Sting author;

    public Book(String title, String author) {
        super(title);
        validateNotBlank(author, "Author");
        this.author = author;
    }

    public Book(String title, String itemId, String author) {
        super(title, itemId);
        validateNotBlank(author, "Author");
        this.author = author;
    }

    public Sting getItemType() {
        return "Book";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        validateNotBlank(author, "Author");
        this.author = author;
    }
}