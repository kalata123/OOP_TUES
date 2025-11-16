package library.transactions;

import library.core.LibraryItem;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems;
    private String[] dueDates;
    private int itemCount;
    
    public void borrowItem(LibraryItem item, String dueDate)
    {
        if(itemCount >= borrowedItems.length)
        {
            throw new IllegalStateException("Borrowing system is full");
        }
        this.borrowedItems[itemCount] = item;
        this.dueDates[itemCount] = dueDate;
        itemCount++;
    }
    public boolean returnItem(String itemId)
    {
        for(int i = 0; i < itemCount; i++)
        {
            if(borrowedItems[i].getItemId().equals(itemId))
            {
                borrowedItems[i] = null;
                dueDates[i] = null;
                itemCount--;
                return true;
            }
        }
        throw new IllegalArgumentException("Item with ID " + itemId + " not found");
    }
    public String[] getBorrowedTitles()
    {
        String[] titles = new String[itemCount];
        for(int i = 0; i < itemCount; i++)
        {
            titles[i] = borrowedItems[i].getTitle();
        }
        return titles;
    }
    public int getBorrowedCount()
    {
        return itemCount;
    }
}