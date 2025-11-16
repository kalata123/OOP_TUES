package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;
    public Book(String title, String author)
    {
        super(title);
        if(validateNotBlank(author, "Author")) this.author = author;
    }
    public Book(String title, String author, String itemID)
    {
        super(title, itemID);
        if(validateNotBlank(author, "Author")) this.author = author;
    }
    public String getItemType()
    {
        return "Book";
    }
}
