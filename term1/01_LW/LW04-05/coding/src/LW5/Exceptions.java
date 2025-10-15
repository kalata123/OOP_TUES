package LW5;

// Example 1 — Unchecked Exception (Runtime)
//public class Exceptions {
//    public static void main(String[] args) {
//        int[] numbers = {1, 2, 3};
//        System.out.println(numbers[5]); // ArrayIndexOutOfBoundsException (unchecked)
//    }
//}

// Example 2 — Checked Exception (File Handling)
//import java.io.FileReader;
//import java.io.IOException;
//
//public class Exceptions {
//    public static void main(String[] args) {
//        try {
//        //   Trying to open a file that does not exist
//            FileReader reader = new FileReader("nonexistent.txt"); // Checked IOException
//            reader.close();
//        } catch (IOException e) {
//            System.out.println("File not found or cannot be opened.");
//        }
//    }
//}

// Example 3 — Basic try-catch
//public class Exceptions {
//    public static void main(String[] args) {
//        try {
//            int result = 10 / 0; // Unchecked ArithmeticException
//        } catch (ArithmeticException e) {
//            System.out.println("Cannot divide by zero!");
//        }
//        System.out.println("Program continues...");
//    }
//}


// Example 4 — Multiple catch Blocks
//public class Exceptions {
//    public static void main(String[] args) {
//        try {
//            int[] nums = {1, 2, 3};
//            System.out.println(nums[4]); // ArrayIndexOutOfBoundsException
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Array index error!");
//        } catch (Exception e) {
//            System.out.println("General error!"); // Warning - investigate why?
//        }
//    }
//}


// Example 5 — Finally Block
//public class Exceptions {
//    public static void main(String[] args) {
//        try {
//            int x = 5 / 0; // ArithmeticException
//        } catch (ArithmeticException e) {
//            System.out.println("Arithmetic problem!");
//        } finally {
//            System.out.println("Execution complete (finally always runs).");
//        }
//    }
//}


// Example 6 — Try with Resources
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class Exceptions {
//    public static void main(String[] args) {
//        //    Try to read file within the try arguments
//        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
//            System.out.println(reader.readLine());
//        } catch (IOException e) {
//            System.out.println("Error reading file.");
//        }
//    }
//}


// Example 7 — Custom Checked Exception
//class InvalidAgeException extends Exception {
//    public InvalidAgeException(String message) {
//        super(message);
//    }
//}
//
//public class Exceptions {
//    static void register(int age) throws InvalidAgeException { // InvalidException is a checked exception extending Exception and therefore is needed to indicate "throws"
//        if (age < 18) {
//            throw new InvalidAgeException("Too young to register!");
//        }
//        System.out.println("Registration successful.");
//    }
//
//    public static void main(String[] args) {
//        try {
//            register(15); // triggers checked custom exception
//        } catch (InvalidAgeException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//}


// Example 8 — Custom Unchecked Exception
class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(String message) {
        super(message);
    }
}

public class Exceptions {
    static void validateScore(int score) { // InvalidScoreException is a RuntimeException (unchecked) and therefore it is not necessary to "throws"
        if (score < 0 || score > 100) {
            throw new InvalidScoreException("Invalid score: must be between 0 and 100.");
        }
        System.out.println("Valid score entered: " + score);
    }

    public static void main(String[] args) {
        validateScore(150); // throws unchecked exception (no try-catch needed)
        IO.println("TEst out "); // does not print
    }
}
