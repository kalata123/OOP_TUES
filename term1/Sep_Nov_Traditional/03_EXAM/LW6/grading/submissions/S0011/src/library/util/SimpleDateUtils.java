package library.util;

import static library.util.LibrarySettings.DEFAULT_LOAN_DAYS;

public class SimpleDateUtils {

    private SimpleDateUtils() {}

    public static String calculateDueDate(int daysFromNow) {
        if (daysFromNow < 0) {
            throw new IllegalArgumentException("daysFromNow cannot be negative");
        }
        return "Due in " + daysFromNow + " days";
    }

    public static String calculateDueDate(String startDate) {
        if(startDate == null || startDate.isEmpty()) {
            throw new IllegalArgumentException("startDate cannot be null or empty");
        }

        return "Due on " + startDate + " plus " + DEFAULT_LOAN_DAYS + " days";
    }

    public static String calculateDueDate(int days, boolean isExtension) {
        if (days < 0) {
            throw new IllegalArgumentException("days cannot be negative");
        }

        if(isExtension) {
            return "Your due date has been extended to " + days + " days";
        }
        else {
            return "You have no extensions and your due date remains the same";
        }
    }
}
