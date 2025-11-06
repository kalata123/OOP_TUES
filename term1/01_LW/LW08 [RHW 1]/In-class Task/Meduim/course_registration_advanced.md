# University Course Registration System

**Time:** 140 minutes total (across two sessions)  
**Package name:** `university`  
**Difficulty:** Increased

---

## Scenario

You are building a course registration system for a university. The system manages students, courses, and enrollments. Students can enroll in courses, but there are rules about capacity, prerequisites, and course types.

---

## Requirements

### 1. Course Types

The system must handle three types of courses:

- **Lecture courses**: Have a maximum capacity and can have prerequisites
- **Lab courses**: Have a maximum capacity and MUST have a prerequisite lecture course
- **Seminar courses**: Have a maximum capacity but no prerequisites

### 2. Course Behavior

Each course must:
- Store course code, title, and capacity
- Track currently enrolled students
- Allow enrollment if capacity permits
- Provide enrollment count and available spots
- Generate a course roster (list of enrolled student names)

### 3. Student Behavior

Each student must:
- Store student ID and name
- Track enrolled courses
- Enroll in courses (if eligible)
- Drop courses
- Get a list of currently enrolled course codes

### 4. Enrollment Rules

- Students cannot enroll in a course if it's at capacity
- Students cannot enroll in the same course twice
- Lab courses require the student to be enrolled in the prerequisite lecture
- If a student drops a prerequisite lecture, they must also be dropped from dependent labs

### 5. Advanced Features

- A student can attempt to enroll in multiple courses at once
- The system should track total enrolled students across all courses
- Courses should be able to report their enrollment status

---

## What to Submit

- All your `.java` files in a folder with your student ID
- Your code must work with the provided `UniversitySystemTest.java` file
- Use package name: `university`

---

## Constraints

- You may create as many classes as you need
- Think about what behavior is shared among course types
- Consider bidirectional relationships (courses know their students, students know their courses)
- Use appropriate collections (ArrayList, HashMap, etc.)
- **Think first, code second** - a good design will make implementation much easier

---

## Test File (Provided)

```java
import university.*;

public class UniversitySystemTest {
    public static void main(String[] args) {
        System.out.println("=== University Registration System Test ===\n");
        
        // Create courses
        LectureCourse oop = new LectureCourse("CS201", "Object-Oriented Programming", 3);
        LabCourse oopLab = new LabCourse("CS201L", "OOP Lab", 2, oop);
        SeminarCourse softEng = new SeminarCourse("CS301", "Software Engineering Seminar", 2);
        
        // Create students
        Student alice = new Student("S001", "Alice");
        Student bob = new Student("S002", "Bob");
        Student charlie = new Student("S003", "Charlie");
        Student diana = new Student("S004", "Diana");
        
        // Test 1: Basic enrollment
        System.out.println("Test 1: Basic Enrollment");
        System.out.println("Alice enrolls in OOP: " + alice.enrollInCourse(oop));
        System.out.println("Bob enrolls in OOP: " + bob.enrollInCourse(oop));
        System.out.println("OOP enrollment: " + oop.getEnrolledCount() + "/" + oop.getCapacity());
        System.out.println();
        
        // Test 2: Capacity limit
        System.out.println("Test 2: Capacity Limit");
        System.out.println("Charlie enrolls in OOP: " + charlie.enrollInCourse(oop));
        System.out.println("Diana tries to enroll in OOP: " + diana.enrollInCourse(oop));
        System.out.println("OOP is full: " + oop.isFull());
        System.out.println();
        
        // Test 3: Lab prerequisite enforcement
        System.out.println("Test 3: Lab Prerequisites");
        System.out.println("Alice enrolls in OOP Lab: " + alice.enrollInCourse(oopLab));
        System.out.println("Diana tries OOP Lab without OOP: " + diana.enrollInCourse(oopLab));
        System.out.println();
        
        // Test 4: Seminar enrollment (no prerequisites)
        System.out.println("Test 4: Seminar Enrollment");
        System.out.println("Diana enrolls in Seminar: " + diana.enrollInCourse(softEng));
        System.out.println("Alice enrolls in Seminar: " + alice.enrollInCourse(softEng));
        System.out.println("Seminar is full: " + softEng.isFull());
        System.out.println();
        
        // Test 5: Student course list
        System.out.println("Test 5: Alice's Enrolled Courses");
        System.out.println(alice.getEnrolledCourses());
        System.out.println();
        
        // Test 6: Course roster
        System.out.println("Test 6: OOP Course Roster");
        System.out.println(oop.getRoster());
        System.out.println();
        
        // Test 7: Dropping courses with dependencies
        System.out.println("Test 7: Dropping Course with Dependencies");
        System.out.println("Alice drops OOP: " + alice.dropCourse(oop));
        System.out.println("Alice still in OOP Lab: " + oopLab.getEnrolledCount());
        System.out.println("Alice's courses after drop: " + alice.getEnrolledCourses());
        System.out.println();
        
        // Test 8: Re-enrollment after drop
        System.out.println("Test 8: Diana Enrolls in OOP (now has space)");
        System.out.println("Diana enrolls in OOP: " + diana.enrollInCourse(oop));
        System.out.println("OOP enrollment: " + oop.getEnrolledCount() + "/" + oop.getCapacity());
        System.out.println();
        
        // Test 9: Prevent duplicate enrollment
        System.out.println("Test 9: Duplicate Enrollment Prevention");
        System.out.println("Bob tries to enroll in OOP again: " + bob.enrollInCourse(oop));
    }
}
```

---

## Expected Output

```
=== University Registration System Test ===

Test 1: Basic Enrollment
Alice enrolls in OOP: true
Bob enrolls in OOP: true
OOP enrollment: 2/3

Test 2: Capacity Limit
Charlie enrolls in OOP: true
Diana tries to enroll in OOP: false
OOP is full: true

Test 3: Lab Prerequisites
Alice enrolls in OOP Lab: true
Diana tries OOP Lab without OOP: false

Test 4: Seminar Enrollment
Diana enrolls in Seminar: true
Alice enrolls in Seminar: true
Seminar is full: true

Test 5: Alice's Enrolled Courses
[CS201, CS201L, CS301]

Test 6: OOP Course Roster
CS201 - Object-Oriented Programming (3/3):
- Alice (S001)
- Bob (S002)
- Charlie (S003)

Test 7: Dropping Course with Dependencies
Alice drops OOP: true
Alice still in OOP Lab: 0
Alice's courses after drop: [CS301]

Test 8: Diana Enrolls in OOP (now has space)
Diana enrolls in OOP: true
OOP enrollment: 3/3

Test 9: Duplicate Enrollment Prevention
Bob tries to enroll in OOP again: false
```

---

## Hints for Success

**Think about:**
- Which class should be responsible for checking prerequisites?
- How do you prevent code duplication across different course types?
- How to handle a bidirectional relationship changes (student drops a course)?
- What collection types make sense for storing students in a course?
- How can you design this so adding a new course type would be easy?

**Good OOP design will make this task much shorter to implement!**