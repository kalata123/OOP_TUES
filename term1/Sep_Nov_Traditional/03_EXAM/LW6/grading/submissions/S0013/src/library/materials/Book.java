package library.core;
import library.core.LibraryItem;

public class Book {
    private String author;
    public Book(String author, String title) {
        this.author = author;
    }
    public Book(String author, String title, String itemId) {
        this.author = author;
        validateNotBlank(title, "title");
        validateNotBlank(itemId, "itemId");
    }
    public String getItemType()
    {
        return "Book";
    }
    public String getAuthor(String author)
    {
        return author;
    }

    protected void validateNotBlank(String text, String fieldName)
    {
        if(text == null || text.isBlank())
        {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    public void setAuthor(String author)
    {
        validateNotBlank("author", author);
        this.author = author;
    }

}



/*
Represents a book in the library:

Extends LibraryItem
Private field: author (String)
Two constructors:
Book(String title, String author)
Book(String title, String author, String itemId)
Implement getItemType() to return "Book"
Use the inherited validation method to ensure author is not blank

 */