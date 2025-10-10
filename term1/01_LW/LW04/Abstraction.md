# 1. Theoretical Base

### Definition
abstraction in Java is the process of focusing on what an object does, rather than how it does it.  
It allows the programmer to define a structure or behavior that other classes must follow, without specifying the full implementation.  
abstraction simplifies complex systems by exposing only the essential operations and hiding the unnecessary internal logic.

In Java, abstraction is achieved mainly through **abstract classes** and **abstract methods**.

---

### Why abstraction Exists
1. **Simplifies complexity** — defines a clear idea of what each class must do.
2. **Improves clarity** — separates interface (what) from implementation (how).
3. **Supports maintainability** — internal logic can change without affecting other classes.
4. **Encourages reusability** — general concepts can be extended and specialized in many ways.

---

### Syntax Rules
An abstract class is declared with the keyword **`abstract`**.  
It can contain both **abstract methods** (no body) and **concrete methods** (with body).  
An abstract class **cannot** be instantiated directly.  
A subclass that extends it **must implement all abstract methods**, unless it is also declared abstract.  
Abstract methods are declared with the keyword **`abstract`**, have only a signature, and no body.

---


## Example 1: Abstract Class: Animal, Dog, Cat

```java
abstract class Animal {
    abstract void sound();

    public void breathe() {
        System.out.println("Breathing...");
    }
}

class Dog extends Animal {
    void sound() {
        System.out.println("Woof!");
    }
}

class Cat extends Animal {
    void sound() {
        System.out.println("Meow!");
    }
}

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.sound();
        dog.breathe();

        Cat cat = new Cat();
        cat.sound();
        cat.breathe();
    }
}

```

## Example 2: Abstract Class as a Contract: Vehicle, Car, Boat

```java
abstract class Vehicle {
    abstract void start();
    abstract void stop();
}

class Car extends Vehicle {
    void start() {
        System.out.println("Car starts with a key.");
    }

    void stop() {
        System.out.println("Car stops with brakes.");
    }
}

class Boat extends Vehicle {
    void start() {
        System.out.println("Boat starts with ignition switch.");
    }

    void stop() {
        System.out.println("Boat stops by reducing throttle.");
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle v1 = new Car();
        Vehicle v2 = new Boat();

        v1.start();
        v1.stop();
        v2.start();
        v2.stop();
    }
}

```

## Example 3: Interface Comparison: Flyable, Bird, Drone

```java
interface Flyable {
    void fly();
}

class Bird implements Flyable {
    public void fly() {
        System.out.println("Bird flaps wings to fly.");
    }
}

class Drone implements Flyable {
    public void fly() {
        System.out.println("Drone uses propellers to fly.");
    }
}

public class Main {
    public static void main(String[] args) {
        Flyable f1 = new Bird();
        Flyable f2 = new Drone();

        f1.fly();
        f2.fly();
    }
}

```

---

# abstraction vs Data Hiding

abstraction decides what actions are available to the outside world and hides the internal logic of how they work.  
Data hiding protects the data itself by restricting direct access through access modifiers such as private.

| Concept | Focus | Example | Result |
|----------|--------|----------|---------|
| abstraction | Hides the implementation logic | The method withdraw(amount) hides how balance is changed | Simplifies usage |
| Data Hiding | Protects the internal data | The field balance is private and accessible only through getters/setters | Prevents misuse |
| Connection | abstraction hides the process, data hiding hides the state | Both ensure the user cannot break internal logic | Clean, safe design |

abstraction is about **hiding the logic**; data hiding is about **hiding the data**.  
Encapsulation and data hiding protect how data is stored and modified.  
abstraction protects how the system behaves.

---

# 3. Explanation Through Scenarios

### Example 1: Real-life analogy
When driving a car, you interact with pedals, steering wheel, and gear lever.  
You do not need to know how the engine produces motion.  
The car’s design abstracts away the complexity, exposing only what you need to operate it.

### Example 2: Abstract class Vehicle
The Vehicle defines that every vehicle can start and stop, but does not define how.  
Car and Boat each have their own implementations of start and stop.  
This allows a program to treat all vehicles equally without needing to know which one it is using.

### Example 3: Interfaces as additional abstraction
Interfaces define shared behaviors that can cross different class hierarchies.  
For instance, both Bird and Drone can implement the interface Flyable, even though they have no structural connection.  
This represents a behavior, not a family relationship.

---

# 4. Classroom Exercises

### Exercise 1: Abstract Shape
Create an abstract class `Shape` with abstract methods `area` and `perimeter`, and a concrete method `describe` that prints a simple message.  
Implement `Rectangle` and `Circle` classes that define `area` and `perimeter`.  
Create both in main and print their calculated results.

### Exercise 2: Abstract Appliance
Create an abstract class `Appliance` with field `brand` and abstract methods `turnOn` and `turnOff`.  
Implement `WashingMachine` and `Microwave` classes with unique messages for each method (turnOn and turnOff).  
Demonstrate them in main.

### Exercise 3
Create an abstract class `GameCharacter` with field `name` and abstract method `attack`.  
Implement `Warrior` and `Mage` classes with unique attack messages.  
Create them in `main` and call their attack methods.

---

# 5. Common Mistakes

| Mistake | Example | Explanation |
|----------|----------|-------------|
| Trying to create an instance of an abstract class | Vehicle v = new Vehicle() | Abstract classes cannot be instantiated |
| Forgetting to implement abstract methods | class Car extends Vehicle {} | Causes compiler error |
| Declaring abstract and final together | abstract final class A {} | Invalid combination |
| Giving a body to an abstract method | abstract void run() {} | Abstract methods cannot have implementation |
| Confusing abstraction with data hiding | Using abstract instead of private | Abstract hides logic, not data |

---

# 6. Homework Tasks

### Task 1: Employee Hierarchy
Create an abstract class Employee with fields name and baseSalary, and an abstract method calculateBonus.  
Add a concrete method printInfo that prints the name and base salary.  
Implement Manager and Intern subclasses with different bonus calculations.  
In main, create both and display their info and bonuses.

### Task 2: Musical Instruments
Create an abstract class Instrument with an abstract method play and a concrete method tune that prints a tuning message.  
Implement subclasses Guitar and Drum that override play with specific messages.  
Demonstrate both in main.

### Task 3 (Advanced)
Create an abstract class Transport with fields type and capacity and abstract methods move and stop.  
Implement Bus and Bicycle subclasses with different messages for moving and stopping.  
Demonstrate calling these methods through Transport references.

---

# 7. Summary

| Concept | Meaning | Example |
|----------|----------|----------|
| abstraction | Hiding implementation details and exposing only essential behavior | abstract class, abstract methods |
| Abstract Class | Defines a general idea but leaves details for subclasses | abstract class Shape |
| Abstract Method | Declares behavior without defining it | abstract double area() |
| Cannot Instantiate | Abstract classes are blueprints only | Vehicle v = new Vehicle() → error |
| Data Hiding | Protects internal data with private fields | private double balance |
| Difference | abstraction hides logic, data hiding hides state | — |
| Benefit | Simplifies code, improves maintainability and structure | Clear what, hidden how |
