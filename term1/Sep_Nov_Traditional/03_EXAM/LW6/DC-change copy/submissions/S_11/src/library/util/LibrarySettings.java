package library.util;

public class LibrarySettings {
    public static final int MAX_BORROW_LIMIT = 5;
    public static final int FINE_PER_DAY = 1;
    public static final int DEFAULT_LOAN_DAYS = 14;

    private LibrarySettings() {}

    public static String generateItemId(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            throw new IllegalArgumentException("prefix cannot be null or empty");
        }

        int randomNum = (int)(Math.random() * 101);
        return prefix + "-" + randomNum;
    }
}
