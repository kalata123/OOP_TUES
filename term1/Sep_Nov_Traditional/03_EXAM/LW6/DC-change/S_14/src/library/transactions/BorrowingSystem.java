package library.transactions;

import library.core.LibraryItem;
import library.util.LibrarySettings;

import java.util.Vector;

public class BorrowingSystem
{
    private Vector<LibraryItem> borrowedItems;
    private Vector<String> dueDate;
    private int itemCount;
    public BorrowingSystem()
    {
        this.borrowedItems = new Vector<>();
        this.dueDate = new Vector<>();
        this.itemCount = 0;
    }
    public void borrowItem(LibraryItem item, String dueDate) throws LibraryException
    {
        if(this.itemCount >= this.borrowedItems.size())
        {
            throw new LibraryException("Borrowing is full");
        }
        this.borrowedItems.add(item);
        this.dueDate.add(dueDate);
        this.itemCount++;
    }
    public boolean returnItem(String itemId) throws LibraryException
    {
        if(this.itemCount == 0)
        {
            throw new LibraryException("Nothing is borrowed");
        }
        for(LibraryItem item : this.borrowedItems)
        {
            if(item.getItemID() == itemId)
            {
                this.borrowedItems.remove(item);
                this.itemCount--;
                return true;
            }
        }
        return false;
    }
    public String[] getBorrowedTitles()
    {
        String[] titles = new String[this.itemCount];
        for(int i = 0; i < this.itemCount; i++)
        {
            titles[i] = this.borrowedItems.get(i).getTitle();
        }
        return titles;
    }
    public int getBorrowedCount()
    {
        return this.itemCount;
    }





}
