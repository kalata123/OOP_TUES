# **Java OOP Exam – Hotel Reservation System**

---

## **Objective**

Create a hotel reservation system that demonstrates object-oriented programming principles. You'll build several interconnected classes that work together to manage rooms, guests, and booking operations.

---

## **Project Structure**

Create this exact package structure in your `src` folder:

```
src/
└── hotel/
    ├── core/
    ├── guests/
    ├── rooms/
    ├── bookings/
    └── util/
```


### **Important Rules**
- Each Java file must begin with the correct package declaration.
- Keep all data fields private.
- Use the provided `Main.java` to test your implementation.

---

*Note:* Check the whole task before starting (e.g. Part D is about Exceptions and Errors)

## **Part A: Foundation Classes**

### **A1) Room Class (`hotel.core.Room`)**

Create a base class for all hotel rooms:

- **Private fields:** `roomNumber` (String), `roomId` (String)
- **Two constructors:**
    - One that takes only `roomNumber` (sets `roomId` to `"TEMP"`)
    - One that takes both `roomNumber` and `roomId`
- **Public methods:** `getRoomNumber()`, `getRoomId()`
- **Abstract method:** `String getRoomType()` (subclasses must implement this)
- **Concrete method:** `String getRoomInfo()` returns format: `"[TYPE]: Room [NUMBER] (ID: [ID])"`
- **Protected helper:** `validateNotEmpty(String text, String fieldName)` that throws `IllegalArgumentException` if `text` is null or empty

---

### **A2) StandardRoom Class (`hotel.rooms.StandardRoom`)**

Represents a standard room in the hotel:

- **Extends** `Room`
- **Private field:** `floor` (String)
- **Two constructors:**
    - `StandardRoom(String roomNumber, String floor)`
    - `StandardRoom(String roomNumber, String floor, String roomId)`
- **Implement** `getRoomType()` to return `"Standard"`
- **Use** the inherited validation method to ensure `floor` is not empty

---

### **A3) Guest Class (`hotel.guests.Guest`)**

Represents a hotel guest:

- **Private fields:** `name` (String), `guestId` (String), `activeBookings` (int)
- **Two constructors:**
    - `Guest(String name, String guestId)`
    - `Guest(String name)` – generates `guestId` as `"GST-" + random number`
- **Methods:**
    - `boolean canBook()` – returns `true` if current count `<` maximum limit (see Part B)
    - `void incrementBookings()` – increases booking count
    - `void decrementBookings()` – decreases booking count
- **Validate** that `name` is not empty

---

## **Part B: Utilities and Settings**

### **B1) HotelSettings (`hotel.util.HotelSettings`)**

Central place for system constants:

- **Prevent instantiation** (private constructor)
- **Constants:**
    - `MAX_BOOKINGS_PER_GUEST = 3`
    - `CANCELLATION_FEE = 50`
    - `DEFAULT_STAY_NIGHTS = 7`
- **Static method:** `String generateRoomId(String prefix)` returns format: `[PREFIX]-[random number]`. You can use `int randomNum = (int)(Math.random() * 101);` to get a random number.

---

### **B2) DateCalculator (`hotel.util.DateCalculator`)**

Helper for date calculations:

- **Prevent instantiation**
- **Static overloaded methods (same name, different parameters):**
    - `calculateCheckout(int nightsFromNow)` returns `"Checkout in X nights"`
    - `calculateCheckout(String checkinDate)` returns `"Checkout on [checkinDate] plus default nights"`
    - `calculateCheckout(int nights, boolean isExtended)` returns different message if it's an extended stay
- **Note:** No complex date parsing needed – use simple string operations

---

## **Part C: Booking Management**

### **C1) ReservationSystem (`hotel.bookings.ReservationSystem`)**

Manages which rooms guests have booked:

- **Private arrays (fixed size = 3):**
    - `Room[] bookedRooms`
    - `String[] checkoutDates`
- **Private field:** `int bookingCount` (tracks current number of bookings)
- **Methods:**
    - `void bookRoom(Room room, String checkoutDate)` – adds to arrays if space available
    - `boolean cancelBooking(String roomId)` – removes booking by ID, returns `true` if successful
    - `String[] getBookedRoomNumbers()` – returns array of currently booked room numbers
    - `int getBookingCount()` – returns current count
- **Validation:**
    - Throw exception if trying to book when array is full
    - Throw exception if `roomId` not found when canceling
    - Keep both arrays synchronized (same bookings at same indexes)

---

### **C2) HotelException (`hotel.bookings.HotelException`)**

Custom exception for hotel operations:

- **Extends** `RuntimeException`
- **Constructor** that accepts a message string

---

### **C3) Integration**

Ensure `Guest` class works with `ReservationSystem`:

- Update `activeBookings` when rooms are booked/canceled
- Use `MAX_BOOKINGS_PER_GUEST` from `HotelSettings`

---

## **Part D: Error Handling**

### **Comprehensive Validation**

Implement validation throughout your classes:

- **Constructors:** Check for null/empty strings
- **ReservationSystem methods:** Validate parameters before operations
- **Business logic:** Prevent invalid operations (booking beyond limits, negative counts)
- **Use** the protected `validateNotEmpty` method where appropriate

### **Exception Usage**

- **Throw** `IllegalArgumentException` for invalid parameters
- **Throw** `HotelException` for business rule violations
- **Include** clear, descriptive error messages

---
## Demonstrate in a Main.java file the program.

Here is provided a Main with hints to test your code.

```java
package hotel;

/**
 * HOTEL RESERVATION SYSTEM - DEMONSTRATION GUIDE
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
        
        System.out.println("=== HOTEL RESERVATION SYSTEM DEMONSTRATION ===");
        
        // HINT: Create rooms using different constructors to show overloading
        // Room 1: Use constructor with roomNumber, floor, and ID
        // Room 2: Use constructor with only roomNumber and floor (auto-generate ID)
        
        // HINT: Create guests using different constructors to show overloading  
        // Guest 1: Use constructor with name and guest ID
        // Guest 2: Use constructor with only name (auto-generate ID)
        
        // HINT: Demonstrate encapsulation - try to access private fields directly
        // Then show how to properly use getters to access the data
        
        // HINT: Show HotelSettings usage
        // Print the maximum bookings per guest constant
        // Generate some room IDs using the static method
        
        // HINT: Demonstrate DateCalculator method overloading
        // Call all three overloaded calculateCheckout methods with different parameters
        // Show the different results
        
        // === PART C3: GUEST + RESERVATIONSYSTEM INTEGRATION ===
        // HINT: Create a ReservationSystem instance and a Guest instance
        // When you book a room through ReservationSystem, ALSO call guest.incrementBookings()
        // When you cancel a booking through ReservationSystem, ALSO call guest.decrementBookings()
        // Show that both systems stay synchronized - the guest count matches ReservationSystem count
        
        // HINT: Test booking up to the maximum limit
        // Show what happens when trying to book beyond the limit
        
        // === PART D: ERROR HANDLING ===
        // HINT: Demonstrate input validation in constructors
        // Try to create a Guest with empty name - catch IllegalArgumentException
        // Try to create a StandardRoom with empty floor - catch IllegalArgumentException
        
        // HINT: Demonstrate business logic errors
        // Try to book a room when ReservationSystem is full - catch HotelException
        // Try to cancel a booking that doesn't exist - handle the return value appropriately
        
        // HINT: Demonstrate proper exception handling structure
        // Use try-catch blocks with specific exception types
        // Use a finally block to show cleanup code
        // Show multiple catch blocks in correct order (specific before general)
        
        // HINT: Test ReservationSystem with invalid parameters
        // Try to call bookRoom with null room parameter
        // Try to call bookRoom with null checkoutDate parameter
        
        // HINT: Show custom HotelException usage
        // Create and throw a HotelException with a meaningful message
        // Catch it and display the message
        
        // HINT: Demonstrate polymorphism
        // Create an array of Room that contains different types of rooms
        // Loop through and call getRoomInfo() on each one
        
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

Add to `ReservationSystem`:

- `boolean hasBookedRoom(String roomId)` – searches by `roomId`
- `String findCheckoutDate(String roomId)` – returns checkout date for specific room

### **Bonus 2: Statistics**

Add to `ReservationSystem`:

- `double getOccupancyPercentage()` – returns `(currentCount / maxLimit) * 100`
- `String getOccupancyStatus()` – returns `"Low/Medium/High"` occupancy based on percentage

## Grading

Based on Class performance. Tentative points for each part:

# ***Tentative* Grading Breakdown – Hotel Reservation System Exam**
# ***Условна* Разбивка на точкуване – Hotel Reservation System Exam**

</br> </br>
Подлежи на промяна. Общ брой точки е 31 + 4 за бонус задачите.

| **Component**                    | **Sub-Component**          | **Points** |
|----------------------------------|----------------------------|-----------|
| **Part A: Foundation Classes**   | **Total**                  | **8**     |
|                                  | A1: Room (abstract)        | 3         |
|                                  | A2: StandardRoom class     | 3         |
|                                  | A3: Guest class            | 2         |
|                                  |                            |            |
| **Part B: Utilities & Settings** | **Total**                  | **6**     |
|                                  | B1: HotelSettings          | 3         |
|                                  | B2: DateCalculator         | 3         |
|                                  |                            |            |
| **Part C: Booking Management**   | **Total**                  | **9**     |
|                                  | C1: ReservationSystem      | 5         |
|                                  | C2: HotelException         | 2         |
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