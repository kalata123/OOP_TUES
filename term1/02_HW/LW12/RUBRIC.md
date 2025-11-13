# Homework 2: Event & Ticket Management System
## Grading Rubric

**Total Points:** 20 base + 5 bonus maximum

---

## Base Requirements (20 Points)

### Compilation & Structure (3 points)

**What's Being Evaluated:**
- **Practice:** Writing code that compiles cleanly
- **Practice:** Following naming conventions for classes and methods
- **Thinking:** Organizing code in a logical structure

**Scoring:**

**3 points:**
- Code compiles without errors or warnings
- All required classes exist: Event, Ticket, Attendee, EventManager, EventManagementException
- All required methods exist with correct names in EventManager
- Logical file/package organization

**2 points:**
- Code compiles with minor warnings
- Most required classes and methods present
- Some naming inconsistencies

**1 point:**
- Code compiles but several required components missing
- Poor organization

**0 points:**
- Code does not compile

---

### Event Management (5 points)

**What's Being Evaluated:**
- **Practice:** Input validation at object construction
- **Practice:** Enforcing business rules (capacity divisible by 10, price ends in .00/.50)
- **Practice:** Working with LocalDate for date logic
- **Thinking:** Understanding WHY constraints exist (business requirements)
- **Thinking:** Handling edge cases (weekend dates, past dates, capacity limits)
- **Thinking:** Choosing appropriate data types for fields

**Scoring:**

**5 points:**
- All validation rules enforced correctly
- Edge cases handled (weekends, past dates, capacity constraints)
- Clear, specific error messages via EventManagementException
- Events can be created, stored, retrieved, and removed
- Duplicate name detection works (case-insensitive)

**4 points:**
- Most validations work
- Minor edge case issues (e.g., weekend check missing)
- Event CRUD operations mostly functional

**3 points:**
- Basic validation present
- Several rules not enforced (e.g., capacity or price format not checked)
- Core functionality works but incomplete

**2 points:**
- Minimal validation
- Many business rules ignored
- Events can be created but with major gaps

**1 point:**
- Barely functional event creation
- Most validations missing

**0 points:**
- Event management completely non-functional

---

### Ticket System (4 points)

**What's Being Evaluated:**
- **Practice:** Managing relationships between ticket, event, and attendee
- **Practice:** Updating multiple collections consistently (transaction-like operations)
- **Practice:** Preventing duplicate registrations per event
- **Thinking:** Understanding data integrity and consistency
- **Thinking:** Designing for referential integrity (what happens when ticket cancelled?)

**Scoring:**

**4 points:**
- Ticket purchase works correctly with all validations
- Email uniqueness enforced per event (one email per event)
- All affected collections updated (tickets, attendees, event capacity)
- Ticket cancellation works and reverts all changes
- Collections remain consistent after operations

**3 points:**
- Basic purchase functionality works
- Minor issues with cancellation or collection updates
- Mostly maintains consistency

**2 points:**
- Purchase works but doesn't prevent duplicate emails
- Doesn't update all affected collections
- Cancellation missing or buggy

**1 point:**
- Basic ticket creation only
- No duplicate prevention
- Collections not updated properly

**0 points:**
- Ticket system non-functional

---

### Collections Usage (3 points)

**What's Being Evaluated:**
- **Practice:** Using ArrayList, HashMap, or HashSet from java.util
- **Practice:** Dynamic sizing (no fixed-size arrays)
- **Thinking:** Choosing the right collection for the task
  - List for ordered sequences
  - Map for key-value lookups
  - Set for uniqueness constraints
- **Thinking:** Understanding performance implications (O(1) vs O(n) operations)

**Scoring:**

**3 points:**
- Appropriate collections used for each purpose
- Dynamic sizing throughout (no arrays)
- Efficient operations (e.g., Map for lookups, Set for uniqueness)
- Shows understanding of collection choice rationale

**2 points:**
- Uses collections but choices questionable
  - Example: ArrayList for frequent lookups by ID (should use HashMap)
  - Example: Manual duplicate checking instead of Set
- Dynamic sizing used

**1 point:**
- Uses some collections but inefficiently
- Linear searches everywhere
- Poor choice of collection types

**0 points:**
- Uses arrays instead of collections
- Doesn't use java.util collections framework

---

### Sorting & Filtering (3 points)

**What's Being Evaluated:**
- **Practice:** Implementing Comparable interface OR using Comparator
- **Practice:** Using Collections.sort() or list.sort()
- **Practice:** Iterating collections with filtering conditions
- **Thinking:** Understanding sort criteria (ascending vs descending, by which field)
- **Thinking:** Designing filter logic (date ranges, boolean conditions, type matching)
- **Thinking:** Returning new collections vs modifying existing

**Scoring:**

**3 points:**
- All three sorting methods work correctly:
  - sortEventsByDate (ascending)
  - sortEventsByPrice (ascending)
  - sortEventsByCapacity (descending)
- All three filter methods return correct results:
  - getEventsInDateRange (inclusive range)
  - getAvailableEvents (has capacity)
  - getEventsByType (exact match)

**2 points:**
- 2 out of 3 sorts work
- 2 out of 3 filters work
- Minor logic errors in one method

**1 point:**
- 1 out of 3 sorts work
- 1 out of 3 filters work
- Significant logic issues

**0 points:**
- Sorting and filtering non-functional or not implemented

---

### Interactive Menu (2 points)

**What's Being Evaluated:**
- **Practice:** Reading user input with Scanner
- **Practice:** Implementing menu loop (repeat until exit)
- **Practice:** Exception handling for invalid input (InputMismatchException, etc.)
- **Thinking:** User experience design (clear prompts, readable output)
- **Thinking:** Error message clarity and helpfulness

**Scoring:**

**2 points:**
- Menu displays clearly and is easy to understand
- Loops correctly until user chooses exit
- Handles invalid input gracefully (non-numeric, out-of-range)
- Shows operation results (success messages, error messages, data display)
- User-friendly formatting (tables, separators, labels)

**1 point:**
- Menu works but poor user experience:
  - Doesn't loop properly
  - Crashes on invalid input
  - Unclear messages
  - Hard to read output
- Basic functionality present

**0 points:**
- No menu implemented
- Menu completely non-functional

---

## Bonus Features (5 Points Maximum)

Students choose ONE bonus to implement. **No partial credit** - bonus must be fully functional to receive points.

### Option A: Smart Seat Allocation Algorithm (5 points)

**What's Being Evaluated:**
- **Thinking:** Algorithm design for constraint satisfaction
- **Thinking:** Handling edge cases (section full, group booking)
- **Thinking:** Maintaining data structure for seat tracking
- **Practice:** Integrating algorithm with existing ticket system
- **Practice:** Displaying seating information clearly

**Requirements:**
- Events divided into sections (Front 30%, Middle 50%, Back 20%)
- VIP → Front, Standard → Middle, Student → Back
- Section full → overflow to next best
- Group bookings kept together
- Seat assignments shown (e.g., "Front-A12")
- Menu option to view seating chart

**Scoring:**
- **5 points:** Fully functional. All edge cases handled. Well integrated. Seating chart clear.
- **0 points:** Not implemented or doesn't work correctly.

---

### Option B: Dynamic Pricing Algorithm (5 points total)

**Part 1: Demand-Based Pricing (3 points)**

**What's Being Evaluated:**
- **Thinking:** Implementing tiered pricing logic
- **Thinking:** Real-time recalculation on state changes
- **Practice:** Updating displays to show current prices
- **Practice:** Maintaining base price vs current price distinction

**Requirements:**
- 0-30% full: base price
- 30-70% full: base price × 1.2
- 70-90% full: base price × 1.4
- 90-100% full: base price × 1.6
- Recalculate on each purchase
- Show current price in listings

**Part 2: Revenue Optimization Report (2 points)**

**What's Being Evaluated:**
- **Thinking:** Business logic (revenue efficiency calculation)
- **Thinking:** Optimization suggestions based on metrics
- **Practice:** Generating formatted report

**Requirements:**
- Calculate: Revenue Efficiency = (Current Revenue / Max Possible) × 100%
- Identify most/least profitable events
- Suggest adjustments for underperforming events
- Menu option for revenue analysis

**Scoring:**
- **5 points (both parts):** Both parts fully functional and integrated.
- **0 points:** Not implemented or incomplete.

---

## Grade Calculation

**Base Score (out of 20):**
- Compilation & Structure: /3
- Event Management: /5
- Ticket System: /4
- Collections Usage: /3
- Sorting & Filtering: /3
- Interactive Menu: /2
- **Total Base: /20**

**Bonus (out of 5):**
- Smart Seat Allocation OR Dynamic Pricing: /5
- **Total Bonus: /5**

**Final Score:** Base + Bonus (capped at 25 total)

**Percentage:** (Final Score / 20) × 100%

**Grade Mapping:**
| Percentage | Grade |
|------------|-------|
| < 55% | 2 (Слаб) |
| 55-64% | 3 (Среден) |
| 65-74% | 4 (Добър) |
| 75-89% | 5 (Мн. добър) |
| 90%+ | 6 (Отличен) |

---

## Evaluation Process

### 1. Automated Structure Test
- Run EventSystemTest.java
- Verifies: compilation, required classes, required methods
- Pass/fail result

### 2. Manual Functionality Review
Teacher runs your program and tests:
- Menu navigation
- Event creation with validation
- Ticket purchase and cancellation
- Sorting operations (all 3)
- Filtering operations (all 3)
- Statistics display
- Error handling (invalid inputs)
- Edge cases (sold out, duplicates, weekends)

### 3. Code Review
Teacher examines source code for:
- Appropriate collection choices
- Proper exception handling
- Code organization and readability
- No use of fixed-size arrays

### 4. Oral Defense (if required)
You may be asked to:
- Explain your design choices
- Walk through specific code sections
- Justify collection type choices
- Explain algorithm logic (especially for bonus)

**Students who cannot adequately explain their implementation may receive reduced grades.**

---

## Common Mistakes to Avoid

**Compilation & Structure:**
- ❌ Using lowercase for class names (event instead of Event)
- ❌ Misspelling required method names
- ❌ Forgetting EventManagementException class

**Event Management:**
- ❌ Not checking capacity divisible by 10
- ❌ Allowing prices like 49.99 instead of 49.50 or 50.00
- ❌ Not validating weekend dates
- ❌ Allowing duplicate event names

**Ticket System:**
- ❌ Not preventing duplicate email per event
- ❌ Not updating event capacity when ticket purchased
- ❌ Not removing attendee when all tickets cancelled
- ❌ Collections getting out of sync

**Collections:**
- ❌ Using arrays: `Event[] events = new Event[100]`
- ❌ Using ArrayList for ID lookups (should use HashMap)
- ❌ Not using Set for uniqueness constraints

**Sorting & Filtering:**
- ❌ Sorting in wrong direction (descending instead of ascending)
- ❌ Filter returning all events instead of filtered subset
- ❌ Modifying original collection instead of returning new one

**Interactive Menu:**
- ❌ Menu doesn't loop (exits after one operation)
- ❌ Crashes on non-numeric input
- ❌ No success/error messages
- ❌ Unreadable output formatting

---

## Example Scoring Scenarios

### Scenario 1: Strong Implementation (Grade 6)
- Compilation & Structure: 3/3
- Event Management: 5/5
- Ticket System: 4/4
- Collections: 3/3
- Sorting & Filtering: 3/3
- Interactive Menu: 2/2
- **Base: 20/20 (100%)**
- Bonus: 5/5 (Dynamic Pricing)
- **Total: 25** (125% → Grade 6)

### Scenario 2: Good Implementation (Grade 5)
- Compilation & Structure: 3/3
- Event Management: 4/5 (weekend check missing)
- Ticket System: 3/4 (cancellation buggy)
- Collections: 3/3
- Sorting & Filtering: 3/3
- Interactive Menu: 2/2
- **Base: 18/20 (90%)**
- Bonus: 0/5
- **Total: 18** (90% → Grade 5)

### Scenario 3: Adequate Implementation (Grade 4)
- Compilation & Structure: 3/3
- Event Management: 3/5 (several validations missing)
- Ticket System: 2/4 (no duplicate prevention)
- Collections: 2/3 (poor choices)
- Sorting & Filtering: 2/3 (1 sort broken)
- Interactive Menu: 1/2 (doesn't loop well)
- **Base: 13/20 (65%)**
- Bonus: 0/5
- **Total: 13** (65% → Grade 4)

### Scenario 4: Minimal Implementation (Grade 3)
- Compilation & Structure: 2/3 (warnings, missing methods)
- Event Management: 2/5 (minimal validation)
- Ticket System: 2/4 (basic purchase only)
- Collections: 1/3 (uses some arrays)
- Sorting & Filtering: 1/3 (most don't work)
- Interactive Menu: 1/2 (crashes often)
- **Base: 9/20 (45%)**
- Bonus: 0/5
- **Total: 9** (45% → Grade 2)

---

## Academic Integrity

**Acceptable:**
- Consulting Java documentation
- Looking up syntax examples
- Discussing concepts with classmates
- Using AI tools to understand concepts

**Not Acceptable:**
- Copying code from classmates
- Submitting AI-generated code you don't understand
- Using complete solutions from online sources
- Having someone else write your code

**During Defense:**
- You must explain any code you submit
- Inability to explain your code may result in grade reduction or zero
- Be prepared to modify code or add features on the spot

---

**Remember:** The goal is learning, not just getting points. Understanding the concepts will serve you in future courses and your career.

Good luck!
