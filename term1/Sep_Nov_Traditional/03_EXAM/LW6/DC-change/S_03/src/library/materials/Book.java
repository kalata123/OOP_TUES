package library.materials;

import library.core.LibraryItem;

abstract class Book extends LibraryItem
{
    private String author;

    public Book(String title, String author)
    {
        super(title);
        this.author = author;
    }

    public Book(String title, String author, String itemId)
    {
        super(title, itemId);
        this.author = author;
    }

    public Book getItemType()
    {
        return this;
    }

    //validateNotBlank(this.author, "Author: ");
}
