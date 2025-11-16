package library.util;

public class SimpleDateUtils {
    private SimpleDateUtils() {}

    public static int calculateDueDate(int daysFromNow) {
        return System.out.println("Due in " + daysFromNow + " days");
    }

    public static calculateDueDate(Sting startDate) {
        return System.out.println("Due on " + startDate + "plus default days");
    }

    public static calculateDueDate(int days, boolean isExtension) {
        if(isExtension) {
            return System.out.println("Extended due date by " + days + " days");
        } else {
            return System.out.println("Due in " + days + " days");
        }
    }
}
