package library;

/**
 * LIBRARY MANAGEMENT SYSTEM - DEMONSTRATION GUIDE
 *
 * Use this Main class to test your implementation.
 * Follow the hints below to demonstrate all required features.
 *
 * REMEMBER: This file contains ONLY hints - you must write the actual code!
 */
import library.materials.Book;
import library.users.Member;
import library.util.LibrarySettings;
import library.util.SimpleDateUtils;
import library.transactions.BorrowingSystem;
import library.core.LibraryItem;
import library.transactions.LibraryException;


public class Main {

    public static void main(String[] args) {

        // === DEMONSTRATION HINTS ===
        // Implement the code below each comment to show your system working

        System.out.println("=== LIBRARY MANAGEMENT SYSTEM DEMONSTRATION ===");

        // HINT: Create books using different constructors to show overloading
        // Book 1: Use constructor with title, author, and ID
        // Book 2: Use constructor with only title and author (auto-generate ID)

        Book b1 = new Book("The Hobit", "B001", "J.R.R. Tolkien");
        Book b2 = new Book("1984", "George Orwell");

        // HINT: Create members using different constructors to show overloading
        // Member 1: Use constructor with name and member ID
        // Member 2: Use constructor with only name (auto-generate ID)

        Member m1 = new Member("Alice", "M001");
        Member m2 = new Member("Bob", memberId);

        // HINT: Demonstrate encapsulation - try to access private fields directly
        // Then show how to properly use getters to access the data

        // HINT: Show LibrarySettings usage
        // Print the maximum borrow limit constant
        // Generate some item IDs using the static method

        System.out.println("Max Borrow Limit: " + LibrarySettings.MAX_BORROW_LIMIT);
        String generatedId1 = LibrarySettings.generateItemId("BK");
        String generatedId2 = LibrarySettings.generateItemId("MB");
        System.out.println("Generated Item ID 1: " + generatedId1);
        System.out.println("Generated Item ID 2: " + generatedId2);

        // HINT: Demonstrate SimpleDateUtils method overloading
        // Call all three overloaded calculateDueDate methods with different parameters
        // Show the different results

        String dueDate1 = SimpleDateUtils.calculateDueDate();
        String dueDate2 = SimpleDateUtils.calculateDueDate(21);
        String dueDate3 = SimpleDateUtils.calculateDueDate("2025-10-12", 10);
        System.out.println("Due Date 1: " + dueDate1);
        System.out.println("Due Date 2: " + dueDate2);
        System.out.println("Due Date 3: " + dueDate3);

        // === PART C3: MEMBER + BORROWINGSYSTEM INTEGRATION ===
        // HINT: Create a BorrowingSystem instance and a Member instance
        // When you borrow an item through BorrowingSystem, ALSO call member.incrementBorrowed()
        // When you return an item through BorrowingSystem, ALSO call member.decrementBorrowed()
        // Show that both systems stay synchronized - the member count matches BorrowingSystem count

        // HINT: Test borrowing up to the maximum limit
        // Show what happens when trying to borrow beyond the limit

        BorrowingSystem borrowingSystem = new BorrowingSystem();
        try {
            borrowingSystem.borrowItem(b1, dueDate1);
            m1.incrementBorrowed();
            System.out.println("Borrowed: " + b1.getTitle() + " Member Borrowed Count: " + m1.getBorrowedCount());

            borrowingSystem.borrowItem(b2, dueDate2);
            m1.incrementBorrowed();
            System.out.println("Borrowed: " + b2.getTitle() + " Member Borrowed Count: " + m1.getBorrowedCount());

            for (int i = 0; i < LibrarySettings.MAX_BORROW_LIMIT; i++) {
                Book extraBook = new Book("Extra Book " + (i + 1), "Author " + (i + 1));
                borrowingSystem.borrowItem(extraBook, SimpleDateUtils.calculateDueDate());
                m1.incrementBorrowed();
                System.out.println("Borrowed: " + extraBook.getTitle() + " Member Borrowed Count: " + m1.getBorrowedCount());
            }
        } catch (LibraryException e) {
            System.out.println("Library Exception: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.out.println("Illegal State: " + e.getMessage());
        }

        // === PART D: ERROR HANDLING ===
        // HINT: Demonstrate input validation in constructors
        // Try to create a Member with blank name - catch IllegalArgumentException
        // Try to create a Book with blank author - catch IllegalArgumentException

        try {
            Member invalidMember = new Member("", "M002");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        try {
            Book invalidBook = new Book("Invisible Man", "");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // HINT: Demonstrate business logic errors
        // Try to borrow an item when BorrowingSystem is full - catch LibraryException
        // Try to return an item that doesn't exist - handle the return value appropriately

        try {
            Book newBook = new Book("Kniga", "avtor X");
            borrowingSystem.borrowItem(extraBook, SimpleDateUtils.calculateDueDate());
        } catch (LibraryException e) {
            System.out.println(e.getMessage());
        }

        // HINT: Demonstrate proper exception handling structure
        // Use try-catch blocks with specific exception types
        // Use a finally block to show cleanup code
        // Show multiple catch blocks in correct order (specific before general)

        // HINT: Test BorrowingSystem with invalid parameters
        // Try to call borrowItem with null item parameter
        // Try to call borrowItem with null dueDate parameter

        try {
            borrowingSystem.borrowItem(null, dueDate1);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // HINT: Show custom LibraryException usage
        // Create and throw a LibraryException with a meaningful message
        // Catch it and display the message

        // HINT: Demonstrate polymorphism
        // Create an array of LibraryItem that contains different types of items
        // Loop through and call getDisplayInfo() on each one
        LibraryItem[] items = new LibraryItem[2];
        items[0] = b1;
        items[1] = b2;
        for (LibraryItem item : items) {
            System.out.println(item.getDisplayInfo());
        }

        // OPTIONAL: If you implemented bonus features, demonstrate them here
        // Show search functionality
        // Show usage statistics

        System.out.println("=== DEMONSTRATION COMPLETE ===");

        // HINT: Make sure your program runs from start to finish without crashing
        // Handle all exceptions gracefully
        // Show clear output so we can see what's happening at each step
    }
}