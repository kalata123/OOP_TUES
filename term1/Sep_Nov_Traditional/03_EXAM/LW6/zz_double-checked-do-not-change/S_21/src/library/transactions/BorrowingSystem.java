package library.transactions;

import library.core.LibraryItem;
import library.users.Member;

public class BorrowingSystem {

    private LibraryItem[] borrowedItems = new LibraryItem[5];
    private String[] dueDates = new String[5];
    private int itemCount = 0;

    Member member;

    public void borrowItem(LibraryItem item, String dueDate) {
        if (itemCount == 5) {
            throw new IndexOutOfBoundsException("Borrowing is full");
        }
        borrowedItems[itemCount] = item;
        dueDates[itemCount] = dueDate;
        itemCount++;
        member.incrementBorrowed();
    }

    public boolean returnItem(String itemId) {
        int i = 0;
        boolean returned = false;
        for (LibraryItem item : borrowedItems) {
            if (item.getItemId().equals(itemId)) {
                returned = true;
                break;
            }
            i ++;
        }
        if(!returned) {
            throw new IllegalArgumentException("Item ID is not found");
        }
        for (int j = i; j < itemCount - 1; j++) {
            borrowedItems[j] = borrowedItems[j + 1];
            dueDates[j] = dueDates[j + 1];
        }
        borrowedItems[itemCount - 1] = null;
        dueDates[itemCount - 1] = null;
        itemCount--;
        member.decrementBorrowed();

        return true;
    }

    public String[] getBorrowedTitles() {
        String[] borrowedTitles = new String[5];
        int i = 0;
        for (LibraryItem item : borrowedItems) {
            borrowedTitles[i] = item.getTitle();
            i ++;
        }

        return borrowedTitles;
    }

    public int getBorrowedCount() {
        return itemCount;
    }
}
