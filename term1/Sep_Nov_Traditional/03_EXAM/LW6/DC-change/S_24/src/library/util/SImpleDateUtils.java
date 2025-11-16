package library.util;
import library.util.LibrarySettings;


import library.util.LibrarySettings.DEFAULT_LOAN_DAYS;

public final class SimpleDateUtils
{

    private SimpleDateUtils(){}

    public String calculateDueDate(int daysFromNow)
    {
        return "Due in" + daysFromNow + "days";
    }

    public String calculateDueDate(String startDate)
    {
        return "Due in" + startDate + DEFAULT_LOAN_DAYS +"days";
    }

    public String calculateDueDate(int days, boolean isExtension)
    {
        if(isExtension){
            return "Extended";
        }
        return "Not extended";
    }
}
