package library.transactions;

import library.core.LibraryItem;

import java.util.Objects;
import library.transactions.LibraryException;

public class BorrowingSystem {
    private final int MAX_ARRAY_COUNT = 5;
    private LibraryItem[] borrowedItems = new LibraryItem[MAX_ARRAY_COUNT];
    private String[] dueDates = new String[MAX_ARRAY_COUNT];

    private int itemCount = 0;

    public void borrowItem(LibraryItem item, String dueDate){
        if(borrowedItems.length == MAX_ARRAY_COUNT && dueDates.length == MAX_ARRAY_COUNT){
            throw new IllegalArgumentException("No more space in borrowed items");
        }

        borrowedItems[itemCount] = item;
        dueDates[itemCount] = dueDate;
        itemCount++;
    }

    public boolean returnItem(String itemId){
        int arrIndex = -1;
        for(int i = 0; i <= this.itemCount; i++){
            if(Objects.equals(this.borrowedItems[i].getItemId(), itemId)){
                arrIndex = itemCount;
                break;
            }
        }

        if(arrIndex == -1){
            throw new LibraryException("No item with id " + itemId);
        }

        LibraryItem[] newborrowedItems = new LibraryItem[itemCount - 1];
        if (arrIndex >= 0) System.arraycopy(this.borrowedItems, 0, newborrowedItems, 0, arrIndex);

        for(int i = arrIndex + 1; i <= this.itemCount; i++){
            newborrowedItems[i] = this.borrowedItems[i];
        }
        System.arraycopy(newborrowedItems, 0, this.borrowedItems, 0, newborrowedItems.length);

        this.itemCount = newborrowedItems.length - 1;
        return true;

    }

    public String[] getBorrowedTitles(){
        String[] borrowedTitles = new String[this.itemCount + 1];
        for(int i = 0; i <= this.itemCount; i++){
            borrowedTitles[i] = this.borrowedItems[i].getTitle();
        }

        return borrowedTitles;
    }

    public int getBorrowedItemCount(){
        return this.itemCount;
    }
}
