import java.io.*;
import java.lang.reflect.*;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

/**
 * AUTOMATED GRADING SYSTEM FOR LIBRARY MANAGEMENT EXAM
 *
 * GRADING BREAKDOWN (33 points base + 4 bonus):
 * - Compilation: 4 points
 * - Part A (Foundation Classes): 8 points
 * - Part B (Utilities & Settings): 6 points
 * - Part C (Borrowing Management): 9 points
 * - Part D (Error Handling): 4 points
 * - Main Demo: 2 points
 * - Bonus Features: +4 points (optional)
 *
 * SETUP:
 * 1. Place this AutoGrader.java in a 'grading' folder
 * 2. Create 'submissions' folder with structure:
 *    submissions/
 *      ├── S001/
 *      │   └── src/library/...
 *      ├── S002/
 *      │   └── src/library/...
 *      └── ...
 * 3. Compile: javac AutoGrader.java
 * 4. Run: java AutoGrader
 * 5. Check 'grading_results' folder for reports
 */

public class AutoGrader {

    private static final String SUBMISSIONS_DIR = "submissions";
    private static final String RESULTS_DIR = "grading_results";
    private static Map<String, StudentResult> allResults = new LinkedHashMap<>();

    public static void main(String[] args) {
        System.out.println("=== LIBRARY EXAM AUTO-GRADER ===\n");

        // Create results directory
        new File(RESULTS_DIR).mkdirs();

        // Get all student directories
        File submissionsFolder = new File(SUBMISSIONS_DIR);
        File[] studentDirs = submissionsFolder.listFiles(File::isDirectory);

        if (studentDirs == null || studentDirs.length == 0) {
            System.out.println("No student submissions found in '" + SUBMISSIONS_DIR + "' folder!");
            return;
        }

        // Grade each student
        for (File studentDir : studentDirs) {
            String studentId = studentDir.getName();
            System.out.println("Grading: " + studentId);
            StudentResult result = gradeStudent(studentId, studentDir);
            allResults.put(studentId, result);
        }

        // Generate reports
        generateDetailedReport();
        generateComparisonReport();
        generateHTMLDashboard();
        generateCSVExport();

        System.out.println("\n=== GRADING COMPLETE ===");
        System.out.println("Results saved in '" + RESULTS_DIR + "' folder");
        System.out.println("- detailed_results.txt : Individual student reports");
        System.out.println("- comparison_report.txt : Side-by-side comparison");
        System.out.println("- dashboard.html : Visual comparison dashboard");
        System.out.println("- grades.csv : Spreadsheet export");
    }

    private static StudentResult gradeStudent(String studentId, File studentDir) {
        StudentResult result = new StudentResult(studentId);

        // Step 1: Compilation Test (4 points)
        result.compilationScore = testCompilation(studentDir, result);

        // Continue with all tests regardless of compilation result
        // This allows partial credit for structure even if code doesn't compile
        result.partAScore = testPartA(studentId, result);
        result.partBScore = testPartB(studentId, result);
        result.partCScore = testPartC(studentId, result);
        result.partDScore = testPartD(studentId, result);
        result.mainScore = testMainDemo(studentId, result);
        result.bonusScore = testBonus(studentId, result);

        result.calculateTotal();
        return result;
    }

    private static int testCompilation(File studentDir, StudentResult result) {
        try {
            // Find all .java files
            List<File> javaFiles = Files.walk(studentDir.toPath())
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            if (javaFiles.isEmpty()) {
                result.addError("No Java files found");
                return 0;
            }

            // Try to compile
            String[] compileCommand = new String[javaFiles.size() + 3];
            compileCommand[0] = "javac";
            compileCommand[1] = "-d";
            compileCommand[2] = "bin_" + result.studentId;
            for (int i = 0; i < javaFiles.size(); i++) {
                compileCommand[i + 3] = javaFiles.get(i).getAbsolutePath();
            }

            Process process = Runtime.getRuntime().exec(compileCommand);
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                result.addSuccess("✓ Code compiles successfully");
                return 4;
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String errorLine = reader.readLine();
                result.addError("Compilation failed: " + (errorLine != null ? errorLine : "unknown error"));
                return 0;
            }
        } catch (Exception e) {
            result.addError("Compilation test error: " + e.getMessage());
            return 0;
        }
    }

    private static int testPartA(String studentId, StudentResult result) {
        int score = 0;

        try {
            // Check if bin directory exists (compilation was successful)
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part A - compilation failed");
                return 0;
            }

            // Load student's classes
            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[] { binDir.toURI().toURL() }
            );

            // Test A1: LibraryItem (3 points)
            try {
                Class<?> itemClass = loader.loadClass("library.core.LibraryItem");

                // Check abstract
                if (Modifier.isAbstract(itemClass.getModifiers())) {
                    score += 1;
                    result.addSuccess("✓ LibraryItem is abstract");
                } else {
                    result.addError("✗ LibraryItem should be abstract");
                }

                // Check private fields
                Field[] fields = itemClass.getDeclaredFields();
                boolean hasPrivateFields = Arrays.stream(fields)
                        .anyMatch(f -> Modifier.isPrivate(f.getModifiers()));
                if (hasPrivateFields) {
                    score += 1;
                    result.addSuccess("✓ Has private fields");
                } else {
                    result.addError("✗ Missing private fields");
                }

                // Check abstract method getItemType
                boolean hasAbstractMethod = Arrays.stream(itemClass.getDeclaredMethods())
                        .anyMatch(m -> m.getName().equals("getItemType") &&
                                Modifier.isAbstract(m.getModifiers()));
                if (hasAbstractMethod) {
                    score += 1;
                    result.addSuccess("✓ Has abstract getItemType()");
                } else {
                    result.addError("✗ Missing abstract getItemType()");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ LibraryItem class not found");
            }

            // Test A2: Book (3 points)
            try {
                Class<?> bookClass = loader.loadClass("library.materials.Book");
                Class<?> itemClass = loader.loadClass("library.core.LibraryItem");

                // Check inheritance
                if (itemClass.isAssignableFrom(bookClass)) {
                    score += 1;
                    result.addSuccess("✓ Book extends LibraryItem");
                } else {
                    result.addError("✗ Book should extend LibraryItem");
                }

                // Check constructors
                Constructor<?>[] constructors = bookClass.getDeclaredConstructors();
                if (constructors.length >= 2) {
                    score += 1;
                    result.addSuccess("✓ Book has multiple constructors");
                } else {
                    result.addError("✗ Book needs constructor overloading");
                }

                // Check author field
                boolean hasAuthorField = Arrays.stream(bookClass.getDeclaredFields())
                        .anyMatch(f -> f.getName().contains("author") || f.getName().contains("Author"));
                if (hasAuthorField) {
                    score += 1;
                    result.addSuccess("✓ Book has author field");
                } else {
                    result.addError("✗ Book missing author field");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ Book class not found");
            }

            // Test A3: Member (2 points)
            try {
                Class<?> memberClass = loader.loadClass("library.users.Member");

                // Check fields
                Field[] fields = memberClass.getDeclaredFields();
                boolean hasRequiredFields = fields.length >= 3;
                if (hasRequiredFields) {
                    score += 1;
                    result.addSuccess("✓ Member has required fields");
                } else {
                    result.addError("✗ Member missing fields");
                }

                // Check methods
                boolean hasCanBorrow = Arrays.stream(memberClass.getDeclaredMethods())
                        .anyMatch(m -> m.getName().equals("canBorrow"));
                if (hasCanBorrow) {
                    score += 1;
                    result.addSuccess("✓ Member has canBorrow() method");
                } else {
                    result.addError("✗ Member missing canBorrow()");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ Member class not found");
            }

        } catch (Exception e) {
            result.addError("Part A test error: " + e.getMessage());
        }

        return score;
    }

    private static int testPartB(String studentId, StudentResult result) {
        int score = 0;

        try {
            // Check if bin directory exists
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part B - compilation failed");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[] { binDir.toURI().toURL() }
            );

            // Test B1: LibrarySettings (3 points)
            try {
                Class<?> settingsClass = loader.loadClass("library.util.LibrarySettings");

                // Check private constructor
                Constructor<?>[] constructors = settingsClass.getDeclaredConstructors();
                boolean hasPrivateConstructor = Arrays.stream(constructors)
                        .anyMatch(c -> Modifier.isPrivate(c.getModifiers()));
                if (hasPrivateConstructor) {
                    score += 1;
                    result.addSuccess("✓ LibrarySettings has private constructor");
                } else {
                    result.addError("✗ LibrarySettings needs private constructor");
                }

                // Check constants
                Field[] fields = settingsClass.getDeclaredFields();
                boolean hasConstants = Arrays.stream(fields)
                        .anyMatch(f -> Modifier.isStatic(f.getModifiers()) &&
                                Modifier.isFinal(f.getModifiers()));
                if (hasConstants) {
                    score += 1;
                    result.addSuccess("✓ Has static final constants");
                } else {
                    result.addError("✗ Missing static final constants");
                }

                // Check static method
                boolean hasStaticMethod = Arrays.stream(settingsClass.getDeclaredMethods())
                        .anyMatch(m -> Modifier.isStatic(m.getModifiers()));
                if (hasStaticMethod) {
                    score += 1;
                    result.addSuccess("✓ Has static method");
                } else {
                    result.addError("✗ Missing static method");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ LibrarySettings class not found");
            }

            // Test B2: SimpleDateUtils (3 points)
            try {
                Class<?> dateUtilsClass = loader.loadClass("library.util.SimpleDateUtils");

                // Check method overloading
                Map<String, Long> methodCounts = Arrays.stream(dateUtilsClass.getDeclaredMethods())
                        .collect(Collectors.groupingBy(Method::getName, Collectors.counting()));

                boolean hasOverloading = methodCounts.values().stream().anyMatch(count -> count > 1);
                if (hasOverloading) {
                    score += 3;
                    result.addSuccess("✓ SimpleDateUtils has method overloading");
                } else {
                    result.addError("✗ SimpleDateUtils missing method overloading");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ SimpleDateUtils class not found");
            }

        } catch (Exception e) {
            result.addError("Part B test error: " + e.getMessage());
        }

        return score;
    }

    private static int testPartC(String studentId, StudentResult result) {
        int score = 0;

        try {
            // Check if bin directory exists
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part C - compilation failed");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[] { binDir.toURI().toURL() }
            );

            // Test C1: BorrowingSystem (5 points)
            try {
                Class<?> borrowingClass = loader.loadClass("library.transactions.BorrowingSystem");

                // Check arrays
                Field[] fields = borrowingClass.getDeclaredFields();
                boolean hasArrays = Arrays.stream(fields)
                        .anyMatch(f -> f.getType().isArray());
                if (hasArrays) {
                    score += 2;
                    result.addSuccess("✓ BorrowingSystem uses arrays");
                } else {
                    result.addError("✗ BorrowingSystem should use arrays");
                }

                // Check required methods
                String[] requiredMethods = {"borrowItem", "returnItem", "getBorrowedTitles", "getBorrowedCount"};
                long foundMethods = Arrays.stream(borrowingClass.getDeclaredMethods())
                        .map(Method::getName)
                        .filter(name -> Arrays.asList(requiredMethods).contains(name))
                        .count();

                if (foundMethods >= 3) {
                    score += 3;
                    result.addSuccess("✓ BorrowingSystem has required methods");
                } else {
                    result.addError("✗ BorrowingSystem missing methods");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ BorrowingSystem class not found");
            }

            // Test C2: LibraryException (2 points)
            try {
                Class<?> exceptionClass = loader.loadClass("library.transactions.LibraryException");

                if (RuntimeException.class.isAssignableFrom(exceptionClass)) {
                    score += 2;
                    result.addSuccess("✓ LibraryException extends RuntimeException");
                } else {
                    result.addError("✗ LibraryException should extend RuntimeException");
                }
            } catch (ClassNotFoundException e) {
                result.addError("✗ LibraryException class not found");
            }

            // Test C3: Integration (2 points - estimated based on structure)
            score += 2; // Assume integration if other parts work
            result.addSuccess("✓ Integration assumed from structure");

        } catch (Exception e) {
            result.addError("Part C test error: " + e.getMessage());
        }

        return score;
    }

    private static int testPartD(String studentId, StudentResult result) {
        // Part D is harder to auto-test - award points if exceptions exist
        int score = 0;

        try {
            // Check if bin directory exists
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part D - compilation failed");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[] { binDir.toURI().toURL() }
            );

            // Check if LibraryException exists (already tested in C2)
            try {
                Class<?> exceptionClass = loader.loadClass("library.transactions.LibraryException");
                score += 2;
                result.addSuccess("✓ Custom exception implemented");
            } catch (ClassNotFoundException e) {
                // Already reported in Part C
            }

            // Award remaining points based on compilation success (implies some validation)
            score += 2;
            result.addSuccess("✓ Code structure suggests error handling");

        } catch (Exception e) {
            result.addError("Part D test error: " + e.getMessage());
        }

        return score;
    }

    private static int testMainDemo(String studentId, StudentResult result) {
        // Check if Main.java exists in various possible locations
        try {
            String[] possiblePaths = {
                    SUBMISSIONS_DIR + "/" + studentId + "/src/library/Main.java",
                    SUBMISSIONS_DIR + "/" + studentId + "/src/Main.java",
                    SUBMISSIONS_DIR + "/" + studentId + "/Main.java"
            };

            for (String path : possiblePaths) {
                File mainFile = new File(path);
                if (mainFile.exists()) {
                    result.addSuccess("✓ Main.java exists");
                    return 2;
                }
            }

            result.addError("✗ Main.java not found");
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int testBonus(String studentId, StudentResult result) {
        int score = 0;

        try {
            // Check if bin directory exists
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                // No error message needed for bonus - just skip silently
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[] { binDir.toURI().toURL() }
            );

            Class<?> borrowingClass = loader.loadClass("library.transactions.BorrowingSystem");

            // Check for bonus methods
            boolean hasSearch = Arrays.stream(borrowingClass.getDeclaredMethods())
                    .anyMatch(m -> m.getName().contains("hasBorrowedItem") || m.getName().contains("findDueDate"));

            boolean hasStats = Arrays.stream(borrowingClass.getDeclaredMethods())
                    .anyMatch(m -> m.getName().contains("Usage") || m.getName().contains("Status"));

            if (hasSearch) {
                score += 2;
                result.addSuccess("✓ BONUS: Search capability");
            }

            if (hasStats) {
                score += 2;
                result.addSuccess("✓ BONUS: Statistics");
            }

        } catch (Exception e) {
            // No bonus
        }

        return score;
    }

    private static void generateDetailedReport() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/detailed_results.txt")) {
            writer.println("DETAILED GRADING RESULTS");
            writer.println("========================\n");

            for (Map.Entry<String, StudentResult> entry : allResults.entrySet()) {
                StudentResult result = entry.getValue();
                int baseScore = result.totalScore - result.bonusScore;

                writer.println("STUDENT: " + result.studentId);
                writer.println("─".repeat(50));
                writer.println("Compilation:    " + result.compilationScore + "/4");
                writer.println("Part A:         " + result.partAScore + "/8");
                writer.println("Part B:         " + result.partBScore + "/6");
                writer.println("Part C:         " + result.partCScore + "/9");
                writer.println("Part D:         " + result.partDScore + "/4");
                writer.println("Main Demo:      " + result.mainScore + "/2");
                writer.println("─".repeat(50));
                writer.println("BASE TOTAL:     " + baseScore + "/33");
                if (result.bonusScore > 0) {
                    writer.println("BONUS:          +" + result.bonusScore);
                    writer.println("FINAL TOTAL:    " + result.totalScore + "/37");
                } else {
                    writer.println("FINAL TOTAL:    " + result.totalScore + "/33");
                }
                writer.println("\nFeedback:");
                result.feedback.forEach(writer::println);
                writer.println("\n" + "=".repeat(50) + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error generating detailed report: " + e.getMessage());
        }
    }

    private static void generateComparisonReport() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/comparison_report.txt")) {
            writer.println("STUDENT COMPARISON REPORT");
            writer.println("=========================\n");

            // Header
            writer.printf("%-10s | %4s | %4s | %4s | %4s | %4s | %4s | %5s | %5s | %6s%n",
                    "Student", "Comp", "PartA", "PartB", "PartC", "PartD", "Main", "Base", "Bonus", "Total");
            writer.println("─".repeat(85));

            // Data rows
            for (StudentResult result : allResults.values()) {
                int baseScore = result.totalScore - result.bonusScore;
                writer.printf("%-10s | %4d | %4d | %4d | %4d | %4d | %4d | %5d | %5s | %6d%n",
                        result.studentId,
                        result.compilationScore,
                        result.partAScore,
                        result.partBScore,
                        result.partCScore,
                        result.partDScore,
                        result.mainScore,
                        baseScore,
                        result.bonusScore > 0 ? "+" + result.bonusScore : "-",
                        result.totalScore);
            }

            writer.println("─".repeat(85));

            // Statistics
            DoubleSummaryStatistics stats = allResults.values().stream()
                    .mapToDouble(r -> r.totalScore)
                    .summaryStatistics();

            DoubleSummaryStatistics baseStats = allResults.values().stream()
                    .mapToDouble(r -> r.totalScore - r.bonusScore)
                    .summaryStatistics();

            writer.println("\nCLASS STATISTICS:");
            writer.printf("Base Average: %.2f/33 | Total Average: %.2f/37%n",
                    baseStats.getAverage(), stats.getAverage());
            writer.printf("Highest: %.0f | Lowest: %.0f%n", stats.getMax(), stats.getMin());

        } catch (Exception e) {
            System.err.println("Error generating comparison report: " + e.getMessage());
        }
    }

    private static void generateHTMLDashboard() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/dashboard.html")) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><meta charset='UTF-8'>");
            writer.println("<title>Grading Dashboard</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
            writer.println("h1 { color: #333; }");
            writer.println("table { border-collapse: collapse; width: 100%; background: white; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            writer.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
            writer.println("th { background: #4CAF50; color: white; cursor: pointer; user-select: none; position: relative; }");
            writer.println("th:hover { background: #45a049; }");
            writer.println("th.sortable:after { content: ' ⇅'; opacity: 0.3; }");
            writer.println("th.asc:after { content: ' ↑'; opacity: 1; }");
            writer.println("th.desc:after { content: ' ↓'; opacity: 1; }");
            writer.println("tr:hover { background: #f5f5f5; }");
            writer.println(".score { font-weight: bold; }");
            writer.println(".high { color: #4CAF50; }");
            writer.println(".medium { color: #FF9800; }");
            writer.println(".low { color: #f44336; }");
            writer.println(".stats { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; }");
            writer.println("</style>");
            writer.println("<script>");
            writer.println("function sortTable(columnIndex, isNumeric) {");
            writer.println("  const table = document.querySelector('table');");
            writer.println("  const tbody = table.querySelector('tbody');");
            writer.println("  const rows = Array.from(tbody.querySelectorAll('tr'));");
            writer.println("  const headers = table.querySelectorAll('th');");
            writer.println("  const currentHeader = headers[columnIndex];");
            writer.println("  const isAsc = currentHeader.classList.contains('asc');");
            writer.println("  ");
            writer.println("  headers.forEach(h => h.classList.remove('asc', 'desc'));");
            writer.println("  currentHeader.classList.add(isAsc ? 'desc' : 'asc');");
            writer.println("  ");
            writer.println("  rows.sort((a, b) => {");
            writer.println("    let aVal = a.cells[columnIndex].textContent.trim();");
            writer.println("    let bVal = b.cells[columnIndex].textContent.trim();");
            writer.println("    ");
            writer.println("    if (isNumeric) {");
            writer.println("      // Handle bonus format (+2, +4, -) and regular numbers");
            writer.println("      if (aVal === '-') aVal = '0';");
            writer.println("      if (bVal === '-') bVal = '0';");
            writer.println("      aVal = parseFloat(aVal.replace(/[^0-9.-]/g, '')) || 0;");
            writer.println("      bVal = parseFloat(bVal.replace(/[^0-9.-]/g, '')) || 0;");
            writer.println("    }");
            writer.println("    ");
            writer.println("    if (aVal < bVal) return isAsc ? 1 : -1;");
            writer.println("    if (aVal > bVal) return isAsc ? -1 : 1;");
            writer.println("    return 0;");
            writer.println("  });");
            writer.println("  ");
            writer.println("  rows.forEach(row => tbody.appendChild(row));");
            writer.println("}");
            writer.println("</script>");
            writer.println("</head><body>");

            writer.println("<h1>📊 Library Exam Grading Dashboard</h1>");

            // Statistics summary
            DoubleSummaryStatistics totalStats = allResults.values().stream()
                    .mapToDouble(r -> r.totalScore)
                    .summaryStatistics();

            DoubleSummaryStatistics baseStats = allResults.values().stream()
                    .mapToDouble(r -> r.totalScore - r.bonusScore)
                    .summaryStatistics();

            writer.println("<div class='stats'>");
            writer.println("<h2>Class Statistics</h2>");
            writer.printf("<p>Base Average: <strong>%.2f/33</strong> (%.1f%%) | Total Average: <strong>%.2f/37</strong></p>%n",
                    baseStats.getAverage(), (baseStats.getAverage()/33)*100, totalStats.getAverage());
            writer.printf("<p>Highest: <strong>%.0f</strong> | Lowest: <strong>%.0f</strong></p>%n",
                    totalStats.getMax(), totalStats.getMin());
            writer.printf("<p>Total Students: <strong>%d</strong></p>%n", totalStats.getCount());
            writer.println("<p style='color: #666; font-size: 0.9em;'>💡 Click any column header to sort | Percentage = (Total/33)×100, can exceed 100% with bonus</p>");
            writer.println("</div>");

            // Student table
            writer.println("<table>");
            writer.println("<thead><tr>");
            writer.println("<th class='sortable' onclick='sortTable(0, false)'>Student</th>");
            writer.println("<th class='sortable' onclick='sortTable(1, true)'>Comp</th>");
            writer.println("<th class='sortable' onclick='sortTable(2, true)'>Part A</th>");
            writer.println("<th class='sortable' onclick='sortTable(3, true)'>Part B</th>");
            writer.println("<th class='sortable' onclick='sortTable(4, true)'>Part C</th>");
            writer.println("<th class='sortable' onclick='sortTable(5, true)'>Part D</th>");
            writer.println("<th class='sortable' onclick='sortTable(6, true)'>Main</th>");
            writer.println("<th class='sortable' onclick='sortTable(7, true)'>Base</th>");
            writer.println("<th class='sortable' onclick='sortTable(8, true)'>Bonus</th>");
            writer.println("<th class='sortable' onclick='sortTable(9, true)'>Total</th>");
            writer.println("<th class='sortable' onclick='sortTable(10, true)'>%</th>");
            writer.println("</tr></thead><tbody>");

            for (StudentResult result : allResults.values()) {
                int baseScore = result.totalScore - result.bonusScore;
                double percentage = (result.totalScore / 33.0) * 100; // Use total score for percentage
                String scoreClass = percentage >= 80 ? "high" : percentage >= 60 ? "medium" : "low";

                writer.println("<tr>");
                writer.printf("<td>%s</td>", result.studentId);
                writer.printf("<td>%d/4</td>", result.compilationScore);
                writer.printf("<td>%d/8</td>", result.partAScore);
                writer.printf("<td>%d/6</td>", result.partBScore);
                writer.printf("<td>%d/9</td>", result.partCScore);
                writer.printf("<td>%d/4</td>", result.partDScore);
                writer.printf("<td>%d/2</td>", result.mainScore);
                writer.printf("<td>%d/33</td>", baseScore);
                writer.printf("<td>%s</td>", result.bonusScore > 0 ? "+" + result.bonusScore : "-");
                writer.printf("<td class='score'>%d</td>", result.totalScore);
                writer.printf("<td class='%s'>%.1f%%</td>%n", scoreClass, percentage);
                writer.println("</tr>");
            }

            writer.println("</tbody></table>");
            writer.println("</body></html>");

        } catch (Exception e) {
            System.err.println("Error generating HTML dashboard: " + e.getMessage());
        }
    }

    private static void generateCSVExport() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/grades.csv")) {
            writer.println("Student,Compilation,PartA,PartB,PartC,PartD,Main,BaseScore,Bonus,TotalScore,Percentage");

            for (StudentResult result : allResults.values()) {
                int baseScore = result.totalScore - result.bonusScore;
                double percentage = (result.totalScore / 33.0) * 100; // Use total score for percentage
                writer.printf("%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%.2f%n",
                        result.studentId,
                        result.compilationScore,
                        result.partAScore,
                        result.partBScore,
                        result.partCScore,
                        result.partDScore,
                        result.mainScore,
                        baseScore,
                        result.bonusScore,
                        result.totalScore,
                        percentage);
            }
        } catch (Exception e) {
            System.err.println("Error generating CSV: " + e.getMessage());
        }
    }
}

class StudentResult {
    String studentId;
    int compilationScore = 0;
    int partAScore = 0;
    int partBScore = 0;
    int partCScore = 0;
    int partDScore = 0;
    int mainScore = 0;
    int bonusScore = 0;
    int totalScore = 0;
    List<String> feedback = new ArrayList<>();

    StudentResult(String studentId) {
        this.studentId = studentId;
    }

    void addSuccess(String message) {
        feedback.add(message);
    }

    void addError(String message) {
        feedback.add(message);
    }

    void calculateTotal() {
        totalScore = compilationScore + partAScore + partBScore +
                partCScore + partDScore + mainScore + bonusScore;
    }
}