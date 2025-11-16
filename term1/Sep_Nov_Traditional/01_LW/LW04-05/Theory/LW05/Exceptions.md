# **Java Topic: Exceptions**

---

## 1. What Are Exceptions?
Exceptions are **unexpected events** that happen while a program is running and **disrupt the normal flow** of execution.  
Unlike errors, exceptions can be **handled** — allowing the program to recover and continue instead of crashing.

---

## 2. Main Types of Exceptions

| Type                        | When It Happens                 | Example                           |
|-----------------------------|---------------------------------|-----------------------------------|
| **Unchecked (Runtime)**     | While running (not checked)     | Divide by zero, invalid index     |
| **Checked (Compile-time)**  | Verified by compiler before run | File not found, input/output fail |
| **Custom / User-defined**   | Created by the programmer       | Invalid password, bad input       |

---

## 3. Short Description

### **Unchecked (Runtime) Exceptions**
Happen during program execution, usually due to logic or invalid input.  
These are **not checked by the compiler**.  
Examples: `ArithmeticException`, `NullPointerException`, `ArrayIndexOutOfBoundsException`.

---

### **Checked Exceptions**
The compiler **forces you to handle** them because they come from external systems (files, devices, network).  
Examples: `IOException`, `FileNotFoundException`.  
If not handled, the code will **not compile**.

---

### **Custom Exceptions**
Created by programmers to represent specific problems in their own logic.  
For example, throwing an exception if a password is too short or an age is invalid.

Custom exceptions can be **checked or unchecked**, depending on which class they extend:
- Extend **`Exception`** → becomes a **checked** exception (must be handled).
- Extend **`RuntimeException`** → becomes an **unchecked** exception (compiler does not enforce handling).

---

## 4. Common Handling Techniques

| Technique              | Purpose                                  | Example Keyword(s)            |
|-------------------------|------------------------------------------|--------------------------------|
| **try-catch**           | Catch and handle runtime problems        | `try { } catch(Exception e) {}` |
| **finally**             | Always run cleanup code (e.g., closing)  | `finally { ... }`              |
| **throw / throws**      | Create or declare custom exceptions      | `throw new Exception()`        |
| **try-with-resources**  | Auto-close streams and files             | `try(FileReader f = …) {}`     |

---

### Examples:

```java
// Example 1 — Unchecked Exception (Runtime)
public class Example1 {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3};
        System.out.println(numbers[5]); // ArrayIndexOutOfBoundsException (unchecked)
    }
}

// Example 2 — Checked Exception (File Handling)
import java.io.FileReader;
import java.io.IOException;

public class Example2 {
    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader("nonexistent.txt"); // Checked IOException
            reader.close();
        } catch (IOException e) {
            System.out.println("File not found or cannot be opened.");
        }
    }
}

// Example 3 — Basic try-catch
public class Example3 {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // Unchecked ArithmeticException
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero!");
        }
        System.out.println("Program continues...");
    }
}

// Example 4 — Multiple catch Blocks
public class Example4 {
    public static void main(String[] args) {
        try {
            int[] nums = {1, 2, 3};
            System.out.println(nums[4]); // ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array index error!");
        } catch (Exception e) {
            System.out.println("General error!");
        }
    }
}

// Example 5 — Finally Block
public class Example5 {
    public static void main(String[] args) {
        try {
            int x = 5 / 0; // ArithmeticException
        } catch (ArithmeticException e) {
            System.out.println("Arithmetic problem!");
        } finally {
            System.out.println("Execution complete (finally always runs).");
        }
    }
}

// Example 6 — Try with Resources
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Example6 {
    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            System.out.println(reader.readLine());
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
    }
}

// Example 7 — Custom Checked Exception
class InvalidAgeException extends Exception {
    public InvalidAgeException(String message) {
        super(message);
    }
}

public class Example7 {
    static void register(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Too young to register!");
        }
        System.out.println("Registration successful.");
    }

    public static void main(String[] args) {
        try {
            register(15); // triggers checked custom exception
        } catch (InvalidAgeException e) {
            System.out.println(e.getMessage());
        }
    }
}

// Example 8 — Custom Unchecked Exception
class InvalidScoreException extends RuntimeException {
    public InvalidScoreException(String message) {
        super(message);
    }
}

public class Example8 {
    static void validateScore(int score) {
        if (score < 0 || score > 100) {
            throw new InvalidScoreException("Invalid score: must be between 0 and 100.");
        }
        System.out.println("Valid score entered: " + score);
    }

    public static void main(String[] args) {
        validateScore(150); // throws unchecked exception (no try-catch needed)
    }
}


```

## 5. Common Mistakes

| Mistake                      | Example                                     | What Happens                          |
|------------------------------|---------------------------------------------|---------------------------------------|
| Catching wrong exception     | Catching `IOException` for division by zero | Doesn’t handle the real problem       |
| Catching `Exception` too early | `catch(Exception e)` before specific ones   | Blocks more specific handlers         |
| Empty catch block            | `catch(Exception e){}`                      | Hides problem, hard to debug          |
| Forgetting finally/cleanup   | Not closing file or scanner                 | Memory or resource leaks              |
| Ignoring compiler warnings   | Not handling a checked exception            | Compilation fails                     |

---

## 6. Summary

| Type             | Description                                           | Example                       | How to Handle                          |
|------------------|-------------------------------------------------------|--------------------------------|----------------------------------------|
| **Unchecked**    | Happens during program run (logic issues)             | Divide by zero                 | Use `try-catch`                        |
| **Checked**      | External operation must be handled                    | File not found                 | Use `try-catch` or `throws`            |
| **Custom**       | Programmer-defined for specific conditions (can be checked or unchecked) | Invalid input | Extend `Exception` for checked, `RuntimeException` for unchecked |
| **finally**      | Code that always executes (cleanup)                   | Closing file, releasing memory | Add `finally {}` after catch           |
| **try-resources**| Auto-close file/stream even if error occurs           | Reading file safely            | Use `try(FileReader r = …) {}`         |

**_notes:_**  
Unchecked exceptions break your logic.  
Checked exceptions force you to handle real-world problems.  
Custom exceptions follow whichever class you extend.  
Handled exceptions keep your program alive.

### **In-Class Tasks**

---

#### **Task 1: File Reading Exception (Checked)**
Write a program that tries to open a file named `"students.txt"` using `FileReader`.
- Declare a method `readFile()` that throws `IOException`.
- In `main()`, call this method inside a `try-catch` block.
- Print a clear message if the file cannot be found.

---

#### **Task 2: Multiple Exceptions Handling**
Write a program that asks the user for two integers and divides them.
- Handle both `ArithmeticException` (divide by zero) and `InputMismatchException` separately.
- Print a different message for each case.
- Always print `"End of calculation."` using a `finally` block.

---

#### **Task 3: Custom Exception (Checked)**
Create a class `InvalidAgeException` that extends `Exception`.
- In a method `register(int age)`, throw this exception if `age < 18`.
- Declare the method with `throws InvalidAgeException`.
- In `main()`, handle it with `try-catch` and print `"Too young to register!"`.

---

### **Homework Tasks**

---

#### **Task 1: Custom Exception (Unchecked)**
Create a class `InvalidScoreException` that extends `RuntimeException`.
- In a method `checkScore(int score)`, throw the exception if `score < 0` or `score > 100`.
- Call this method in `main()` without `try-catch`.
- Then repeat using `try-catch` to print a friendly message.

---

#### **Task 2: Propagating Exceptions**
Write three methods:  
`openFile()`, `processFile()`, and `main()`.
- `openFile()` throws `IOException`.
- `processFile()` calls `openFile()` and redeclares `throws IOException`.
- `main()` handles the exception with `try-catch`.  
  Print which method handles it in the output.

---

#### **Task 3: Simple Exception Practice**
Write a program that asks the user to enter an integer.
- Handle the case when the user enters a non-integer value using `InputMismatchException`.
- If the input is negative, throw a custom `NegativeNumberException` that extends `RuntimeException`.
- Catch and print a message like `"Please enter a positive number."`