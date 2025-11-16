package library;


import library.*;
import library.core.LibraryItem;
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

        Book book1 = new Book("Book1", "Author Authorov");
        Book book2 = new Book("Tahiti tourist guide", "Arthur Morgan", "ITEM-ID-1");

        // HINT: Create members using different constructors to show overloading  
        // Member 1: Use constructor with name and member ID
        // Member 2: Use constructor with only name (auto-generate ID)

        Member mem1 = new Member("Dutch van der Linde", "MEM-067");
        Member mem2 = new Member("Stalin Stalinov");

        // HINT: Demonstrate encapsulation - try to access private fields directly
        // Then show how to properly use getters to access the data

        try{
            IO.println(mem1.name);
            IO.println(mem1.memberId);
            IO.println(mem1.borrowedCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mi nqma getteri
        IO.println();

        // HINT: Show LibrarySettings usage
        // Print the maximum borrow limit constant
        // Generate some item IDs using the static method

        IO.println(LibrarySettings.MAX_BORROW_LIMIT);
        IO.println(LibrarySettings.generateItemId("s"));
        IO.println(LibrarySettings.generateItemId("s"));
        IO.println(LibrarySettings.generateItemId("s"));
        IO.println(LibrarySettings.generateItemId("s"));
        IO.println(LibrarySettings.generateItemId("s"));
        IO.println(LibrarySettings.generateItemId("s"));


        // HINT: Demonstrate SimpleDateUtils method overloading
        // Call all three overloaded calculateDueDate methods with different parameters
        // Show the different results

        IO.println(SimpleDateUtils.calculateDueDate(67));
        IO.println(SimpleDateUtils.calculateDueDate("4 apr 2025"));
        IO.println(SimpleDateUtils.calculateDueDate(68, true));

        // === PART C3: MEMBER + BORROWINGSYSTEM INTEGRATION ===
        // HINT: Create a BorrowingSystem instance and a Member instance
        // When you borrow an item through BorrowingSystem, ALSO call member.incrementBorrowed()
        // When you return an item through BorrowingSystem, ALSO call member.decrementBorrowed()
        // Show that both systems stay synchronized - the member count matches BorrowingSystem count

        Member mem3 = new Member("Joseph Stalin");
        BorrowingSystem bor = new BorrowingSystem();

        bor.borrowItem(book2, "21");
        mem3.incrementBorrowed();

        bor.returnItem("ITEM-ID-1");
        mem3.decrementBorrowed();

        // HINT: Test borrowing up to the maximum limit
        // Show what happens when trying to borrow beyond the limit

        // === PART D: ERROR HANDLING ===
        // HINT: Demonstrate input validation in constructors
        // Try to create a Member with blank name - catch IllegalArgumentException
        // Try to create a Book with blank author - catch IllegalArgumentException

        try {
            Member swosh = new Member("");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        try {
            Book mymomiskindahomeless = new Book("Mymomiskindahomeless Book", "");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        // HINT: Demonstrate business logic errors
        // Try to borrow an item when BorrowingSystem is full - catch LibraryException
        // Try to return an item that doesn't exist - handle the return value appropriately
        try {
            bor.borrowItem(book2, "123");
            bor.borrowItem(book2, "123");
            bor.borrowItem(book2, "123");
            bor.borrowItem(book2, "123");
            bor.borrowItem(book2, "123");
        } catch (LibraryException e) {
            e.printStackTrace();
        }


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