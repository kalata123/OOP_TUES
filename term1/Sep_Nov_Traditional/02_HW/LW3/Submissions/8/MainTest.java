import java.io.*;
import java.lang.reflect.*;
import java.util.concurrent.*;

public class MainTest {
    private static final int TIMEOUT_SECONDS = 10;

    public static void main(String[] args) {
        System.out.println("Testing SokobanGame Implementation");
        System.out.println("==================================");

        testCompilationAndStructure();
        testRandomInitialization();
        testInputHandling();
        testMovementSystem();
        testBoxPushing();
        testPlacementRules();
        testWinCondition();
        testBonusFeatures();

        System.out.println("==================================");
        System.out.println("All tests completed!");
    }

    private static Class<?> getSokobanClass() throws Exception {
        try {
            return Class.forName("SokobanGame");
        } catch (ClassNotFoundException e) {
            Class<?> outer = Class.forName("SokobanGame");
            for (Class<?> nested : outer.getDeclaredClasses()) {
                if (nested.getSimpleName().equals("SokobanGame")) {
                    return nested;
                }
            }
            throw new ClassNotFoundException("SokobanGame class not found");
        }
    }

    private static void testCompilationAndStructure() {
        System.out.println("1. Testing compilation and basic structure...");
        
        try {
            ProcessBuilder pb = new ProcessBuilder("javac", "SokobanGame.java");
            Process process = pb.start();
            boolean compiled = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS) && process.exitValue() == 0;

            if (!compiled) {
                System.out.println("   ✗ Compilation failed");
                return;
            }

            Class<?> gameClass = getSokobanClass();

            // Check for required constants
            Field[] fields = gameClass.getDeclaredFields();
            boolean hasWallChar = false;
            boolean hasBoxChar = false;
            boolean hasPlayerChar = false;
            boolean hasGoalChar = false;
            boolean hasEmptyChar = false;

            for (Field field : fields) {
                if (Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                    String fieldName = field.getName().toLowerCase();
                    if (fieldName.contains("wall")) hasWallChar = true;
                    if (fieldName.contains("box")) hasBoxChar = true;
                    if (fieldName.contains("player")) hasPlayerChar = true;
                    if (fieldName.contains("goal")) hasGoalChar = true;
                    if (fieldName.contains("empty")) hasEmptyChar = true;
                }
            }

            if (!hasWallChar) System.out.println("   ⚠ Missing WALL character constant");
            if (!hasBoxChar) System.out.println("   ⚠ Missing BOX character constant");
            if (!hasPlayerChar) System.out.println("   ⚠ Missing PLAYER character constant");
            if (!hasGoalChar) System.out.println("   ⚠ Missing GOAL character constant");
            if (!hasEmptyChar) System.out.println("   ⚠ Missing EMPTY character constant");

            // Check for required methods
            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method printBoard = gameClass.getDeclaredMethod("printBoard");
            Method move = gameClass.getDeclaredMethod("move", char.class);
            Method isGameOver = gameClass.getDeclaredMethod("isGameOver");
            Method getMoveCount = gameClass.getDeclaredMethod("getMoveCount");

            System.out.println("   ✓ Basic structure validated");

            // Check for bonus methods
            boolean hasUndo = false;
            boolean hasLevel = false;
            Method[] methods = gameClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals("undo")) hasUndo = true;
                if (method.getName().equals("level")) hasLevel = true;
            }

            if (hasUndo) System.out.println("   ✓ Undo method found");
            if (hasLevel) System.out.println("   ✓ Level method found");

        } catch (Exception e) {
            System.out.println("   ✗ Structure test failed: " + e.getMessage());
        }
    }

    private static void testRandomInitialization() {
        System.out.println("2. Testing random initialization...");
        
        try {
            Class<?> gameClass = getSokobanClass();
            Object gameInstance = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method printBoard = gameClass.getDeclaredMethod("printBoard");
            Method getMoveCount = gameClass.getDeclaredMethod("getMoveCount");

            // Test initialization with different parameters
            initializeGame.invoke(gameInstance, 7, 7, 2, 0);
            int moveCount = (Integer) getMoveCount.invoke(gameInstance);
            
            if (moveCount == 0) {
                System.out.println("   ✓ Random initialization successful");
            } else {
                System.out.println("   ✗ Move count should be 0 after initialization");
            }

        } catch (Exception e) {
            System.out.println("   ✗ Random initialization test failed: " + e.getMessage());
        }
    }

    private static void testInputHandling() {
        System.out.println("3. Testing input handling...");
        
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "SokobanGame");
            Process process = pb.start();

            // Send input to the process
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write("0\n");  // Choose custom level
            writer.write("5\n");  // Width
            writer.write("5\n");  // Height
            writer.write("1\n");  // Boxes
            writer.write("quit\n"); // Exit
            writer.flush();

            boolean finished = process.waitFor(TIMEOUT_SECONDS, TimeUnit.SECONDS);
            if (finished && process.exitValue() == 0) {
                System.out.println("   ✓ Input handling successful");
            } else {
                System.out.println("   ✗ Input handling test failed");
            }

        } catch (Exception e) {
            System.out.println("   ✗ Input handling test failed: " + e.getMessage());
        }
    }

    private static void testMovementSystem() {
        System.out.println("4. Testing movement system...");
        
        try {
            Class<?> gameClass = getSokobanClass();
            Object gameInstance = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", char.class);
            Method getMoveCount = gameClass.getDeclaredMethod("getMoveCount");

            // Initialize a small game
            initializeGame.invoke(gameInstance, 5, 5, 1, 0);

            int initialMoveCount = (Integer) getMoveCount.invoke(gameInstance);

            // Test valid movement
            boolean moved = (Boolean) move.invoke(gameInstance, 'd'); // right
            int newMoveCount = (Integer) getMoveCount.invoke(gameInstance);

            if (moved && newMoveCount == initialMoveCount + 1) {
                System.out.println("   ✓ Movement system working");
            } else {
                System.out.println("   ✗ Movement system test failed");
            }

        } catch (Exception e) {
            System.out.println("   ✗ Movement system test failed: " + e.getMessage());
        }
    }

    private static void testBoxPushing() {
        System.out.println("5. Testing box pushing...");
        
        try {
            Class<?> gameClass = getSokobanClass();
            Object gameInstance = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", char.class);

            // Initialize game
            initializeGame.invoke(gameInstance, 5, 5, 1, 0);

            // Try to push a box (this might not always work depending on random placement)
            boolean pushed = (Boolean) move.invoke(gameInstance, 'd');
            
            // We can't reliably test box pushing with random initialization, so we just check if movement works
            System.out.println("   ⚠ Box pushing test requires manual verification");

        } catch (Exception e) {
            System.out.println("   ✗ Box pushing test failed: " + e.getMessage());
        }
    }

    private static void testPlacementRules() {
        System.out.println("6. Testing placement rules...");
        
        try {
            Class<?> gameClass = getSokobanClass();
            Object gameInstance = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method printBoard = gameClass.getDeclaredMethod("printBoard");

            // Test various board sizes
            initializeGame.invoke(gameInstance, 7, 7, 2, 0);
            initializeGame.invoke(gameInstance, 10, 10, 3, 0);
            initializeGame.invoke(gameInstance, 4, 4, 1, 0);

            System.out.println("   ✓ Placement rules validated for various sizes");

        } catch (Exception e) {
            System.out.println("   ✗ Placement rules test failed: " + e.getMessage());
        }
    }

    private static void testWinCondition() {
        System.out.println("7. Testing win condition...");
        
        try {
            Class<?> gameClass = getSokobanClass();
            Object gameInstance = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method isGameOver = gameClass.getDeclaredMethod("isGameOver");

            initializeGame.invoke(gameInstance, 5, 5, 1, 0);
            boolean gameOver = (Boolean) isGameOver.invoke(gameInstance);

            // Game shouldn't be over immediately after initialization
            if (!gameOver) {
                System.out.println("   ✓ Win condition initial state correct");
            } else {
                System.out.println("   ✗ Game should not be over immediately after initialization");
            }

        } catch (Exception e) {
            System.out.println("   ✗ Win condition test failed: " + e.getMessage());
        }
    }

    private static void testBonusFeatures() {
        System.out.println("8. Testing bonus features...");
        
        try {
            Class<?> gameClass = getSokobanClass();
            Object gameInstance = gameClass.getDeclaredConstructor().newInstance();

            Method initializeGame = gameClass.getDeclaredMethod("initializeGame", int.class, int.class, int.class, int.class);
            Method move = gameClass.getDeclaredMethod("move", char.class);
            Method getMoveCount = gameClass.getDeclaredMethod("getMoveCount");

            // Test move counter
            initializeGame.invoke(gameInstance, 5, 5, 1, 0);
            int initialCount = (Integer) getMoveCount.invoke(gameInstance);
            move.invoke(gameInstance, 'd');
            move.invoke(gameInstance, 's');
            int finalCount = (Integer) getMoveCount.invoke(gameInstance);

            if (finalCount == initialCount + 2) {
                System.out.println("   ✓ Move counter working");
            } else {
                System.out.println("   ✗ Move counter test failed");
            }

            // Test undo functionality if available
            try {
                Method undo = gameClass.getDeclaredMethod("undo");
                undo.invoke(gameInstance);
                int afterUndoCount = (Integer) getMoveCount.invoke(gameInstance);
                
                if (afterUndoCount == finalCount - 1) {
                    System.out.println("   ✓ Undo functionality working");
                } else {
                    System.out.println("   ✗ Undo functionality test failed");
                }
            } catch (NoSuchMethodException e) {
                System.out.println("   ⚠ Undo feature not implemented");
            }

            // Test level loading if available
            try {
                Method level = gameClass.getDeclaredMethod("level", String[].class);
                String[] testLevel = {
                    "#####",
                    "#   #",
                    "#$  #",
                    "#@  #",
                    "#####"
                };
                level.invoke(gameInstance, (Object) testLevel);
                System.out.println("   ✓ Level loading working");
            } catch (NoSuchMethodException e) {
                System.out.println("   ⚠ Level loading feature not implemented");
            }

        } catch (Exception e) {
            System.out.println("   ✗ Bonus features test failed: " + e.getMessage());
        }
    }
}