package library.utils;

public class SimpleDateUtils {
    private SimpleDateUtils() {}
    static String calculateDueDate(int daysFromNow)
    {
        return "Due in " + daysFromNow + " days";  
    }
    static String calculateDueDate(String startDate)
    {
        return "Due on " + startDate + " plus " + LibrarySettings.DEFAULT_LOAN_DAYS + " days";
    }
    static String calculateDueDate(int days, boolean isExtension)
    {
        if(isExtension)
        {
            return "Extended by " + days + " days";
        }
        else
        {
            return "Due in " + days + " days";
        }
    }
}