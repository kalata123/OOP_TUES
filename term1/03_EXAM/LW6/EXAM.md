# **Java OOP Exam — Library Management System**

---

## **Objective**

Create a library management system that demonstrates object-oriented programming principles. You'll build several interconnected classes that work together to manage books, members, and borrowing operations.

---

## **Project Structure**

Create this exact package structure in your `src` folder:

```
src/
└── library/
├── core/
├── users/
├── materials/
├── transactions/
└── util/
```


### **Important Rules**
- Each Java file must begin with the correct package declaration.
- Keep all data fields private.
- Use the provided `Main.java` to test your implementation.

---

*Note:* Check the whole task before starting (e.g. Part D is about Exceptions and Errors)

## **Part A: Foundation Classes**

### **A1) LibraryItem Class (`library.core.LibraryItem`)**

Create a base class for all library materials:

- **Private fields:** `title` (String), `itemId` (String)
- **Two constructors:**
    - One that takes only `title` (sets `itemId` to `"TEMP"`)
    - One that takes both `title` and `itemId`
- **Public methods:** `getTitle()`, `getItemId()`
- **Abstract method:** `String getItemType()` (subclasses must implement this)
- **Concrete method:** `String getDisplayInfo()` returns format: `"[TYPE]: [TITLE] (ID: [ID])"`
- **Protected helper:** `validateNotBlank(String text, String fieldName)` that throws `IllegalArgumentException` if `text` is null or empty

---

### **A2) Book Class (`library.materials.Book`)**

Represents a book in the library:

- **Extends** `LibraryItem`
- **Private field:** `author` (String)
- **Two constructors:**
    - `Book(String title, String author)`
    - `Book(String title, String author, String itemId)`
- **Implement** `getItemType()` to return `"Book"`
- **Use** the inherited validation method to ensure `author` is not blank

---

### **A3) Member Class (`library.users.Member`)**

Represents a library user:

- **Private fields:** `name` (String), `memberId` (String), `borrowedCount` (int)
- **Two constructors:**
    - `Member(String name, String memberId)`
    - `Member(String name)` — generates `memberId` as `"MEM-" + random number`
- **Methods:**
    - `boolean canBorrow()` — returns `true` if current count `<` maximum limit (see Part B)
    - `void incrementBorrowed()` — increases borrowed count
    - `void decrementBorrowed()` — decreases borrowed count
- **Validate** that `name` is not blank

---

## **Part B: Utilities and Settings**

### **B1) LibrarySettings (`library.util.LibrarySettings`)**

Central place for system constants:

- **Prevent instantiation** (private constructor)
- **Constants:**
    - `MAX_BORROW_LIMIT = 5`
    - `FINE_PER_DAY = 1`
    - `DEFAULT_LOAN_DAYS = 14`
- **Static method:** `String generateItemId(String prefix)` returns format: `[PREFIX]-[random number]`. You can use `int randomNum = (int)(Math.random() * 101);` to get a random number. 

---

### **B2) SimpleDateUtils (`library.util.SimpleDateUtils`)**

Helper for date calculations:

- **Prevent instantiation**
- **Static overloaded methods (same name, different parameters):**
    - `calculateDueDate(int daysFromNow)` returns `"Due in X days"`
    - `calculateDueDate(String startDate)` returns `"Due on [startDate] plus default days"`
    - `calculateDueDate(int days, boolean isExtension)` returns different message if it's an extension
- **Note:** No complex date parsing needed — use simple string operations

---

## **Part C: Borrowing Management**

### **C1) BorrowingSystem (`library.transactions.BorrowingSystem`)**

Manages which items members have borrowed:

- **Private arrays (fixed size = 5):**
    - `LibraryItem[] borrowedItems`
    - `String[] dueDates`
- **Private field:** `int itemCount` (tracks current number of items)
- **Methods:**
    - `void borrowItem(LibraryItem item, String dueDate)` — adds to arrays if space available
    - `boolean returnItem(String itemId)` — removes item by ID, returns `true` if successful
    - `String[] getBorrowedTitles()` — returns array of currently borrowed item titles
    - `int getBorrowedCount()` — returns current count
- **Validation:**
    - Throw exception if trying to borrow when array is full
    - Throw exception if `itemId` not found when returning
    - Keep both arrays synchronized (same items at same indexes)

---

### **C2) LibraryException (`library.transactions.LibraryException`)**

Custom exception for library operations:

- **Extends** `RuntimeException`
- **Constructor** that accepts a message string

---

### **C3) Integration**

Ensure `Member` class works with `BorrowingSystem`:

- Update `borrowedCount` when items are borrowed/returned
- Use `MAX_BORROW_LIMIT` from `LibrarySettings`

---

## **Part D: Error Handling**

### **Comprehensive Validation**

Implement validation throughout your classes:

- **Constructors:** Check for null/blank strings
- **BorrowingSystem methods:** Validate parameters before operations
- **Business logic:** Prevent invalid operations (borrowing beyond limits, negative counts)
- **Use** the protected `validateNotBlank` method where appropriate

### **Exception Usage**

- **Throw** `IllegalArgumentException` for invalid parameters
- **Throw** `LibraryException` for business rule violations
- **Include** clear, descriptive error messages

---
## Demonstrate in a Main.java file the program. 

Here is provided a Main with hints to test your code.

```java
package library;

/**
 * LIBRARY MANAGEMENT SYSTEM - DEMONSTRATION GUIDE
 * 
 * Use this Main class to test your implementation.
 * Follow the hints below to demonstrate all required features.
 * 
 * REMEMBER: This file contains ONLY hints - you must write the actual code!
 */

public class Main {
    
    public static void main(String[] args) {
        
        // === DEMONSTRATION HINTS ===
        // Implement the code below each comment to show your system working
        
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM DEMONSTRATION ===");
        
        // HINT: Create books using different constructors to show overloading
        // Book 1: Use constructor with title, author, and ID
        // Book 2: Use constructor with only title and author (auto-generate ID)
        
        // HINT: Create members using different constructors to show overloading  
        // Member 1: Use constructor with name and member ID
        // Member 2: Use constructor with only name (auto-generate ID)
        
        // HINT: Demonstrate encapsulation - try to access private fields directly
        // Then show how to properly use getters to access the data
        
        // HINT: Show LibrarySettings usage
        // Print the maximum borrow limit constant
        // Generate some item IDs using the static method
        
        // HINT: Demonstrate SimpleDateUtils method overloading
        // Call all three overloaded calculateDueDate methods with different parameters
        // Show the different results
        
        // === PART C3: MEMBER + BORROWINGSYSTEM INTEGRATION ===
        // HINT: Create a BorrowingSystem instance and a Member instance
        // When you borrow an item through BorrowingSystem, ALSO call member.incrementBorrowed()
        // When you return an item through BorrowingSystem, ALSO call member.decrementBorrowed()
        // Show that both systems stay synchronized - the member count matches BorrowingSystem count
        
        // HINT: Test borrowing up to the maximum limit
        // Show what happens when trying to borrow beyond the limit
        
        // === PART D: ERROR HANDLING ===
        // HINT: Demonstrate input validation in constructors
        // Try to create a Member with blank name - catch IllegalArgumentException
        // Try to create a Book with blank author - catch IllegalArgumentException
        
        // HINT: Demonstrate business logic errors
        // Try to borrow an item when BorrowingSystem is full - catch LibraryException
        // Try to return an item that doesn't exist - handle the return value appropriately
        
        // HINT: Demonstrate proper exception handling structure
        // Use try-catch blocks with specific exception types
        // Use a finally block to show cleanup code
        // Show multiple catch blocks in correct order (specific before general)
        
        // HINT: Test BorrowingSystem with invalid parameters
        // Try to call borrowItem with null item parameter
        // Try to call borrowItem with null dueDate parameter
        
        // HINT: Show custom LibraryException usage
        // Create and throw a LibraryException with a meaningful message
        // Catch it and display the message
        
        // HINT: Demonstrate polymorphism
        // Create an array of LibraryItem that contains different types of items
        // Loop through and call getDisplayInfo() on each one
        
        // OPTIONAL: If you implemented bonus features, demonstrate them here
        // Show search functionality
        // Show usage statistics
        
        System.out.println("=== DEMONSTRATION COMPLETE ===");
        
        // HINT: Make sure your program runs from start to finish without crashing
        // Handle all exceptions gracefully
        // Show clear output so we can see what's happening at each step
    }
}
```


---

## **Bonus Features (Optional)**

### **Bonus 1: Search Capability**

Add to `BorrowingSystem`:

- `boolean hasBorrowedItem(String itemId)` — searches by `itemId`
- `String findDueDate(String itemId)` — returns due date for specific item

### **Bonus 2: Statistics**

Add to `BorrowingSystem`:

- `double getUsagePercentage()` — returns `(currentCount / maxLimit) * 100`
- `String getBorrowingStatus()` — returns `"Light/Medium/Heavy"` usage based on percentage

## Grading

Based on Class performance. Tentative points for each part:

# ***Tentative* Grading Breakdown — Library Management System Exam**
# ***Условна* Разбивка на точкуване — Library Management System Exam**

</br> </br>
Подлежи на промяна. Общ брой точки е 31 + 4 за бонус задачите. 

| **Component**                    | **Sub-Component**          | **Points** |
|----------------------------------|----------------------------|-----------|
| **Part A: Foundation Classes**   | **Total**                  | **8**     |
|                                  | A1: LibraryItem (abstract) | 3         |
|                                  | A2: Book class             | 3         |
|                                  | A3: Member class           | 2         |
|                                  |                            |            |
| **Part B: Utilities & Settings** | **Total**                  | **6**     |
|                                  | B1: LibrarySettings        | 3         |
|                                  | B2: SimpleDateUtils        | 3         |
|                                  |                            |            |
| **Part C: Borrowing Management** | **Total**                  | **9**     |
|                                  | C1: BorrowingSystem        | 5         |
|                                  | C2: LibraryException       | 2         |
|                                  | C3: Integration            | 2         |
|                                  |                            |            |
| **Part D: Error Handling**       | **Total**                  | **4**     |
|                                  |                            |            |
| **Main.java Demonstration**      | **Total**                  | **2**     |
|                                  | Functionality Showcase     | 2         |
|                                  |                            |            |
| **Program Compilation**          | **Total**                  | **4**     | 
|                                  |                            |            |
| **BONUS FEATURES**               | **Total**                  | **+4**    |
|                                  | Bonus 1: Search            | +2        |
|                                  | Bonus 2: Statistics        | +2        |

