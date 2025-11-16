package library.materials;
import core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {}
    public Book(String title, String author, String itemId) {}

    public String getItemType() {
        return "Book";
    }

    validateNotBlank(author, this.author);
}