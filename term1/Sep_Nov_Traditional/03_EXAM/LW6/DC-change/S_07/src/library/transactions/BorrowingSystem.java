package library.transactions;
import library.core.LibraryItem;
import library.util.LibrarySettings;
import library.transactions.LibraryException;

public class BorrowingSystem {

    private LibraryItem[] borrowedItems;
    private String[] dueDates;

    private int itemCount = 0;

    public BorrowingSystem() {
        this.borrowedItems = new LibraryItem[5];
        this.dueDates = new String[5];
    }

    public void borrowItem(LibraryItem item, String dueDate) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (dueDate == null || dueDate.trim().isBlank()) {
            throw new IllegalArgumentException("Due date cannot be null or blank");
        }
        if (itemCount >= LibrarySettings.MAX_BORROW_LIMIT) {
            throw new LibraryException("Item limit reached");
        }
        for (int i = 0; i < 5; i++) {
            if (borrowedItems[i] == null) {
                borrowedItems[i] = item;
                dueDates[i] = dueDate;
            }
        }
        itemCount++;
    }

    public boolean returnItem(String itemId) {
        boolean suc = false;
        for (int i = 0; i < 5; i++) {
            if (borrowedItems[i] != null) {
                if (borrowedItems[i].getItemId().equals(itemId)) {
                    borrowedItems[i] = null;
                    dueDates[i] = null;
                    suc = true;
                }
            }
        }
        return suc;
    }

    public String[] getBorrowedTitles() {
        String[] borrowedTitles = new String[5];
        for (int i = 0; i < 5; i++) {
            if (borrowedItems[i] != null) {
                borrowedTitles[i] = borrowedItems[i].getTitle();
            }
        }
        return borrowedTitles;
    }

    public int getBorrowedCount() {
        return itemCount;
    }
}
