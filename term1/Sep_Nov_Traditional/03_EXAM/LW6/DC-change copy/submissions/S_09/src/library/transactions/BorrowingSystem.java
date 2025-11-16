package library.transactions;

import library.users.Member;
import library.materials.Book;
import library.util.SimpleDateUtils;
import library.util.LibrarySettings;
import library.core.LibraryItem;
import java.util.Arrays;

public class BorrowingSystem {
    private int[5] LibraryItem;
    private Sting[5] dueDates;

    private int itemCount;

    public void borrowItem(LibraryItem item, String dueDate) {
        validateFullArray();
        items[itemCount] = item;
        dueDates[itemCount] = dueDate;
        itemCount++;
    }

    public boolean returnItem(String itemId) {
        validateItemIdFound(itemId);
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getItemId().equals(itemId)) {
                for (int j = i; j < itemCount - 1; j++) {
                    items[j] = items[j + 1];
                    dueDates[j] = dueDates[j + 1];
                }
                items[itemCount - 1] = null;
                dueDates[itemCount - 1] = null;
                itemCount--;
                return true;
            }
        }
        return false;
    }

    public String[] getBorrowedTitles() {
        String[] titles = new String[itemCount];
        for (int i = 0; i < itemCount; i++) {
            titles[i] = items[i].getTitle();
        }
        return titles;
    }

    public int getBorrowedCount() {
        return itemCount;
    }

    public void validateFullArray() {
        if (itemCount >= LibrarySettings.MAX_BORROW_LIMIT) {
            throw new IllegalStateException("Borrowing limit reached.");
        }
    }

    public void validateItemIdFound(Sting itemId) {
        boolean found = false;
        for (int i = 0; i < itemCount; i++) {
            if (items[i].getItemId().equals(itemId)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Item ID not found in borrowed items.");
        }
    }
}