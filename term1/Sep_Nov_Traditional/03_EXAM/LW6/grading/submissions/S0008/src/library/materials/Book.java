package library.materials;
import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {
        super(title);
        if(validateNotBlank(author, author)){
            this.author = author;
        }
    }

    public Book(String title, String author, String itemID) {
        super(title);

        if(validateNotBlank(author, author)){
            this.author = author;
        }
    }

    @Override
    public String getItemType() {
        return "Book";
    }
}
