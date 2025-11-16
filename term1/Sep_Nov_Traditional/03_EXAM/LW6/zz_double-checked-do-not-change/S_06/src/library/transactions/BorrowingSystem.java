package library.transactions;

import library.core.LibraryItem;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems;
    private String[] dueDates;
    private int itemCount;

    public BorrowingSystem() {
        this.borrowedItems = new LibraryItem[5];
        this.dueDates = new String[5];
        this.itemCount = 0;
    }

    public void borrowItem(LibraryItem item, String dueDate) {
        if(this.borrowedItems == null)
            throw new IllegalArgumentException("(Borrowing System borrowItem) Array is empty!");
        if(this.itemCount < 0 || this.itemCount >= borrowedItems.length || this.itemCount >= dueDates.length)
            throw new IndexOutOfBoundsException("(Borrowing System borrowItem) ItemCount is out of bounds!");

        this.borrowedItems[this.itemCount] = item;
        this.dueDates[itemCount] = dueDate;
        this.itemCount++;
    }

    public boolean returnItem(String itemId) {
        if(itemId.isEmpty() || itemId.isBlank())
            throw new IllegalArgumentException("(Borrowing System returnItem) Given Item ID is blank!");
        for(int i = 0; i < this.itemCount; i++) {
            if(this.borrowedItems[i].getItemId().equals(itemId)) {
                this.borrowedItems[i + 1] = this.borrowedItems[i];
                this.itemCount--;
                return true;
            }
        }
        return false;
    }

    String[] getBorrowedTitles() {
        String[] titles = new String[this.itemCount];
        for(int i = 0; i < this.itemCount; i++) {
            titles[i] = this.borrowedItems[i].getTitle();
        }
        return titles;
    }

    int getBorrowedCount() {
        return itemCount;
    }
}
