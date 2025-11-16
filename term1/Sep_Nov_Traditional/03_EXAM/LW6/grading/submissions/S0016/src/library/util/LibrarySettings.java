package library.util;

public class LibrarySettings {
  	final public int MAX_BORROW_LIMIT;
  	final public int FINE_PER_DAY;
  	final public int DEFAULT_LOAN_DAYS;
  

 	LibrarySettings(){
    	MAX_BORROW_LIMIT = 5;
    	FINE_PER_DAY = 1;
    	DEFAULT_LOAN_DAYS = 14;
 	}


  	public static String generateItemId(String prefix) {
  		int randomNum = (int)(Math.random() * 101); 
		return prefix + randomNum;
	}
}
