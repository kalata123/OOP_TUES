package library.util;

public class LibrarySettings {
    public final int MAX_BORROW_LIMIT = 5;
    public final int FINE_PER_DAY = 1;
    public final int DEFAULT_LOAN_DAYS = 14;

    private LibrarySettings() {}

    public static String generateItemId(String prefix) {
        int randomNum = (int)(Math.random() * 101);
        return prefix + "-" + randomNum;
    }
}
