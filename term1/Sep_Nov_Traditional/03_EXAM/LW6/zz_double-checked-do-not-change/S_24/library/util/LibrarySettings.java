package library.util;
import java.util.Random;

public class LibrarySettings {
   final int MAX_BORROW_LIMIT = 5;
   final int FINE_PER_DAY = 1;
   final int DEFAULT_LOAN_DAYS = 14;

   private LibrarySettings(){}

    static String generateItemId(String prefix){
       return prefix +  "-" + new Random().nextInt(10000);
    }
}
