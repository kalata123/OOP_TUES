# Access & Non-access Modifiers

## 1. Theory

### Definition
**Modifiers** in Java are keywords that **_change_** the meaning or behavior of classes, methods, and variables.  
They define `access level`, `visibility`, `inheritance rules`, and sometimes `behavioral properties` of members.  
Modifiers are divided into **access** and **non-access** types.

---

### Categories

#### 1. Access Modifiers
Control **visibility** between classes and packages.

| Modifier | Applies To | Visibility |
|-----------|-------------|-------------|
| `public` | class, method, variable | Visible everywhere |
| `protected` | method, variable, constructor | Visible within the same package and subclasses |
| (no modifier) *default* | class, method, variable | Visible only within the same package |
| `private` | method, variable, constructor | Visible only within the same class |

#### 2. Non-Access Modifiers
Control **behavior, inheritance, and memory** features.

| Modifier | Applies To | Meaning |
|-----------|-------------|----------|
| `static` | variable, method, nested class | Belongs to the class, not to instances |
| `final` | variable, method, class | Prevents modification, overriding, or inheritance |
| `abstract` | class, method | Defines a contract to be implemented by subclasses |
| `synchronized` | method, block | Allows one thread at a time (thread safety) |
| `volatile` | variable | Ensures the variable is read directly from memory |
| `transient` | variable | Excluded from serialization |

---

### Why Modifiers Matter
1. **Encapsulation** – Control how data and methods are accessed.
2. **Safety** – Prevent unintended use or modification.
3. **Code Clarity** – Define the intent of a class or method clearly.
4. **Inheritance Control** – Specify which members can or cannot be overridden.

---

## 4. Classroom Exercises

### Exercise 1: Access Modifiers in Practice
```java
class Car {
    public String model = "BMW";
    private int speed = 120;
    protected int year = 2020;
    int weight = 1500; // default

    public void printInfo() {
        System.out.println("Model: " + model);
        System.out.println("Speed: " + speed);
        System.out.println("Year: " + year);
        System.out.println("Weight: " + weight);
    }
}

class Modifiers {
    static void main(String[] args) {
        Car c = new Car();
        System.out.println(c.model);
        // System.out.println(c.speed); // error: speed has private access in Car
        System.out.println(c.year);
        System.out.println(c.weight);
    }
}
```

### Exercise 2: `final` and `static`

```java
public class Constants {
    public static final double PI = 3.14159;

    public static double circleArea(double radius) {
        return PI * radius * radius;
    }

    public static void main(String[] args) {
        System.out.println(Constants.circleArea(3));
        // Constants.PI = 3.14; // error: cannot assign a value to final variable PI
    }
}

```
### Exercise 3: `abstract` and `final` Classes

```java
abstract class Animal {
    abstract void sound();
    public void breathe() {
        System.out.println("Breathing...");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Woof!");
    }
}

public class Modifiers {
    static void main(String[] args) {
        Animal a = new Dog();
        a.sound();
        a.breathe();
    }
}
```
Explain that:
- `abstract` method has no body.
- `abstract` class cannot be instantiated.
- Concrete subclass must implement abstract methods.

---

# 5. Common Mistakes

| Mistake | Example | Explanation |
|----------|----------|-------------|
| Declaring both `abstract` and `final` together | `public abstract final class A {}` | Impossible – `abstract` requires subclassing, `final` forbids it |
| Overusing `public` | Exposes implementation unnecessarily | Breaks encapsulation |
| Forgetting `static` for utility methods | Makes them callable only via object | Wastes memory |
| Modifying `final` variables | Causes compilation error | Values cannot change |
| Using `private` constructors without purpose | Blocks instantiation | Used only in singleton patterns |

---

# 6. Homework Tasks

### Task 1: Access Levels Across Packages
Create:
- Package `school` with class `Teacher` (has public, private, protected, and default fields)
- Package `student` with class `TestTeacher` that tries to access them.  
  Observe which fields are accessible and explain why.

---

### Task 2: Static Utility
Write class `MathHelper`:
- Contains `static` methods: `square(int)`, `cube(int)`, `max(int, int)`
- Add a static field `count` tracking how many times a method was used.  
  Test all in `main`.

---

### Task 3: Abstract Shapes
- Define abstract class `Shape` with abstract `area()` and `perimeter()`.
- Implement `Circle` and `Rectangle`.
- Demonstrate polymorphism: override method area for `Circle` and `Rectangle`



---

# 7. Summary

| Category | Modifier | Purpose | Example |
|-----------|-----------|----------|----------|
| Access | `public` | Visible everywhere | `public class A {}` |
| Access | `private` | Visible only inside class | `private int x;` |
| Access | `protected` | Visible in package + subclasses | `protected void run()` |
| Access | *default* | Visible only in package | `void show()` |
| Non-access | `static` | Shared across instances | `static int counter;` |
| Non-access | `final` | Cannot change or override | `final double PI` |
| Non-access | `abstract` | Must be implemented | `abstract void draw()` |
| Non-access | `synchronized` | Thread-safe method | `synchronized void run()` |
| Non-access | `volatile` | Prevents caching | `volatile int flag;` |
| Non-access | `transient` | Skips serialization | `transient String temp;` |


