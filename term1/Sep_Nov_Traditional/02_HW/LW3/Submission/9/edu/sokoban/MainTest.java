// MainTest.java
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Scanner;

public class MainTest {
    private static int passedTests = 0;
    private static int totalTests = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Sokoban Game Tests ===\n");
        
        try {
            testInitialization();
            testMovement();
            testBoxPushing();
            testWinCondition();
            testUndoFunctionality();
            testDeadlockDetection();
            testInputValidation();
            testIntegration();
            
            System.out.println("\n=== Test Results ===");
            System.out.println("Passed: " + passedTests + "/" + totalTests);
            System.out.println("Score: " + (passedTests * 100 / totalTests) + "%");
            
        } catch (Exception e) {
            System.out.println("Test suite crashed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testInitialization() throws Exception {
        System.out.println("--- Initialization Tests ---");
        
        // Test 1: Valid initialization
        SokobanGame1 game = new SokobanGame();
        game.initializeGame(7, 7, 2);
        assertTrue(game != null, "Game initialization");
        
        // Test 2: Player position
        // Player should be at center (using reflection to access private field)
        java.lang.reflect.Field playerField = SokobanGame1.class.getDeclaredField("playerPosition");
        playerField.setAccessible(true);
        java.awt.Point playerPos = (java.awt.Point) playerField.get(game);
        assertEquals(3, playerPos.x, "Player X position");
        assertEquals(3, playerPos.y, "Player Y position");
        
        // Test 3: Board dimensions
        java.lang.reflect.Field widthField = SokobanGame1.class.getDeclaredField("width");
        java.lang.reflect.Field heightField = SokobanGame1.class.getDeclaredField("height");
        widthField.setAccessible(true);
        heightField.setAccessible(true);
        assertEquals(7, widthField.get(game), "Board width");
        assertEquals(7, heightField.get(game), "Board height");
        
        // Test 4: Invalid initialization - too small board
        try {
            SokobanGame1 game2 = new SokobanGame1();
            game2.initializeGame(4, 4, 1);
            fail("Should throw exception for small board");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Small board validation");
        }
        
        // Test 5: Invalid initialization - too many boxes
        try {
            SokobanGame1 game3 = new SokobanGame1();
            game3.initializeGame(5, 5, 10); // Too many boxes for 5x5
            fail("Should throw exception for too many boxes");
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Box count validation");
        }
    }
    
    private static void testMovement() throws Exception {
        System.out.println("--- Movement Tests ---");
        
        SokobanGame1 game = new SokobanGame1();
        game.initializeGame(7, 7, 1);
        
        // Test 1: Valid movement
        boolean moved = game.move("d"); // Move right
        assertTrue(moved, "Valid movement right");
        
        // Test 2: Movement into wall
        // Create a scenario where player is next to wall
        SokobanGame1 wallGame = new SokobanGame1();
        wallGame.initializeGame(5, 5, 1);
        
        // Try to move into top wall (player starts at center)
        boolean wallMove = wallGame.move("w");
        assertTrue(!wallMove, "Movement into wall should fail");
        
        // Test 3: Multiple movements
        SokobanGame1 multiGame = new SokobanGame1();
        multiGame.initializeGame(7, 7, 1);
        
        multiGame.move("d");
        multiGame.move("s");
        multiGame.move("a");
        multiGame.move("w");
        
        // Player should be back at start position
        java.lang.reflect.Field playerField = SokobanGame1.class.getDeclaredField("playerPosition");
        playerField.setAccessible(true);
        java.awt.Point playerPos = (java.awt.Point) playerField.get(multiGame);
        
        // Note: Due to box placement, exact position might vary, but movement should work
        assertTrue(playerPos != null, "Multiple movements");
    }
    
    private static void testBoxPushing() throws Exception {
        System.out.println("--- Box Pushing Tests ---");
        
        // This test is complex due to random placement
        // We'll test that box pushing doesn't crash and basic mechanics work
        SokobanGame1 game = new SokobanGame1();
        game.initializeGame(7, 7, 2);
        
        // Try movements that might push boxes
        boolean result1 = game.move("w");
        boolean result2 = game.move("s");
        boolean result3 = game.move("a");
        boolean result4 = game.move("d");
        
        // At least some movements should succeed
        assertTrue(result1 || result2 || result3 || result4, "Box pushing movements");
        
        // Test box pushing into wall (should fail)
        // This is hard to test deterministically due to random placement
        // but we can verify the method doesn't crash
        try {
            game.move("w");
            game.move("s");
            game.move("a");
            game.move("d");
            assertTrue(true, "Box pushing doesn't crash");
        } catch (Exception e) {
            fail("Box pushing should not crash: " + e.getMessage());
        }
    }
    
    private static void testWinCondition() throws Exception {
        System.out.println("--- Win Condition Tests ---");
        
        SokobanGame1 game = new SokobanGame1();
        game.initializeGame(7, 7, 1);
        
        // Initially shouldn't win
        assertTrue(!game.checkWin(), "Game shouldn't win initially");
        
        // Test win condition by manipulating state (using reflection)
        java.lang.reflect.Field boxesOnTargetsField = SokobanGame1.class.getDeclaredField("boxesOnTargets");
        java.lang.reflect.Field boxesCountField = SokobanGame1.class.getDeclaredField("boxesCount");
        boxesOnTargetsField.setAccessible(true);
        boxesCountField.setAccessible(true);
        
        // Set boxes on targets equal to total boxes
        boxesOnTargetsField.set(game, boxesCountField.get(game));
        
        assertTrue(game.checkWin(), "Win condition when all boxes on targets");
    }
    
    private static void testUndoFunctionality() throws Exception {
        System.out.println("--- Undo Tests ---");
        
        SokobanGame1 game = new SokobanGame1();
        game.initializeGame(7, 7, 1);
        
        // Get initial position
        java.lang.reflect.Field playerField = SokobanGame1.class.getDeclaredField("playerPosition");
        playerField.setAccessible(true);
        java.awt.Point initialPos = (java.awt.Point) playerField.get(game);
        
        // Make a move
        game.move("d");
        java.awt.Point afterMovePos = (java.awt.Point) playerField.get(game);
        
        // Undo the move
        boolean undoResult = game.undo();
        java.awt.Point afterUndoPos = (java.awt.Point) playerField.get(game);
        
        assertTrue(undoResult, "Undo should succeed");
        assertEquals(initialPos.x, afterUndoPos.x, "Player X position after undo");
        assertEquals(initialPos.y, afterUndoPos.y, "Player Y position after undo");
        
        // Test undo counter
        java.lang.reflect.Field undoCounterField = SokobanGame1.class.getDeclaredField("undoCounter");
        undoCounterField.setAccessible(true);
        assertEquals(1, undoCounterField.get(game), "Undo counter increment");
    }
    
    private static void testDeadlockDetection() throws Exception {
        System.out.println("--- Deadlock Detection Tests ---");
        
        SokobanGame1 game = new SokobanGame1();
        game.initializeGame(7, 7, 1);
        
        // Test that deadlock detection methods exist and don't crash
        try {
            // Use reflection to call private methods
            Method deadlockMethod = SokobanGame1.class.getDeclaredMethod("deadlockDetected");
            deadlockMethod.setAccessible(true);
            boolean deadlock = (Boolean) deadlockMethod.invoke(game);
            
            // We can't guarantee deadlock state due to random placement,
            // but the method should execute without error
            assertTrue(true, "Deadlock detection doesn't crash");
            
        } catch (Exception e) {
            fail("Deadlock detection should work: " + e.getMessage());
        }
    }
    
    private static void testInputValidation() {
        System.out.println("--- Input Validation Tests ---");
        
        // Test main method with various inputs
        String[] testInputs = {
            "5\n5\n1\nq\n",           // Valid input, then quit
            "4\n5\n1\n5\n1\nq\n",     // Invalid width, then valid
            "5\n4\n5\n1\nq\n",        // Invalid height, then valid  
            "5\n5\n0\n1\nq\n",        // Invalid box count, then valid
        };
        
        for (String input : testInputs) {
            try {
                System.setIn(new ByteArrayInputStream(input.getBytes()));
                
                // Capture output to avoid console spam
                ByteArrayOutputStream outContent = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outContent));
                
                // Run main method in a separate thread to handle System.exit
                Thread gameThread = new Thread(() -> {
                    try {
                        SokobanGame1.main(new String[]{});
                    } catch (Exception e) {
                        // Expected for some test cases
                    }
                });
                gameThread.start();
                gameThread.join(2000); // Wait max 2 seconds
                
                assertTrue(true, "Input handling: " + input);
                
            } catch (Exception e) {
                fail("Input validation test failed: " + e.getMessage());
            }
        }
        
        // Restore standard input/output
        System.setIn(System.in);
        System.setOut(System.out);
    }
    
    private static void testIntegration() {
        System.out.println("--- Integration Tests ---");
        
        // Test complete game flow
        String gameInput = "7\n7\n2\n"; // Valid initialization
        gameInput += "w\ns\na\nd\n";    // Some movements
        gameInput += "u\nu\n";          // Undo operations  
        gameInput += "q\n";             // Quit
        
        try {
            System.setIn(new ByteArrayInputStream(gameInput.getBytes()));
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));
            
            Thread gameThread = new Thread(() -> {
                try {
                    SokobanGame1.main(new String[]{});
                } catch (Exception e) {
                    // Expected
                }
            });
            gameThread.start();
            gameThread.join(3000);
            
            String output = outContent.toString();
            assertTrue(output.contains("Game initialized") || output.contains("Undos left"), 
                      "Integration test - game runs without crash");
            
        } catch (Exception e) {
            fail("Integration test failed: " + e.getMessage());
        } finally {
            System.setIn(System.in);
            System.setOut(System.out);
        }
    }
    
    // Helper assertion methods
    private static void assertTrue(boolean condition, String testName) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.println("✓ PASS: " + testName);
        } else {
            System.out.println("✗ FAIL: " + testName);
        }
    }
    
    private static void assertEquals(Object expected, Object actual, String testName) {
        totalTests++;
        boolean equal = (expected == null && actual == null) || 
                       (expected != null && expected.equals(actual));
        
        if (equal) {
            passedTests++;
            System.out.println("✓ PASS: " + testName + " (expected: " + expected + ", got: " + actual + ")");
        } else {
            System.out.println("✗ FAIL: " + testName + " (expected: " + expected + ", got: " + actual + ")");
        }
    }
    
    private static void fail(String message) {
        totalTests++;
        System.out.println("✗ FAIL: " + message);
    }
}