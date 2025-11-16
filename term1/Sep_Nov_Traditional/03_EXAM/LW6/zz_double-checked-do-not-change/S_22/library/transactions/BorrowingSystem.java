package library.transactions;

import library.core.LibraryItem;

public class BorrowingSystem {
    private static final int MAX_ITEMS = 5;

    private final LibraryItem[] borrowedItems;
    private final String[] dueDates;
    private int itemCount;

    public BorrowingSystem() {
        this.borrowedItems = new LibraryItem[MAX_ITEMS];
        this.dueDates = new String[MAX_ITEMS];
        this.itemCount = 0;
    }

    public void borrowItem(LibraryItemFlu item, String dueDate) {
        validateNotNull(item, "Item");
        validateNotBlank(dueDate, "Due date");

        if (itemCount >= MAX_ITEMS) {
            throw new IllegalStateException("Ne moje da borrowne poveche itemi: maximum ot " + MAX_ITEMS + " dostignat");
        }

        borrowedItems[itemCount] = item;
        dueDates[itemCount] = dueDate;
        itemCount++;
    }

    public boolean returnItem(String itemId) {
        validateNotBlank(itemId, "Item ID");

        int index = findItemIndex(itemId);
        if (index == -1) {
            return false; 
        }

        for (int i = index; i < itemCount - 1; i++) {
            borrowedItems[i] = borrowedItems[i + 1];
            dueDates[i] = dueDates[i + 1];
        }


        borrowedItems[itemCount - 1] = null;
        dueDates[itemCount - 1] = null;
        itemCount--;

        return true;
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

    private int findItemIndex(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if (itemId.equals(borrowedItems[i].getItemId())) {
                return i;
            }
        }
        return -1;
    }

    private void validateNotBlank(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " Ne moje da e null ili blank");
        }
    }

    private void validateNotNull(Object obj, String fieldName) {
        if (obj == null) {
            throw new IllegalArgumentException(fieldName + " Ne moje da e null");
        }
    }
}