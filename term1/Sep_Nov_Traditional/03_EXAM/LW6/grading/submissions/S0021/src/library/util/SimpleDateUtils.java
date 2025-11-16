package library.util;

import static library.util.LibrarySettings.DEFAULT_LOAN_DAYS;

// TODO -- make not instational
public final class SimpleDateUtils {
    static String calculateDueDate(int daysFromNow) {
        return String.format("Due in %d days", daysFromNow);
    }

    static String calculateDueDate(String startDate) {
        return String.format("Due on %s plus %d", startDate, DEFAULT_LOAN_DAYS);
    }

    String calculateDueDate(int days, boolean isExtension) {
        if (isExtension) {
            return String.format("Due on %d days without extension", days);
        }
        return String.format("Due on %d days with extension", days);
    }
}