package library.materials;
import library.core.LibraryItem;

public class Book extends LibraryItem{

    private String Author;

    public Book(String title, String author)
    {
        super(title);
        this.Author = author;
        try{
            ValidateNotBlank("Author", author);
        }catch(Exception e){
            System.err.println("Author Required");
        }
    }

    public Book(String title, String author, String itemId)
    {
        super(title, itemId);
        this.Author = author;
        try{
            ValidateNotBlank("Author", author);
        }catch(Exception e){
            System.err.println("Author Required");
        }
    }


    @Override public String getItemType()
    {
        return "Book";
    }
}
