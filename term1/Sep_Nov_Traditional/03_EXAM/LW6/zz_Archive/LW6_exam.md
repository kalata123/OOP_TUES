# **Java OOP Exam — Library Management System (Refined Version)**

---

## **Time:** 60 minutes
## **Total Points:** 30 + 2 bonus points
## **Environment:** IntelliJ IDEA (no internet access)

---

## **Exam Objective**

Build a small **console-based Library Management System** that shows your understanding of:
- Packages and access modifiers
- Encapsulation and validation
- Abstraction and inheritance
- Static fields and methods
- Basic exception handling
- Fixed-size array management

Keep your project simple, readable, and fully runnable.

---

## **Project Structure**

```
library/
├── core/
├── users/
├── materials/
├── transactions/
├── util/
└── Main.java
```

**Rules:**
- Each file must begin with a correct `package` declaration.
- Only classes that are used across packages should be `public`.
- All data fields must be `private`.

---

## **Part A — Core Abstraction & Inheritance (6 points)**

### **A1) Abstract LibraryItem (library.core)**
Create an abstract class that defines the base structure for all library materials.

**Requirements:**
- Private fields: `title` and `itemId`
- Two constructors: one with only `title`, one with both `title` and `itemId`
- Public getters for both fields
- Abstract method: `String getItemType()`
- Concrete method: `String getDisplayInfo()` → returns formatted text like  
  `"Book: Java Basics (ID: BK-101)"`
- Protected helper method: `validateNotBlank(String text, String fieldName)` that throws an `IllegalArgumentException` if empty

---

### **A2) Book (library.materials)**
Represents a library book.

**Requirements:**
- Extends `LibraryItem`
- Private field: `author`
- Two constructors (one minimal, one full)
- Implements `getItemType()` → `"Book"`
- Validates `author` using the inherited `validateNotBlank` method

---

### **A3) Member (library.users)**
Represents a library user.

**Requirements:**
- Private fields: `name`, `memberId`, `borrowedCount`
- Two constructors (one minimal, one full)
- Method: `borrowItem()` → increases count if below limit
- Method: `returnItem()` → decreases count if above zero
- Borrowing limit: 5 items (use constant from settings)
- Validation for non-empty `name`

---

## **Part B — Modifiers & Static Context (6 points)**

### **B1) LibrarySettings (library.util)**
Central place for constants and static helpers.

**Requirements:**
- `public final` class
- `public static final int MAX_BORROW_LIMIT = 5`
- `public static final int FINE_PER_DAY = 1`
- Private constructor to prevent instantiation
- Static method `generateItemId(String prefix, int number)` → returns formatted ID like `"BK-1001"`

---

### **B2) DateUtils (library.util)**
Simplified example of static overloading.

**Requirements:**
- Two `daysBetween` methods:
    - One takes two integers (`start`, `end`) and returns difference.
    - One takes two strings (`"YYYY-MM-DD"`) and simulates difference by returning a random small number (you may skip actual date parsing).

---

## **Part C — Array Management & Encapsulation (8 points)**

### **C1) BorrowingSystem (library.transactions)**
Tracks which books each member has borrowed.

**Requirements:**
- Private arrays:
    - `LibraryItem[] borrowedItems`
    - `String[] dueDates`
- Constant capacity = `MAX_BORROW_LIMIT`
- Integer counter `count`
- Methods:
    - `borrowItem(LibraryItem item, String dueDate)`
    - `returnItem(String title)`
    - `getBorrowedItems()` → returns current count and titles

**Rules:**
- Do not resize arrays.
- Prevent adding if full (throw exception).
- Prevent returning if array is empty.
- Keep both arrays synchronized by index.

---

### **C2) Business Logic**
- When borrowing beyond limit → throw custom `LibraryException`.
- When returning non-existent item → throw custom `LibraryException`.
- Use simple date strings (e.g. `"2025-10-30"`).
- If overdue logic is added, it should be basic (manual number comparison).

---

## **Part D — Exception Handling (6 points)**

### **D1) Custom Exceptions (library.transactions)**
Create one checked and one unchecked exception.

- `LibraryException` → extends `RuntimeException`  
  Used for invalid operations (e.g., borrowing too many items).
- `OverdueException` → extends `Exception`  
  Used when trying to return overdue items.

---

### **D2) Exception Usage**
In `Main.java`, demonstrate:

- Try borrowing more than allowed → catch `LibraryException`.
- Try returning an overdue item → catch `OverdueException`.
- Use `finally` block to print `"Operation complete."`

---

## **Implementation Requirements (4 points)**

### **Access Modifiers**
- Fields are always private.
- Methods that are only used by subclasses → protected.
- Public only where needed.

### **Encapsulation**
- No direct array exposure.
- Validation handled inside constructors or setters.

### **Overloading**
- Demonstrate at least one constructor and one method overloading (e.g., in `Book` or `DateUtils`).

---

## **Bonus Section (2 points total)**

### **Bonus 1 — Search (1 pt)**
Add a method in `BorrowingSystem` that searches for a borrowed item by title and prints whether it’s found.

### **Bonus 2 — Statistics (1 pt)**
Add a method that calculates how many total items are currently borrowed and displays the percentage of capacity used.

---

## **Main Class Demonstration (10 points total)**

In `Main.java`, show:

1. Creating `Book` and `Member` objects using different constructors.
2. Borrowing and returning items using `BorrowingSystem`.
3. Catching and printing messages from both custom exceptions.
4. Using `LibrarySettings.generateItemId()`.
5. Using `DateUtils.daysBetween()` overloads.
6. Printing display info for all borrowed items (polymorphism).

---

## **Grading Focus**

| Concept | Points |
|----------|---------|
| Abstraction & Inheritance | 6 |
| Modifiers & Static Context | 6 |
| Encapsulation & Arrays | 8 |
| Exception Handling | 6 |
| Implementation & Clarity | 4 |
| **Total** | **30** |

---

## **Hints**
- Focus on clarity and correctness, not on features.
- Keep code short — each class should fit within 30 lines.
- Don’t use advanced APIs or collections.
- Follow OOP principles shown in revision materials.
- Comment short explanations for validation or exceptions.

---
