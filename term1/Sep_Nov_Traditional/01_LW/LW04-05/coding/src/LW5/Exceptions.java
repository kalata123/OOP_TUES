package LW5;

// Example 1 — Unchecked Exception (Runtime)
//public class Exceptions {
//    public static void main(String[] args) {
//        int[] numbers = {1, 2, 3};
//        IO.println("Hello World!");
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
//            System.out.println("File not found or cannot be opened. Error: " + e.getMessage() );
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
//        } catch (ArithmeticException e) {
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
//            int x = 5 / 5; // ArithmeticException
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
//        IO.println("Hello World!");
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
//class InvalidScoreException extends RuntimeException {
//    public InvalidScoreException(String message) {
//        super(message);
//    }
//}
//
//public class Exceptions {
//    static void validateScore(int score) { // InvalidScoreException is a RuntimeException (unchecked) and therefore it is not necessary to "throws"
//        if (score < 0 || score > 100) {
//            throw new InvalidScoreException("Invalid score: must be between 0 and 100.");
//        }
//        System.out.println("Valid score entered: " + score);
//    }
//
//    public static void main(String[] args) {
//        validateScore(150);
//        try {
//            validateScore(150);
//        } catch (RuntimeException e) {
//            System.out.println(e.getMessage());
//        }
//         // throws unchecked exception (no try-catch needed)
//        IO.println("Test out "); // does not print
//    }
//}

//Task 1: File Reading Exception (Checked)
//Write a program that tries to open a file named "students.txt" using FileReader.
//
//        Declare a method readFile() that throws IOException.
//        In main(), call this method inside a try-catch block.
//        Print a clear message if the file cannot be found.

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Exceptions {
    public static void readFile() throws IOException {
        System.out.println("Hello, World");
            FileReader fileReader = new FileReader("students.txt");
            if(fileReader.ready())
                throw new IOException("Task 1 - Error! Cannot open students.txt");
            fileReader.close();
    }
    public static void main(String[] args) {
        try {
            readFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
//            System.out.println(e);
        }
    }
}



//Task 2: Multiple Exceptions Handling
//Write a program that asks the user for two integers and divides them.
//
//Handle both ArithmeticException (divide by zero) and InputMismatchException separately.
//Print a different message for each case.
//Always print "End of calculation." using a finally block.



//public class Exceptions {
//    static int divide(int num1, int num2) {
////        if(num2 == 0) {
////            throw new ArithmeticException("Division by 0 is not allowed");
////        }
//
//        return num1/num2;
//    }
//
//    public static void main(String[] args) {
//        System.out.println("Enter two numbers to divide: ");
//        Scanner input = new Scanner(System.in);
//
//        int num1, num2;
//
//        try {
//            num1 = input.nextInt();
//            num2 = input.nextInt();
//            System.out.println("Result: " + divide(num1, num2));
//        } catch (InputMismatchException e) {
//            System.out.println("Invalid input");
//        } catch (ArithmeticException e) {
//            System.out.println(e.getMessage());
//            System.out.println("Invalid input");
//        } finally {
//            System.out.println("End of calculation");
//        }
//    }
//}




//Task 3: Custom Exception (Checked)
//Create a class InvalidAgeException that extends Exception.
//
//In a method register(int age), throw this exception if age < 18.
//Declare the method with throws InvalidAgeException.
//        In main(), handle it with try-catch and print "Too young to register!".




//class InvalidAgeException extends Exception {
//    public InvalidAgeException(String message){
//        super(message);
//    }
//}
//
//public class Exceptions{
//    static void register(int age) throws InvalidAgeException{
//        if(age < 18){
//            throw new InvalidAgeException("Cannot register if under 18");
//        }
//
//        System.out.println("Registration completed");
//    }
//
//    public static void main(String[] args){
//        try{
//            register(15);
//        }
//        catch(InvalidAgeException e){
//            System.out.println(e);
//        }
//    }
//}
