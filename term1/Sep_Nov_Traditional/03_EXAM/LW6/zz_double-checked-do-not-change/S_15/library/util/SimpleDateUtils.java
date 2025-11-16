package library.util;

public class SimpleDateUtils {
    private SimpleDateUtils() {
        throw new AssertionError("SimpleDateUtils cannot be instantiated.");
    }

    public String calculateDueDate(int daysFromNow){
        return "Due in " + daysFromNow + " days";
    }

    public String calculateDueDate(String startDate){

        return "Due on " + startDate + " plus " + LibrarySettings.DEFAULT_LOAN_DAYS + " days";
    }

    public String calculateDueDate(int days, boolean isExtension){
        if(isExtension){
            return "Extended. Due in " + days + " days";
        }else{
            return "Due on " + days + " days";
        }
    }
}
