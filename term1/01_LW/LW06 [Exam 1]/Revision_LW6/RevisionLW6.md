# **Gym Management System - Revision Task**

---

## **Project Structure & Setup**

### **Package Organization**

Create this package structure in your `src` folder:

```
gym/
├── core/
├── members/
├── staff/
├── billing/
├── util/
└── Main.java
```


**Rules:**
- Each Java file must begin with the correct package declaration.
- Only cross-package classes should be public.
- Helper classes can use package-private (default) access.

---

## **Part A: Core Abstraction & Encapsulation (40 min)**

### **A1) Abstract Person Class (`gym.core.Person`)**

Create an abstract class with:

- **Private fields:** `name` (String), `age` (int)
- **Constructor overloading:**
    - `Person(String name)` → sets `age` to 0
    - `Person(String name, int age)` → sets both fields
- **Encapsulation:**
    - Public getters for both fields
    - Public setter for `age` with validation: if `age < 0`, set to 0
- **Abstract method:** `String getRole()` (to be implemented by subclasses)
- **Concrete method:** `String getBadge()` returns `"NAME (ROLE), age AGE"` - the name, role and age
- **Protected helper:** `validateNotBlank(String value, String fieldName)` that throws `IllegalArgumentException` if value is null or blank

---

### **A2) Member Class (`gym.members.Member`)**

Extends `Person` with:

- **Private fields:**
    - `membershipLevel` (int, 1–3)
    - `charges` (int array, size 100)
    - `chargeCount` (int, tracks used positions)

- **Constructor overloading:**
    - `Member(String name)`
    - `Member(String name, int age, int membershipLevel)`

- **Implement `getRole()`** to return `"Member"`

- **Add charge management:**
    - `addCharge(int amount)` — validates and adds to array
    - `getTotalCharges()` — calculates sum of all charges
    - `getAverageCharge()` — returns average or 0 if no charges

---

### **A3) Trainer Class (`gym.staff.Trainer`)**

Extends `Person` with:

- **Private field:** `specialty` (String, default `"General Fitness"`)
- **Constructor overloading:**
    - `Trainer(String name)`
    - `Trainer(String name, int age, String specialty)` — uses `validateNotBlank` for specialty
- **Implement `getRole()`** to return `"Trainer"`

---

## **Part B: Modifiers & Static Context (30 min)**

### **B1) Settings Utility (`gym.util.Settings`)**

Create a `public final` class with:

- **Public static final constants:**
    - `MIN_CHARGE = 1`
    - `MAX_CHARGE = 1000`
    - `MAX_CHARGES = 100` (for array size)
- **Private constructor** to prevent instantiation

---

### **B2) ID Generator (`gym.util.IdGenerator`)**

Create a class demonstrating static usage:

- **Private static field:** `nextId` (int, starts at 1000)
- **Static method overloading:**
    - `nextId()` returns `"MEM-" + nextId++`
    - `nextId(String prefix)` returns `prefix + "-" + nextId++`

---

## **Part C: Method Overloading & Array Management (40 min)**

### **C1) Enhanced Charge Management in Member**

Expand the `Member` class with:

- **Method overloading for `addCharge`:**
    - `addCharge(int amount)` — validates amount and stores in array
    - `addCharge(String description, int amount)` — validates both parameters and stores in array (description can be stored in a separate array for descriptions that is fixed size of MAX_CHARGES)

- **Array management rules:**
    - Initialize `charges` array with size `MAX_CHARGES`
    - Use `chargeCount` to track next available position
    - Throw exception if array is full
    - Validate amount against `MIN_CHARGE` and `MAX_CHARGE`

- **Calculation methods:**
    - `getTotalCharges()` — sums all stored charges
    - `getAverageCharge()` — returns average or 0 if no charges

---

### **C2) Invoice Processor (`gym.billing.Invoice`)**

Create a `public final` class with static utility methods:

- **Static methods:**
    - `applyMonthlyFee(Member member, int fee)` — adds fee to member’s charges
    - `applyDiscount(Member member, double percentage)` — calculates and adds negative charge

- **Validation:** All methods should validate parameters

---

## **Part D: Exception Handling (40 min)**

### **D1) Custom Exceptions (`gym.billing`)**

Create two custom exception classes:

- **GymException (unchecked):**
    - Extends `RuntimeException`
    - Constructor accepts a message
    - Used for business rule violations

- **BillingException (checked):**
    - Extends `Exception`
    - Constructor accepts a message
    - Used for recoverable billing errors

---

### **D2) Integrated Exception Handling**

In the `Member` class:

Throw `GymException` when:
- Charge amount is outside valid range
- Charges array is full
- Membership level is invalid

In `Main` class demonstrate:
- try-catch for `GymException` when adding charges
- try-catch-finally block for file reading simulation
- Multiple catch blocks with specific exception ordering
- Using `throws` declaration appropriately

---

## **Part E: Main Demonstration (20 min)**

### **Comprehensive Test Scenario**

In `Main.java`, demonstrate all concepts:

#### **Constructor Overloading**
- Create Members and Trainers using different constructors

#### **Encapsulation & Validation**
- Attempt to set invalid values and show validation
- Use getters to display information

#### **Method Overloading**
- Add charges with and without descriptions
- Show array bounds checking

#### **Static Context**
- Generate IDs using both overloaded methods
- Access `Settings` constants

#### **Abstraction & Polymorphism**
- Create a `Person` array containing both Members and Trainers
- Call `getBadge()` on each to demonstrate polymorphism

#### **Exception Handling**
- Trigger and catch custom exceptions
- Demonstrate try-catch-finally with resource cleanup simulation
- Show multiple catch block ordering

---

## **Implementation Constraints**

### **Array Management**
- Use fixed-size array in `Member` class (`charges = new int[MAX_CHARGES]`)
- Maintain `chargeCount` to track used positions
- Throw exception when array is full
- Never return the internal array reference to external code

### **Access Modifiers**
- All fields must be private
- Use public methods for controlled access
- `protected` only for helper methods in abstract class
- package-private for same-package helpers

### **Validation Rules**
- Age: cannot be negative (default to 0)
- Membership level: 1–3 only
- Charge amount: between `MIN_CHARGE` and `MAX_CHARGE`
- Names/specialties: cannot be blank
- Array bounds: check before adding charges

### **Error Conditions to Handle**
- Array index out of bounds (full array)
- Invalid parameter values
- Division by zero in average calculation
- Null/blank strings

---

## **Success Criteria Checklist**

- All packages created with correct structure
- Abstract Person class with encapsulation
- Member and Trainer subclasses implement all abstract methods
- Constructor and method overloading demonstrated
- Static fields and methods used appropriately
- Fixed-size array manages charges correctly
- Custom exceptions created and used
- Comprehensive exception handling in Main
- All access modifiers used correctly
- Validation prevents invalid state
- Code compiles and runs without runtime errors

---

## **Extension Challenges (Optional)**

- Add a `ResizableArray` helper class that handles array growing internally
- Create a membership expiry system with date validation
- Add logging to track all charge operations
- Implement a simple search feature to find charges above a certain amount