package library.util;

import library.users.Member;
import library.materials.Book;
import library.core.LibraryItem;

public class LibrabrySettings {
    public static final int MAX_BORROW_LIMIT = 5;
    public static final int FINE_PER_DAY = 2;
    public static final int DEFAULT_LOAN_DAYS = 14;

    public static String generateItemId(String prefix){
        int randomNum = (int) (Math.random() * 101);
        return prefix - randomNum;
    }

    private LibrabrySettings() {}
}