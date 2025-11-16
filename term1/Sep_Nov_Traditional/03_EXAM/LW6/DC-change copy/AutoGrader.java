import java.io.*;
import java.lang.reflect.*;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.*;

/**
 * IMPROVED AUTOMATED GRADING SYSTEM FOR LIBRARY MANAGEMENT EXAM
 *
 * KEY IMPROVEMENTS:
 * - Individual class compilation tracking for better partial credit
 * - Proper handling of partially compiling projects
 * - Clear identification of which classes failed and why
 * - Better error reporting and feedback
 * - Skips tests for classes that didn't compile (avoids confusion)
 *
 * GRADING BREAKDOWN (33 points base + 4 bonus):
 * - Compilation: 4 points (partial credit for individual classes)
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

    // Track compilation details per student
    private static Map<String, CompilationDetails> compilationDetails = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("=== LIBRARY EXAM AUTO-GRADER (IMPROVED) ===\n");

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
        generateDetailedReportHTML();
        generateComparisonReport();
        generateHTMLDashboard();
        generateCSVExport();

        System.out.println("\n=== GRADING COMPLETE ===");
        System.out.println("Results saved in '" + RESULTS_DIR + "' folder");
        System.out.println("- detailed_results.html : Interactive detailed feedback (RECOMMENDED)");
        System.out.println("- detailed_results.txt : Text version of detailed feedback");
        System.out.println("- comparison_report.txt : Side-by-side comparison");
        System.out.println("- dashboard.html : Visual comparison dashboard");
        System.out.println("- grades.csv : Spreadsheet export");
    }

    private static StudentResult gradeStudent(String studentId, File studentDir) {
        StudentResult result = new StudentResult(studentId);

        // Step 1: Compilation Test (4 points) - with individual class tracking
        result.compilationScore = testCompilation(studentDir, result);

        // Step 2: Test each part, skipping tests for classes that didn't compile
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
            CompilationDetails details = new CompilationDetails(result.studentId);
            compilationDetails.put(result.studentId, details);

            // Find all .java files
            List<File> javaFiles = Files.walk(studentDir.toPath())
                    .filter(p -> p.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());

            if (javaFiles.isEmpty()) {
                result.addError("✗ No Java files found");
                return 0;
            }

            // Create bin directory
            File binDir = new File("bin_" + result.studentId);
            binDir.mkdirs();

            // Map to track which files are which classes
            Map<String, File> classFiles = new HashMap<>();
            for (File file : javaFiles) {
                String fileName = file.getName();
                classFiles.put(fileName, file);
            }

            // Separate Main.java from core classes
            File mainFile = classFiles.get("Main.java");
            List<File> coreFiles = javaFiles.stream()
                    .filter(f -> !f.getName().equals("Main.java"))
                    .collect(Collectors.toList());

            // Expected core class files for the exam
            String[] expectedClasses = {
                    "LibraryItem.java",
                    "Book.java",
                    "Member.java",
                    "LibrarySettings.java",
                    "SimpleDateUtils.java",
                    "BorrowingSystem.java",
                    "LibraryException.java"
            };

            int score = 0;

            // Strategy 1: Try compiling all core files together (most common case)
            if (!coreFiles.isEmpty()) {
                boolean allCoreCompiled = tryCompileFiles(coreFiles, result.studentId, details);

                if (allCoreCompiled) {
                    // All core classes compiled successfully!
                    score = 3;
                    result.addSuccess("✓ All core classes compile successfully");
                    details.coreClassesCompiled = true;

                    // Mark all core classes as compiled
                    for (File file : coreFiles) {
                        details.compiledClasses.add(file.getName());
                    }

                    // Try compiling Main.java if it exists
                    if (mainFile != null) {
                        boolean mainCompiled = tryCompileFiles(Collections.singletonList(mainFile),
                                result.studentId, details);
                        if (mainCompiled) {
                            score = 4;
                            result.addSuccess("✓ Main.java also compiles successfully");
                            details.compiledClasses.add("Main.java");
                            details.mainCompiled = true;
                        } else {
                            result.addError("✗ Main.java has compilation errors");
                            details.addFailedClass("Main.java", getLastCompilationError(result.studentId));
                        }
                    } else {
                        // No Main.java found, but core is good
                        score = 4; // Full marks for core classes
                        result.addError("⚠ Main.java not found (not required, no penalty)");
                    }
                } else {
                    // Core classes didn't all compile - try individual compilation
                    result.addError("✗ Core classes have compilation errors - trying individual compilation...");

                    // Try compiling each class individually to identify which ones work
                    int compiledCount = 0;
                    for (File file : coreFiles) {
                        boolean compiled = tryCompileFiles(Collections.singletonList(file),
                                result.studentId, details);
                        if (compiled) {
                            compiledCount++;
                            details.compiledClasses.add(file.getName());
                            result.addSuccess("  ✓ " + file.getName() + " compiles");
                        } else {
                            details.addFailedClass(file.getName(), getLastCompilationError(result.studentId));
                            result.addError("  ✗ " + file.getName() + " failed");
                        }
                    }

                    // Award partial credit based on how many compiled
                    if (compiledCount >= 6) {
                        score = 3; // Most classes compiled
                        result.addSuccess("✓ Most core classes compiled (" + compiledCount + "/7)");
                    } else if (compiledCount >= 4) {
                        score = 2; // More than half compiled
                        result.addSuccess("✓ Several core classes compiled (" + compiledCount + "/7)");
                    } else if (compiledCount >= 2) {
                        score = 1; // Some classes compiled
                        result.addSuccess("✓ Some core classes compiled (" + compiledCount + "/7)");
                    } else {
                        score = 0;
                        result.addError("✗ Very few or no classes compiled");
                    }

                    // Try Main.java if it exists and at least some core compiled
                    if (mainFile != null && compiledCount > 0) {
                        boolean mainCompiled = tryCompileFiles(Collections.singletonList(mainFile),
                                result.studentId, details);
                        if (mainCompiled) {
                            details.compiledClasses.add("Main.java");
                            details.mainCompiled = true;
                            result.addSuccess("  ✓ Main.java compiles");
                        } else {
                            details.addFailedClass("Main.java", getLastCompilationError(result.studentId));
                            result.addError("  ✗ Main.java failed");
                        }
                    }
                }
            } else {
                result.addError("✗ No core class files found (only Main.java?)");
            }

            // Final summary
            int totalExpected = expectedClasses.length + (mainFile != null ? 1 : 0);
            int totalCompiled = details.compiledClasses.size();
            result.addSuccess(String.format("Compilation Summary: %d/%d files compiled",
                    totalCompiled, totalExpected));

            return score;

        } catch (Exception e) {
            result.addError("Compilation test error: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    private static boolean tryCompileFiles(List<File> files, String studentId, CompilationDetails details) {
        try {
            if (files.isEmpty()) return false;

            String[] compileCommand = new String[files.size() + 3];
            compileCommand[0] = "javac";
            compileCommand[1] = "-d";
            compileCommand[2] = "bin_" + studentId;
            for (int i = 0; i < files.size(); i++) {
                compileCommand[i + 3] = files.get(i).getAbsolutePath();
            }

            Process process = Runtime.getRuntime().exec(compileCommand);

            // Capture error output
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            StringBuilder errors = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            // Store compilation errors
            if (exitCode != 0 && errors.length() > 0) {
                details.compilationErrors.put(
                        files.stream().map(File::getName).collect(Collectors.joining(", ")),
                        errors.toString()
                );
            }

            return exitCode == 0;

        } catch (Exception e) {
            return false;
        }
    }

    private static String getLastCompilationError(String studentId) {
        CompilationDetails details = compilationDetails.get(studentId);
        if (details == null || details.compilationErrors.isEmpty()) {
            return "Unknown error";
        }

        // Get the most recent error
        String lastError = details.compilationErrors.values().stream()
                .reduce((first, second) -> second)
                .orElse("Unknown error");

        // Extract first error line for brevity
        String[] lines = lastError.split("\n");
        for (String line : lines) {
            if (line.toLowerCase().contains("error:")) {
                String error = line.substring(line.toLowerCase().indexOf("error:") + 6).trim();
                if (error.length() > 80) {
                    error = error.substring(0, 77) + "...";
                }
                return error;
            }
        }

        return "Compilation failed";
    }

    private static boolean hasClassCompiled(String studentId, String className) {
        CompilationDetails details = compilationDetails.get(studentId);
        if (details == null) return false;

        // Check if the Java file compiled
        String javaFile = className.substring(className.lastIndexOf('.') + 1) + ".java";
        return details.compiledClasses.contains(javaFile);
    }

    private static int testPartA(String studentId, StudentResult result) {
        int score = 0;

        try {
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part A - no compiled classes");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[]{binDir.toURI().toURL()}
            );

            // Test A1: LibraryItem (3 points)
            if (hasClassCompiled(studentId, "LibraryItem")) {
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
                    long privateFieldCount = Arrays.stream(fields)
                            .filter(f -> Modifier.isPrivate(f.getModifiers()) &&
                                    !f.getName().startsWith("this$"))
                            .count();

                    if (privateFieldCount >= 2) {
                        score += 1;
                        result.addSuccess("✓ Has required private fields");
                    } else {
                        result.addError("✗ Missing required private fields (found " + privateFieldCount + ", need 2)");
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
                    result.addError("✗ LibraryItem compiled but cannot be loaded (package issue?)");
                }
            } else {
                result.addError("✗ Cannot test LibraryItem - did not compile");
            }

            // Test A2: Book (3 points)
            if (hasClassCompiled(studentId, "Book")) {
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
                        result.addSuccess("✓ Book has multiple constructors (overloading)");
                    } else {
                        result.addError("✗ Book needs constructor overloading (found " + constructors.length + ")");
                    }

                    // Check author field
                    boolean hasAuthorField = Arrays.stream(bookClass.getDeclaredFields())
                            .anyMatch(f -> f.getName().toLowerCase().contains("author") &&
                                    Modifier.isPrivate(f.getModifiers()));
                    if (hasAuthorField) {
                        score += 1;
                        result.addSuccess("✓ Book has private author field");
                    } else {
                        result.addError("✗ Book missing private author field");
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test Book - class loading issue (package/compilation problem)");
                }
            } else {
                result.addError("✗ Cannot test Book - did not compile");
            }

            // Test A3: Member (2 points)
            if (hasClassCompiled(studentId, "Member")) {
                try {
                    Class<?> memberClass = loader.loadClass("library.users.Member");

                    // Check fields
                    Field[] fields = memberClass.getDeclaredFields();
                    long privateFieldCount = Arrays.stream(fields)
                            .filter(f -> Modifier.isPrivate(f.getModifiers()) &&
                                    !f.getName().startsWith("this$"))
                            .count();

                    if (privateFieldCount >= 3) {
                        score += 1;
                        result.addSuccess("✓ Member has required fields (3+)");
                    } else {
                        result.addError("✗ Member missing fields (found " + privateFieldCount + ", need 3)");
                    }

                    // Check canBorrow method
                    boolean hasCanBorrow = Arrays.stream(memberClass.getDeclaredMethods())
                            .anyMatch(m -> m.getName().equals("canBorrow") &&
                                    m.getReturnType() == boolean.class);
                    if (hasCanBorrow) {
                        score += 1;
                        result.addSuccess("✓ Member has canBorrow() method");
                    } else {
                        result.addError("✗ Member missing canBorrow() method");
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test Member - class loading issue");
                }
            } else {
                result.addError("✗ Cannot test Member - did not compile");
            }

        } catch (Exception e) {
            result.addError("Part A test error: " + e.getMessage());
        }

        return score;
    }

    private static int testPartB(String studentId, StudentResult result) {
        int score = 0;

        try {
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part B - no compiled classes");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[]{binDir.toURI().toURL()}
            );

            // Test B1: LibrarySettings (3 points)
            if (hasClassCompiled(studentId, "LibrarySettings")) {
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
                        result.addError("✗ LibrarySettings needs private constructor (utility class)");
                    }

                    // Check constants
                    Field[] fields = settingsClass.getDeclaredFields();
                    long constantCount = Arrays.stream(fields)
                            .filter(f -> Modifier.isStatic(f.getModifiers()) &&
                                    Modifier.isFinal(f.getModifiers()) &&
                                    Modifier.isPublic(f.getModifiers()))
                            .count();

                    if (constantCount >= 3) {
                        score += 1;
                        result.addSuccess("✓ Has required static final constants (3+)");
                    } else {
                        result.addError("✗ Missing constants (found " + constantCount + ", need 3)");
                    }

                    // Check static method
                    boolean hasStaticMethod = Arrays.stream(settingsClass.getDeclaredMethods())
                            .anyMatch(m -> Modifier.isStatic(m.getModifiers()) &&
                                    Modifier.isPublic(m.getModifiers()));
                    if (hasStaticMethod) {
                        score += 1;
                        result.addSuccess("✓ Has static method");
                    } else {
                        result.addError("✗ Missing static method");
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test LibrarySettings - class loading issue");
                }
            } else {
                result.addError("✗ Cannot test LibrarySettings - did not compile");
            }

            // Test B2: SimpleDateUtils (3 points)
            if (hasClassCompiled(studentId, "SimpleDateUtils")) {
                try {
                    Class<?> dateUtilsClass = loader.loadClass("library.util.SimpleDateUtils");

                    // Check private constructor
                    Constructor<?>[] constructors = dateUtilsClass.getDeclaredConstructors();
                    boolean hasPrivateConstructor = Arrays.stream(constructors)
                            .anyMatch(c -> Modifier.isPrivate(c.getModifiers()));
                    if (hasPrivateConstructor) {
                        score += 1;
                        result.addSuccess("✓ SimpleDateUtils has private constructor");
                    }

                    // Check method overloading
                    Map<String, Long> methodCounts = Arrays.stream(dateUtilsClass.getDeclaredMethods())
                            .filter(m -> Modifier.isStatic(m.getModifiers()) &&
                                    Modifier.isPublic(m.getModifiers()))
                            .collect(Collectors.groupingBy(Method::getName, Collectors.counting()));

                    boolean hasOverloading = methodCounts.values().stream().anyMatch(count -> count >= 3);
                    if (hasOverloading) {
                        score += 2;
                        result.addSuccess("✓ SimpleDateUtils has method overloading (3+ versions)");
                    } else {
                        long maxOverload = methodCounts.values().stream().max(Long::compare).orElse(0L);
                        if (maxOverload >= 2) {
                            score += 1;
                            result.addError("✗ SimpleDateUtils has partial overloading (need 3 versions)");
                        } else {
                            result.addError("✗ SimpleDateUtils missing method overloading");
                        }
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test SimpleDateUtils - class loading issue");
                }
            } else {
                result.addError("✗ Cannot test SimpleDateUtils - did not compile");
            }

        } catch (Exception e) {
            result.addError("Part B test error: " + e.getMessage());
        }

        return score;
    }

    private static int testPartC(String studentId, StudentResult result) {
        int score = 0;

        try {
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part C - no compiled classes");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[]{binDir.toURI().toURL()}
            );

            // Test C1: BorrowingSystem (5 points)
            if (hasClassCompiled(studentId, "BorrowingSystem")) {
                try {
                    Class<?> borrowingClass = loader.loadClass("library.transactions.BorrowingSystem");

                    // Check arrays
                    Field[] fields = borrowingClass.getDeclaredFields();
                    long arrayCount = Arrays.stream(fields)
                            .filter(f -> f.getType().isArray() &&
                                    Modifier.isPrivate(f.getModifiers()))
                            .count();

                    if (arrayCount >= 2) {
                        score += 2;
                        result.addSuccess("✓ BorrowingSystem uses arrays (2+)");
                    } else {
                        result.addError("✗ BorrowingSystem should use arrays (found " + arrayCount + ")");
                    }

                    // Check required methods
                    String[] requiredMethods = {"borrowItem", "returnItem", "getBorrowedTitles", "getBorrowedCount"};
                    Set<String> foundMethods = Arrays.stream(borrowingClass.getDeclaredMethods())
                            .map(Method::getName)
                            .collect(Collectors.toSet());

                    int methodScore = 0;
                    for (String method : requiredMethods) {
                        if (foundMethods.contains(method)) {
                            methodScore++;
                        }
                    }

                    if (methodScore >= 4) {
                        score += 3;
                        result.addSuccess("✓ BorrowingSystem has all required methods");
                    } else if (methodScore >= 2) {
                        score += 2;
                        result.addError("✗ BorrowingSystem missing some methods (" + methodScore + "/4)");
                    } else {
                        result.addError("✗ BorrowingSystem missing most methods");
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test BorrowingSystem - class loading issue");
                }
            } else {
                result.addError("✗ Cannot test BorrowingSystem - did not compile");
            }

            // Test C2: LibraryException (2 points)
            if (hasClassCompiled(studentId, "LibraryException")) {
                try {
                    Class<?> exceptionClass = loader.loadClass("library.transactions.LibraryException");

                    if (RuntimeException.class.isAssignableFrom(exceptionClass)) {
                        score += 2;
                        result.addSuccess("✓ LibraryException extends RuntimeException");
                    } else {
                        result.addError("✗ LibraryException should extend RuntimeException");
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test LibraryException - class loading issue");
                }
            } else {
                result.addError("✗ Cannot test LibraryException - did not compile");
            }

            // Test C3: Integration (2 points)
            // Award if both Member and BorrowingSystem exist and have appropriate methods
            if (hasClassCompiled(studentId, "Member") && hasClassCompiled(studentId, "BorrowingSystem")) {
                try {
                    Class<?> memberClass = loader.loadClass("library.users.Member");
                    boolean hasIncrement = Arrays.stream(memberClass.getDeclaredMethods())
                            .anyMatch(m -> m.getName().contains("increment") || m.getName().contains("Increment"));
                    boolean hasDecrement = Arrays.stream(memberClass.getDeclaredMethods())
                            .anyMatch(m -> m.getName().contains("decrement") || m.getName().contains("Decrement"));

                    if (hasIncrement && hasDecrement) {
                        score += 2;
                        result.addSuccess("✓ Integration methods present (increment/decrement)");
                    } else {
                        result.addError("✗ Missing integration methods in Member");
                    }
                } catch (Exception e) {
                    result.addError("✗ Cannot fully test integration");
                }
            } else {
                result.addError("✗ Cannot test integration - required classes didn't compile");
            }

        } catch (Exception e) {
            result.addError("Part C test error: " + e.getMessage());
        }

        return score;
    }

    private static int testPartD(String studentId, StudentResult result) {
        int score = 0;

        try {
            File binDir = new File("bin_" + studentId);
            if (!binDir.exists()) {
                result.addError("✗ Cannot test Part D - no compiled classes");
                return 0;
            }

            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[]{binDir.toURI().toURL()}
            );

            // Check if LibraryException exists (custom exception)
            if (hasClassCompiled(studentId, "LibraryException")) {
                try {
                    Class<?> exceptionClass = loader.loadClass("library.transactions.LibraryException");
                    score += 2;
                    result.addSuccess("✓ Custom exception implemented");

                    // Award additional points for having error handling structure
                    // Check if key classes have validation (check for validateNotBlank or similar)
                    if (hasClassCompiled(studentId, "LibraryItem")) {
                        Class<?> itemClass = loader.loadClass("library.core.LibraryItem");
                        boolean hasValidation = Arrays.stream(itemClass.getDeclaredMethods())
                                .anyMatch(m -> m.getName().contains("validat") || m.getName().contains("Validat"));
                        if (hasValidation) {
                            score += 2;
                            result.addSuccess("✓ Validation methods present");
                        } else {
                            score += 1;
                            result.addError("✗ Limited validation implementation");
                        }
                    } else {
                        score += 1;
                        result.addSuccess("✓ Basic error handling structure present");
                    }
                } catch (ClassNotFoundException e) {
                    result.addError("✗ Cannot test error handling - class loading issue");
                }
            } else {
                result.addError("✗ Cannot test error handling - LibraryException didn't compile");
            }

        } catch (Exception e) {
            result.addError("Part D test error: " + e.getMessage());
        }

        return score;
    }

    private static int testMainDemo(String studentId, StudentResult result) {
        // Check if Main.java compiled
        if (!hasClassCompiled(studentId, "Main")) {
            result.addError("✗ Main.java not found or didn't compile");
            return 0;
        }

        File binDir = new File("bin_" + studentId);
        if (!binDir.exists()) {
            result.addError("✗ Cannot test Main - no bin directory");
            return 0;
        }

        // Check if Main.class exists
        File mainClassInPackage = new File(binDir, "library/Main.class");
        File mainClassInRoot = new File(binDir, "Main.class");

        boolean mainClassExists = mainClassInPackage.exists() || mainClassInRoot.exists();

        if (!mainClassExists) {
            result.addError("✗ Main.java compiled but Main.class not found (check package)");
            return 0;
        }

        // Try to run Main.java
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-cp",
                    binDir.getAbsolutePath(),
                    "library.Main"
            );
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Read output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            int lineCount = 0;

            long startTime = System.currentTimeMillis();
            while ((line = reader.readLine()) != null && lineCount < 100) {
                output.append(line).append("\n");
                lineCount++;

                // Timeout after 5 seconds
                if (System.currentTimeMillis() - startTime > 5000) {
                    process.destroy();
                    break;
                }
            }

            // Wait for process to complete
            boolean completed = process.waitFor(2, TimeUnit.SECONDS);
            if (!completed) {
                process.destroy();
                result.addError("✗ Main.java runs but took too long (possible infinite loop)");
                return 0;
            }

            int exitCode = process.exitValue();
            String outputStr = output.toString().trim();

            if (exitCode != 0) {
                if (outputStr.toLowerCase().contains("main method")) {
                    result.addError("✗ Main class exists but has no valid main method");
                } else {
                    result.addError("✗ Main.java has runtime errors");
                }
                return 0;
            }

            if (outputStr.length() == 0) {
                result.addError("✗ Main.java runs but produces no output");
                return 0;
            }

            // Has some output - award 1 point
            result.addSuccess("✓ Main.java runs and produces output");
            int score = 1;

            // Check for good demonstration
            if (outputStr.length() > 50 && lineCount >= 3) {
                String lowerOutput = outputStr.toLowerCase();
                boolean hasObjectCreation = lowerOutput.contains("book") ||
                        lowerOutput.contains("member") ||
                        lowerOutput.contains("borrow");
                boolean hasException = lowerOutput.contains("exception") ||
                        lowerOutput.contains("error");

                if (hasObjectCreation || hasException) {
                    score = 2;
                    result.addSuccess("✓ Main.java demonstrates program functionality");
                }
            }

            return score;

        } catch (Exception e) {
            result.addError("✗ Could not execute Main.java: " + e.getMessage());
            return 0;
        }
    }

    private static int testBonus(String studentId, StudentResult result) {
        int score = 0;

        if (!hasClassCompiled(studentId, "BorrowingSystem")) {
            // No bonus without BorrowingSystem
            return 0;
        }

        try {
            File binDir = new File("bin_" + studentId);
            URLClassLoader loader = URLClassLoader.newInstance(
                    new java.net.URL[]{binDir.toURI().toURL()}
            );

            Class<?> borrowingClass = loader.loadClass("library.transactions.BorrowingSystem");

            // Check for bonus methods
            Set<String> methodNames = Arrays.stream(borrowingClass.getDeclaredMethods())
                    .map(Method::getName)
                    .collect(Collectors.toSet());

            boolean hasSearch = methodNames.stream()
                    .anyMatch(m -> m.contains("hasBorrowed") || m.contains("findDue"));

            boolean hasStats = methodNames.stream()
                    .anyMatch(m -> m.contains("Usage") || m.contains("Status") || m.contains("Percentage"));

            if (hasSearch) {
                score += 2;
                result.addSuccess("✓ BONUS: Search capability implemented");
            }

            if (hasStats) {
                score += 2;
                result.addSuccess("✓ BONUS: Statistics implemented");
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

            // Sort by student ID for consistent ordering
            for (Map.Entry<String, StudentResult> entry : allResults.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList())) {
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

                // Add compilation details
                CompilationDetails details = compilationDetails.get(result.studentId);
                if (details != null && !details.failedClasses.isEmpty()) {
                    writer.println("\n" + "=".repeat(50));
                    writer.println("COMPILATION DETAILS:");
                    writer.println("=".repeat(50));
                    writer.println("Compiled Successfully: " + String.join(", ", details.compiledClasses));
                    writer.println("\nFailed to Compile:");
                    for (Map.Entry<String, String> failed : details.failedClasses.entrySet()) {
                        writer.println("  • " + failed.getKey() + ": " + failed.getValue());
                    }
                }

                writer.println("\n" + "=".repeat(50) + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error generating detailed report: " + e.getMessage());
        }
    }

    private static void generateDetailedReportHTML() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/detailed_results.html")) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html><head><meta charset='UTF-8'>");
            writer.println("<title>Detailed Grading Results</title>");
            writer.println("<style>");
            writer.println("body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }");
            writer.println("h1 { color: #333; }");
            writer.println(".search-box { margin: 20px 0; padding: 15px; background: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            writer.println(".search-box input { width: 300px; padding: 10px; font-size: 16px; border: 2px solid #ddd; border-radius: 4px; }");
            writer.println(".student-card { background: white; margin: 15px 0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
            writer.println(".student-header { padding: 15px 20px; background: #4CAF50; color: white; cursor: pointer; display: flex; justify-content: space-between; }");
            writer.println(".student-content { padding: 20px; display: none; }");
            writer.println(".student-content.expanded { display: block; }");
            writer.println(".score-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 20px; }");
            writer.println(".score-item { padding: 10px; background: #f9f9f9; border-left: 4px solid #4CAF50; }");
            writer.println(".feedback-section { margin-top: 20px; padding: 15px; background: #f9f9f9; }");
            writer.println(".feedback-item.success { color: #4CAF50; }");
            writer.println(".feedback-item.error { color: #f44336; }");
            writer.println(".compilation-details { background: #fff3cd; padding: 15px; margin-top: 15px; border-left: 4px solid #ffc107; }");
            writer.println("</style>");
            writer.println("<script>");
            writer.println("function toggleStudent(id) {");
            writer.println("  document.getElementById('content-' + id).classList.toggle('expanded');");
            writer.println("}");
            writer.println("</script>");
            writer.println("</head><body>");

            writer.println("<h1>📋 Detailed Grading Results</h1>");

            // Sort by student ID for consistent ordering
            for (Map.Entry<String, StudentResult> entry : allResults.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toList())) {
                String studentId = entry.getKey();
                StudentResult result = entry.getValue();
                int baseScore = result.totalScore - result.bonusScore;

                writer.printf("<div class='student-card'>%n");
                writer.printf("<div class='student-header' onclick='toggleStudent(\"%s\")'>%n", studentId);
                writer.printf("<h3>%s</h3>%n", studentId);
                writer.printf("<span>%d/33</span>%n", result.totalScore);
                writer.println("</div>");

                writer.printf("<div class='student-content' id='content-%s'>%n", studentId);

                writer.println("<div class='score-grid'>");
                writer.printf("<div class='score-item'><label>Compilation</label><div>%d/4</div></div>%n", result.compilationScore);
                writer.printf("<div class='score-item'><label>Part A</label><div>%d/8</div></div>%n", result.partAScore);
                writer.printf("<div class='score-item'><label>Part B</label><div>%d/6</div></div>%n", result.partBScore);
                writer.printf("<div class='score-item'><label>Part C</label><div>%d/9</div></div>%n", result.partCScore);
                writer.printf("<div class='score-item'><label>Part D</label><div>%d/4</div></div>%n", result.partDScore);
                writer.printf("<div class='score-item'><label>Main Demo</label><div>%d/2</div></div>%n", result.mainScore);
                writer.println("</div>");

                writer.println("<div class='feedback-section'><h4>Feedback</h4>");
                for (String feedback : result.feedback) {
                    String cssClass = feedback.startsWith("✓") ? "success" : "error";
                    writer.printf("<div class='feedback-item %s'>%s</div>%n", cssClass, feedback);
                }
                writer.println("</div>");

                // Add compilation details
                CompilationDetails details = compilationDetails.get(result.studentId);
                if (details != null && !details.failedClasses.isEmpty()) {
                    writer.println("<div class='compilation-details'>");
                    writer.println("<h4>⚙️ Compilation Details</h4>");
                    writer.println("<p><strong>Compiled:</strong> " + String.join(", ", details.compiledClasses) + "</p>");
                    writer.println("<p><strong>Failed:</strong></p><ul>");
                    for (Map.Entry<String, String> failed : details.failedClasses.entrySet()) {
                        writer.println("<li>" + failed.getKey() + ": " + failed.getValue() + "</li>");
                    }
                    writer.println("</ul></div>");
                }

                writer.println("</div></div>");
            }

            writer.println("</body></html>");

        } catch (Exception e) {
            System.err.println("Error generating HTML report: " + e.getMessage());
        }
    }

    private static void generateComparisonReport() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/comparison_report.txt")) {
            writer.println("STUDENT COMPARISON REPORT");
            writer.println("=========================\n");

            writer.printf("%-10s | %4s | %4s | %4s | %4s | %4s | %4s | %5s | %5s | %6s%n",
                    "Student", "Comp", "PartA", "PartB", "PartC", "PartD", "Main", "Base", "Bonus", "Total");
            writer.println("─".repeat(85));

            // Sort by student ID for consistent ordering
            for (StudentResult result : allResults.values().stream()
                    .sorted(Comparator.comparing(r -> r.studentId))
                    .collect(Collectors.toList())) {
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
            writer.println("th:first-child, td:first-child { text-align: center; width: 50px; color: #999; font-weight: normal; }");
            writer.println("th { background: #4CAF50; color: white; cursor: pointer; user-select: none; position: relative; }");
            writer.println("th:first-child { cursor: default; }");
            writer.println("th:hover { background: #45a049; }");
            writer.println("th:first-child:hover { background: #4CAF50; }");
            writer.println("th.sortable:after { content: ' ⇅'; opacity: 0.3; }");
            writer.println("th.asc:after { content: ' ↑'; opacity: 1; }");
            writer.println("th.desc:after { content: ' ↓'; opacity: 1; }");
            writer.println("tr:hover { background: #f5f5f5; }");
            writer.println(".score { font-weight: bold; }");
            writer.println(".high { color: #4CAF50; }");
            writer.println(".medium { color: #FF9800; }");
            writer.println(".low { color: #f44336; }");
            writer.println(".stats { background: white; padding: 20px; margin: 20px 0; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); display: flex; gap: 30px; align-items: flex-start; }");
            writer.println(".stats-text { flex: 1; }");
            writer.println(".stats-chart { flex: 0 0 450px; }");
            writer.println("#gradeChart { border: 1px solid #ddd; border-radius: 4px; }");
            writer.println("@media (max-width: 900px) { .stats { flex-direction: column; } .stats-chart { flex: 1; width: 100%; } #gradeChart { width: 100%; } }");
            writer.println("</style>");
            writer.println("<script>");
            writer.println("function sortTable(columnIndex, isNumeric) {");
            writer.println("  const table = document.querySelector('table');");
            writer.println("  const tbody = table.querySelector('tbody');");
            writer.println("  const rows = Array.from(tbody.querySelectorAll('tr'));");
            writer.println("  const headers = table.querySelectorAll('th');");
            writer.println("  const currentHeader = headers[columnIndex];");
            writer.println("  const isAsc = currentHeader.classList.contains('asc');");
            writer.println("  headers.forEach(h => h.classList.remove('asc', 'desc'));");
            writer.println("  currentHeader.classList.add(isAsc ? 'desc' : 'asc');");
            writer.println("  rows.sort((a, b) => {");
            writer.println("    let aVal = a.cells[columnIndex].textContent.trim();");
            writer.println("    let bVal = b.cells[columnIndex].textContent.trim();");
            writer.println("    if (isNumeric) {");
            writer.println("      if (aVal === '-') aVal = '0';");
            writer.println("      if (bVal === '-') bVal = '0';");
            writer.println("      aVal = parseFloat(aVal.replace(/[^0-9.-]/g, '')) || 0;");
            writer.println("      bVal = parseFloat(bVal.replace(/[^0-9.-]/g, '')) || 0;");
            writer.println("    }");
            writer.println("    if (aVal < bVal) return isAsc ? 1 : -1;");
            writer.println("    if (aVal > bVal) return isAsc ? -1 : 1;");
            writer.println("    return 0;");
            writer.println("  });");
            writer.println("  rows.forEach(row => tbody.appendChild(row));");
            writer.println("  renumberRows();");
            writer.println("}");
            writer.println("function renumberRows() {");
            writer.println("  const rows = document.querySelectorAll('tbody tr');");
            writer.println("  rows.forEach((row, index) => { row.cells[0].textContent = index + 1; });");
            writer.println("}");
            writer.println("function drawGradeDistribution() {");
            writer.println("  const canvas = document.getElementById('gradeChart');");
            writer.println("  const ctx = canvas.getContext('2d');");
            writer.println("  const width = canvas.width;");
            writer.println("  const height = canvas.height;");
            writer.println("  const padding = { top: 40, right: 30, bottom: 60, left: 60 };");
            writer.println("  const chartWidth = width - padding.left - padding.right;");
            writer.println("  const chartHeight = height - padding.top - padding.bottom;");
            writer.println("  const rows = document.querySelectorAll('tbody tr');");
            writer.println("  const grades = Array.from(rows).map(row => parseInt(row.cells[10].textContent));");
            writer.println("  const bins = [");
            writer.println("    { label: '0-10', min: 0, max: 10, count: 0, color: '#f44336' },");
            writer.println("    { label: '10-20', min: 10, max: 20, count: 0, color: '#FF9800' },");
            writer.println("    { label: '20-25', min: 20, max: 25, count: 0, color: '#FFC107' },");
            writer.println("    { label: '25-30', min: 25, max: 30, count: 0, color: '#8BC34A' },");
            writer.println("    { label: '30-33', min: 30, max: 33, count: 0, color: '#4CAF50' },");
            writer.println("    { label: '33+', min: 33, max: 100, count: 0, color: '#2196F3' }");
            writer.println("  ];");
            writer.println("  grades.forEach(grade => {");
            writer.println("    for (let bin of bins) {");
            writer.println("      if (grade >= bin.min && (grade < bin.max || (bin.max === 100 && grade >= bin.min))) {");
            writer.println("        bin.count++; break;");
            writer.println("      }");
            writer.println("    }");
            writer.println("  });");
            writer.println("  ctx.clearRect(0, 0, width, height);");
            writer.println("  const actualMaxCount = Math.max(...bins.map(b => b.count), 1);");
            writer.println("  const maxCount = actualMaxCount + 3;");
            writer.println("  const barWidth = chartWidth / bins.length;");
            writer.println("  ctx.fillStyle = '#333';");
            writer.println("  ctx.font = 'bold 14px Arial';");
            writer.println("  ctx.textAlign = 'left';");
            writer.println("  ctx.fillText('Grade Distribution', padding.left, 25);");
            writer.println("  ctx.fillStyle = '#fafafa';");
            writer.println("  ctx.fillRect(padding.left, padding.top, chartWidth, chartHeight);");
            writer.println("  ctx.strokeStyle = '#e0e0e0';");
            writer.println("  ctx.lineWidth = 1;");
            writer.println("  for (let i = 0; i <= 5; i++) {");
            writer.println("    const y = padding.top + (chartHeight / 5) * i;");
            writer.println("    ctx.beginPath();");
            writer.println("    ctx.moveTo(padding.left, y);");
            writer.println("    ctx.lineTo(padding.left + chartWidth, y);");
            writer.println("    ctx.stroke();");
            writer.println("    const value = Math.round(maxCount - (maxCount / 5) * i);");
            writer.println("    ctx.fillStyle = '#666';");
            writer.println("    ctx.font = '11px Arial';");
            writer.println("    ctx.textAlign = 'right';");
            writer.println("    ctx.fillText(value, padding.left - 10, y + 4);");
            writer.println("  }");
            writer.println("  const barCenters = [];");
            writer.println("  bins.forEach((bin, i) => {");
            writer.println("    const barHeight = (bin.count / maxCount) * chartHeight;");
            writer.println("    const x = padding.left + i * barWidth;");
            writer.println("    const y = padding.top + chartHeight - barHeight;");
            writer.println("    const actualBarWidth = barWidth * 0.8;");
            writer.println("    const barX = x + (barWidth - actualBarWidth) / 2;");
            writer.println("    barCenters.push({ x: barX + actualBarWidth / 2, y: y, count: bin.count });");
            writer.println("    ctx.fillStyle = bin.color;");
            writer.println("    ctx.fillRect(barX, y, actualBarWidth, barHeight);");
            writer.println("    if (bin.count > 0) {");
            writer.println("      ctx.fillStyle = '#333';");
            writer.println("      ctx.font = 'bold 13px Arial';");
            writer.println("      ctx.textAlign = 'center';");
            writer.println("      ctx.fillText(bin.count, barX + actualBarWidth / 2, y - 8);");
            writer.println("    }");
            writer.println("    ctx.fillStyle = '#666';");
            writer.println("    ctx.font = '11px Arial';");
            writer.println("    ctx.textAlign = 'center';");
            writer.println("    ctx.fillText(bin.label, barX + actualBarWidth / 2, padding.top + chartHeight + 20);");
            writer.println("  });");
            writer.println("  if (barCenters.length > 1) {");
            writer.println("    ctx.strokeStyle = '#1976D2';");
            writer.println("    ctx.lineWidth = 2.5;");
            writer.println("    ctx.setLineDash([]);");
            writer.println("    ctx.beginPath();");
            writer.println("    barCenters.forEach((point, i) => {");
            writer.println("      if (i === 0) {");
            writer.println("        ctx.moveTo(point.x, point.y);");
            writer.println("      } else {");
            writer.println("        const prevPoint = barCenters[i - 1];");
            writer.println("        const cp1x = prevPoint.x + (point.x - prevPoint.x) / 3;");
            writer.println("        const cp1y = prevPoint.y;");
            writer.println("        const cp2x = prevPoint.x + 2 * (point.x - prevPoint.x) / 3;");
            writer.println("        const cp2y = point.y;");
            writer.println("        ctx.bezierCurveTo(cp1x, cp1y, cp2x, cp2y, point.x, point.y);");
            writer.println("      }");
            writer.println("    });");
            writer.println("    ctx.stroke();");
            writer.println("    ctx.fillStyle = '#1976D2';");
            writer.println("    ctx.font = '11px Arial';");
            writer.println("    ctx.textAlign = 'right';");
            writer.println("    ctx.fillText('— Distribution', width - padding.right, 25);");
            writer.println("  }");
            writer.println("  ctx.strokeStyle = '#999';");
            writer.println("  ctx.lineWidth = 2;");
            writer.println("  ctx.beginPath();");
            writer.println("  ctx.moveTo(padding.left, padding.top);");
            writer.println("  ctx.lineTo(padding.left, padding.top + chartHeight);");
            writer.println("  ctx.lineTo(padding.left + chartWidth, padding.top + chartHeight);");
            writer.println("  ctx.stroke();");
            writer.println("  ctx.fillStyle = '#666';");
            writer.println("  ctx.font = '12px Arial';");
            writer.println("  ctx.textAlign = 'center';");
            writer.println("  ctx.save();");
            writer.println("  ctx.translate(15, padding.top + chartHeight / 2);");
            writer.println("  ctx.rotate(-Math.PI / 2);");
            writer.println("  ctx.fillText('Number of Students', 0, 0);");
            writer.println("  ctx.restore();");
            writer.println("  ctx.fillText('Score Range', padding.left + chartWidth / 2, height - 15);");
            writer.println("}");
            writer.println("window.onload = function() { drawGradeDistribution(); renumberRows(); };");
            writer.println("</script></head><body>");

            writer.println("<h1>📊 Library Exam Grading Dashboard</h1>");

            DoubleSummaryStatistics totalStats = allResults.values().stream()
                    .mapToDouble(r -> r.totalScore).summaryStatistics();
            DoubleSummaryStatistics baseStats = allResults.values().stream()
                    .mapToDouble(r -> r.totalScore - r.bonusScore).summaryStatistics();

            writer.println("<div class='stats'>");
            writer.println("<div class='stats-text'>");
            writer.println("<h2>Class Statistics</h2>");
            writer.printf("<p>Base Average: <strong>%.2f/33</strong> (%.1f%%) | Total Average: <strong>%.2f/37</strong></p>%n",
                    baseStats.getAverage(), (baseStats.getAverage()/33)*100, totalStats.getAverage());
            writer.printf("<p>Highest: <strong>%.0f</strong> | Lowest: <strong>%.0f</strong></p>%n",
                    totalStats.getMax(), totalStats.getMin());
            writer.printf("<p>Total Students: <strong>%d</strong></p>%n", totalStats.getCount());
            writer.println("<p style='color: #666; font-size: 0.9em;'>💡 Click any column header to sort (except #) | # shows current display order | Percentage = (Total/33)×100, can exceed 100% with bonus</p>");
            writer.println("</div>");
            writer.println("<div class='stats-chart'>");
            writer.println("<canvas id='gradeChart' width='450' height='300'></canvas>");
            writer.println("</div>");
            writer.println("</div>");

            writer.println("<table><thead><tr>");
            writer.println("<th>#</th>");
            writer.println("<th class='sortable' onclick='sortTable(1, false)'>Student</th>");
            writer.println("<th class='sortable' onclick='sortTable(2, true)'>Comp</th>");
            writer.println("<th class='sortable' onclick='sortTable(3, true)'>Part A</th>");
            writer.println("<th class='sortable' onclick='sortTable(4, true)'>Part B</th>");
            writer.println("<th class='sortable' onclick='sortTable(5, true)'>Part C</th>");
            writer.println("<th class='sortable' onclick='sortTable(6, true)'>Part D</th>");
            writer.println("<th class='sortable' onclick='sortTable(7, true)'>Main</th>");
            writer.println("<th class='sortable' onclick='sortTable(8, true)'>Base</th>");
            writer.println("<th class='sortable' onclick='sortTable(9, true)'>Bonus</th>");
            writer.println("<th class='sortable' onclick='sortTable(10, true)'>Total</th>");
            writer.println("<th class='sortable' onclick='sortTable(11, true)'>%</th>");
            writer.println("</tr></thead><tbody>");

            int rowNumber = 1;
            for (StudentResult result : allResults.values()) {
                int baseScore = result.totalScore - result.bonusScore;
                double percentage = (result.totalScore / 33.0) * 100;
                String scoreClass = percentage >= 80 ? "high" : percentage >= 60 ? "medium" : "low";
                writer.println("<tr>");
                writer.printf("<td>%d</td>", rowNumber++);
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

            writer.println("</tbody></table></body></html>");

        } catch (Exception e) {
            System.err.println("Error generating dashboard: " + e.getMessage());
        }
    }

    private static void generateCSVExport() {
        try (PrintWriter writer = new PrintWriter(RESULTS_DIR + "/grades.csv")) {
            writer.println("Student,Compilation,PartA,PartB,PartC,PartD,Main,BaseScore,Bonus,TotalScore,Percentage");

            // Sort by student ID for consistent ordering
            for (StudentResult result : allResults.values().stream()
                    .sorted(Comparator.comparing(r -> r.studentId))
                    .collect(Collectors.toList())) {
                int baseScore = result.totalScore - result.bonusScore;
                double percentage = (result.totalScore / 33.0) * 100;
                writer.printf("%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%.2f%n",
                        result.studentId, result.compilationScore, result.partAScore,
                        result.partBScore, result.partCScore, result.partDScore,
                        result.mainScore, baseScore, result.bonusScore,
                        result.totalScore, percentage);
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

class CompilationDetails {
    String studentId;
    Set<String> compiledClasses = new HashSet<>();
    Map<String, String> failedClasses = new LinkedHashMap<>();
    Map<String, String> compilationErrors = new LinkedHashMap<>();
    boolean coreClassesCompiled = false;
    boolean mainCompiled = false;

    CompilationDetails(String studentId) {
        this.studentId = studentId;
    }

    void addFailedClass(String className, String error) {
        failedClasses.put(className, error);
    }
}