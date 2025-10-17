package school.util;


public class IdGenerator {
    private static int counter = 1000;
    public static final String PREFIX = "SC";

    public static String nextId(){
        return PREFIX + counter++;
    }
}
