package library.transactions;

import library.core.LibraryItem;
import library.util.LibrarySettings;

public class BorrowingSystem {
    private LibraryItem[] borrowedItems = new LibraryItem[LibrarySettings.MAX_BORROW_LIMIT];
    private String[] dueDates = new String[LibrarySettings.MAX_BORROW_LIMIT]; //?

    private int itemCount = 0;

    public void borrowItem(LibraryItem item, String dueDate) throws Exception{
        if(dueDate != null && !dueDate.isBlank() && !dueDate.isEmpty())dueDates[itemCount] = dueDate;

        if(itemCount < LibrarySettings.MAX_BORROW_LIMIT){
            borrowedItems[itemCount] = item;
        }
        else{
            throw new LibraryException("cant barrow when items is full!");
        }
    }

    public boolean returnItem(String itemId){
        LibraryItem item = null;
        for(int i = 0;i < itemCount;i++){
            if(borrowedItems[itemCount].getItemId().equals(itemId)){
                item = borrowedItems[itemCount];

                for(int j = i;j < itemCount - 2;j++){
                    borrowedItems[j] = borrowedItems[j+1];
                }
                itemCount--;
                break;
            }
        }

        return !(item == null);
    }


    public String[] getBorrowedTitles(){
        String[] result = new String[5];

        for(int i = 0;i < itemCount;i++){
            result[i] = borrowedItems[i].getTitle();
        }

        return result;
    }

    public int getBorrowedCount(){
        return itemCount;
    }

}
