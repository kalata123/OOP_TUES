# 1. Theoretical Base

### Definition
A package in Java is a namespace that groups related classes, interfaces, and subpackages together.  
It provides a logical structure that helps organize code, prevent naming conflicts, and manage large projects efficiently.  
Packages can be viewed as folders that contain Java classes with similar functionality.

---

### Why Packages Exist
1. **Organization** – Keep related code grouped logically (e.g., `school.students`, `school.teachers`).
2. **Name conflict prevention** – Two classes with the same name can coexist if they are in different packages.
3. **Access control** – Packages work with access modifiers to determine visibility between classes.
4. **Reusability** – You can import only what you need and reuse code across projects.
5. **Maintenance** – Large projects become easier to navigate, extend, and debug.

---

### Types of Packages
1. **Built-in packages** – Provided by Java (`java.util`, `java.io`, `java.lang`).
2. **User-defined packages** – Created by the programmer to organize project code.

---

### Syntax Rules
- The `package` declaration must be the **first non-comment line** in a Java file.
- The folder structure must exactly match the package name.
- To use a class from another package, you must **import** it or use its **fully qualified name**.
- If no package is declared, the class belongs to the **default package** (not recommended for large projects).

---

# 2. Examples

### Exercise 1: Simple Package
File 1 `animals/Dog.java`
```java
package animals;

public class Dog {
    public void bark() {
        System.out.println("Woof! The dog is barking.");
    }
}
```
File 2 `Main.java`

```java
import animals.Dog;

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.bark();
    }
}
```

### Exercise 2: Multi-level Packages

File 1: `school/staff/Teacher.java`
```java
package school.staff;

public class Teacher {
    public void teach() {
        System.out.println("Teaching students in class.");
    }
}
```

File 2: `school/students/Student.java`

```java
package school.students;

public class Student {
    public void study() {
        System.out.println("Studying for exams.");
    }
}

```

File 3 `Main.java`

```java
import school.staff.Teacher;
import school.students.Student;

public class Main {
    public static void main(String[] args) {
        Teacher teacher = new Teacher();
        Student student = new Student();

        teacher.teach();
        student.study();
    }
}
```


---

# 3. Explanation Through Scenarios

### Example 1: Organization
A large school management system might use these packages:  
`school.students`, `school.teachers`, `school.staff`, `school.library`.  
Each package contains code related only to its domain, improving clarity and structure.

### Example 2: Conflict Prevention
Two different classes named `Student` can exist — one in `school.students` and another in `university.students`.  
They can both be used in the same project without conflict because they belong to different packages.

### Example 3: Access Control
Public classes and methods can be accessed everywhere.  
Default (package-private) classes and methods are visible only inside their own package.  
This allows related classes to cooperate internally while remaining hidden from unrelated parts of the program.

---

# 4. Classroom Exercises

### Exercise 1: Simple Package
Create package `animals` and define class `Dog` with method `bark()`.  
Create another file that imports `animals.Dog` and calls `bark()` from `main()`.

### Exercise 2: Multi-level Packages
Create packages `school.staff` and `school.students`.  
In `school.staff`, define class `Teacher` with method `teach()`.  
In `school.students`, define class `Student` with method `study()`.  
Import both into a `Main` class and call their methods.

### Exercise 3 (Challenge)
Create package `devices` containing `Phone` and `Laptop` classes with simple methods.  
In `Main.java`, import both classes and call their methods.  
Then repeat without `import` by using their fully qualified names.

---

# 5. Common Mistakes

| Mistake | Example | Explanation |
|----------|----------|-------------|
| Missing `package` declaration | Class lacks a package header | Treated as default package; poor organization |
| Mismatched folder and package name | Declares `package school.students;` but file is not in that folder | Causes compile-time error |
| Forgetting `public` for shared classes | Class not visible to other packages | Must be declared `public` |
| Placing `import` after `class` definition | `import` must appear before class declaration | Invalid placement |
| Using duplicate class names | Two classes named `Student` without packages | Causes conflict unless packaged properly |

---

# 6. Homework Tasks

### Task 1: Create Package Hierarchy
Create folders `school/students` and `school/teachers`.
- In `students`, define `Student` class with `study()` method.
- In `teachers`, define `Teacher` class with `teach()` method.
- In `Main`, import both and call their methods.

### Task 2: Access Control Across Packages
Create a `bank` package with class `Account` that has a private field `balance` and public methods `deposit()` and `getBalance()`.  
Create `Main` in a different package that imports and uses `Account`.  
Demonstrate that the private field cannot be accessed directly.

### Task 3 (Advanced)
Create a `library` package with:
- `Book` class (fields `title`, `author`, method `printInfo()`)
- `Librarian` class (method `issueBook(Book)`)  
  Import both in `Main` and simulate issuing a book.

---

# 7. Summary

| Concept | Description | Example |
|----------|--------------|----------|
| **Package** | Groups related classes logically | `package school.students;` |
| **Import** | Allows using external classes | `import school.students.Student;` |
| **Subpackage** | A nested package under another | `school.staff` |
| **Default Package** | Classes without a declared package | Used in small projects only |
| **Fully Qualified Name** | Using full package path directly | `school.staff.Principal p = new school.staff.Principal();` |
| **Access Control** | Defines class and method visibility | `public`, `protected`, or default |
| **Benefit** | Organizes large codebases and prevents conflicts | Easier maintenance and modular design |
