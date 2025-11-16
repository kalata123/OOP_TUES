package library.util;

public class SimpleDateUtils {

    private SimpleDateUtils() {
    }
    
    public static String calculateDueDate(int daysFromNow) {
        return "Due in " + daysFromNow + " days";
    }
    public static String calculateDueDate(String startDate){
        return "Due on " + startDate +" plus default days";
    }
    public static String calculateDueDate(int days, boolean isExtension){
        return "Due on " + (isExtension ? " plus " + days + " days" : "");
    }
}
