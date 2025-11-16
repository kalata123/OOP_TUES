package library.transactions;

import library.core.LibraryItem;

import static library.util.LibrarySettings.MAX_BORROW_LIMIT;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems = new LibraryItem[MAX_BORROW_LIMIT];
    private String[] dueDates = new String[MAX_BORROW_LIMIT];
    private int itemCount = 0;

    public void borrowItem(LibraryItem item, String dueDate) {
        if(itemCount >= MAX_BORROW_LIMIT) {
            throw new LibraryException("Maximum borrow limit reached");
        }

        if(dueDate == null || dueDate.isEmpty()) {
            throw new IllegalArgumentException("Due date cannot be empty");
        }

//        borrowedItems[itemCount] = item;
//        dueDates[itemCount] = dueDate;
//        itemCount++;

        for(int i = 0; i < MAX_BORROW_LIMIT; i++) {
            if(borrowedItems[i] == null) {
                borrowedItems[i] = item;
                dueDates[i] = dueDate;
                itemCount++;
            }
        }
    }

    public boolean returnItem(String itemId) {
        if(itemId == null || itemId.isEmpty()) {
            throw new IllegalArgumentException("itemId cannot be null or empty");
        }

        for(int i = 0; i < itemCount; i++) {
            if(itemId.equals(borrowedItems[i].getItemId())) {
                borrowedItems[i] = null;
                dueDates[i] = null;
                itemCount--;
                return true;
            }
        }

        throw new LibraryException("itemId not found");
//        return false;
    }

    public String[] getBorrowedTitles() {
        String[] borrowedTitles = new String[itemCount];
        for(int i = 0; i < itemCount; i++) {
            borrowedTitles[i] = borrowedItems[i].getTitle();
        }
        return borrowedTitles;
    }

    public int getBorrowedCount() {
        return itemCount;
    }
}
