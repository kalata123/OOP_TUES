package library.util;

public class SimpleDateUtils {
    private SimpleDateUtils() {
    }

    public static String calculateDueDate(int daysFromNow) {
        return "Due in " + daysFromNow + " days";
    }

    public static String calculateDueDate(String startDate) {
        return "Due on " + startDate + " plus " + LibrarySettings.DEFAULT_LOAN_DAYS + " days";
    }

    public static String calculateDueDate(int days, boolean isExtension) {
        if (isExtension) {
            return "Due in " + days + " days (Extension)";
        } else {
            return "Due in " + days + " days";
        }
    }
}