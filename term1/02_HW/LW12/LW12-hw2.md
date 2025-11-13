# Homework 2: Event & Ticket Management System
## Interactive Console Application

**Assigned:** Learning Week 12 (December 4-5, 2024)
**Due:** [Insert Due Date]
**Points:** 20 base + 5 bonus maximum

---

## What You'll Build

An **Event & Ticket Management System** - an interactive console application for managing concerts, conferences, and workshops. Users can create events, purchase tickets, view statistics, and manage bookings through a menu-driven interface.

This is a working application you can run and demonstrate, not just a collection of classes.

---

## The Application

Your program presents an interactive menu:

```
=== Event Management System ===
1. Add Event
2. List All Events
3. Purchase Ticket
4. View Event Details
5. Show Statistics
6. Sort Events
7. Filter Events
8. Exit
```

Users navigate through options, perform operations, and see immediate results. The program loops until the user chooses to exit.

---

## Required Components

You must implement these components with the exact class names and method signatures specified below. These components will be organized into packages as shown in the Submission Structure section.

### Event
*Domain Model Class*

**What it must do:**
- Store event information: unique ID, name, type, date, capacity, base price
- Track current number of attendees
- Support sorting by date
- **Event types must be exactly:** "Concert", "Conference", "Workshop", "Festival" (case-sensitive)

**Validation requirements:**
- Event ID cannot be empty
- Name must be 3-50 characters
- Type must be one of the four allowed types (exact spelling)
- Date cannot be in the past
- Date cannot be on weekends (Saturday or Sunday)
- Capacity must be between 10 and 1000, **and divisible by 10** (e.g., 50, 100, 200)
- Base price must be positive and **end in .00 or .50** (e.g., 25.00, 49.50, not 29.99)

Throw EventManagementException with clear error messages when validation fails.

---

### Ticket
*Domain Model Class*

**What it must do:**
- Link a ticket to an event and an attendee
- Store ticket ID, event ID, attendee email, price, and category
- Track whether ticket has been used
- **Ticket categories must be exactly:** "VIP", "Standard", "Student" (case-sensitive)

**Validation requirements:**
- Ticket ID and event ID cannot be empty
- Email must contain exactly one '@' symbol
- Price must be positive
- Category must be one of the three allowed categories (exact spelling)

---

### Attendee
*Domain Model Class*

**What it must do:**
- Store attendee information: email, name, phone number
- Track all tickets owned by this attendee (use dynamic collection)

**Validation requirements:**
- Email must contain exactly one '@' symbol, no spaces
- Name must be 2-50 characters
- Phone must be exactly 10 digits (no dashes or spaces)

---

### EventManager
*Management Logic Class*

**What it must do:**
- Manage all events using dynamic collections
- Manage all tickets using dynamic collections
- Track which emails are registered for which events (no duplicates per event)
- Store attendee information

**Core operations:**
- **Add event:** Store new event, check for duplicate IDs and names (case-insensitive)
- **Remove event:** Delete event and all associated tickets
- **Get event:** Retrieve event by ID
- **Purchase ticket:** Create ticket, validate event has capacity, prevent duplicate email per event, update collections
- **Cancel ticket:** Remove ticket, update attendee, free up event capacity

**Sorting operations (must have these exact method names):**
- `sortEventsByDate` - sort by date ascending
- `sortEventsByPrice` - sort by price ascending
- `sortEventsByCapacity` - sort by capacity descending

**Filtering operations (must have these exact method names):**
- `getEventsInDateRange` - return events within date range (inclusive)
- `getAvailableEvents` - return events with available seats
- `getEventsByType` - return events of specific type

**Statistics operations (must have these exact method names):**
- `getTotalAttendees` - count total unique attendees
- `getEventRevenue` - calculate revenue for specific event
- `getTotalRevenue` - calculate revenue across all events

---

### EventManagementException
*Custom Exception Class*

**What it must do:**
- Custom checked exception for all validation errors
- Extend Exception (not RuntimeException)

**Required error messages (must match exactly for automated testing):**
- "Event ID cannot be null or empty"
- "Event name must be 3-50 characters"
- "Event type must be Concert, Conference, Workshop, or Festival"
- "Event date cannot be in the past"
- "Event date cannot be on weekends"
- "Event capacity must be divisible by 10"
- "Event capacity must be between 10 and 1000"
- "Base price must end in .00 or .50"
- "Event with duplicate name already exists: [name]"
- "Event not found: [eventId]"
- "Event is sold out: [eventName]"
- "Email is already registered for this event: [email]"
- "Invalid email format: [email]"
- "Phone number must be exactly 10 digits"

---

## Interactive Menu Implementation

### Menu Display

Your main program must display the menu and process user choices in a loop. Handle invalid input gracefully (non-numeric input, out-of-range options).

### Menu Options

**1. Add Event**
- Prompt for: event ID, name, type, date, capacity, price
- Validate all inputs
- Show success or error message
- Return to main menu

**2. List All Events**
- Display all events with: ID, name, type, date, capacity (current/max), price
- If no events, show "No events available"
- Return to main menu

**3. Purchase Ticket**
- Prompt for: event ID, email, name, phone, ticket category
- Validate event exists and has capacity
- Check email not already registered for this event
- Generate ticket ID (e.g., "TKT001", "TKT002", ...)
- Show confirmation with ticket ID and price
- Return to main menu

**4. View Event Details**
- Prompt for event ID
- Show full event information including all attendees
- Return to main menu

**5. Show Statistics**
- Display:
  - Total number of events
  - Total number of attendees
  - Total revenue
  - Average attendance per event
- Return to main menu

**6. Sort Events**
- Show submenu: "Sort by: (D)ate, (P)rice, (C)apacity"
- Sort events accordingly
- Display sorted list
- Return to main menu

**7. Filter Events**
- Show submenu: "Filter by: (T)ype, (A)vailability, (D)ate Range"
- Prompt for filter criteria
- Display filtered results
- Return to main menu

**8. Exit**
- Display "Thank you for using Event Management System!"
- Terminate program

---

## Grading (20 Base Points)

| Component | Points | What's Evaluated |
|-----------|--------|------------------|
| **Compilation & Structure** | 3 | Code compiles without errors. Required classes exist: Event, Ticket, Attendee, EventManager, EventManagementException. Required methods exist with correct names. |
| **Event Management** | 5 | Events can be created, stored, and retrieved. All validation rules enforced (capacity ÷ 10, price .00/.50, no weekends). Edge cases handled properly. |
| **Ticket System** | 4 | Tickets can be purchased with proper validation. Email uniqueness enforced per event. Ticket cancellation works. Collections updated correctly. |
| **Collections Usage** | 3 | Uses appropriate dynamic collections (ArrayList, HashMap, or HashSet). Efficient operations. No fixed-size arrays. |
| **Sorting & Filtering** | 3 | All three sorting methods work correctly. All three filtering methods return correct results. |
| **Interactive Menu** | 2 | Menu displays clearly, loops until exit, handles invalid input, shows operation results. User-friendly experience. |

**Note:** Each component receives whole number points only (0, 1, 2, 3, etc.). No partial points like 2.5.

---

## Bonus (5 Points Maximum)

Choose **ONE** hard bonus to implement. Bonus points awarded only if fully functional.

### Option A: Smart Seat Allocation Algorithm (5 points)

Events have sections with different capacities:
- Front Section: 30% of total capacity
- Middle Section: 50% of total capacity
- Back Section: 20% of total capacity

Implement intelligent seat assignment:
- VIP tickets → Front Section (best available)
- Standard tickets → Middle Section (best available)
- Student tickets → Back Section (best available)

**Algorithm must handle:**
- Section full → overflow to next best section
- Group bookings → keep group together in same section
- Show seat assignments on ticket (e.g., "Front-A12")

**Why this is hard:**
- Requires algorithm design (constraint satisfaction)
- Must handle edge cases (section capacity, group splitting)
- Need to track seats per section efficiently
- Must maintain seat assignments through cancellations

**Menu integration:** Add option "9. View Seating Chart" showing section capacities and assignments.

---

### Option B: Dynamic Pricing Algorithm (5 points total)

Split into two parts (both required for full credit):

**Part 1: Demand-Based Pricing (3 points)**
- Prices increase as event fills up based on demand tiers:
  - 0-30% full: base price
  - 30-70% full: base price × 1.2
  - 70-90% full: base price × 1.4
  - 90-100% full: base price × 1.6
- Recalculate price dynamically on each ticket purchase
- Show current price in event listings (may differ from base price)

**Part 2: Revenue Optimization Report (2 points)**
- Algorithm calculates "revenue efficiency" per event:
  - Revenue Efficiency = (Current Revenue / Max Possible Revenue) × 100%
  - Max Possible Revenue = Capacity × Current Price Tier
- Show which events are most/least profitable
- Suggest price adjustments for underperforming events

**Why this is hard:**
- Requires real-time calculation on state changes
- Must update all displays to show dynamic prices
- Revenue optimization needs business logic thinking
- Must recalculate efficiencies after each purchase

**Menu integration:** Add option "9. Revenue Analysis" showing optimization report.

---

## Case-Sensitive Requirements

For automated testing, use **exact spelling** (case-sensitive):

**Event Types:**
- "Concert" (not "concert" or "CONCERT")
- "Conference" (not "conference")
- "Workshop" (not "workshop")
- "Festival" (not "festival")

**Ticket Categories:**
- "VIP" (not "vip" or "Vip")
- "Standard" (not "standard")
- "Student" (not "student")

**Days of Week (for weekend checking):**
- Use `java.time.DayOfWeek.SATURDAY` and `java.time.DayOfWeek.SUNDAY`
- Or compare with "Saturday" and "Sunday" strings if using string logic

**Required Class Names:**
- Event
- Ticket
- Attendee
- EventManager
- EventManagementException

**Required Method Names (in EventManager):**
- addEvent
- removeEvent
- getEvent
- purchaseTicket
- cancelTicket
- sortEventsByDate
- sortEventsByPrice
- sortEventsByCapacity
- getEventsInDateRange
- getAvailableEvents
- getEventsByType
- getTotalAttendees
- getEventRevenue
- getTotalRevenue

---

## Submission Structure

Submit a folder named `S_XX` (replace XX with your student number) with the following **exact structure**:

```
S_XX/
├── Main.java
└── eventmanagement/
    ├── model/
    │   ├── Event.java
    │   ├── Ticket.java
    │   └── Attendee.java
    ├── manager/
    │   └── EventManager.java
    └── exception/
        └── EventManagementException.java
```

**Required structure:**
- `Main.java` in the root of S_XX (default package) - contains the interactive menu
- `eventmanagement.model` package - contains Event, Ticket, Attendee (domain models)
- `eventmanagement.manager` package - contains EventManager (management logic)
- `eventmanagement.exception` package - contains EventManagementException (custom exception)

**Your submission will not be testable if you deviate from this structure.**

---

## Checklist

Before submitting, verify:

**Compilation & Structure**
- [ ] Code compiles without errors
- [ ] All required classes exist with correct names
- [ ] All required methods exist with correct names
- [ ] Submission follows exact structure (Main.java in root, packages as specified)

**Functionality**
- [ ] Interactive menu displays and works
- [ ] Can add multiple events successfully
- [ ] All validation rules enforced (try invalid inputs)
- [ ] Can purchase tickets and see confirmation
- [ ] Email duplicates prevented per event
- [ ] All three sorting options work
- [ ] All three filtering options work
- [ ] Statistics display correctly

**User Experience**
- [ ] Menu loops until user chooses exit
- [ ] Invalid menu choices handled gracefully
- [ ] Clear success/error messages shown
- [ ] Event listings are readable and well-formatted

**Optional**
- [ ] Bonus feature fully implemented and functional

---

## Evaluation

Your submission will be evaluated as follows:

1. **Automated Structure Test**
   - Compilation check
   - Required classes verification
   - Required methods verification

2. **Manual Functionality Review**
   - Run your program
   - Test menu options
   - Verify validation rules
   - Check sorting and filtering
   - Test edge cases

3. **Code Quality Assessment**
   - Appropriate use of collections
   - Proper exception handling
   - Clean, readable code

4. **Oral Defense (if required)**
   - You may be asked to explain your implementation
   - Be prepared to walk through your code
   - Explain design decisions you made

**Students who cannot explain their implementation may receive reduced grades.**

---

Good luck!
