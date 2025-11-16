package library.util;

public class SimpleDateUtils {



	public static String calculateDueDate(int daysFromNow) 
	{ return "Due in " + daysFromNow + " days"; } 
    
	public static String calculateDueDate(String startDate)
	{ return "Due on " + startDate + " plus default days"; }  
    
	//TODO 
	public static String calculateDueDate(int days, boolean isExtension)
	{ return null; }  // different message if it's an extension
}