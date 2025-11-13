import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * Automated Grader for Event & Ticket Management System (Homework 2)
 *
 * This grader:
 * 1. Compiles student submissions
 * 2. Runs comprehensive tests with partial credit
 * 3. Analyzes code quality
 * 4. Detects suspicious patterns
 * 5. Generates detailed CSV reports
 *
 * Usage: java EventSystemAutoGrader <student_folder> <output_csv>
 * Example: java EventSystemAutoGrader submissions/S_15 results/S_15_report.csv
 */
public class EventSystemAutoGrader {

    // Grading components
    private String studentId;
    private String submissionPath;
    private double compilationScore = 0;
    private double partAScore = 0;
    private double partBScore = 0;
    private double partCScore = 0;
    private double partDScore = 0;
    private double bonusScore = 0;
    private StringBuilder comments = new StringBuilder();
    private StringBuilder detailedFeedback = new StringBuilder();
    private List<String> suspiciousPatterns = new ArrayList<>();

    // Test results tracking
    private Map<String, Boolean> testResults = new LinkedHashMap<>();

    public EventSystemAutoGrader(String studentId, String submissionPath) {
        this.studentId = studentId;
        this.submissionPath = submissionPath;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java EventSystemAutoGrader <student_folder> <output_csv>");
            System.out.println("Example: java EventSystemAutoGrader submissions/S_15 results/S_15_report.csv");
            return;
        }

        String studentFolder = args[0];
        String outputCsv = args[1];

        // Extract student ID from folder name
        String studentId = new File(studentFolder).getName();

        EventSystemAutoGrader grader = new EventSystemAutoGrader(studentId, studentFolder);
        grader.grade();
        grader.generateCSVReport(outputCsv);
    }

    public void grade() {
        System.out.println("=".repeat(60));
        System.out.println("Grading Student: " + studentId);
        System.out.println("Submission Path: " + submissionPath);
        System.out.println("=".repeat(60));

        // Step 1: Compilation Test
        if (!testCompilation()) {
            System.out.println("\n✗ Compilation failed. Cannot proceed with testing.");
            System.out.println("Score: 0/20 (0%)");
            return;
        }

        // Step 2: Test Foundation Classes (Part A)
        testPartA_FoundationClasses();

        // Step 3: Test Collection Management (Part B)
        testPartB_CollectionManagement();

        // Step 4: Test Operations (Part C)
        testPartC_Operations();

        // Step 5: Test Validation & Exceptions (Part D)
        testPartD_Validation();

        // Step 6: Code Quality Analysis
        analyzeCodeQuality();

        // Step 7: Detect Suspicious Patterns
        detectSuspiciousPatterns();

        // Step 8: Check for Bonus Features
        checkBonusFeatures();

        // Print final summary
        printFinalSummary();
    }

    private boolean testCompilation() {
        System.out.println("\n--- Testing Compilation ---");

        try {
            // Find all .java files in eventmanagement package
            File pkgDir = new File(submissionPath + "/eventmanagement");
            if (!pkgDir.exists() || !pkgDir.isDirectory()) {
                comments.append("✗ eventmanagement package not found | ");
                detailedFeedback.append("ERROR: Package structure incorrect - eventmanagement folder missing\n");
                return false;
            }

            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));
            if (javaFiles == null || javaFiles.length == 0) {
                comments.append("✗ No Java files found in package | ");
                detailedFeedback.append("ERROR: No .java files found in eventmanagement package\n");
                return false;
            }

            // Compile all Java files
            List<String> fileNames = new ArrayList<>();
            for (File file : javaFiles) {
                fileNames.add(file.getAbsolutePath());
            }

            ProcessBuilder pb = new ProcessBuilder();
            pb.command().add("javac");
            pb.command().add("-d");
            pb.command().add(submissionPath);
            pb.command().addAll(fileNames);

            Process process = pb.start();
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("✓ Compilation successful");
                compilationScore = 4.0;
                comments.append("✓ Compilation clean | ");
                detailedFeedback.append("COMPILATION: Success - all files compiled without errors\n");
                testResults.put("Compilation", true);
                return true;
            } else if (errors.toString().contains("warning")) {
                System.out.println("⚠ Compilation successful with warnings");
                System.out.println(errors.toString());
                compilationScore = 2.0;
                comments.append("⚠ Compilation with warnings | ");
                detailedFeedback.append("COMPILATION: Warnings present:\n" + errors.toString() + "\n");
                testResults.put("Compilation", true);
                suspiciousPatterns.add("Compilation warnings present");
                return true;
            } else {
                System.out.println("✗ Compilation failed");
                System.out.println(errors.toString());
                compilationScore = 0;
                comments.append("✗ Compilation failed | ");
                detailedFeedback.append("COMPILATION: Failed with errors:\n" + errors.toString() + "\n");
                testResults.put("Compilation", false);
                return false;
            }

        } catch (Exception e) {
            System.out.println("✗ Exception during compilation: " + e.getMessage());
            comments.append("✗ Compilation exception | ");
            detailedFeedback.append("COMPILATION: Exception - " + e.getMessage() + "\n");
            testResults.put("Compilation", false);
            return false;
        }
    }

    private void testPartA_FoundationClasses() {
        System.out.println("\n--- Testing Part A: Foundation Classes (4 points) ---");

        double eventScore = testEventClass();  // 1.5 pts
        double ticketScore = testTicketClass();  // 1.0 pt
        double attendeeScore = testAttendeeClass();  // 1.5 pts

        partAScore = eventScore + ticketScore + attendeeScore;
        System.out.printf("Part A Score: %.1f/4.0\n", partAScore);
    }

    private double testEventClass() {
        System.out.println("\nTesting Event class (1.5 pts):");
        double score = 0;

        try {
            Class<?> eventClass = Class.forName("eventmanagement.Event");

            // Test 1: Fields exist (0.3 pts)
            if (hasPrivateField(eventClass, "eventId") &&
                hasPrivateField(eventClass, "name") &&
                hasPrivateField(eventClass, "capacity")) {
                score += 0.3;
                System.out.println("  ✓ Required fields present (0.3)");
                testResults.put("Event.fields", true);
            } else {
                System.out.println("  ✗ Missing required fields");
                testResults.put("Event.fields", false);
            }

            // Test 2: Validation in constructor (0.4 pts)
            try {
                // Try invalid capacity (not divisible by 10)
                Constructor<?> constructor = eventClass.getConstructor(
                    String.class, String.class, String.class,
                    Class.forName("java.time.LocalDate"), int.class, double.class
                );

                Object invalidEvent = null;
                boolean exceptionThrown = false;
                try {
                    invalidEvent = constructor.newInstance(
                        "EVT001", "Test Event", "Concert",
                        java.time.LocalDate.now().plusDays(3),
                        75, 50.00  // Invalid: 75 not divisible by 10
                    );
                } catch (InvocationTargetException e) {
                    if (e.getCause().getClass().getSimpleName().equals("EventManagementException")) {
                        exceptionThrown = true;
                    }
                }

                if (exceptionThrown) {
                    score += 0.4;
                    System.out.println("  ✓ Validation works (capacity check) (0.4)");
                    testResults.put("Event.validation", true);
                } else {
                    System.out.println("  ✗ Validation incomplete");
                    testResults.put("Event.validation", false);
                }
            } catch (Exception e) {
                System.out.println("  ✗ Validation test failed: " + e.getMessage());
                testResults.put("Event.validation", false);
            }

            // Test 3: Business methods (0.3 pts)
            if (hasMethod(eventClass, "hasAvailableSeats") &&
                hasMethod(eventClass, "addAttendee") &&
                hasMethod(eventClass, "removeAttendee")) {
                score += 0.3;
                System.out.println("  ✓ Business methods present (0.3)");
                testResults.put("Event.methods", true);
            } else {
                System.out.println("  ✗ Missing business methods");
                testResults.put("Event.methods", false);
            }

            // Test 4: Comparable implementation (0.3 pts)
            if (Comparable.class.isAssignableFrom(eventClass)) {
                score += 0.3;
                System.out.println("  ✓ Implements Comparable (0.3)");
                testResults.put("Event.comparable", true);
            } else {
                System.out.println("  ✗ Does not implement Comparable");
                testResults.put("Event.comparable", false);
            }

            // Test 5: Required comments (0.2 pts)
            if (hasRequiredComment("Event.java", "DESIGN DECISION") &&
                hasRequiredComment("Event.java", "VALIDATION LOGIC")) {
                score += 0.2;
                System.out.println("  ✓ Required comments present (0.2)");
                testResults.put("Event.comments", true);
            } else {
                System.out.println("  ✗ Missing required comments");
                testResults.put("Event.comments", false);
                suspiciousPatterns.add("Event class missing DESIGN DECISION or VALIDATION LOGIC comments");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("  ✗ Event class not found");
            detailedFeedback.append("ERROR: Event class not found in eventmanagement package\n");
            testResults.put("Event", false);
        }

        return score;
    }

    private double testTicketClass() {
        System.out.println("\nTesting Ticket class (1.0 pt):");
        double score = 0;

        try {
            Class<?> ticketClass = Class.forName("eventmanagement.Ticket");

            // Basic structure (0.6 pts)
            if (hasPrivateField(ticketClass, "ticketId") &&
                hasPrivateField(ticketClass, "price") &&
                hasMethod(ticketClass, "use") &&
                hasMethod(ticketClass, "isValid")) {
                score += 0.6;
                System.out.println("  ✓ Structure complete (0.6)");
                testResults.put("Ticket.structure", true);
            } else {
                System.out.println("  ✗ Structure incomplete");
                testResults.put("Ticket.structure", false);
            }

            // Validation (0.4 pts)
            try {
                Constructor<?> constructor = ticketClass.getConstructor(
                    String.class, String.class, String.class, double.class, String.class
                );

                boolean exceptionThrown = false;
                try {
                    constructor.newInstance("TKT001", "EVT001", "invalid-email", 50.0, "Standard");
                } catch (InvocationTargetException e) {
                    if (e.getCause().getClass().getSimpleName().equals("EventManagementException")) {
                        exceptionThrown = true;
                    }
                }

                if (exceptionThrown) {
                    score += 0.4;
                    System.out.println("  ✓ Validation works (0.4)");
                    testResults.put("Ticket.validation", true);
                } else {
                    System.out.println("  ✗ Validation incomplete");
                    testResults.put("Ticket.validation", false);
                }
            } catch (Exception e) {
                testResults.put("Ticket.validation", false);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("  ✗ Ticket class not found");
            testResults.put("Ticket", false);
        }

        return score;
    }

    private double testAttendeeClass() {
        System.out.println("\nTesting Attendee class (1.5 pts):");
        double score = 0;

        try {
            Class<?> attendeeClass = Class.forName("eventmanagement.Attendee");

            // Check for ArrayList field (0.4 pts)
            Field ticketIdsField = null;
            for (Field field : attendeeClass.getDeclaredFields()) {
                if (field.getType().getName().contains("List")) {
                    ticketIdsField = field;
                    break;
                }
            }

            if (ticketIdsField != null) {
                score += 0.4;
                System.out.println("  ✓ Uses List for ticketIds (0.4)");
                testResults.put("Attendee.ArrayList", true);
            } else {
                System.out.println("  ✗ Does not use List for ticketIds");
                testResults.put("Attendee.ArrayList", false);
            }

            // Check methods (0.6 pts)
            if (hasMethod(attendeeClass, "addTicket") &&
                hasMethod(attendeeClass, "removeTicket") &&
                hasMethod(attendeeClass, "getTicketIds")) {
                score += 0.6;
                System.out.println("  ✓ Ticket management methods present (0.6)");
                testResults.put("Attendee.methods", true);
            } else {
                System.out.println("  ✗ Missing ticket management methods");
                testResults.put("Attendee.methods", false);
            }

            // Check for COLLECTION CHOICE comment (0.2 pts)
            if (hasRequiredComment("Attendee.java", "COLLECTION CHOICE")) {
                score += 0.2;
                System.out.println("  ✓ COLLECTION CHOICE comment present (0.2)");
                testResults.put("Attendee.comments", true);
            } else {
                System.out.println("  ✗ Missing COLLECTION CHOICE comment");
                testResults.put("Attendee.comments", false);
                suspiciousPatterns.add("Attendee class missing COLLECTION CHOICE comment");
            }

            // Check defensive copying (0.3 pts)
            try {
                Method getTicketIds = attendeeClass.getMethod("getTicketIds");
                // This is a heuristic - we can't fully test defensive copying statically
                score += 0.3;
                System.out.println("  ✓ getTicketIds method present (0.3)");
                testResults.put("Attendee.defensiveCopy", true);
            } catch (NoSuchMethodException e) {
                testResults.put("Attendee.defensiveCopy", false);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("  ✗ Attendee class not found");
            testResults.put("Attendee", false);
        }

        return score;
    }

    private void testPartB_CollectionManagement() {
        System.out.println("\n--- Testing Part B: Collection Management (9 points) ---");

        // For brevity, simplified version
        // In full implementation, would test each collection type and operation

        double score = 0;

        try {
            Class<?> managerClass = Class.forName("eventmanagement.EventManager");

            // Check generic declaration (1 pt)
            if (managerClass.getTypeParameters().length > 0) {
                score += 1.0;
                System.out.println("✓ Generic class (1.0)");
                testResults.put("EventManager.generic", true);
            } else {
                System.out.println("✗ Not generic");
                testResults.put("EventManager.generic", false);
                suspiciousPatterns.add("EventManager is not generic");
            }

            // Check collection fields (3 pts)
            if (hasFieldOfType(managerClass, "List") &&
                hasFieldOfType(managerClass, "Map") &&
                hasFieldOfType(managerClass, "Set")) {
                score += 3.0;
                System.out.println("✓ Uses ArrayList, HashMap, HashSet (3.0)");
                testResults.put("EventManager.collections", true);
            } else {
                System.out.println("✗ Missing or incorrect collection types");
                testResults.put("EventManager.collections", false);
                score += 1.5; // Partial credit
            }

            // Check key methods (3 pts)
            if (hasMethod(managerClass, "addEvent") &&
                hasMethod(managerClass, "purchaseTicket") &&
                hasMethod(managerClass, "getAttendeeCount")) {
                score += 3.0;
                System.out.println("✓ Key methods present (3.0)");
                testResults.put("EventManager.methods", true);
            } else {
                System.out.println("✗ Missing key methods");
                testResults.put("EventManager.methods", false);
                score += 1.5; // Partial credit
            }

            // Check required comments (2 pts)
            int commentCount = 0;
            if (hasRequiredComment("EventManager.java", "COLLECTION CHOICE")) commentCount++;
            if (hasRequiredComment("EventManager.java", "INTEGRATION LOGIC")) commentCount++;
            if (hasRequiredComment("EventManager.java", "DESIGN DECISION")) commentCount++;

            score += (commentCount / 3.0) * 2.0;
            System.out.printf("✓ Comments: %d/3 required (%.1f)\n", commentCount, (commentCount / 3.0) * 2.0);
            testResults.put("EventManager.comments", commentCount >= 2);

            if (commentCount < 2) {
                suspiciousPatterns.add("EventManager missing " + (3 - commentCount) + " required comments");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("✗ EventManager class not found");
            testResults.put("EventManager", false);
        }

        partBScore = score;
        System.out.printf("Part B Score: %.1f/9.0\n", partBScore);
    }

    private void testPartC_Operations() {
        System.out.println("\n--- Testing Part C: Operations (4 points) ---");

        double score = 0;

        try {
            Class<?> managerClass = Class.forName("eventmanagement.EventManager");

            // Check sorting methods (2 pts)
            if (hasMethod(managerClass, "sortEventsByDate") &&
                hasMethod(managerClass, "sortEventsByPrice") &&
                hasMethod(managerClass, "sortEventsByCapacity")) {
                score += 2.0;
                System.out.println("✓ Sorting methods present (2.0)");
                testResults.put("Sorting", true);
            } else {
                System.out.println("✗ Sorting methods missing");
                testResults.put("Sorting", false);
                score += 1.0; // Partial
            }

            // Check filtering methods (2 pts)
            if (hasMethod(managerClass, "getEventsInDateRange") &&
                hasMethod(managerClass, "getAvailableEvents") &&
                hasMethod(managerClass, "getSoldOutEvents") &&
                hasMethod(managerClass, "getEventsByTypeAndPriceRange")) {
                score += 2.0;
                System.out.println("✓ Filtering methods present (2.0)");
                testResults.put("Filtering", true);
            } else {
                System.out.println("✗ Filtering methods incomplete");
                testResults.put("Filtering", false);
                score += 1.0; // Partial
            }

        } catch (ClassNotFoundException e) {
            testResults.put("Operations", false);
        }

        partCScore = score;
        System.out.printf("Part C Score: %.1f/4.0\n", partCScore);
    }

    private void testPartD_Validation() {
        System.out.println("\n--- Testing Part D: Validation & Exceptions (3 points) ---");

        double score = 0;

        try {
            Class<?> exceptionClass = Class.forName("eventmanagement.EventManagementException");

            // Check extends Exception (1 pt)
            if (Exception.class.isAssignableFrom(exceptionClass)) {
                score += 1.0;
                System.out.println("✓ EventManagementException exists and extends Exception (1.0)");
                testResults.put("Exception.class", true);
            } else {
                System.out.println("✗ Exception class structure incorrect");
                testResults.put("Exception.class", false);
            }

            // Exception usage is tested through validation tests above
            // Award partial credit based on those results
            int validationsPassed = 0;
            int totalValidations = 0;
            for (Map.Entry<String, Boolean> entry : testResults.entrySet()) {
                if (entry.getKey().contains("validation")) {
                    totalValidations++;
                    if (entry.getValue()) validationsPassed++;
                }
            }

            if (totalValidations > 0) {
                double validationScore = (validationsPassed / (double) totalValidations) * 2.0;
                score += validationScore;
                System.out.printf("✓ Exception usage: %d/%d validations (%.1f)\n",
                    validationsPassed, totalValidations, validationScore);
                testResults.put("Exception.usage", validationsPassed >= totalValidations / 2);
            } else {
                score += 1.0; // Assume some usage
                testResults.put("Exception.usage", true);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("✗ EventManagementException class not found");
            testResults.put("Exception", false);
        }

        partDScore = score;
        System.out.printf("Part D Score: %.1f/3.0\n", partDScore);
    }

    private void analyzeCodeQuality() {
        System.out.println("\n--- Analyzing Code Quality ---");

        // Check for public fields (deduction)
        int publicFields = countPublicFields();
        if (publicFields > 0) {
            System.out.println("⚠ Found " + publicFields + " public fields (should be private)");
            suspiciousPatterns.add(publicFields + " public fields found (poor encapsulation)");
        }

        // Check for raw types (deduction)
        if (hasRawTypes()) {
            System.out.println("⚠ Raw types detected (missing generic type parameters)");
            suspiciousPatterns.add("Raw types used (e.g., ArrayList without <T>)");
        }

        // Check package declaration
        if (!hasPackageDeclaration()) {
            System.out.println("⚠ Missing package declaration in some files");
            suspiciousPatterns.add("Package declaration missing");
        }
    }

    private void detectSuspiciousPatterns() {
        System.out.println("\n--- Detecting Suspicious Patterns ---");

        // Check comment-to-code ratio
        int totalComments = countComments();
        int totalLines = countCodeLines();

        if (totalLines > 0) {
            double commentRatio = totalComments / (double) totalLines;

            if (commentRatio < 0.05 && partAScore + partBScore + partCScore + partDScore > 15) {
                suspiciousPatterns.add("High score (" + String.format("%.1f", partAScore + partBScore + partCScore + partDScore) +
                    "/20) but very few comments (" + totalComments + ")");
                System.out.println("⚠ Suspiciously few comments for high score");
            }

            if (commentRatio > 0.4) {
                System.out.println("ℹ High comment density (possibly over-commented)");
            }
        }

        // Check for generic variable names
        if (hasGenericVariableNames()) {
            suspiciousPatterns.add("Generic variable names detected (e.g., list1, map2, set3)");
            System.out.println("⚠ Generic variable names suggest AI-generated code");
        }

        System.out.println("Suspicious patterns detected: " + suspiciousPatterns.size());
    }

    private void checkBonusFeatures() {
        System.out.println("\n--- Checking Bonus Features ---");

        try {
            Class<?> managerClass = Class.forName("eventmanagement.EventManager");

            // Check for various bonus methods
            if (hasMethod(managerClass, "transferTicket")) {
                bonusScore += 3.0;
                System.out.println("✓ Ticket transfer feature (+3)");
            }

            if (hasMethod(managerClass, "cancelEvent")) {
                bonusScore += 3.0;
                System.out.println("✓ Event cancellation feature (+3)");
            }

            if (hasMethod(managerClass, "addToWaitingList")) {
                bonusScore += 3.0;
                System.out.println("✓ Waiting list feature (+3)");
            }

            if (hasMethod(managerClass, "getRevenueBreakdown")) {
                bonusScore += 3.0;
                System.out.println("✓ Revenue breakdown feature (+3)");
            }

            if (hasMethod(managerClass, "exportEventsToCSV")) {
                bonusScore += 3.0;
                System.out.println("✓ CSV export feature (+3)");
            }

            // Check for enhanced toString
            Class<?> eventClass = Class.forName("eventmanagement.Event");
            try {
                Method toString = eventClass.getMethod("toString");
                if (toString.getDeclaringClass() == eventClass) {
                    bonusScore += 1.0;
                    System.out.println("✓ Enhanced toString (+1)");
                }
            } catch (NoSuchMethodException e) {
                // toString not overridden
            }

        } catch (ClassNotFoundException e) {
            // Classes not found
        }

        System.out.printf("Bonus Score: %.1f\n", bonusScore);
    }

    private void printFinalSummary() {
        double baseScore = compilationScore + partAScore + partBScore + partCScore + partDScore;
        double totalScore = baseScore + bonusScore;
        double percentage = (totalScore / 20.0) * 100;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("FINAL GRADING SUMMARY");
        System.out.println("=".repeat(60));
        System.out.printf("Compilation:     %.1f / 4.0\n", compilationScore);
        System.out.printf("Part A:          %.1f / 4.0\n", partAScore);
        System.out.printf("Part B:          %.1f / 9.0\n", partBScore);
        System.out.printf("Part C:          %.1f / 4.0\n", partCScore);
        System.out.printf("Part D:          %.1f / 3.0\n", partDScore);
        System.out.println("-".repeat(60));
        System.out.printf("Base Score:      %.1f / 20.0\n", baseScore);
        System.out.printf("Bonus Score:     %.1f\n", bonusScore);
        System.out.printf("Total Score:     %.1f\n", totalScore);
        System.out.printf("Percentage:      %.1f%%\n", percentage);
        System.out.println("=".repeat(60));

        if (!suspiciousPatterns.isEmpty()) {
            System.out.println("\n⚠ DEFENSE REQUIRED - Suspicious Patterns Detected:");
            for (String pattern : suspiciousPatterns) {
                System.out.println("  - " + pattern);
            }
        }
    }

    private void generateCSVReport(String outputPath) {
        try {
            double baseScore = compilationScore + partAScore + partBScore + partCScore + partDScore;
            double totalScore = baseScore + bonusScore;
            double percentage = (totalScore / 20.0) * 100;

            // Build detailed comments
            StringBuilder fullComments = new StringBuilder();
            fullComments.append(String.format("Compilation: %.1f/4 ", compilationScore));
            fullComments.append(comments.toString());
            fullComments.append(String.format("PartA: %.1f/4 | ", partAScore));
            fullComments.append(String.format("PartB: %.1f/9 | ", partBScore));
            fullComments.append(String.format("PartC: %.1f/4 | ", partCScore));
            fullComments.append(String.format("PartD: %.1f/3 | ", partDScore));

            if (bonusScore > 0) {
                fullComments.append(String.format("Bonus: +%.1f | ", bonusScore));
            }

            if (!suspiciousPatterns.isEmpty()) {
                fullComments.append("⚠ DEFENSE REQUIRED: ");
                fullComments.append(String.join("; ", suspiciousPatterns));
            }

            // CSV format: Assignment;Type;Student;Compilation;PartA;PartB;PartC;PartD;Main;BaseScore;MaxBaseScore;Bonus;TotalScore;Percentage;Comments
            String csvLine = String.format(
                "Homework 2 - Events;Homework;%s;%.1f;%.1f;%.1f;%.1f;%.1f;0;%.1f;20;%.1f;%.1f;%.2f;\"%s\"",
                studentId,
                compilationScore,
                partAScore,
                partBScore,
                partCScore,
                partDScore,
                baseScore,
                bonusScore,
                totalScore,
                percentage,
                fullComments.toString().replace("\"", "\"\"") // Escape quotes
            );

            // Write to file
            Files.write(Paths.get(outputPath), csvLine.getBytes());
            System.out.println("\n✓ CSV report generated: " + outputPath);

        } catch (IOException e) {
            System.err.println("✗ Failed to write CSV report: " + e.getMessage());
        }
    }

    // ========== Helper Methods ==========

    private boolean hasPrivateField(Class<?> clazz, String fieldName) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(fieldName) && Modifier.isPrivate(field.getModifiers())) {
                    return true;
                }
            }
        } catch (Exception e) {
            // Field not found
        }
        return false;
    }

    private boolean hasMethod(Class<?> clazz, String methodName) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasFieldOfType(Class<?> clazz, String typeName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().getName().contains(typeName) ||
                field.getGenericType().toString().contains(typeName)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasRequiredComment(String fileName, String commentKeyword) {
        try {
            String filePath = submissionPath + "/eventmanagement/" + fileName;
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            return content.contains(commentKeyword);
        } catch (IOException e) {
            return false;
        }
    }

    private int countPublicFields() {
        int count = 0;
        try {
            File pkgDir = new File(submissionPath + "/eventmanagement");
            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (javaFiles != null) {
                for (File file : javaFiles) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    // Simple regex to find public fields (not foolproof but good enough)
                    String[] lines = content.split("\n");
                    for (String line : lines) {
                        if (line.trim().startsWith("public") &&
                            !line.contains("(") && // Not a method
                            !line.contains("class") && // Not a class declaration
                            line.contains(";")) { // Ends with semicolon (field)
                            count++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Error reading files
        }
        return count;
    }

    private boolean hasRawTypes() {
        try {
            File pkgDir = new File(submissionPath + "/eventmanagement");
            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (javaFiles != null) {
                for (File file : javaFiles) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    // Look for ArrayList, HashMap, HashSet without <>
                    if (content.matches(".*\\b(ArrayList|HashMap|HashSet)\\s+[a-zA-Z].*") &&
                        !content.matches(".*\\b(ArrayList|HashMap|HashSet)<.*>.*")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // Error reading files
        }
        return false;
    }

    private boolean hasPackageDeclaration() {
        try {
            File pkgDir = new File(submissionPath + "/eventmanagement");
            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (javaFiles != null) {
                for (File file : javaFiles) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    if (!content.contains("package eventmanagement;")) {
                        return false;
                    }
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int countComments() {
        int count = 0;
        try {
            File pkgDir = new File(submissionPath + "/eventmanagement");
            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (javaFiles != null) {
                for (File file : javaFiles) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    count += content.split("//").length - 1; // Single line comments
                    count += content.split("/\\*").length - 1; // Multi-line comments
                }
            }
        } catch (IOException e) {
            // Error reading files
        }
        return count;
    }

    private int countCodeLines() {
        int count = 0;
        try {
            File pkgDir = new File(submissionPath + "/eventmanagement");
            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (javaFiles != null) {
                for (File file : javaFiles) {
                    List<String> lines = Files.readAllLines(file.toPath());
                    for (String line : lines) {
                        if (!line.trim().isEmpty() && !line.trim().startsWith("//") && !line.trim().startsWith("/*")) {
                            count++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Error reading files
        }
        return count;
    }

    private boolean hasGenericVariableNames() {
        try {
            File pkgDir = new File(submissionPath + "/eventmanagement");
            File[] javaFiles = pkgDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (javaFiles != null) {
                for (File file : javaFiles) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    // Look for generic names like list1, map2, set3, etc.
                    if (content.matches(".*(list|map|set|array)\\d+.*")) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // Error reading files
        }
        return false;
    }
}
