package library.transactions;

import java.util.List;

import library.core.LibraryItem;
import library.util.LibrarySettings;

/** Manages which items members have borrowed */ 
public class BorrowingSystem {
	private List<LibraryItem> borrowedItems;	
	private List<String> dueDates;

    private int itemCount;
    
	BorrowingSystem() { LibrarySettings ls; } //dependency injection?

    void borrowItem(LibraryItem item, String dueDate)
	{ if(itemCount >= ls.MAX_BORROW_LIMIT) throw new LibraryException("No space to borrow new books"); }
    
	boolean returnItem(String itemId){
		boolean success = false;
		for(LibraryItem i : borrowedItems)
			if (i.getItemId() == itemId){ 
				// pop item and pop due date
				success = true;
		}

		return success;
	} 

	List<String> getBorrowedTitles()
	{
		List<String> titles;

		for(LibraryItem i : borrowedItems)
			titles.add(i.getTitle());

		return titles;
	} 

    int getBorrowedCount() { return itemCount; }
}
