import java.lang.reflect.Method;

/**
 * Event Management System - Structure Validation Test
 *
 * This test verifies that your code:
 * 1. Compiles without errors
 * 2. Has all required classes
 * 3. Has all required methods
 *
 * This does NOT test functionality - you must test that manually by running your program.
 */
public class EventSystemTest {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("Event Management System - Structure Validation");
        System.out.println("=".repeat(60));
        System.out.println();

        int passed = 0;
        int total = 0;

        // Test 1: Compilation
        System.out.println("Test 1: Compilation Check");
        System.out.println("----------------------------------------");
        System.out.println("✓ If you see this message, your code compiles successfully!");
        System.out.println();
        total++;
        passed++;

        // Test 2: Required Classes
        System.out.println("Test 2: Required Classes");
        System.out.println("----------------------------------------");
        total++;

        String[] requiredClasses = {
            "eventmanagement.model.Event",
            "eventmanagement.model.Ticket",
            "eventmanagement.model.Attendee",
            "eventmanagement.manager.EventManager",
            "eventmanagement.exception.EventManagementException"
        };

        boolean allClassesExist = true;
        for (String className : requiredClasses) {
            String simpleName = className.substring(className.lastIndexOf('.') + 1);
            if (classExists(className)) {
                System.out.println("✓ " + simpleName + " (" + className + ") found");
            } else {
                System.out.println("✗ " + simpleName + " (" + className + ") NOT FOUND");
                allClassesExist = false;
            }
        }

        if (allClassesExist) {
            passed++;
            System.out.println("\n✓ All required classes exist");
        } else {
            System.out.println("\n✗ Some classes are missing");
        }
        System.out.println();

        // Test 3: EventManager Required Methods
        System.out.println("Test 3: EventManager Required Methods");
        System.out.println("----------------------------------------");
        total++;

        String[] requiredMethods = {
            "addEvent",
            "removeEvent",
            "getEvent",
            "purchaseTicket",
            "cancelTicket",
            "sortEventsByDate",
            "sortEventsByPrice",
            "sortEventsByCapacity",
            "getEventsInDateRange",
            "getAvailableEvents",
            "getEventsByType",
            "getTotalAttendees",
            "getEventRevenue",
            "getTotalRevenue"
        };

        boolean allMethodsExist = true;
        int foundMethods = 0;

        if (classExists("eventmanagement.manager.EventManager")) {
            for (String methodName : requiredMethods) {
                if (methodExists("eventmanagement.manager.EventManager", methodName)) {
                    System.out.println("✓ " + methodName + "()");
                    foundMethods++;
                } else {
                    System.out.println("✗ " + methodName + "() NOT FOUND");
                    allMethodsExist = false;
                }
            }

            if (allMethodsExist) {
                passed++;
                System.out.println("\n✓ All required methods exist (" + foundMethods + "/" + requiredMethods.length + ")");
            } else {
                System.out.println("\n✗ Some methods are missing (" + foundMethods + "/" + requiredMethods.length + " found)");
            }
        } else {
            System.out.println("✗ Cannot check methods - EventManager (eventmanagement.manager.EventManager) class not found");
            allMethodsExist = false;
        }
        System.out.println();

        // Final Results
        System.out.println("=".repeat(60));
        System.out.println("STRUCTURE VALIDATION RESULTS");
        System.out.println("=".repeat(60));
        System.out.println();
        System.out.println("Tests Passed: " + passed + "/" + total);
        System.out.println();

        if (passed == total) {
            System.out.println("✓✓✓ STRUCTURE VALIDATION PASSED! ✓✓✓");
            System.out.println();
            System.out.println("Your code structure is correct.");
            System.out.println();
            System.out.println("NEXT STEPS:");
            System.out.println("1. Run your program and test the interactive menu");
            System.out.println("2. Test all menu options (1-8)");
            System.out.println("3. Try invalid inputs to test error handling");
            System.out.println("4. Verify all validation rules work");
            System.out.println("5. Check sorting and filtering operations");
            System.out.println();
            System.out.println("Remember: This test only checks structure, not functionality!");
        } else {
            System.out.println("✗✗✗ STRUCTURE VALIDATION FAILED ✗✗✗");
            System.out.println();
            System.out.println("Please fix the issues listed above:");
            if (!allClassesExist) {
                System.out.println("- Add missing classes");
            }
            if (!allMethodsExist) {
                System.out.println("- Add missing methods to EventManager");
            }
            System.out.println();
            System.out.println("After fixing, run this test again.");
        }

        System.out.println("=".repeat(60));
    }

    /**
     * Check if a class exists in the classpath
     */
    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Check if a method exists in a class (checks by name only, not parameters)
     */
    private static boolean methodExists(String className, String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    return true;
                }
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
