# Overloading
## 1. Theory

### Definition
**Overloading** in Java means defining **multiple methods or constructors with the same name**, but with **different parameter lists** (different number or types of parameters).  
The compiler determines which version to call **at compile time** based on the arguments provided — this is **compile-time polymorphism**.

### Why It Exists
1. **Readability** – The same logical operation can be applied to different data types or contexts without creating new names.
2. **Convenience** – Allows flexible object creation (e.g., constructors with optional parameters).
3. **Extensibility** – Makes APIs easier to evolve without breaking older code.

### Syntax Rules
- Methods must differ **by parameter number or type**, not only by return type.
- Access modifiers and return types may vary, but do **not** define a new overload alone.
- Constructors can also be overloaded the same way as methods.

---

## 2. Syntax Examples

### Method Overloading

```java
public class Calculator {
    // Add two integers
    public int add(int a, int b) {
        return a + b;
    }

    // Add three integers
    public int add(int a, int b, int c) {
        return a + b + c;
    }

    // Add two doubles
    public double add(double a, double b) {
        return a + b;
    }

    static void main() {
        Calculator calc = new Calculator();
        System.out.println(calc.add(2, 3));         // uses first
        System.out.println(calc.add(1, 2, 3));      // uses second
        System.out.println(calc.add(2.5, 4.5));     // uses third
    }
}
```

### Constructor Overloading

```java
public class LW4.Rectangle {
    int width;
    int height;

    // Constructor 1
    public LW4.Rectangle() {
        width = 1;
        height = 1;
    }

    // Constructor 2
    public LW4.Rectangle(int w, int h) {
        width = w;
        height = h;
    }

    // Constructor 3
    public LW4.Rectangle(int side) {
        width = side;
        height = side;
    }

    public int area() {
        return width * height;
    }

    static void main() {
        LW4.Rectangle a = new LW4.Rectangle();
        LW4.Rectangle b = new LW4.Rectangle(5, 3);
        LW4.Rectangle c = new LW4.Rectangle(4);

        System.out.println(a.area()); // 1
        System.out.println(b.area()); // 15
        System.out.println(c.area()); // 16
    }
}
```

## 3. Explanation Through Scenarios

### Why overloading

- Without overloading
```java
calc.addInt(1, 2);
calc.addDouble(2.3, 4.1);
calc.addThreeInts(1, 2, 3);

-> Inconsistent naming
```

- With overloading
```java
calc.add(1, 2);
calc.add(2.3, 4.1);
calc.add(1, 2, 3);

-> Consistent naming, clear logic
```

### Constructor Example in Real Classes

- The ```String``` class in Java has many overloaded constructors

```java
String s1 = new String();                // empty string
String s2 = new String("hello");         // from literal
String s3 = new String(new char[]{'h','i'}); // from char array
```

## 4. Classroom Exercises

### Exercise 1: Overloaded Methods (5–10 minutes)
Create a class `LW4.Printer` with several versions of `print`:
- `print(String message)`
- `print(int number)`
- `print(double number)`
- `print(String message, int times)` → prints message multiple times

Run and observe which method is called for each argument.

---

### Exercise 2: Constructor Overloading (10–15 minutes)
Create a class `Person` with overloaded constructors:
- `Person()` → name = "Unknown", age = 0
- `Person(String name)`
- `Person(String name, int age)`  
  Add a method `introduce()` that prints `"Hi, I am <name> and I am <age> years old."`

---

### Exercise 3 (Challenge)
Write a class `Box` with overloaded constructors that:
- Accept no arguments (default 1x1x1)
- Accept one argument (cube)
- Accept three arguments (custom dimensions)  
  Add a `volume()` method and print all three results.

---

## 5. Common Mistakes

| Mistake | Example | Explanation |
|----------|----------|-------------|
| Overloading only by return type | `int sum(int a, int b)` vs `double sum(int a, int b)` | Illegal – ambiguous for compiler |
| Using same parameter count and type order | `sum(int, double)` vs `sum(double, int)` | Works only if argument order differs |
| Forgetting to `this()` chain constructors | Leads to repeated code – use `this(...)` to reuse other constructors |

---

## 6. Homework Tasks

### Task 1: Overloaded Area Calculator
Create class `Area` with overloaded methods:
- `double area(int side)` → square
- `double area(int width, int height)` → rectangle
- `double area(double radius)` → circle

Test all in `main`.

---

### Task 2: Constructor Overloading – Student Profiles
Create class `Student`:
- Attributes: `name`, `grade`, `school`
- Constructors:
    1. Default → “Unknown”, 0, “Unassigned”
    2. Name and grade only
    3. All three parameters  
       Add `printInfo()` to show all data.

---

### Task 3 (Optional for advanced)
Implement class `MathUtils` with several overloaded `max()` methods:
- `max(int, int)`
- `max(double, double)`
- `max(int, int, int)`

---

## 7. Summary

| Concept | Meaning | Key Point |
|----------|----------|-----------|
| Method Overloading | Same method name, different parameters | Decided at compile-time |
| Constructor Overloading | Multiple constructors in one class | Flexible object creation |
| Advantage | Improves readability, usability, maintainability | |




