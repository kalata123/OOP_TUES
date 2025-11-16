package library.util;

public final class SimpleDateUtils
{
    public static String calculateDueDate(int daysFromNow)
    {
        return "Duo in " + daysFromNow + " days";
    }
    public static String calculateDueDate(String startDate)
    {
        return "Due on " + (LibrarySettings.DEFAULT_LOAN_DAYS) + " days";
    }

}
