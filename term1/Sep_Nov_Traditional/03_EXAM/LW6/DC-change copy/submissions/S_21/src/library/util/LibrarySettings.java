package library.util;

public class LibrarySettings {

    public final static int MAX_BORROW_LIMIT = 5;
    public final static int FINE_PER_DAY = 1;
    public final static int DEFAULT_LOAN_DAYS = 14;

    private LibrarySettings() {}

    static public String generateItemId(String prefix) {
        return String.format("%s-%d", prefix, (int)(Math.random() * 101));
    }
}