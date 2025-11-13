import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Grading Helper for Event & Ticket Management System (Homework 2)
 *
 * This tool assists with grading by:
 * 1. Running the automated structure test (EventSystemTest)
 * 2. Providing a manual grading template
 * 3. Recording scores in CSV format
 *
 * Grading is primarily manual, following the rubric:
 * - Compilation & Structure: /3
 * - Event Management: /5
 * - Ticket System: /4
 * - Collections Usage: /3
 * - Sorting & Filtering: /3
 * - Interactive Menu: /2
 * - Bonus: /5
 *
 * Usage: java EventSystemGradingHelper <student_folder>
 */
public class EventSystemGradingHelper {

    private String studentId;
    private String submissionPath;
    private Scanner scanner = new Scanner(System.in);

    // Scores
    private int compilation = 0;          // /3
    private int eventManagement = 0;      // /5
    private int ticketSystem = 0;         // /4
    private int collections = 0;          // /3
    private int sortingFiltering = 0;     // /3
    private int interactiveMenu = 0;      // /2
    private int bonus = 0;                // /5
    private String comments = "";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java EventSystemGradingHelper <student_folder>");
            System.out.println("Example: java EventSystemGradingHelper ../submissions/S_15");
            return;
        }

        String studentFolder = args[0];
        String studentId = new File(studentFolder).getName();

        EventSystemGradingHelper helper = new EventSystemGradingHelper(studentId, studentFolder);
        helper.runGrading();
    }

    public EventSystemGradingHelper(String studentId, String submissionPath) {
        this.studentId = studentId;
        this.submissionPath = submissionPath;
    }

    public void runGrading() {
        System.out.println("============================================================");
        System.out.println("Event Management System - Grading Helper");
        System.out.println("Student: " + studentId);
        System.out.println("============================================================");
        System.out.println();

        // Step 1: Automated Structure Test
        runStructureTest();

        // Step 2: Manual Grading Guide
        manualGradingGuide();

        // Step 3: Record Scores
        recordScores();

        // Step 4: Generate Report
        generateReport();
    }

    private void runStructureTest() {
        System.out.println("--- Step 1: Automated Structure Test ---");
        System.out.println("Running EventSystemTest...");
        System.out.println();

        try {
            // Compile student code
            ProcessBuilder compileBuilder = new ProcessBuilder(
                "javac",
                submissionPath + "/eventmanagement/model/*.java",
                submissionPath + "/eventmanagement/manager/*.java",
                submissionPath + "/eventmanagement/exception/*.java",
                submissionPath + "/Main.java"
            );
            compileBuilder.directory(new File(submissionPath).getParentFile());
            Process compileProcess = compileBuilder.start();
            int compileResult = compileProcess.waitFor();

            if (compileResult == 0) {
                System.out.println("✓ Code compiles successfully");
                compilation = 3;  // Full points for compilation
            } else {
                System.out.println("✗ Compilation failed");
                compilation = 0;

                // Show compilation errors
                BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(compileProcess.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.out.println(line);
                }

                System.out.println("\nCannot proceed with grading - code must compile.");
                System.out.println("Final Score: 0/20");
                return;
            }

            // Check for required classes and methods using reflection
            checkStructure();

        } catch (Exception e) {
            System.out.println("✗ Error running structure test: " + e.getMessage());
            compilation = 0;
        }

        System.out.println();
        System.out.println("Compilation & Structure Score: " + compilation + "/3");
        System.out.println();
    }

    private void checkStructure() {
        System.out.println("\nChecking required classes and methods...");

        String[] requiredClasses = {
            "eventmanagement.model.Event",
            "eventmanagement.model.Ticket",
            "eventmanagement.model.Attendee",
            "eventmanagement.manager.EventManager",
            "eventmanagement.exception.EventManagementException"
        };

        String[] requiredMethods = {
            "addEvent", "removeEvent", "getEvent", "purchaseTicket", "cancelTicket",
            "sortEventsByDate", "sortEventsByPrice", "sortEventsByCapacity",
            "getEventsInDateRange", "getAvailableEvents", "getEventsByType",
            "getTotalAttendees", "getEventRevenue", "getTotalRevenue"
        };

        boolean allClassesExist = true;
        for (String className : requiredClasses) {
            if (classExists(className)) {
                System.out.println("  ✓ " + className);
            } else {
                System.out.println("  ✗ " + className + " NOT FOUND");
                allClassesExist = false;
            }
        }

        if (!allClassesExist) {
            System.out.println("\n⚠ Missing classes - compilation score may be reduced");
            compilation = Math.min(compilation, 2);
        }

        if (classExists("eventmanagement.manager.EventManager")) {
            System.out.println("\nChecking EventManager methods...");
            int foundMethods = 0;
            for (String methodName : requiredMethods) {
                if (methodExists("eventmanagement.manager.EventManager", methodName)) {
                    foundMethods++;
                } else {
                    System.out.println("  ✗ " + methodName + "() missing");
                }
            }
            System.out.println("Found " + foundMethods + "/" + requiredMethods.length + " required methods");

            if (foundMethods < requiredMethods.length) {
                System.out.println("\n⚠ Missing methods - compilation score may be reduced");
                compilation = Math.min(compilation, 2);
            }
        }
    }

    private boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean methodExists(String className, String methodName) {
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

    private void manualGradingGuide() {
        System.out.println("--- Step 2: Manual Functionality Review ---");
        System.out.println();
        System.out.println("Now you need to:");
        System.out.println("1. Run the student's program (java Main or their entry point)");
        System.out.println("2. Test all menu options (1-8)");
        System.out.println("3. Try invalid inputs to test error handling");
        System.out.println("4. Verify validation rules (capacity ÷ 10, price .00/.50, no weekends)");
        System.out.println("5. Test sorting and filtering operations");
        System.out.println("6. Check if bonus features are implemented");
        System.out.println();
        System.out.println("Press ENTER when you have finished testing...");
        scanner.nextLine();
    }

    private void recordScores() {
        System.out.println("\n--- Step 3: Record Scores ---");
        System.out.println();
        System.out.println("Compilation & Structure: " + compilation + "/3 (already determined)");
        System.out.println();

        // Event Management (5 points)
        System.out.println("Event Management (/5):");
        System.out.println("  5 = All validations work, edge cases handled, clear error messages");
        System.out.println("  4 = Most validations work, minor edge case issues");
        System.out.println("  3 = Basic validation, several rules not enforced");
        System.out.println("  2 = Minimal validation, many rules ignored");
        System.out.println("  1 = Barely functional, most validations missing");
        System.out.println("  0 = Non-functional");
        eventManagement = getScore("Event Management", 5);

        // Ticket System (4 points)
        System.out.println("\nTicket System (/4):");
        System.out.println("  4 = All features work, duplicate prevention, collections consistent");
        System.out.println("  3 = Basic purchase works, minor cancellation issues");
        System.out.println("  2 = Purchase works but no duplicate prevention");
        System.out.println("  1 = Basic ticket creation only");
        System.out.println("  0 = Non-functional");
        ticketSystem = getScore("Ticket System", 4);

        // Collections Usage (3 points)
        System.out.println("\nCollections Usage (/3):");
        System.out.println("  3 = Appropriate collections, dynamic sizing, efficient operations");
        System.out.println("  2 = Uses collections but questionable choices");
        System.out.println("  1 = Uses collections inefficiently");
        System.out.println("  0 = Uses arrays or doesn't use collections framework");
        collections = getScore("Collections Usage", 3);

        // Sorting & Filtering (3 points)
        System.out.println("\nSorting & Filtering (/3):");
        System.out.println("  3 = All 3 sorts and all 3 filters work correctly");
        System.out.println("  2 = 2/3 sorts and 2/3 filters work");
        System.out.println("  1 = 1/3 sorts and 1/3 filters work");
        System.out.println("  0 = Non-functional or not implemented");
        sortingFiltering = getScore("Sorting & Filtering", 3);

        // Interactive Menu (2 points)
        System.out.println("\nInteractive Menu (/2):");
        System.out.println("  2 = Menu clear, loops correctly, handles invalid input, good UX");
        System.out.println("  1 = Menu works but poor UX or doesn't loop properly");
        System.out.println("  0 = No menu or completely non-functional");
        interactiveMenu = getScore("Interactive Menu", 2);

        // Bonus (5 points)
        System.out.println("\nBonus Features (/5):");
        System.out.println("  5 = Smart Seat Allocation OR Dynamic Pricing fully functional");
        System.out.println("  0 = Not implemented or doesn't work");
        bonus = getScore("Bonus", 5);

        // Comments
        System.out.println("\nAdditional Comments (optional):");
        System.out.print("Enter: ");
        scanner.nextLine();  // Clear buffer
        comments = scanner.nextLine();
    }

    private int getScore(String component, int max) {
        while (true) {
            System.out.print("Score (/"+max+"): ");
            try {
                int score = scanner.nextInt();
                if (score >= 0 && score <= max) {
                    return score;
                } else {
                    System.out.println("Please enter a number between 0 and " + max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();  // Clear invalid input
            }
        }
    }

    private void generateReport() {
        int baseScore = compilation + eventManagement + ticketSystem +
                       collections + sortingFiltering + interactiveMenu;
        int totalScore = baseScore + bonus;
        double percentage = (baseScore / 20.0) * 100;

        System.out.println("\n============================================================");
        System.out.println("GRADING SUMMARY");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("Student: " + studentId);
        System.out.println();
        System.out.println("Compilation & Structure:  " + compilation + "/3");
        System.out.println("Event Management:         " + eventManagement + "/5");
        System.out.println("Ticket System:            " + ticketSystem + "/4");
        System.out.println("Collections Usage:        " + collections + "/3");
        System.out.println("Sorting & Filtering:      " + sortingFiltering + "/3");
        System.out.println("Interactive Menu:         " + interactiveMenu + "/2");
        System.out.println("                          ----");
        System.out.println("Base Score:               " + baseScore + "/20");
        System.out.println("Bonus:                   +" + bonus + "/5");
        System.out.println("                          ----");
        System.out.println("Total Score:              " + totalScore + "/20");
        System.out.println("Percentage:               " + String.format("%.1f", percentage) + "%");
        System.out.println();

        String grade = getGrade(percentage);
        System.out.println("Grade: " + grade);
        System.out.println();

        if (!comments.trim().isEmpty()) {
            System.out.println("Comments: " + comments);
            System.out.println();
        }

        // Save to CSV
        saveToCSV(baseScore, totalScore, percentage);
    }

    private String getGrade(double percentage) {
        if (percentage < 55) return "2 (Слаб)";
        if (percentage < 65) return "3 (Среден)";
        if (percentage < 75) return "4 (Добър)";
        if (percentage < 90) return "5 (Мн. добър)";
        return "6 (Отличен)";
    }

    private void saveToCSV(int baseScore, int totalScore, double percentage) {
        try {
            // Create results directory if doesn't exist
            new File("results").mkdirs();

            String csvFile = "results/" + studentId + "_report.csv";
            FileWriter writer = new FileWriter(csvFile);

            // CSV Header (compatible with dashboard)
            writer.write("Assignment;Type;Student;Compilation;EventMgmt;TicketSys;Collections;");
            writer.write("SortFilter;Menu;BaseScore;MaxBaseScore;Bonus;TotalScore;Percentage;Comments\n");

            // Data row
            writer.write("HW2-EventManagement;HOMEWORK;" + studentId + ";");
            writer.write(compilation + ";" + eventManagement + ";" + ticketSystem + ";");
            writer.write(collections + ";" + sortingFiltering + ";" + interactiveMenu + ";");
            writer.write(baseScore + ";20;" + bonus + ";" + totalScore + ";");
            writer.write(String.format("%.1f", percentage) + ";");
            writer.write(comments.replace(";", ",") + "\n");

            writer.close();

            System.out.println("Report saved to: " + csvFile);
            System.out.println();

        } catch (IOException e) {
            System.out.println("Error saving CSV: " + e.getMessage());
        }
    }
}
