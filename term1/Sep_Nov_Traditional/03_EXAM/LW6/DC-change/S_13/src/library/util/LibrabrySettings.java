package library.util;
import java.util.*;

public class LibrabrySettings {
    private LibrabrySettings(){}
    public static final int MAX_BORROW_LIMIT = 5;
    public static final int FINE_PER_DAY = 1;
    public static final int DEFAULT_LOAN_DAYS = 14;
    public static String generateItemId(String prefix)
    {
        return String.format("[PREFIX] - [%d]", Integer.parseInt(prefix));
    }

}


/*
B1) LibrarySettings (library.util.LibrarySettings)
Central place for system constants:

Prevent instantiation (private constructor)
Constants:
MAX_BORROW_LIMIT = 5
FINE_PER_DAY = 1
DEFAULT_LOAN_DAYS = 14
Static method: String generateItemId(String prefix) returns format: [PREFIX]-[random number]. You can use int randomNum = (int)(Math.random() * 101); to get a random number.

 */