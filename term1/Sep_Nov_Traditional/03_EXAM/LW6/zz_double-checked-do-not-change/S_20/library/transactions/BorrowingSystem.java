package library.transactions;

import library.core.LibraryItem;
import library.users.Member;
import library.util.LibrarySettings;

public class BorrowingSystem {

    private LibraryItem[] borrowedItems;
    private String[] dueDates;
    private int itemCount;
    private Member member;

    public BorrowingSystem(Member member) {
        this.member = member;
        this.borrowedItems = new LibraryItem[LibrarySettings.MAX_BORROWED_COUNT];
        this.dueDates = new String[LibrarySettings.MAX_BORROWED_COUNT];
        this.itemCount = 0;
    }  

    public void borrowItem(LibraryItem item, String dueDate) {
        if (member.getBorrowedCount() >= LibrarySettings.MAX_BORROWED_COUNT) {
            throw new IllegalStateException("Cannot borrow more than " + LibrarySettings.MAX_BORROWED_COUNT + " items");
        }
        member.incrementBorrowed();
        borrowedItems[itemCount] = item;
        dueDates[itemCount] = dueDate;
        itemCount++;
    }
    public boolean returnItem(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if (borrowedItems[i].getItemId().equals(itemId)) {
                member.decrementBorrowed();
                borrowedItems[i] = null;    
                dueDates[i] = null;
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