package library.utils;

public class LibrarySettings
{
    final int MAX_BORROW_LIMIT = 5;
    final int FINE_PER_DAY = 1;
    final int DEFAULT_LOAN_DAYS = 14;

    static String generateItemId(String prefix)
    {
        return "[" + prefix + "]-[" + (Math.random() * 101) + "]";
    }
}
