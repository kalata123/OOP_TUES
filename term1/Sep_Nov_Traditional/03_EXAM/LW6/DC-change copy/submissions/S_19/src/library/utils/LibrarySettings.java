package library.utils;

public class LibrarySettings {
    public static final int MAX_BORROWED_LIMIT = 5;
    public static final int FINE_PER_DAY = 1;
    public static final int DEFAULT_LOAN_DAYS = 14;
    private LibrarySettings() {}

    public static String generateRandomId(String prefix)
    {
        int randomNum = (int)(Math.random() * 101);
        return prefix + "-" + randomNum;
    }
}
