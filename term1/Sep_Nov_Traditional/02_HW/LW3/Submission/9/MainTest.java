import java.io.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

public class MainTest {
    private static final int TIMEOUT_SECONDS = 10;
    private static int testsPassed = 0;
    private static int totalTests = 0;
    private static Class<?> gameClass;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("      SokobanGame Implementation Test");
        System.out.println("==================================================");

        // First test compilation separately
        if (!testCompilation()) {
            System.out.println("Skipping other tests due to compilation failure.");
            System.out.println("==================================================");
            System.out.println("TEST SUMMARY: " + testsPassed + "/" + totalTests + " tests passed");
            System.out.println("==================================================");
            return;
        }

        // Run all other test categories
        runTest("Structure", MainTest::testStructure);
        runTest("Random Initialization", MainTest::testRandomInitialization);
        runTest("Movement System", MainTest::testMovementSystem);
        runTest("Box Pushing Mechanics", MainTest::testBoxPushing);
        runTest("Game Rules Validation", MainTest::testGameRules);
        runTest("Win Condition", MainTest::testWinCondition);
        runTest("Bonus Features", MainTest::testBonusFeatures);
        runTest("Built-in Levels", MainTest::testBuiltInLevels);
        runTest("Integration Test", MainTest::testIntegration);

        System.out.println("==================================================");
        System.out.println("TEST SUMMARY: " + testsPassed + "/" + totalTests + " tests passed");
        System.out.println("==================================================");
    }

    private static void runTest(String testName, Runnable test) {
        System.out.println("\n🔍 " + testName + "...");
        totalTests++;
        try {
            test.run();
            testsPassed++;
            System.out.println("✅ " + testName + " - PASSED");
        } catch (Exception e) {
            System.out.println("❌ " + testName + " - FAILED: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("   Caused by: " + e.getCause().getMessage());
            }
        }
    }

    private static boolean testCompilation() {
        totalTests++;
        System.out.println("\n🔍 Compilation...");
        
        try {
            // Debug information
            System.out.println("   Current working directory: " + new File(".").getAbsolutePath());
            System.out.println("   Classpath: " + System.getProperty("java.class.path"));
            
            // List files for debugging
            listFiles(new File("."), "   ");
            listFiles(new File("edu"), "   ");
            listFiles(new File("edu/sokoban"), "   ");

            // Check Java version
            ProcessBuilder versionPb = new ProcessBuilder("javac", "-version");
            versionPb.redirectErrorStream(true);
            Process versionProcess = versionPb.start();
            String versionOutput = readStream(versionProcess.getInputStream());
            versionProcess.waitFor();
            System.out.println("   Java compiler version: " + versionOutput);

            // Check if source files exist
            File sokobanFile = new File("edu/sokoban/SokobanGame.java");
            File mainFile = new File("edu/sokoban/Main.java");
            
            if (!sokobanFile.exists()) {
                throw new RuntimeException("SokobanGame.java not found at: " + sokobanFile.getAbsolutePath());
            }
            if (!mainFile.exists()) {
                throw new RuntimeException("Main.java not found at: " + mainFile.getAbsolutePath());
            }

            System.out.println("   Source files found, compiling...");

            // Compile the source files
            ProcessBuilder compilePb = new ProcessBuilder("javac", 
                "-d", ".", 
                "-cp", ".",
                "edu/sokoban/SokobanGame.java", 
                "edu/sokoban/Main.java");
            compilePb.redirectErrorStream(true);
            
            Process compileProcess = compilePb.start();
            String compilationOutput = readStream(compileProcess.getInputStream());
            boolean compiled = compileProcess.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS) 
                && compileProcess.exitValue() == 0;

            if (!compiled) {
                throw new RuntimeException("Compilation failed. Output:\n" + compilationOutput);
            }

            System.out.println("   Compilation successful!");
            
            // Verify class files were created
            File sokobanClassFile = new File("edu/sokoban/SokobanGame.class");
            File mainClassFile = new File("edu/sokoban/Main.class");
            
            if (!sokobanClassFile.exists()) {
                throw new RuntimeException("SokobanGame.class not found after compilation");
            }
            if (!mainClassFile.exists()) {
                throw new RuntimeException("Main.class not found after compilation");
            }

            testsPassed++;
            System.out.println("✅ Compilation - PASSED");
            return true;

        } catch (Exception e) {
            System.out.println("❌ Compilation - FAILED: " + e.getMessage());
            return false;
        }
    }

    private static void testStructure() {
        try {
            // Load the class using context class loader
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            gameClass = classLoader.loadClass("edu.sokoban.SokobanGame");
            System.out.println("   ✅ SokobanGame class loaded successfully");

            // Test required constants
            testConstant(gameClass, "WALL", '#');
            testConstant(gameClass, "PLAYER", '@');
            testConstant(gameClass, "BOX", 'B');
            testConstant(gameClass, "TARGET", '*');
            testConstant(gameClass, "EMPTY", '.');
            testConstant(gameClass, "BOX_ON_TARGET", 'O');

            // Test required methods
            testMethodExists(gameClass, "initializeGame", int.class, int.class, int.class);
            testMethodExists(gameClass, "printBoard");
            testMethodExists(gameClass, "move", String.class);
            testMethodExists(gameClass, "checkWin");
            testMethodExists(gameClass, "getMoves");

            // Test bonus methods
            try {
                testMethodExists(gameClass, "undo");
                System.out.println("   ✅ Undo method present");
            } catch (Exception e) {
                System.out.println("   ⚠ Undo method missing (optional)");
            }

            try {
                testMethodExists(gameClass, "loadBuiltInLevelFromFile", int.class);
                System.out.println("   ✅ Built-in level loading present");
            } catch (Exception e) {
                System.out.println("   ⚠ Built-in level loading missing (optional)");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testConstant(Class<?> clazz, String constantName, char expectedValue) throws Exception {
        Field field = clazz.getDeclaredField(constantName);
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers())) {
            throw new RuntimeException(constantName + " should be public static final");
        }
        char value = (Character) field.get(null);
        if (value != expectedValue) {
            throw new RuntimeException(constantName + " should be '" + expectedValue + "' but was '" + value + "'");
        }
        System.out.println("   ✅ Constant " + constantName + " = '" + value + "'");
    }

    private static void testMethodExists(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws Exception {
        Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
        if (!Modifier.isPublic(method.getModifiers())) {
            throw new RuntimeException(methodName + " should be public");
        }
        System.out.println("   ✅ Method " + methodName + " present");
    }

    private static void testRandomInitialization() {
        try {
            Object game = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class);
            Method getMoves = gameClass.getDeclaredMethod("getMoves");
            Method printBoard = gameClass.getDeclaredMethod("printBoard");

            // Test various valid configurations
            int[][] testConfigs = {
                {7, 7, 2},
                {10, 10, 3},
                {5, 5, 1}
            };

            for (int[] config : testConfigs) {
                initializeGame.invoke(game, config[0], config[1], config[2]);
                int moves = (Integer) getMoves.invoke(game);
                
                if (moves != 0) {
                    throw new RuntimeException("Move count should be 0 after initialization, but was " + moves);
                }
                
                // Verify board can be printed without errors
                printBoard.invoke(game);
                System.out.println("   ✅ Initialized " + config[0] + "x" + config[1] + " with " + config[2] + " boxes");
            }

            System.out.println("   ✅ Random initialization working for various configurations");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testMovementSystem() {
        try {
            Object game = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", String.class);
            Method getMoves = gameClass.getDeclaredMethod("getMoves");

            initializeGame.invoke(game, 7, 7, 1);
            int initialMoves = (Integer) getMoves.invoke(game);

            // Test valid movement commands
            String[] validMoves = {"w", "a", "s", "d", "up", "down", "left", "right"};
            int successfulMoves = 0;
            
            for (String cmd : validMoves) {
                boolean result = (Boolean) move.invoke(game, cmd);
                if (result) {
                    successfulMoves++;
                }
            }

            int afterValidMoves = (Integer) getMoves.invoke(game);
            if (afterValidMoves != initialMoves + successfulMoves) {
                throw new RuntimeException("Move count should be " + (initialMoves + successfulMoves) + 
                    " but was " + afterValidMoves);
            }

            // Test invalid movement commands
            String[] invalidMoves = {"x", "q", "invalid", "updown", ""};
            for (String cmd : invalidMoves) {
                boolean result = (Boolean) move.invoke(game, cmd);
                if (result) {
                    throw new RuntimeException("Invalid move '" + cmd + "' should return false");
                }
            }

            System.out.println("   ✅ Movement system working (" + successfulMoves + "/" + validMoves.length + " valid moves processed)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testBoxPushing() {
        try {
            Object game = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", String.class);
            Method getMoves = gameClass.getDeclaredMethod("getMoves");
            Method checkWin = gameClass.getDeclaredMethod("checkWin");

            // Test with a simple configuration
            initializeGame.invoke(game, 5, 5, 1);
            
            // Make several moves to test box interaction
            for (int i = 0; i < 5; i++) {
                move.invoke(game, "d");
                move.invoke(game, "s");
                move.invoke(game, "a");
                move.invoke(game, "w");
            }

            int finalMoves = (Integer) getMoves.invoke(game);
            boolean gameWon = (Boolean) checkWin.invoke(game);

            if (finalMoves != 20) {
                throw new RuntimeException("Expected 20 moves after 5 cycles, got " + finalMoves);
            }

            if (gameWon) {
                System.out.println("   ⚠ Game won unexpectedly - may indicate lucky box placement");
            } else {
                System.out.println("   ✅ Box pushing mechanics working (game not prematurely won)");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testGameRules() {
        try {
            Object game = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", String.class);
            Method getMoves = gameClass.getDeclaredMethod("getMoves");

            // Test wall collision
            initializeGame.invoke(game, 5, 5, 1);
            int initialMoves = (Integer) getMoves.invoke(game);

            // Try to move into walls multiple times
            for (int i = 0; i < 3; i++) {
                move.invoke(game, "a"); // left toward wall
                move.invoke(game, "w"); // up toward wall
            }

            int afterWallMoves = (Integer) getMoves.invoke(game);
            if (afterWallMoves != initialMoves) {
                throw new RuntimeException("Wall collisions should not increase move count");
            }

            System.out.println("   ✅ Wall collision detection working");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testWinCondition() {
        try {
            Object game = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class);
            Method checkWin = gameClass.getDeclaredMethod("checkWin");

            // Game should not be won immediately after initialization
            initializeGame.invoke(game, 7, 7, 2);
            boolean initialWin = (Boolean) checkWin.invoke(game);
            
            if (initialWin) {
                throw new RuntimeException("Game should not be won immediately after initialization");
            }

            System.out.println("   ✅ Win condition initial state correct");
            System.out.println("   ⚠ Actual win condition test requires manual verification (random levels)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testBonusFeatures() {
        try {
            Object game = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", String.class);
            Method getMoves = gameClass.getDeclaredMethod("getMoves");

            // Test move counter accuracy
            initializeGame.invoke(game, 5, 5, 1);
            int startMoves = (Integer) getMoves.invoke(game);

            // Perform sequence of moves
            String[] moves = {"d", "s", "d", "w", "a", "s"};
            for (String m : moves) {
                move.invoke(game, m);
            }

            int endMoves = (Integer) getMoves.invoke(game);
            if (endMoves != startMoves + moves.length) {
                throw new RuntimeException("Move counter inaccurate: expected " + 
                    (startMoves + moves.length) + ", got " + endMoves);
            }

            System.out.println("   ✅ Move counter working accurately");

            // Test undo functionality if available
            try {
                Method undo = gameClass.getDeclaredMethod("undo");
                
                int beforeUndo = (Integer) getMoves.invoke(game);
                boolean undone = (Boolean) undo.invoke(game);
                int afterUndo = (Integer) getMoves.invoke(game);
                
                if (!undone) {
                    throw new RuntimeException("Undo should return true when moves are available");
                }
                
                if (afterUndo != beforeUndo - 1) {
                    throw new RuntimeException("Undo should decrease move count by 1");
                }
                
                System.out.println("   ✅ Undo functionality working");
                
            } catch (NoSuchMethodException e) {
                System.out.println("   ⚠ Undo feature not implemented");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testBuiltInLevels() {
        try {
            // Create directory structure for level files
            new File("hw1/resources/levels").mkdirs();
            
            // Create test level files
            createLevelFiles();

            Object game = gameClass.getDeclaredConstructor().newInstance();

            try {
                Method loadBuiltInLevel = gameClass.getDeclaredMethod("loadBuiltInLevelFromFile", int.class);
                Method getMoves = gameClass.getDeclaredMethod("getMoves");

                // Test loading each built-in level
                for (int i = 0; i < 4; i++) {
                    loadBuiltInLevel.invoke(game, i);
                    int moves = (Integer) getMoves.invoke(game);
                    
                    if (moves != 0) {
                        throw new RuntimeException("Level " + (i + 1) + " should start with 0 moves");
                    }
                    
                    System.out.println("   ✅ Level " + (i + 1) + " loaded successfully");
                }
                
            } catch (NoSuchMethodException e) {
                System.out.println("   ⚠ Built-in level loading not implemented");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void testIntegration() {
        try {
            // Test the complete game flow by running the Main class
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", ".", "edu.sokoban.Main");
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Send test input sequence
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write("1\n");    // Choose random level
            writer.write("7\n");    // Width
            writer.write("7\n");    // Height  
            writer.write("2\n");    // Boxes
            writer.write("d\n");    // Move right
            writer.write("s\n");    // Move down
            writer.write("w\n");    // Move up
            writer.write("a\n");    // Move left
            writer.write("quit\n"); // Quit game
            writer.flush();

            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (!finished) {
                process.destroy();
                throw new RuntimeException("Integration test timed out");
            }

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("   ✅ Integration test completed successfully");
            } else {
                String output = readStream(process.getInputStream());
                throw new RuntimeException("Integration test failed with exit code: " + exitCode + "\nOutput: " + output);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String readStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    private static void listFiles(File dir, String indent) {
        if (!dir.exists()) {
            System.out.println(indent + "Directory " + dir.getAbsolutePath() + " does not exist.");
            return;
        }
        System.out.println(indent + "Files in " + dir.getAbsolutePath() + ":");
        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println(indent + " [null]");
            return;
        }
        for (File f : files) {
            System.out.println(indent + " " + f.getName() + (f.isDirectory() ? "/" : ""));
        }
    }

    private static void createLevelFiles() {
        try {
            // Level 1
            String[] level1 = {
                "#######",
                "#.....#",
                "#..@..#",
                "#..B*.#",
                "#.....#",
                "#.....#",
                "#######"
            };
            writeLevelFile("level0.txt", level1);

            // Level 2
            String[] level2 = {
                "#########",
                "#.......#",
                "#..B.*.@#",
                "#..#....#",
                "#..***..#",
                "#..#....#",
                "#..B.*..#",
                "#.......#",
                "#########"
            };
            writeLevelFile("level1.txt", level2);

            // Level 3
            String[] level3 = {
                "###########",
                "#..#......#",
                "#..#..B...#",
                "#..#..#...#",
                "#..@..#.*.#",
                "#..#..#...#",
                "#..#..B...#",
                "#.....#...#",
                "#..***....#",
                "###########"
            };
            writeLevelFile("level2.txt", level3);

            // Level 4
            String[] level4 = {
                "#############",
                "#..B..*.#...#",
                "#..B.*..#.*.#",
                "#..#........#",
                "#..#..##....#",
                "#..#..#.@...#",
                "#..#..#....B#",
                "#..***#....*#",
                "#......#....#",
                "#############"
            };
            writeLevelFile("level3.txt", level4);

            System.out.println("   ✅ Test level files created");

        } catch (IOException e) {
            System.out.println("   ⚠ Could not create level files: " + e.getMessage());
        }
    }

    private static void writeLevelFile(String filename, String[] level) throws IOException {
        PrintWriter writer = new PrintWriter("hw1/resources/levels/" + filename);
        for (String line : level) {
            writer.println(line);
        }
        writer.close();
    }
}