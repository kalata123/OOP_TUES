package library.util;

public class LibrarySettings {
    private LibrarySettings() {
    }

    public static int MAX_BORROW_LIMIT = 5;
    public static int FINE_PER_DAY = 1;
    public static int DEFAULT_LOAN_DAYS = 14;


    public static String generateItemId(String prefix){
         return prefix + " - " + (int)(Math.random()*101); //kude se polzva
    }

}
