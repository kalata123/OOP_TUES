# In-Class Revision Task (≈120 minutes)

## Goal
Build a small console project that demonstrates the following Java topics:
- Packages
- Access & non-access modifiers
- Encapsulation
- Constructor/method overloading
- Abstraction
- Errors and exceptions (checked, unchecked, try/catch/finally, throws, custom)

Deliver **one runnable `Main` class** that showcases all these elements.

---

## Project Skeleton (10 min)

### Folder Structure

```
src/
    school/
        core/ → Person.java
        people/ → Student.java, Teacher.java
        grading/ → GradeBook.java, InvalidScoreException.java
        util/ → IdGenerator.java, Config.java
    Main.java
```


### Rules
- Each `.java` file starts with a correct `package` declaration.
- Only classes used outside their package are **public**.
- Helper classes should be **package-private**.

---

## Part A — Abstraction + Encapsulation (25 min)

### A1) `Person` (abstract) — in `school.core`
- **Abstract class** `Person`.
- **Private fields:**
    - `String name`
    - `int age`
- **Constructor overloading:**
    - `Person(String name)` → default `age = 0`
    - `Person(String name, int age)`
- **Getters:** `getName()`, `getAge()`
- **Setter:** `setAge(int age)` → if `age < 0`, set to `0`
- **Abstract method:** `String role()` (implemented in subclasses)
- **Concrete method:**  
  `String idCard()` returning `"NAME (ROLE), age AGE"`

---

### A2) `Student` and `Teacher` — in `school.people`

#### `Student`
- Extends `Person`.
- Adds **private field** `int[] scores`.
- Constructor overloading aligned to `super`.
- Implements `role()` → returns `"Student"`.

#### `Teacher`
- Extends `Person`.
- Adds **private field** `String subject` (default `"General"`).
- **Constructors:**
    - `(String name)`
    - `(String name, int age, String subject)`
- Implements `role()` → returns `"Teacher"`.

All fields remain **private**, with **getters/setters only when needed**.

## Part B — Modifiers (20 min)

### B1) `IdGenerator` — in `school.util`
Demonstrates use of **static**, **final**, and **package-private** access.

**Fields:**
- `private static int counter = 1000;`
- `public static final String PREFIX = "SC";`

**Method:**
- `public static String nextId()` → returns `"SC" + counter++`.

---

### B2) `Config` — in `school.util`
- Declared as `public final class Config`.
- `public static final int MAX_SCORE = 100;`
- Private constructor to prevent instantiation.

---

### B3) Protected Usage
- Add `protected void validateNonEmpty(String s, String fieldName)` in `Person`.
- Use it in the `Teacher` constructor to validate the `subject` field.

---

## Part C — Overloading (15 min)

### C1) `GradeBook` — in `school.grading`
Declared as `public final class GradeBook`.  
Handles score registration and statistics.

**Overloaded methods:**
- `public void addScore(Student s, int score)`
- `public void addScore(Student s, int... scores)` (varargs)

**Validation:**  
Throw `InvalidScoreException` if any score `< 0` or `> Config.MAX_SCORE`.

**Static utility methods:**
- `public static double average(Student s)`
- `public static int max(Student s)`

---

## Part D — Exceptions (25 min)

### D1) Custom Exception
Create `InvalidScoreException` extending `RuntimeException`.  
**Message:** `"Score must be between 0 and " + Config.MAX_SCORE.`

---

### D2) Checked Exception with `throws`
Add in `GradeBook`:
- `public static double safeDivide(int a, int b) throws java.io.IOException`  
  If `b == 0`, throw `new IOException("Division by zero in safeDivide")`.  
  Else, return `a / (double) b`.

---

### D3) Try/Catch/Finally in `Main`
- Read two integers using `Scanner`.
- Wrap the division in a `try/catch/finally` structure:
    - `try` calls `safeDivide(a, b)`
    - `catch` handles `IOException` and `Exception` separately
    - `finally` prints `"End of division attempt"`

---

### D4) Multiple Catch Ordering
Trigger a deliberate error (e.g., `ArrayIndexOutOfBoundsException` or `NullPointerException`).  
Catch the **specific exception first**, then the **general Exception**.

---

## Part E — Errors (10 min)

Show examples of:

- **Compilation Error:** comment a line with a missing semicolon.
- **Runtime Error:** perform division by zero, then comment it out after testing.
- **Logic Error:** compute an incorrect average once, correct it, and keep both versions commented with an explanation.

---

## Part F — Main Class Demonstration (15 min)

Demonstrate all required features:

### Constructor Overloading
Create objects:

```java
Student s1 = new Student("Eva");
Student s2 = new Student("Ivan", 17);
Teacher t1 = new Teacher("Nina");
Teacher t2 = new Teacher("Miro", 34, "OOP");
```

### Abstract Method & ID Card
Print ID cards of all objects.

### Static and Final
Display generated IDs and `Config.MAX_SCORE`.

### Encapsulation
Attempt to modify private fields directly (commented to show protection).  
Use setter to update age correctly.

### Overloading and Exception Handling
Add multiple scores using both overloaded methods.  
Attempt to add invalid scores to trigger `InvalidScoreException`.  
Display the computed average and max.

### Checked Exception
Prompt user for two numbers.  
Call `safeDivide()` and handle exceptions with try/catch/finally.

### Multiple Catch
Force and handle `ArrayIndexOutOfBoundsException`.

### Protected Helper
Demonstrate that `Teacher` constructor uses `validateNonEmpty()` internally.
