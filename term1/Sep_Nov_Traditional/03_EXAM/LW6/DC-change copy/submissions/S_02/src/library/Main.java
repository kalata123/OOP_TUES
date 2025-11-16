package library;

import library.materials.Book;
import library.core.LibraryItem;
import library.transactions.BorrowingSystem;
import library.transactions.LibraryException;
import library.users.Member;
import library.util.LibrarySettings;
import library.util.SimpleDateUtils;

public class Main {

    public static void main(String[] args) {

        System.out.println("=== LIBRARY MANAGEMENT SYSTEM DEMONSTRATION ===");

        Book book1 = new Book("Harry Potter", "J.K.Rowling", LibrarySettings.generateItemId("HP"));
        Book book2 = new Book("Iron Lamp", "Dimitar Talev");

        System.out.println("Created books: ");
        System.out.println("  " + book1.getDisplayInfo());
        System.out.println("  " + book2.getDisplayInfo());

        Member member1 = new Member("Kaloyan", "M-100");
        Member member2 = new Member("Alek");

        System.out.println("\nMembers:");
        System.out.println("  " + member1.getName() + " (ID: " + member1.getMemberId() + ")");
        System.out.println("  " + member2.getName() + " (ID: " + member2.getMemberId() + ")");

        System.out.println("\nLibrary settings:");
        System.out.println("  MAX_BORROW_LIMIT = " + LibrarySettings.MAX_BORROW_LIMIT);
        System.out.println("  Generated item id example: " + LibrarySettings.generateItemId("ITEM"));

        System.out.println("\nDate utils examples:");
        System.out.println("  " + SimpleDateUtils.calculateDueDate(7));
        System.out.println("  " + SimpleDateUtils.calculateDueDate("2025-10-24"));
        System.out.println("  " + SimpleDateUtils.calculateDueDate(3, true));

        BorrowingSystem system = new BorrowingSystem();
        try {
            System.out.println("\nBorrowing operations:");
            system.borrowItem(book1, SimpleDateUtils.calculateDueDate(LibrarySettings.DEFAULT_LOAN_DAYS));
            member1.incrementBorrowed();
            System.out.println("  Borrowed: " + book1.getTitle());

            system.borrowItem(book2, SimpleDateUtils.calculateDueDate(LibrarySettings.DEFAULT_LOAN_DAYS));
            member1.incrementBorrowed();
            System.out.println("  Borrowed: " + book2.getTitle());

            System.out.println("  System borrowed count = " + system.getBorrowedCount() + ", member1 borrowed = "
                    + member1.getBorrowedCount());

            for (int i = 0; i < 4; i++) {
                Book extra = new Book("Extra Book " + i, "Author " + i, LibrarySettings.generateItemId("BK"));
                system.borrowItem(extra, SimpleDateUtils.calculateDueDate(7));
                member1.incrementBorrowed();
                System.out.println("  Borrowed extra: " + extra.getTitle());
            }

        } catch (IllegalArgumentException iae) {
            System.out.println("IllegalArgumentException: " + iae.getMessage());
        } catch (LibraryException le) {
            System.out.println("LibraryException: " + le.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }

        try {
            boolean returned = system.returnItem(book1.getItemId());
            if (returned) {
                member1.decrementBorrowed();
                System.out.println("\nReturned item: " + book1.getTitle());
            }
        } catch (LibraryException le) {
            System.out.println("Return failed: " + le.getMessage());
        }

        try {
            system.returnItem("NON-EXISTENT-ID");
        } catch (LibraryException le) {
            System.out.println("Expected failure when returning missing item: " + le.getMessage());
        }

        try {
            new Member("", "ID-1");
        } catch (IllegalArgumentException ex) {
            System.out.println("\nValidation caught (member): " + ex.getMessage());
        }

        try {
            new Book("Some Title", "");
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation caught (book): " + ex.getMessage());
        }

        System.out.println("\nPolymorphism demonstration:");
        LibraryItem[] items = new LibraryItem[] { book1, book2 };
        for (LibraryItem it : items) {
            System.out.println("  " + it.getDisplayInfo());
        }

        System.out.println("=== DEMONSTRATION COMPLETE ===");

    }
}