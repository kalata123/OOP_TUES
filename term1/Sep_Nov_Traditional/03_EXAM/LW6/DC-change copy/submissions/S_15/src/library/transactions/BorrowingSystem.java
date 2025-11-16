package library.transactions;

import library.core.LibraryItem;
import library.util.LibrarySettings;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems = new LibraryItem[5];
    private String[] DueDates = new String[5];
    private int itemCount;

    void borrowItem(LibraryItem item, String dueDate) {
        if(itemCount < borrowedItems.length){
            borrowedItems[itemCount] = item;
            DueDates[itemCount] = dueDate;
            itemCount++;
        }
        else{
            throw new IllegalStateException("Borrowing array is full.");
        }
    }

    boolean returnItem(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if(borrowedItems[i].getItemId().equals(itemId)){
                borrowedItems[i] = null;
                DueDates[i] = null;
                itemCount--;
                return true;
            }
        }
        throw new IllegalStateException("ItemId not found.");
    }

    String[] getBorrowedTitles() {
        String[] borrowedTitles = new String[5];
        for (int i = 0; i < borrowedItems.length; i++) {
            borrowedTitles[i] = borrowedItems[i].getTitle();
        }
        return borrowedTitles;
    }

    int getBorrowedCount(){
        return itemCount;
    }

    boolean hasBorrowedItem(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if(borrowedItems[i].getItemId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    String findDueDate(String itemId) {
        for (int i = 0; i < itemCount; i++) {
            if(borrowedItems[i].getItemId().equals(itemId)){
                return DueDates[i];
            }
        }
        throw new IllegalStateException("ItemId not found.");
    }

    double getUsagePercentage(){
        return (double)itemCount / LibrarySettings.MAX_BORROW_LIMIT*100;
    }

    String getBorrowStatus(){
        double usage = getUsagePercentage();
        if(usage < 40) {
            return "Light";
        }else if(usage > 40 && usage < 80) {
            return "Medium";
        }else if(usage > 80) {
            return "Heavy";
        }
        return "Unknown";
    }
}
