# 1. Theoretical Base

### Definition
**Encapsulation** is the process of **bundling data (fields)** and **methods (behavior)** that operate on that data into a single unit — a **class** — and **restricting direct access** to some components.  
It protects the internal state of an object by **controlling how data is accessed and modified**.

### Core Principles
1. **Private fields** → Data cannot be accessed directly from outside the class.
2. **Public getters/setters** → Controlled access to data.
3. **Data hiding** → Internal representation is hidden; only an interface is exposed.

### Why Encapsulation Matters
- Prevents **unauthorized modification** of data.
- Allows **validation logic** in setters (e.g., disallow negative age).
- Makes the system **flexible** — internal structure can change without affecting external code.
- Encourages **modular**, **maintainable**, and **safe** design.
---





# 4. Classroom Exercises

### Exercise 1: Basic Encapsulation
```java
public class Person {
    private String name;
    private int age;

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    // Getter
    public int getAge() {
        return age;
    }

    // Setter with validation
    public void setAge(int age) {
        if (age >= 0) {
            this.age = age;
        } else {
            System.out.println("Age cannot be negative!");
        }
    }

    public static void main(String[] args) {
        Person p = new Person();
        p.setName("Maria");
        p.setAge(17);
        System.out.println(p.getName() + " is " + p.getAge() + " years old.");
        p.setAge(-5); // triggers validation
    }
}
```

Demonstrates:
- Private fields.
- Controlled access via getters/setters.
- Validation l ogic within a setter.

### Exercise 2: Data Hiding and Read-Only Fields
```java
public class Student {
private final int id; // cannot be changed once assigned
private String name;

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter only (read-only)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter (optional)
    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Student s = new Student(1, "Kaloyan");
        System.out.println("ID: " + s.getId() + ", Name: " + s.getName());
        s.setName("Petar");
        System.out.println("Updated name: " + s.getName());
        // s.id = 10; // error: cannot assign a value to final variable id
    }
}
```

Demonstrates how encapsulation supports immutability and read-only data

### Exercise 3: Internal Representation Hidden

```java
public class BankAccount {
    private double balance;

    public BankAccount(double startBalance) {
        if (startBalance >= 0)
            balance = startBalance;
        else
            balance = 0;
    }

    public void deposit(double amount) {
        if (amount > 0)
            balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance)
            balance -= amount;
        else
            System.out.println("Invalid withdrawal.");
    }

    public double getBalance() {
        return balance;
    }

    public static void main(String[] args) {
        BankAccount acc = new BankAccount(100);
        acc.deposit(50);
        acc.withdraw(30);
        System.out.println("Balance: " + acc.getBalance());
        // acc.balance = -1000; // not allowed
    }
}
```
Demonstrates encapsulation as data protection — the internal state (balance) cannot be modified directly.

# 5. Common Mistakes

| Mistake | Example | Explanation |
|----------|----------|-------------|
| Making fields `public` | `public int age;` | Breaks encapsulation — anyone can modify directly |
| Omitting validation in setters | `this.age = age;` | Allows invalid data |
| Returning references to internal mutable objects | `return list;` | External code can modify internal data |
| No getters/setters when needed | Cannot read or write safely | Hides data completely |
| Overusing getters/setters mechanically | Adding for every field unnecessarily | Violates intention of information hiding |

---

# 6. Homework Tasks

### Task 1: Car Class
Create class `Car` with:
- Private fields: `brand`, `speed`
- Getters and setters
    - `setSpeed()` only accepts values between 0 and 300
    - `getSpeed()` returns current speed  
      Create a main class to test the restrictions.

```java
public class Car {
    private String brand;
    private int speed;

    public Car(String brand, int speed) {
        this.brand = brand;
        setSpeed(speed);
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        if (speed >= 0 && speed <= 300)
            this.speed = speed;
        else
            System.out.println("Invalid speed value!");
    }

    public static void main(String[] args) {
        Car c = new Car("Audi", 120);
        System.out.println(c.getBrand() + " speed: " + c.getSpeed());
        c.setSpeed(350); // invalid
        c.setSpeed(200);
        System.out.println("Updated speed: " + c.getSpeed());
    }
}
```

### Task 2: Library Book

Create class `Book` with:
- Private fields: `title`, `author`, `available`
- Constructor initializing all fields
- Public methods: `borrowBook()`, `returnBook()`, `isAvailable()`
- Book can only be borrowed if available.  
  Demonstrate proper state control in `main`.

```java
public class Book {
    private String title;
    private String author;
    private boolean available;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }

    public boolean isAvailable() {
        return available;
    }

    public void borrowBook() {
        if (available) {
            available = false;
            System.out.println(title + " has been borrowed.");
        } else {
            System.out.println(title + " is already borrowed.");
        }
    }

    public void returnBook() {
        available = true;
        System.out.println(title + " has been returned.");
    }

    public static void main(String[] args) {
        Book b = new Book("Java Basics", "John Doe");
        b.borrowBook();
        b.borrowBook();
        b.returnBook();
        System.out.println("Available: " + b.isAvailable());
    }
}
```

### Task 3 (Advanced): Temperature Sensor

Create class TemperatureSensor:
- Private field: celsius
- Method setCelsius(double) with validation (between -50 and 100)
- Methods getCelsius(), getFahrenheit(), getKelvin()
- Demonstrate conversions and data protection.

```java
public class TemperatureSensor {
    private double celsius;

    public void setCelsius(double celsius) {
        if (celsius >= -50 && celsius <= 100)
            this.celsius = celsius;
        else
            System.out.println("Invalid temperature range!");
    }

    public double getCelsius() {
        return celsius;
    }

    public double getFahrenheit() {
        return celsius * 9 / 5 + 32;
    }

    public double getKelvin() {
        return celsius + 273.15;
    }

    public static void main(String[] args) {
        TemperatureSensor sensor = new TemperatureSensor();
        sensor.setCelsius(25);
        System.out.println("Celsius: " + sensor.getCelsius());
        System.out.println("Fahrenheit: " + sensor.getFahrenheit());
        System.out.println("Kelvin: " + sensor.getKelvin());
        sensor.setCelsius(150); // invalid
    }
}

```

# 7. Summary

| Concept           | Description                                | Example                            |
|-------------------|--------------------------------------------|------------------------------------|
| **Encapsulation** | Bundling data and methods in one class     | `private fields + getters/setters` |
| **Data Hiding**   | Preventing direct access to internal state | `private int balance`              |
| **Getter**        | Reads private field safely                 | `public int getAge()`              |
| **Setter**        | Controls and validates data change         | `public void setAge(int age)`      |
| **Immutability**  | Using `final` or omitting setters          | `private final int id`             |
| **Advantage**     | Protects state, improves maintainability   | Safer, cleaner design              |
