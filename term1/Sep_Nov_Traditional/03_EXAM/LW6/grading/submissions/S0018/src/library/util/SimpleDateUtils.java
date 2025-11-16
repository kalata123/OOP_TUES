package library.util;

import library.util.LibrarySettings;

public class SimpleDateUtils {
    public static String dueDate(int daysFromNow) {
        return "Due in "  + daysFromNow + " days";
    }

    public static String dueDate(String startDate) {
        return "Due on " + startDate + " plus" + LibrarySettings.DEFAULT_LOAN_DAYS + " days";
    }

    public static String dueDate(int days, boolean isExtension) {
        if(isExtension) {
            return "Due in " + days + " days, because of extension";
        } else  {
            return dueDate(days);
        }
    }
}
