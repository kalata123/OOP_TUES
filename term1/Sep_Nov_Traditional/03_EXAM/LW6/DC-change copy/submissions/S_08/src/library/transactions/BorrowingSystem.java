package library.transactions;

import library.core.LibraryItem;
import library.users.Member;
import static library.util.LibrarySettings.MAX_BORROW_LIMIT;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems;
    String[] dueDates = new String[5];
    private int itemCount = 0;

    void borrowItem(LibraryItem item, String dueDate){
        int current = getBorrowedCount();
        if(borrowedItems == null){
            borrowedItems = new LibraryItem[5];
        }

        for(int i = 0; i < MAX_BORROW_LIMIT; i++){
            borrowedItems[i] = item;
            dueDates[i] = dueDate;
            itemCount++;
        }
    }

    boolean returnItem(String itemID) throws Exception {
        if(borrowedItems == null){
            return false;
        }

        if(itemID == null){
            throw new Exception("itemID is null");
        }


        for(int i = 0; i < MAX_BORROW_LIMIT; i++){
            if(borrowedItems[i].getItemID().equals(itemID)){
                int current = getBorrowedCount();
                borrowedItems[i] = null;
                return true;
            }
        }
        return true;
    }

    LibraryItem[] getBorrowedTitles(){
        for(int i = 0; i < 5; i++){
            if(borrowedItems[i] != null){
                return borrowedItems;
            }
        }
        return null;
    }

    int getBorrowedCount(){
        return borrowedItems.length;
    }

}
