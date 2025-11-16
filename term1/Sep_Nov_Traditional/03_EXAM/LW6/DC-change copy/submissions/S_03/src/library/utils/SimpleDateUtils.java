package library.utils;

public class SimpleDateUtils
{
    static String calculateDueDate(int daysFromNow)
    {
        return "Due in" + daysFromNow + " days";
    }

    static String calculateDueDate(String startDate)
    {
        return "Due on [" + startDate + "] plus default days";
    }

    static String calculateDueDate(int days, boolean isExtension)
    {
        if(isExtension)
        {
            return "Due in " + days + " days";
        }
        else
        {
            return "Not due in " + days + "days";

        }
    }
}
