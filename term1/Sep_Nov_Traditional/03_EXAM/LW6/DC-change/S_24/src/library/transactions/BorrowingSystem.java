package library.transactions;
import library.core.LibraryItem;

public class BorrowingSystem {
    LibraryItem[] borrowed_items = new  LibraryItem[5];
    String[] dueDates = new String[5];
    private int itemCount = 0;

    public void BorrowItem(LibraryItem item, String dueDate)
    {
        if(itemCount < 5)
        {
            borrowed_items[itemCount] = item;
            dueDates[itemCount] = dueDate;
            itemCount++;
        }
        else throw new RuntimeException("Borrowing is full");
    }
    public boolean returnItem(String itemId)
    {
        for(int i = 0; i < 5; i++)
        {
            if(borrowed_items[i].getItemId().equals(itemId))
            {
                borrowed_items[i] = null;
                itemCount--;
                return true;
            }
        }
        throw new RuntimeException("Item Id is not found");
    }

   public  String[] getBorrowedTitles()
    {
        String[] titles = new String[5];
        for(int i = 0; i < 5; i++)
        {
            titles[i] = borrowed_items[i].getTitle();
        }
        return titles;
    }

    public int getBorrowedCount()
    {
        return itemCount;
    }
}
