// src/library/util/SimpleDateUtils.java
package library.util;

public final class SimpleDateUtils {
    private SimpleDateUtils() { }

    // 1) "Due in X days"
    public static String calculateDueDate(int daysFromNow) {
        return "Due in " + daysFromNow + " days";
    }

    // 2) "Due on [startDate] plus default days"
    public static String calculateDueDate(String startDate) {
        if (startDate == null || startDate.trim().isEmpty()) {
            throw new IllegalArgumentException("startDate must not be blank");
        }
        return "Due on " + startDate.trim() + " plus " + LibrarySettings.DEFAULT_LOAN_DAYS + " days";
    }

    // 3) Different message if it's an extension
    public static String calculateDueDate(int days, boolean isExtension) {
        if (isExtension) {
            return "Extension: +" + days + " days";
        }
        return "Due in " + days + " days";
    }
}
