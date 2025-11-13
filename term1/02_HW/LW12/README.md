# Homework 2: Event & Ticket Management System
## Complete Assignment and Grading Package

**Course:** OOP with Java - Term 1
**Topic:** Collections (ArrayList, HashMap, HashSet) and Interactive Console Applications
**Points:** 20 base + 5 bonus = 25 total
**Estimated Time:** 3-4 hours for base requirements

---

## 📚 Package Contents

This directory contains everything needed for Homework 2:

### For Students:
- **`LW12-hw2.md`** - Complete homework assignment (10 pages)
- **`EventSystemTest.java`** - Test template to validate implementation
- **`example_output.txt`** - Expected program output
- **`RUBRIC.md`** - Detailed grading rubric (12 pages)

### For Teachers:
- **`grading/`** - Automated grading system
  - `EventSystemAutoGrader.java` - Main auto-grader
  - `grade_all.sh` - Batch grading script
  - `reference_solution/eventmanagement/` - Teacher's reference implementation
  - `results/` - Generated reports (created during grading)
- **`submissions/`** - Student submissions go here (S_01, S_02, ... S_25)

---

## 🎯 Assignment Overview

Students create an **Interactive Event & Ticket Management System** - a working console application with menu-driven interface that demonstrates:
- **Collections:** ArrayList, HashMap, HashSet for dynamic data management
- **Interactive UI:** Scanner-based menu system with 8 options
- **Sorting:** Comparable and Comparator for multiple sort criteria
- **Filtering:** Date ranges, availability, event types
- **Validation:** Custom exceptions with specific edge cases
- **User Experience:** Clear prompts, error handling, formatted output

**Domain:** Concert/Conference event management with tickets and attendees.

**Key Features:**
- Real working application (not just classes)
- Professional user experience with formatted tables
- Specific constraints (capacity divisible by 10, prices ending in .00/.50, no weekends)
- Intentional complexity (weekend date validation, per-event email uniqueness)

---

## 👨‍🎓 For Students: Getting Started

### Step 1: Read the Assignment
```bash
# Open the main assignment document
open LW12-hw2.md
# or
cat LW12-hw2.md
```

### Step 2: Create Your Submission Folder
```bash
mkdir S_XX
cd S_XX
```
Replace `XX` with your student number (e.g., S_15).

### Step 3: Implement the Components
Create these classes (scoring shown):
- `Event` (part of 5 pts Event Management)
- `Ticket` (part of 4 pts Ticket System)
- `Attendee` (part of 4 pts Ticket System)
- `EventManager` (covers Collections 3 pts, Sorting/Filtering 3 pts)
- `EventManagementException` (part of validation)
- `Main` or entry point for interactive menu (2 pts Interactive Menu)

### Step 4: Test Your Code
```bash
# Compile your code
javac eventmanagement/*.java

# Copy test file to your directory
cp ../EventSystemTest.java .

# Run the test
javac EventSystemTest.java
java EventSystemTest
```

Expected output matches `example_output.txt`.

### Step 5: Review Rubric
```bash
# Check grading criteria
open RUBRIC.md
```

Make sure you have:
- ✅ Code compiles without errors
- ✅ All required classes and methods exist
- ✅ Interactive menu displays and works
- ✅ All validation rules enforced (capacity ÷ 10, price .00/.50, no weekends)
- ✅ All sorting and filtering operations work
- ✅ Good user experience (clear prompts, error messages, formatting)

### Step 6: Submit
Your submission folder **must** follow this exact structure:
```bash
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

**This structure is required for your submission to be testable.**

---

## 👨‍🏫 For Teachers: Grading System

### Overview

Grading is **primarily manual** following the rubric, with automated assistance for structure checking.

**Grading Process:**
1. **Automated Structure Test** - Run EventSystemTest.java to verify compilation and required classes/methods
2. **Manual Functionality Review** - Run the student's program and test all features
3. **Score Recording** - Use EventSystemGradingHelper to record scores
4. **CSV Generation** - Automatically creates CSV compatible with dashboard

### Quick Grading (Single Student)

```bash
cd grading

# Compile the grading helper
javac EventSystemGradingHelper.java

# Run grading for a student
java EventSystemGradingHelper ../submissions/S_15

# The helper will:
# 1. Run automated structure tests
# 2. Guide you through manual testing
# 3. Prompt you to enter scores for each rubric component
# 4. Generate results/S_15_report.csv
```

### Batch Grading (All Students)

For grading multiple students, run the helper for each:
```bash
for student in ../submissions/S_*; do
    java EventSystemGradingHelper "$student"
done
```

Then combine the CSV files:
```bash
cat results/*_report.csv > results/homework2_results.csv
```

### Grading Components

#### 1. **Automated Structure Testing (3 points max)**
- Compilation check (pass/fail)
- Required classes exist: Event, Ticket, Attendee, EventManager, EventManagementException
- Required methods exist in EventManager (14 methods)
- Automatic scoring: 3 if all pass, 2 if minor issues, 0 if fails

#### 2. **Manual Functionality Review (17 points)**
Teacher runs the program and evaluates:

**Event Management (5 points):**
- Validation rules enforced (capacity ÷ 10, price .00/.50, no weekends)
- Edge cases handled (past dates, duplicate names)
- Clear error messages

**Ticket System (4 points):**
- Purchase works with validations
- Email uniqueness per event enforced
- Cancellation works
- Collections updated correctly

**Collections Usage (3 points):**
- Appropriate collection types (ArrayList, HashMap, HashSet)
- No fixed-size arrays
- Efficient operations

**Sorting & Filtering (3 points):**
- All 3 sorts work (date, price, capacity)
- All 3 filters work (type, availability, date range)

**Interactive Menu (2 points):**
- Menu displays clearly
- Loops until exit
- Handles invalid input
- Good user experience

#### 3. **Bonus Features (5 points max)**
Choose ONE to implement fully:
- Smart Seat Allocation Algorithm (5 pts)
- Dynamic Pricing Algorithm (5 pts)

#### 4. **CSV Generation**
Outputs format compatible with Student Performance Analytics Dashboard:
```csv
Assignment;Type;Student;Compilation;EventMgmt;TicketSys;Collections;SortFilter;Menu;BaseScore;MaxBaseScore;Bonus;TotalScore;Percentage;Comments
```

### Directory Structure

```
LW12/
├── LW12-hw2.md                   # Main assignment
├── RUBRIC.md                     # Grading rubric
├── EventSystemTest.java          # Student test template
├── example_output.txt            # Expected output
├── README.md                     # This file
│
├── submissions/                  # Student submissions
│   ├── S_01/
│   │   ├── Main.java
│   │   └── eventmanagement/
│   │       ├── model/
│   │       │   ├── Event.java
│   │       │   ├── Ticket.java
│   │       │   └── Attendee.java
│   │       ├── manager/
│   │       │   └── EventManager.java
│   │       └── exception/
│   │           └── EventManagementException.java
│   ├── S_02/
│   ├── ...
│   └── S_25/
│
└── grading/                      # Grading system
    ├── EventSystemGradingHelper.java # Interactive grading helper
    ├── reference_solution/
    │   ├── Main.java                 # Interactive menu implementation
    │   └── eventmanagement/
    │       ├── model/
    │       │   ├── Event.java
    │       │   ├── Ticket.java
    │       │   └── Attendee.java
    │       ├── manager/
    │       │   └── EventManager.java
    │       └── exception/
    │           └── EventManagementException.java
    └── results/                      # Generated during grading
        ├── homework2_results.csv     # Final CSV for all students
        ├── S_01_report.csv           # Individual reports
        └── ...
```

---

## 📊 Importing Results to Analytics Dashboard

### Step 1: Open Dashboard
```bash
cd ../../../../2025-2026/Dashboard
./start-server.sh
```

Then open: `http://localhost:8000/student-performance-analytics-v5.0.html`

### Step 2: Import CSV
1. Click **"📁 Качи CSV"** button
2. Select `LW12/grading/results/homework2_results.csv`
3. Data loads automatically

### Step 3: View Analytics
- **Overview:** Class statistics and grade distribution
- **Students:** Individual performance with trend charts
- **Task Details:** Click homework to see per-student breakdown

---

## 🔍 Defense Interview Process

### When Required
Students flagged in `DEFENSE_REQUIRED.txt` must be interviewed if:
- High score (≥95%) but insufficient comments (<3)
- Generic variable names detected
- Comments don't match code behavior
- Edge case failures suggest AI usage

### Interview Format
1. **Show student their code** (no advance warning)
2. **Ask 3-5 specific questions:**
   - "Walk me through your purchaseTicket() method. What happens step by step?"
   - "Why did you choose HashMap for ticketCatalog instead of ArrayList?"
   - "Explain this validation check. Why is it necessary?"
   - "This edge case is handled here. Explain why it works."
3. **Assess understanding:**
   - Can explain clearly → Keep auto-grade score
   - Cannot explain → 0 points for assignment

### Sample Questions (from RUBRIC.md)
- Collections choice rationale
- Integration logic walkthrough
- Validation necessity explanations
- Edge case handling
- Comparable vs Comparator decision

### Documentation
Record interview outcomes in `results/defense_outcomes.txt`:
```
S_03 - PASS - Could explain all code, kept score (18.5/20)
S_07 - FAIL - Could not explain purchaseTicket logic, score → 0
S_15 - PASS - Explained design decisions clearly, kept score (19/20)
```

Update CSV after defense:
```bash
# Edit homework2_results.csv manually
# Change scores for students who failed defense to 0
# Re-import to dashboard
```

---

## 🛠️ Troubleshooting

### Students Report: "My code doesn't compile"
**Common Issues:**
1. Missing package declarations: `package eventmanagement.model;` for Event, Ticket, Attendee
2. Wrong imports: Use `java.time.LocalDate` not `java.util.Date`
3. Public fields instead of private
4. Missing semicolons or braces
5. Incorrect package structure (files not in the right subdirectories)

**Solution:** Run compilation test:
```bash
javac eventmanagement/model/*.java eventmanagement/manager/*.java eventmanagement/exception/*.java Main.java 2>&1 | head -20
```

### Auto-Grader Fails to Run
**Issue:** Permission denied on grade_all.sh

**Solution:**
```bash
chmod +x grading/grade_all.sh
```

**Issue:** Java not found

**Solution:**
```bash
# Check Java version
java -version  # Should be Java 8+

# If not installed
# macOS: brew install openjdk
# Ubuntu: sudo apt install default-jdk
```

### CSV Import Fails in Dashboard
**Issue:** Delimiter mismatch

**Solution:** Verify CSV uses semicolon (`;`) as delimiter:
```bash
head -1 grading/results/homework2_results.csv
```

Should show:
```
Assignment;Type;Student;Compilation;PartA;PartB;...
```

**Issue:** File not found

**Solution:** Run dashboard from correct directory:
```bash
cd TUES/2025-2026/Dashboard
./start-server.sh
```

### Grading Takes Too Long
**Issue:** 25 students × 30 seconds = 12+ minutes

**Solution:** Grade in parallel (requires modification):
```bash
# Split students into batches
# Run multiple grade_all.sh instances on different ranges
# Merge CSV files afterward
```

---

## 📈 Grade Interpretation

### Grade Thresholds
| Grade | Bulgarian | Percentage | Typical Profile |
|-------|-----------|------------|----------------|
| 6 | Отличен | 90%+ | Full base + significant bonus, excellent comments |
| 5 | Мн. добър | 75-89% | Strong base (16-18/20) + some bonus OR full base |
| 4 | Добър | 65-74% | Most features work (13-15/20), minor issues |
| 3 | Среден | 55-64% | Basic functionality (11-13/20), incomplete features |
| 2 | Слаб | <55% | Significant gaps, many features missing |

### Score Breakdown Examples

**Example 1: Excellent (Grade 6)**
- Compilation & Structure: 3/3 (all classes and methods present)
- Event Management: 5/5 (all validations work, edge cases handled)
- Ticket System: 4/4 (full functionality, collections consistent)
- Collections Usage: 3/3 (appropriate types, efficient operations)
- Sorting & Filtering: 3/3 (all 3 sorts and filters work)
- Interactive Menu: 2/2 (excellent UX, handles errors)
- **Base: 20/20** (100%)
- Bonus: +5 (Dynamic Pricing fully implemented)
- **Total: 25/20** (125%) → Grade 6

**Example 2: Very Good (Grade 5)**
- Compilation & Structure: 3/3
- Event Management: 4/5 (weekend check missing)
- Ticket System: 4/4
- Collections Usage: 3/3
- Sorting & Filtering: 3/3
- Interactive Menu: 2/2
- **Base: 19/20** (95%)
- Bonus: 0
- **Total: 19/20** (95%) → Grade 5 or 6

**Example 3: Good (Grade 4)**
- Compilation & Structure: 3/3
- Event Management: 3/5 (several validations missing)
- Ticket System: 2/4 (no duplicate prevention)
- Collections Usage: 2/3 (poor collection choices)
- Sorting & Filtering: 2/3 (1 sort broken)
- Interactive Menu: 1/2 (doesn't loop well)
- **Base: 13/20** (65%)
- Bonus: 0
- **Total: 13/20** (65%) → Grade 4

---

## 🎓 Learning Outcomes

By the end of this homework, students should be able to:

✅ **Use Java Collections Framework:**
- ArrayList for dynamic lists
- HashMap for key-value storage
- HashSet for unique elements
- Understand when to use each type

✅ **Build Interactive Console Applications:**
- Scanner-based user input handling
- Menu-driven program flow
- Error handling for invalid input
- Professional output formatting

✅ **Apply Sorting and Filtering:**
- Implement Comparable for natural ordering
- Use Comparator for custom sorting
- Filter collections based on criteria

✅ **Handle Exceptions Properly:**
- Create custom checked exceptions
- Throw exceptions with meaningful messages
- Validate input thoroughly

✅ **Design User Experience:**
- Clear prompts and error messages
- Readable output (tables, formatting)
- Graceful error handling
- Intuitive menu navigation

---

## 📝 Frequently Asked Questions

### For Students:

**Q: Can I use AI tools like ChatGPT or GitHub Copilot?**
A: Yes, for learning and understanding, but you must:
- Understand every line of code
- Be able to explain your implementation during defense
- Write your own design decision comments
- If you can't explain it, you'll receive 0 points

**Q: How strict is the grading on edge cases?**
A: Very strict. The auto-grader tests specific constraints:
- Capacity divisible by 10
- Prices ending in .00 or .50
- No weekend dates
- Exact exception messages

**Q: Can I use libraries beyond java.time and java.util?**
A: No. Use only:
- `java.time.LocalDate`
- `java.util.ArrayList`, `HashMap`, `HashSet`
- `java.util.Collections`, `Comparator`
- `java.util.Scanner` for input
- Standard Java language features

**Q: What packages do I need to create?**
A: You must create three packages:
- `eventmanagement.model` - for Event, Ticket, Attendee
- `eventmanagement.manager` - for EventManager
- `eventmanagement.exception` - for EventManagementException
Main.java goes in the default package (root folder).

**Q: What if EventSystemTest.java doesn't compile with my code?**
A: Your method signatures or class names are probably wrong. Check:
- Class names match exactly: Event, Ticket, Attendee, EventManager, EventManagementException
- Method names in EventManager match exactly (14 required methods)
- Compile your code first, then the test

### For Teachers:

**Q: How long does grading take?**
A: Approximately 5-10 minutes per student:
- 1 min: Automated structure test
- 5-8 min: Manual functionality review (run their program, test all features)
- 1 min: Record scores in grading helper

For 25 students: 2-4 hours total.

**Q: Can I modify scores after recording them?**
A: Yes, edit the CSV files manually and re-import to dashboard. Common adjustments:
- Adjust points after oral defense
- Award partial credit for attempted features
- Deduct for late submissions

**Q: How do I test the reference solution?**
A: Run the reference solution to see expected behavior:
```bash
cd grading/reference_solution
javac eventmanagement/model/*.java eventmanagement/manager/*.java eventmanagement/exception/*.java Main.java
java Main
```

This shows what a perfect implementation looks like.

**Q: What if a student's code doesn't compile?**
A: Score is automatically 0 if code doesn't compile. Advise student to fix compilation errors and resubmit if policy allows.

---

## 🔄 Version History

- **v2.0** (Dec 2024) - Major restructuring
  - Interactive console application focus (menu-driven interface)
  - Simplified grading: manual review with automated structure test
  - New rubric: 20 base + 5 bonus (whole numbers only)
  - Two hard bonus options: Smart Seat Allocation OR Dynamic Pricing
  - No package requirements, no comment requirements
  - EventSystemGradingHelper for manual grading assistance

- **v1.0** (Dec 2024) - Initial release
  - Event & Ticket Management domain
  - Complex auto-grader with partial credit
  - 20 base + 20 bonus points
  - Required comments and package structure

---

## 📧 Support

For issues or questions:
1. Check this README thoroughly
2. Review RUBRIC.md for grading details
3. Examine reference solution for correct implementation
4. Check auto-grader logs for specific errors

---

## 📜 License & Usage

This homework assignment and grading system is provided for educational use in the OOP TUES course.

**Teachers:** Feel free to adapt for your courses, but please:
- Credit the original structure
- Maintain anti-AI detection features
- Keep the comprehensive grading approach

**Students:** This is original coursework. Copying violates academic integrity.

---

**Good luck, and happy coding!** 🚀
