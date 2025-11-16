package library.util;

public class LibrarySettings {
    private LibrarySettings() {
        throw new AssertionError("Library Settings cannot be instantiated.");
    }

    public static final int MAX_BORROW_LIMIT = 5;
    public static final int FINE_PER_DAY = 1;
    public static final int DEFAULT_LOAN_DAYS = 14;

    static String generateItemId(String prefix){
        if(prefix == null){
            throw new IllegalArgumentException("Prefix cannot be null.");
        }
        return prefix + "-" + (int) (Math.random()*1001);
    }
}
