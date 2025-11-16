package library.util;

public class SimpleDateUtils {
    static String calculateDueDate(int daysFromNow){
        return "Due in" + daysFromNow + "days";
    }

    static String calculateDueDate(String startDate){
        return  "Due on" + startDate + "plus default days";
    }

    static String calculateDueDate(int days, boolean isExtension){
        if(isExtension){
            return  "Extended for" + days + "days";
        }else{
            return  "Not Extended!";
        }
    }

    private SimpleDateUtils(){}
}
