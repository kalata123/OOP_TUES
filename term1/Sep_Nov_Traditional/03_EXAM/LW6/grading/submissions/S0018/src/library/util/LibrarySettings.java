package library.util;

import java.util.Random;

public class LibrarySettings {
    public final static int MAX_BORROW_LIMIT = 5;
    public final static int FINE_PER_DAY = 1;
    public final static int DEFAULT_LOAN_DAYS = 14;

    private LibrarySettings() {}

    public static String generateItemId(String prefix) {
        Random rand = new Random();
        int num = rand.nextInt(100, 1000);

        return prefix + num;
    }
}
