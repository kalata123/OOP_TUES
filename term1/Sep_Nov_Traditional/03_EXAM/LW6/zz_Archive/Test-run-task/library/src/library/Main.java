package library;

import library.core.LibraryItem;
import library.materials.Book;
import library.users.Member;
import library.transactions.BorrowingSystem;
import library.transactions.LibraryException;
import library.util.LibrarySettings;
import library.util.SimpleDateUtils;

/**
 * STUDENT TESTING CLASS FOR LIBRARY MANAGEMENT SYSTEM
 * Use this to test your implementation during the exam
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM - STUDENT TEST ===");
        System.out.println();

        boolean partAPassed = testPartA();
        boolean partBPassed = testPartB();
        boolean partCPassed = testPartC();
        boolean partDPassed = testPartD();
        boolean bonusImplemented = testBonus();

        System.out.println();
        System.out.println("=== IMPLEMENTATION SUMMARY ===");
        System.out.println("Part A - Foundation Classes: " + (partAPassed ? "COMPLETE" : "INCOMPLETE"));
        System.out.println("Part B - Utilities & Settings: " + (partBPassed ? "COMPLETE" : "INCOMPLETE"));
        System.out.println("Part C - Borrowing System: " + (partCPassed ? "COMPLETE" : "INCOMPLETE"));
        System.out.println("Part D - Error Handling: " + (partDPassed ? "COMPLETE" : "INCOMPLETE"));
        System.out.println("Bonus Features: " + (bonusImplemented ? "IMPLEMENTED" : "NOT IMPLEMENTED"));

        if (partAPassed && partBPassed && partCPassed && partDPassed) {
            System.out.println();
            System.out.println("✅ ALL CORE REQUIREMENTS IMPLEMENTED!");
            System.out.println("Your implementation should be ready for submission.");
        } else {
            System.out.println();
            System.out.println("⚠️  SOME REQUIREMENTS MISSING");
            System.out.println("Check the test output above to see which parts need work.");
        }
    }

    private static boolean testPartA() {
        System.out.println("TESTING PART A - FOUNDATION CLASSES");
        boolean allTestsPassed = true;

        try {
            // Test LibraryItem through Book class
            System.out.print("🔍 Testing LibraryItem (via Book class)... ");
            Book book1 = new Book("Java Programming", "John Smith", "BK-1001");
            Book book2 = new Book("Python Basics", "Jane Doe");

            if (!book1.getItemType().equals("Book")) {
                System.out.println("FAIL - getItemType() should return 'Book'");
                allTestsPassed = false;
            } else if (!book1.getAuthor().equals("John Smith")) {
                System.out.println("FAIL - getAuthor() not working correctly");
                allTestsPassed = false;
            } else if (!book1.getDisplayInfo().contains("Java Programming")) {
                System.out.println("FAIL - getDisplayInfo() format incorrect");
                allTestsPassed = false;
            } else {
                System.out.println("PASS");
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test Member class
            System.out.print("🔍 Testing Member class... ");
            Member member1 = new Member("Alice Johnson", "MEM-001");
            Member member2 = new Member("Bob Wilson");

            if (!member1.getName().equals("Alice Johnson")) {
                System.out.println("FAIL - getName() not working");
                allTestsPassed = false;
            } else if (!member1.canBorrow()) {
                System.out.println("FAIL - New member should be able to borrow");
                allTestsPassed = false;
            } else {
                // Test borrowing capacity
                for (int i = 0; i < 5; i++) {
                    member1.incrementBorrowed();
                }
                if (member1.canBorrow()) {
                    System.out.println("FAIL - Member at limit should not be able to borrow");
                    allTestsPassed = false;
                } else {
                    System.out.println("PASS");
                }
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test validation
            System.out.print("🔍 Testing input validation... ");
            try {
                new Member("", "MEM-001");
                System.out.println("FAIL - Should validate blank names");
                allTestsPassed = false;
            } catch (IllegalArgumentException e) {
                System.out.println("PASS");
            }
        } catch (Exception e) {
            System.out.println("FAIL - Validation not working: " + e.getMessage());
            allTestsPassed = false;
        }

        System.out.println("Part A: " + (allTestsPassed ? "✅ COMPLETE" : "❌ INCOMPLETE"));
        System.out.println();
        return allTestsPassed;
    }

    private static boolean testPartB() {
        System.out.println("TESTING PART B - UTILITIES & SETTINGS");
        boolean allTestsPassed = true;

        try {
            // Test LibrarySettings
            System.out.print("🔍 Testing LibrarySettings... ");

            if (LibrarySettings.MAX_BORROW_LIMIT != 5) {
                System.out.println("FAIL - MAX_BORROW_LIMIT should be 5");
                allTestsPassed = false;
            } else if (LibrarySettings.FINE_PER_DAY != 1) {
                System.out.println("FAIL - FINE_PER_DAY should be 1");
                allTestsPassed = false;
            } else {
                String id1 = LibrarySettings.generateItemId("BK");
                if (id1 == null || !id1.startsWith("BK-")) {
                    System.out.println("FAIL - generateItemId format incorrect");
                    allTestsPassed = false;
                } else {
                    System.out.println("PASS");
                }
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test SimpleDateUtils overloading (ADJUSTED)
            System.out.print("🔍 Testing SimpleDateUtils method overloading... ");

            // Test the three overloaded versions
            String due1 = SimpleDateUtils.calculateDueDate(14);
            String due2 = SimpleDateUtils.calculateDueDate("2024-01-01");
            String due3 = SimpleDateUtils.calculateDueDate(7, true);

            if (due1 == null || due2 == null || due3 == null) {
                System.out.println("FAIL - Methods should not return null");
                allTestsPassed = false;
            } else if (!due1.contains("14") || !due2.contains("2024-01-01") || !due3.contains("7")) {
                System.out.println("FAIL - Method outputs incorrect");
                allTestsPassed = false;
            } else {
                System.out.println("PASS");
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        System.out.println("Part B: " + (allTestsPassed ? "✅ COMPLETE" : "❌ INCOMPLETE"));
        System.out.println();
        return allTestsPassed;
    }

    private static boolean testPartC() {
        System.out.println("TESTING PART C - BORROWING MANAGEMENT");
        boolean allTestsPassed = true;

        try {
            // Test basic BorrowingSystem operations
            System.out.print("🔍 Testing basic borrowing operations... ");
            BorrowingSystem system = new BorrowingSystem();
            Book book1 = new Book("Book 1", "Author 1", "BK-001");
            Book book2 = new Book("Book 2", "Author 2", "BK-002");

            system.borrowItem(book1, "2024-12-01");
            system.borrowItem(book2, "2024-12-02");

            if (system.getBorrowedCount() != 2) {
                System.out.println("FAIL - Borrowed count should be 2");
                allTestsPassed = false;
            } else {
                String[] titles = system.getBorrowedTitles();
                if (titles.length != 2 || !titles[0].equals("Book 1")) {
                    System.out.println("FAIL - getBorrowedTitles incorrect");
                    allTestsPassed = false;
                } else if (!system.returnItem("BK-001")) {
                    System.out.println("FAIL - returnItem should return true for success");
                    allTestsPassed = false;
                } else if (system.getBorrowedCount() != 1) {
                    System.out.println("FAIL - Count should be 1 after return");
                    allTestsPassed = false;
                } else {
                    System.out.println("PASS");
                }
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test LibraryException
            System.out.print("🔍 Testing custom exceptions... ");
            try {
                throw new LibraryException("Test message");
            } catch (LibraryException e) {
                if (!e.getMessage().equals("Test message")) {
                    System.out.println("FAIL - Exception message not preserved");
                    allTestsPassed = false;
                } else {
                    System.out.println("PASS");
                }
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test borrowing limits
            System.out.print("🔍 Testing borrowing limits... ");
            BorrowingSystem system = new BorrowingSystem();
            Member member = new Member("Test User", "MEM-100");

            // Fill to capacity
            for (int i = 0; i < 5; i++) {
                Book book = new Book("Book " + i, "Author", "BK-" + i);
                system.borrowItem(book, "2024-12-01");
                member.incrementBorrowed();
            }

            if (system.getBorrowedCount() != 5) {
                System.out.println("FAIL - Should reach capacity at 5 items");
                allTestsPassed = false;
            } else if (member.canBorrow()) {
                System.out.println("FAIL - Member at limit should not be able to borrow");
                allTestsPassed = false;
            } else {
                // Try to exceed capacity
                try {
                    Book extraBook = new Book("Extra Book", "Author", "BK-999");
                    system.borrowItem(extraBook, "2024-12-01");
                    System.out.println("FAIL - Should throw exception when exceeding capacity");
                    allTestsPassed = false;
                } catch (LibraryException e) {
                    System.out.println("PASS");
                }
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        System.out.println("Part C: " + (allTestsPassed ? "✅ COMPLETE" : "❌ INCOMPLETE"));
        System.out.println();
        return allTestsPassed;
    }

    private static boolean testPartD() {
        System.out.println("TESTING PART D - ERROR HANDLING");
        boolean allTestsPassed = true;

        try {
            // Test input validation
            System.out.print("🔍 Testing constructor validation... ");
            try {
                new Member("", "MEM-001");
                System.out.println("FAIL - Should validate blank names");
                allTestsPassed = false;
            } catch (IllegalArgumentException e) {
                System.out.println("PASS");
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test Book validation
            System.out.print("🔍 Testing Book validation... ");
            try {
                new Book("Valid Title", "");
                System.out.println("FAIL - Should validate blank author");
                allTestsPassed = false;
            } catch (IllegalArgumentException e) {
                System.out.println("PASS");
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        try {
            // Test business logic protection
            System.out.print("🔍 Testing business logic protection... ");
            BorrowingSystem system = new BorrowingSystem();

            // Test invalid parameters
            try {
                system.borrowItem(null, "2024-12-01");
                System.out.println("FAIL - Should validate null items");
                allTestsPassed = false;
            } catch (IllegalArgumentException e) {
                // Expected - continue testing
            }

            // Test returning non-existent item
            if (system.returnItem("NON-EXISTENT")) {
                System.out.println("FAIL - Should return false for non-existent items");
                allTestsPassed = false;
            } else {
                System.out.println("PASS");
            }
        } catch (Exception e) {
            System.out.println("FAIL - " + e.getMessage());
            allTestsPassed = false;
        }

        System.out.println("Part D: " + (allTestsPassed ? "✅ COMPLETE" : "❌ INCOMPLETE"));
        System.out.println();
        return allTestsPassed;
    }

    private static boolean testBonus() {
        System.out.println("CHECKING BONUS FEATURES");
        boolean anyBonusImplemented = false;

        try {
            BorrowingSystem system = new BorrowingSystem();
            Book book1 = new Book("Test Book", "Author", "BONUS-001");
            system.borrowItem(book1, "2024-12-01");

            // Check for bonus methods using reflection
            try {
                java.lang.reflect.Method hasBorrowed = system.getClass().getMethod("hasBorrowedItem", String.class);
                java.lang.reflect.Method findDueDate = system.getClass().getMethod("findDueDate", String.class);

                boolean hasItem = (Boolean) hasBorrowed.invoke(system, "BONUS-001");
                String dueDate = (String) findDueDate.invoke(system, "BONUS-001");

                if (hasItem && dueDate != null) {
                    System.out.println("✅ Bonus 1 (Search) - IMPLEMENTED");
                    anyBonusImplemented = true;
                }
            } catch (NoSuchMethodException e) {
                System.out.println("📝 Bonus 1 (Search) - Not implemented");
            }

            try {
                java.lang.reflect.Method getUsage = system.getClass().getMethod("getUsagePercentage");
                java.lang.reflect.Method getStatus = system.getClass().getMethod("getBorrowingStatus");

                double usage = (Double) getUsage.invoke(system);
                String status = (String) getStatus.invoke(system);

                if (usage >= 0 && status != null) {
                    System.out.println("✅ Bonus 2 (Statistics) - IMPLEMENTED");
                    anyBonusImplemented = true;
                }
            } catch (NoSuchMethodException e) {
                System.out.println("📝 Bonus 2 (Statistics) - Not implemented");
            }

        } catch (Exception e) {
            System.out.println("Error checking bonus features: " + e.getMessage());
        }

        if (!anyBonusImplemented) {
            System.out.println("No bonus features implemented");
        }

        System.out.println();
        return anyBonusImplemented;
    }
}