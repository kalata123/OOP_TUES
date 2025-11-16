package library.util;

public final class SimpleDateUtils {
    private SimpleDateUtils() {}

    public static String calculateDueDate(int daysFromNow) {
        return "Due in " + daysFromNow + " days";
    }

    public static String
