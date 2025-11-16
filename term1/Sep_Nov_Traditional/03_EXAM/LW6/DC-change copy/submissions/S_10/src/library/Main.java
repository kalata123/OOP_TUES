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
import library.transactions.BorrowingSystem;
import library.transactions.LibraryException;
import library.util.LibrarySettings;
import library.util.SimpleDateUtils;

public class Main {

    public static void main(String[] args) {

        // === DEMONSTRATION HINTS ===
        // Implement the code below each comment to show your system working

        System.out.println("=== LIBRARY MANAGEMENT SYSTEM DEMONSTRATION ===");

        // HINT: Create books using different constructors to show overloading
        // Book 1: Use constructor with title, author, and ID
        // Book 2: Use constructor with only title and author (auto-generate ID)
        Book book1 = new Book("Harry Potter", "J.K.Rolling", "SUICIDE-2727");
        Book book2 = new Book("All quiet on the Western front", "Енрих-Мария Ремарк");


        // HINT: Create members using different constructors to show overloading
        // Member 1: Use constructor with name and member ID
        // Member 2: Use constructor with only name (auto-generate ID)
        Member member1 = new Member("John Smith", "MEM-12");
        Member member2 = new Member("Tatyana Vasileva");

        // HINT: Demonstrate encapsulation - try to access private fields directly
        // Then show how to properly use getters to access the data
        //member1.name; - won't work
        System.out.println("Member1: " + member1.getName());
        System.out.println("Member2: " + member2.getName());
        System.out.println("Book1: " + book1.getDisplayInfo());
        System.out.println("Book2: " + book2.getDisplayInfo());

        // HINT: Show LibrarySettings usage
        // Print the maximum borrow limit constant
        // Generate some item IDs using the static method
        System.out.println("Max borrow limit: " + LibrarySettings.MAX_BORROW_LIMIT);
        System.out.println("Random ID1: " + LibrarySettings.generateItemId("KILLME"));
        System.out.println("Random ID2: " + LibrarySettings.generateItemId("KILLME"));

        // HINT: Demonstrate SimpleDateUtils method overloading
        // Call all three overloaded calculateDueDate methods with different parameters
        // Show the different results
        System.out.println(SimpleDateUtils.calculateDueDate(4));
        System.out.println(SimpleDateUtils.calculateDueDate("12-10-2025"));
        System.out.println(SimpleDateUtils.calculateDueDate(20, true));

        // === PART C3: MEMBER + BORROWINGSYSTEM INTEGRATION ===
        // HINT: Create a BorrowingSystem instance and a Member instance
        // When you borrow an item through BorrowingSystem, ALSO call member.incrementBorrowed()
        // When you return an item through BorrowingSystem, ALSO call member.decrementBorrowed()
        // Show that both systems stay synchronized - the member count matches BorrowingSystem count
        BorrowingSystem borrowingSystemMem1 = new BorrowingSystem();
        borrowingSystemMem1.borrowItem(book2, "12-10-2025");
        member1.incrementBorrowed();
        borrowingSystemMem1.returnItem(book1.getItemId());
        member1.decrementBorrowed();

        // HINT: Test borrowing up to the maximum limit
        // Show what happens when trying to borrow beyond the limit

        // === PART D: ERROR HANDLING ===
        // HINT: Demonstrate input validation in constructors
        // Try to create a Member with blank name - catch IllegalArgumentException
        // Try to create a Book with blank author - catch IllegalArgumentException
        try{
            Member notmember1 = new Member("", "MEM-12");
        }
        catch(IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }

        try{
            Book notbook1 = new Book("", "");
        }
        catch(IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }

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