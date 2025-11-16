package library.transactions;

import library.core.LibraryItem;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems;
    private String[] dueDates;
    private int itemCount;

    public BorrowingSystem(LibraryItem[] borrowedItems, String[] dueDates) {
        this.borrowedItems = borrowedItems;
        this.dueDates = dueDates;
        itemCount = borrowedItems.length;
    }


    public BorrowingSystem() {
        this.borrowedItems = new LibraryItem[5];
        this.dueDates = new String[5];
        this.itemCount = 0;
    }

    public void borrowItem(LibraryItem item, String dueDate) {
        if(itemCount >= 5) {
            throw new IllegalStateException("Borrowing is full");
        }
        this.borrowedItems[itemCount] = item;
        dueDates[itemCount] = dueDate;
    }

    public boolean returnItem(String itemId) {
        for(int i = 0; i < itemCount; i++) {
            if(borrowedItems[i].getItemId() ==  itemId) {
                borrowedItems[i] = null;
                dueDates[i] = null;
                itemCount--;
                return true;
            }
        }
        throw  new IllegalStateException("Item not found");
    }

    public String[] getBorrowedTitles() {
        String[] titles = new String[itemCount];
        for(int i = 0; i < itemCount; i++) {
            titles[i] = borrowedItems[i].getTitle();
        }
        return titles;
    }

    public int getBorrowedItemCount() {
        return itemCount;
    }



}
