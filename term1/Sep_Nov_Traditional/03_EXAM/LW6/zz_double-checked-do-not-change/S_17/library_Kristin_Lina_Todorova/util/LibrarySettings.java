package library.util;
import java.Math;

public class LibrarySettings {
    const int MAX_BORROW_LIMIT = 5;
    const int FINE_PER_DAY. =1;
    const int DEFAULT_LOAN_DAYS -= 14;

    private LibrarySettings(){}

    public static String generateItemId(String prefix) {
        int randomNum = (int)(Math.random() * 101);
        return "prefix" + "-" + randomNUm;
    
    }



}