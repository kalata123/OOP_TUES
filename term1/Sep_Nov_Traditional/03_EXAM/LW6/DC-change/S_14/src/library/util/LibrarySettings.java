package library.util;


public final class LibrarySettings {
    public static final int MAX_BORROW_LIMIT = 5;
    public static final int FINE_PER_DAY = 1;
    public static final int DEFAULT_LOAN_DAYS = 14;

    private LibrarySettings()
    {}

    public static String generateItemId(String prefix)
    {
        int randomID = (int)(Math.random() * 101);
        return prefix + Integer.toString(randomID);
    }

}
