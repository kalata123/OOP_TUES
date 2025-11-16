package library.util;

public class SimpleDataUtils {
    private SimpleDataUtils() {}
    static String calculateDueDate(int daysFromNow)
    {
        return String.format("Due in %d days", daysFromNow);
    }
    static String calculateDueDate(String startDate)
    {
        return String.format("Due on [%s]", startDate);
    }
    static String calculateDueDate(int days, boolean isExtension)
    {
       if(isExtension)
       {
           return String.format("There is an extension in %d days", days);
       }
       return String.format("End of the message");
    }
}
