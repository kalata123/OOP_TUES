package library.transactions;

import library.core.LibraryItem;

public class BorrowingSystem {
    private static final int MAX = 5;
    private LibraryItem[] borrowedItems = new LibraryItem[MAX];
    private String[] dueDates = new String[MAX];
    private int itemCount = 0;

    public void borrowItem(LibraryItem item, String dueDate) {
        if (item == null) {
            throw new IllegalArgumentException("item cannot be null");
        }
        if (dueDate == null || dueDate.trim().isEmpty()) {
            throw new IllegalArgumentException("dueDate cannot be null or blank");
        }
        if (itemCount >= MAX) {
            throw new LibraryException("Borrowing system is full");
        }
        // add to first empty slot
        for (int i = 0; i < MAX; i++) {
            if (borrowedItems[i] == null) {
                borrowedItems[i] = item;
                dueDates[i] = dueDate;
                itemCount++;
                return;
            }
        }
    }

    public boolean returnItem(String itemId) {
        if (itemId == null || itemId.trim().isEmpty()) {
            throw new IllegalArgumentException("itemId cannot be null or blank");
        }
        for (int i = 0; i < MAX; i++) {
            if (borrowedItems[i] != null && borrowedItems[i].getItemId().equals(itemId)) {
                borrowedItems[i] = null;
                dueDates[i] = null;
                itemCount--;
                return true;
            }
        }
        throw new LibraryException("Item with ID " + itemId + " not found");
    }

    public String[] getBorrowedTitles() {
        String[] titles = new String[itemCount];
        int idx = 0;
        for (int i = 0; i < MAX; i++) {
            if (borrowedItems[i] != null) {
                titles[idx++] = borrowedItems[i].getTitle();
            }
        }
        return titles;
    }

    public int getBorrowedCount() {
        return itemCount;
    }
}
