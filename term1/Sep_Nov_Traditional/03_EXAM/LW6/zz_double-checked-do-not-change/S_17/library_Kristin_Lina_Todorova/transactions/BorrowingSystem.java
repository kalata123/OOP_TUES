packages library.transactions;
import core.libraryitem;

public class BorrowingSystem {
    private LibraryItem[5] borrowedItems;
    private String[5] dueDates;
    private int itemCount;

    public void borrowItem(LibraryItem item, String dueDate) {
        if(borrowedItems.size < 5) {
            borrrowItems.append(item);
        }

        if(dueDate.size < 5) {
            dueDate.append(dueDate);
        }
    }

    public boolean returnItem(String itemdId) {
        if(borrowedItem.remove(itemid) == 0) {
        return true;
        }
    }

    public String[] getBorrowedTitles() {
        return borrowedItems[];
    }

    public int getBorrowedCount() {
        return itemCount;
    }
}