package library.transactions;

import library.core.LibraryItem;

public class BorrowingSystem {
    private static final int MAX_ITEMS = 5;
    private LibraryItem[] borrowedItems = new LibraryItem[MAX_ITEMS];
    private String[] dueDates = new String[MAX_ITEMS];
    private int itemCount = 0;

    public void borrowItem(LibraryItem item, String dueDate) {
        if (itemCount >= MAX_ITEMS) {
            throw new LibraryException("Cannot borrow more than " + MAX_ITEMS + " items");
        }

        borrowedItems[itemCount] = item;
        itemCount++;
    }

    public boolean returnItem(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if (borrowedItems[i].getItemId().equals(itemId)) {
                borrowedItems[itemCount - 1] = null;
                itemCount--;
                return true;
            }
        }
        return false;
    }

    public String[] getBorrowedTitles() {
        String[] titles = new String[itemCount];
        for (int i = 0; i < itemCount; i++) {
            titles[i] = borrowedItems[i].getTitle();
        }
        return titles;
    }

    public int getBorrowedCount() {
        return itemCount;
    }
}