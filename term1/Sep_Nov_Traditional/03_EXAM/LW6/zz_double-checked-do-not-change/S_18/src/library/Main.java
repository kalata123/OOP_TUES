package library;

import library.materials.Book;
import library.transactions.BorrowingSystem;
import library.transactions.LibraryException;
import library.users.Member;
import library.util.LibrarySettings;
import library.util.SimpleDateUtils;

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
        Book book1 = new Book("Pod Igoto", "Ivan Vazov");
        Book book2 = new Book("Zhelezniqt Svetilnik", "Dimitar Talev", "DT-1001");

        // HINT: Create members using different constructors to show overloading  
        // Member 1: Use constructor with name and member ID
        // Member 2: Use constructor with only name (auto-generate ID)
        Member m1 = new Member("Jane Doe", "CMEM-150");
        Member m2 = new Member("Jane Doe");

        // HINT: Demonstrate encapsulation - try to access private fields directly
        // Then show how to properly use getters to access the data

        //System.out.println("Book1 author (wrong): " + book1.author);

        System.out.println("Book1 author (right): " + book1.getAuthor());

        // HINT: Show LibrarySettings usage
        // Print the maximum borrow limit constant
        // Generate some item IDs using the static method
        System.out.println("Maximum borrow limit: " + LibrarySettings.MAX_BORROW_LIMIT);
        System.out.println("Generated ID: " + LibrarySettings.generateItemId("TEST-"));
        System.out.println("Generated ID: " + LibrarySettings.generateItemId("TEST-"));
        System.out.println("Generated ID: " + LibrarySettings.generateItemId("TEST-"));

        // HINT: Demonstrate SimpleDateUtils method overloading
        // Call all three overloaded calculateDueDate methods with different parameters
        // Show the different results
        System.out.println(SimpleDateUtils.dueDate(5));
        System.out.println(SimpleDateUtils.dueDate("31.10"));
        System.out.println(SimpleDateUtils.dueDate(5, true));
        System.out.println(SimpleDateUtils.dueDate(8, false));

        // === PART C3: MEMBER + BORROWINGSYSTEM INTEGRATION ===
        // HINT: Create a BorrowingSystem instance and a Member instance
        // When you borrow an item through BorrowingSystem, ALSO call member.incrementBorrowed()
        // When you return an item through BorrowingSystem, ALSO call member.decrementBorrowed()
        // Show that both systems stay synchronized - the member count matches BorrowingSystem count
        BorrowingSystem bs = new BorrowingSystem();
        System.out.println("Test of borrowing system:");
        try {
            m1.canBorrow();
        } catch(LibraryException e) {
            System.out.println(e.getMessage());
        }
        bs.borrowItem(book1, "15.11");
        bs.borrowItem(book2, "10.11");
        m1.incrementBorrowCount();


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