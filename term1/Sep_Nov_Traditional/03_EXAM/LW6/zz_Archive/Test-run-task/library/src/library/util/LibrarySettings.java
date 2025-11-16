// src/library/util/LibrarySettings.java
package library.util;

import java.util.concurrent.ThreadLocalRandom;

public final class LibrarySettings {
    public static final int MAX_BORROW_LIMIT = 5;
    public static final int FINE_PER_DAY = 1;
    public static final int DEFAULT_LOAN_DAYS = 14;

    private LibrarySettings() { }

    public static String generateItemId(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            throw new IllegalArgumentException("prefix must not be blank");
        }
        int code = ThreadLocalRandom.current().nextInt(1000, 10000);
        return prefix + "-" + code;
    }
}
