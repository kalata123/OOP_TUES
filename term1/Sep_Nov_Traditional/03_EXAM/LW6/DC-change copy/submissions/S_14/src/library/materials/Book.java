package library.materials;
import library.core.LibraryItem;
public class Book extends LibraryItem {
    private String author;
    public Book(String title, String author)
    {
        super(title);
        try{
            this.validateNotBlank(author, "Author");
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        this.author = author;
    }

    public Book(String title, String itemID, String author)
    {
        super(title, itemID);
        try{
            this.validateNotBlank(author, "Author");
        }
        catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        this.author = author;
    }
    @Override
    public String getItemType()
    {
        return "Book";
    }


}
