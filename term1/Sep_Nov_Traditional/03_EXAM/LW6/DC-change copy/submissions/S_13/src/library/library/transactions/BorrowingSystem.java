package library.transactions;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems = new LibraryItem[5];
    private String[] dueDates = new String[5];
    private int itemCount = 0;
    public void borrowItem(LibraryItem item, String dueDate)
    {
        if(itemCount >= borrowedItems.length)
        {
            throw new IllegalStateException("Cannot borrow more items — limit of 5 reached");
        }
        borrowedItems[itemCount] = item;
        dueDates[itemCount] = dueDate;
        itemCount++;
    }
    public boolean returnItem(String itemId)
    {
        int index = findItemIndexById(itemId);
        if(index == -1)
        {
            throw new IllegalArgumentException("Item with ID " + itemId + " not found");
        }

        for(int i = index; i<itemCount-1; i++)
        {
            borrowedItems[i] = borrowedItems[i + 1];
            dueDates[i] = dueDates[i + 1];
        }

        borrowedItems[itemCount - 1] = null;
        dueDates[itemCount - 1] = null;
        itemCount--;
        return true;
    }

    public String[] getBorrowedTitles()
    {
        String[] titles = new String[itemCount];
        for(int i = 0;i<itemCount; i++)
        {
            titles[i] = borrowedItems[i].getTitle();
        }
        return titles;
    }
    public int getBorrowedCount()
    {
        return itemCount;
    }

    private int findItemIndexById(String itemId)
    {
        for (int i = 0;i<itemCount; i++)
        {
            if (borrowedItems[i].getItemId().equals(itemId))
            {
                return i;
            }
        }
        return -1;
    }
}
