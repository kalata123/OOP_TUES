# **Java Topic: Errors**

---

## 1. What Are Errors?
Errors are problems that **stop your program from working correctly**.  
They can appear before running, during running, or produce wrong results even when no crash happens.

---

## 2. Main Types of Errors

| Type                  | When It Happens                 | Example                          |
|-----------------------|---------------------------------|----------------------------------|
| **Compilation Error** | Before running (compiler check) | Missing `;`, undeclared variable | 
| **Runtime Error**     | While running                   | Divide by zero, invalid index    | 
| **Logic Error**       | Runs fine but wrong output      | Wrong math formula               |

---

## 3. Short Description

### **Compilation Error**
Detected by the compiler before the program starts.  
You must fix these or the program won’t even run.  
Example: Missing semicolon or wrong variable type.

---

### **Runtime Error**
The program starts but crashes mid-way because of an invalid operation.  
Example: Dividing by zero, using `null`, or going outside array bounds.

---

### **Logic Error**
The code runs without crashing, but gives **wrong results**.  
Hardest to find because there’s no visible error — only incorrect output.

---

### Example

```java
// Example 1 — Compilation Error
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World") // missing semicolon
    }
}

// Example 2 — Runtime Error
public class Main {
    public static void main(String[] args) {
        int result = 10 / 0;
        System.out.println("This line will not execute.");
    }
}

// Example 3 — Logic Error
public class Main {
    public static void main(String[] args) {
        int sum = 90;
        int count = 10;
        double average = sum / count - 1; // wrong logic
        System.out.println("Average = " + average);
    }
}
```

## 4. Common Mistakes

| Mistake             | Example                    | Type of error   |
|---------------------|----------------------------|-----------------|
| Missing semicolon   | `System.out.println("Hi")` | Compilation Err |
| Undeclared variable | `System.out.println(num);` | Compilation Err |
| Divide by zero      | `10 / 0`                   | Runtime Err     |
| Wrong formula       | `average = sum / n - 1`    | Logic error     |

---

## 5. Summary

| Type            | Description                                          | Example            | How to Fix                             |
|-----------------|------------------------------------------------------|--------------------|----------------------------------------|
| **Compilation** | Syntax or type mistakes                              | Missing `;`        | Read compiler message → correct syntax |
| **Runtime**     | Happens during program execution (invalid operation) | Divide by zero     | Validate inputs before using them      |
| **Logic**       | Code runs but result is wrong                        | Incorrect formula  | Use print statements, test carefully   |

**_Notes:_**  
Compilation stops your program.  
Runtime crashes your program.  
Logic silently lies to you.