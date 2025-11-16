import java.util.*;

public class SokobanGame {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';
    
    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets;
    
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;
    
    private Stack<GameState> history;
    
    private Random random;
    
    // Main Class
    // Custom Exception Classes
    
    public static class InvalidMoveException extends Exception {
        public enum MoveError {
            WALL_COLLISION("Cannot move into a wall"),
            BOX_BLOCKED("Cannot push box - blocked by wall or another box"),
            INVALID_DIRECTION("Invalid movement direction"),
            OUT_OF_BOUNDS("Move would go out of bounds");
            
            private final String message;
            
            MoveError(String message) {
                this.message = message;
            }
            
            public String getMessage() {
                return message;
            }
        }
        
        private final MoveError errorType;
        
        public InvalidMoveException(MoveError errorType) {
            super(errorType.getMessage());
            this.errorType = errorType;
        }
        
        public MoveError getErrorType() {
            return errorType;
        }
    }
    
    //Exception thrown when there's an invalid game state
 
    public static class InvalidGameStateException extends Exception {
        public InvalidGameStateException(String message) {
            super(message);
        }
    }
    
    // Inner class for storing game state
    private static class GameState {
        int playerX, playerY;
        int moves, boxesOnTargets;
        boolean[][] hasBox;
        
        GameState(int px, int py, int m, int bot, boolean[][] box) {
            this.playerX = px;
            this.playerY = py;
            this.moves = m;
            this.boxesOnTargets = bot;
            this.hasBox = deepCopy(box);
        }
        
        private static boolean[][] deepCopy(boolean[][] array) {
            boolean[][] copy = new boolean[array.length][];
            for (int i = 0; i < array.length; i++) {
                copy[i] = array[i].clone();
            }
            return copy;
        }
    }
    
    public SokobanGame() {
        this.random = new Random();
        this.history = new Stack<>();
    }
    
        //initialize game
    public void initializeGame(int w, int h, int k) throws InvalidGameStateException {
        // Validate input parameters
        if (w < 5 || h < 5) {
            throw new InvalidGameStateException("Board dimensions must be at least 5x5");
        }
        
        int maxBoxes = ((w - 2) * (h - 2) - 2) / 2;
        if (k < 1 || k > maxBoxes) {
            throw new InvalidGameStateException(
                "Box count must be between 1 and " + maxBoxes + " for a " + w + "x" + h + " board"
            );
        }
        
        this.width = w;
        this.height = h;
        this.boxesCount = k;
        this.moves = 0;
        this.boxesOnTargets = 0;
        
        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];
        
        initializeWalls();
        placePlayer();
        placeTargets();
        placeBoxes();
        updateDisplay();
        
        // Validate that game state is valid after initialization
        if (boxesOnTargets > boxesCount) {
            throw new InvalidGameStateException("Invalid state: More boxes on targets than total boxes");
        }
    }
    
    //walls

    private void initializeWalls() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    displayBoard[y][x] = WALL;
                } else {
                    displayBoard[y][x] = EMPTY;
                }
            }
        }
    }
    
    
    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
    }
    
    //Place K targets randomly on interior cells
     
    private void placeTargets() {
        List<int[]> availableCells = new ArrayList<>();
        
        // Collect all interior cells except player position
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (x != playerX || y != playerY) {
                    availableCells.add(new int[]{x, y});
                }
            }
        }
        
        // Randomly select K cells for targets
        Collections.shuffle(availableCells, random);
        for (int i = 0; i < boxesCount; i++) {
            int[] cell = availableCells.get(i);
            isTarget[cell[1]][cell[0]] = true;
        }
    }
    
    //Place K boxes randomly following all placement rules
    private void placeBoxes() {
        List<int[]> availableCells = new ArrayList<>();
        
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (isValidBoxPlacement(x, y)) {
                    availableCells.add(new int[]{x, y});
                }
            }
        }
        
        // place boxes randomly
        Collections.shuffle(availableCells, random);
        int placedBoxes = 0;
        
        for (int[] cell : availableCells) {
            if (placedBoxes >= boxesCount) break;
            
            int x = cell[0];
            int y = cell[1];
            
            if (isValidBoxPlacement(x, y)) {
                hasBox[y][x] = true;
                if (isTarget[y][x]) {
                    boxesOnTargets++;
                }
                placedBoxes++;
            }
        }
        
        // retry with a new configuration if sth wrong
        if (placedBoxes < boxesCount) {
            hasBox = new boolean[height][width];
            boxesOnTargets = 0;
            placeBoxes();
        }
    }
    
    //Valid box placement
    private boolean isValidBoxPlacement(int x, int y) {
        // R1
        if (x == playerX && y == playerY) return false; 
        if (hasBox[y][x]) return false; 
        if (isTarget[y][x]) return false; 
        
        // R2
        if (!isTarget[y][x] && isInteriorCorner(x, y)) {
            return false;
        }
        //R3
        if (!checkWallCapacity(x, y)) {
            return false;
        }
        
        return true;
    }
    
    //Check if position is an interior corner

    private boolean isInteriorCorner(int x, int y) {
        return (x == 1 && y == 1) ||
               (x == width - 2 && y == 1) ||
               (x == 1 && y == height - 2) ||
               (x == width - 2 && y == height - 2);
    }
    
    //R3 - Check wall capacity constraint
    private boolean checkWallCapacity(int x, int y) {
        boolean nextToLeftWall = (x == 1);
        boolean nextToRightWall = (x == width - 2);
        boolean nextToTopWall = (y == 1);
        boolean nextToBottomWall = (y == height - 2);
        
        if (nextToLeftWall) {
            if (!checkWallCapacityOnSide(1, true)) return false;
        }
        if (nextToRightWall) {
            if (!checkWallCapacityOnSide(width - 2, true)) return false;
        }
        if (nextToTopWall) {
            if (!checkWallCapacityOnSide(1, false)) return false;
        }
        if (nextToBottomWall) {
            if (!checkWallCapacityOnSide(height - 2, false)) return false;
        }
        
        return true;
    }
    
    //Check capacity for a specific wall side
    private boolean checkWallCapacityOnSide(int pos, boolean isVertical) {
        int boxesOnWall = 0;
        int targetsOnWall = 0;
        
        if (isVertical) {
            // Count along a column
            for (int y = 1; y < height - 1; y++) {
                if (hasBox[y][pos]) boxesOnWall++;
                if (isTarget[y][pos]) targetsOnWall++;
            }
            boxesOnWall++;
        } else {
            for (int x = 1; x < width - 1; x++) {
                if (hasBox[pos][x]) boxesOnWall++;
                if (isTarget[pos][x]) targetsOnWall++;
            }
            boxesOnWall++;
        }
        
        return boxesOnWall <= targetsOnWall;
    }
    
    //Moving
    public void move(String direction) throws InvalidMoveException {
        int dx = 0, dy = 0;
        
        switch (direction.toLowerCase()) {

            case "w":
                dy = -1;
                break;
            case "s":
                dy = 1;
                break;
            case "a":
                dx = -1;
                break;
            case "d":
                dx = 1;
                break;
            default:
                throw new InvalidMoveException(InvalidMoveException.MoveError.INVALID_DIRECTION);
        }
        
        int nextX = playerX + dx;
        int nextY = playerY + dy;
        
        // Check bounds
        if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) {
            throw new InvalidMoveException(InvalidMoveException.MoveError.OUT_OF_BOUNDS);
        }
        
        // Check walls
        if (displayBoard[nextY][nextX] == WALL) {
            throw new InvalidMoveException(InvalidMoveException.MoveError.WALL_COLLISION);
        }
        
        // Check if there's a box at the next position
        if (hasBox[nextY][nextX]) {
            int boxNextX = nextX + dx;
            int boxNextY = nextY + dy;
            
            // Check if box can be pushed
            if (boxNextX < 0 || boxNextX >= width || boxNextY < 0 || boxNextY >= height) {
                throw new InvalidMoveException(InvalidMoveException.MoveError.OUT_OF_BOUNDS);
            }
            
            if (displayBoard[boxNextY][boxNextX] == WALL || hasBox[boxNextY][boxNextX]) {
                throw new InvalidMoveException(InvalidMoveException.MoveError.BOX_BLOCKED);
            }
            
            saveState();
            
            hasBox[nextY][nextX] = false;
            hasBox[boxNextY][boxNextX] = true;
            
            if (isTarget[nextY][nextX]) boxesOnTargets--;
            if (isTarget[boxNextY][boxNextX]) boxesOnTargets++;
        } else {
            saveState();
        }
        
        playerX = nextX;
        playerY = nextY;
        moves++;
        
        updateDisplay();
    }
    
    //Save state
    private void saveState() {
        history.push(new GameState(playerX, playerY, moves, boxesOnTargets, hasBox));
    }
    
    //Undo
    public boolean undo() {
        if (history.isEmpty()) {
            System.out.println("No moves to undo");
            return false;
        }
        
        GameState previousState = history.pop();
        playerX = previousState.playerX;
        playerY = previousState.playerY;
        moves = previousState.moves;
        boxesOnTargets = previousState.boxesOnTargets;
        hasBox = previousState.hasBox;
        
        updateDisplay();
        System.out.println("Move undone");
        return true;
    }
    
    //Update display board
     
    private void updateDisplay() {
        // Reset interior cells
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (isTarget[y][x]) {
                    displayBoard[y][x] = TARGET;
                } else {
                    displayBoard[y][x] = EMPTY;
                }
            }
        }
        
        // Place boxes
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (hasBox[y][x]) {
                    if (isTarget[y][x]) {
                        displayBoard[y][x] = BOX_ON_TARGET;
                    } else {
                        displayBoard[y][x] = BOX;
                    }
                }
            }
        }
        
        // Place player
        displayBoard[playerY][playerX] = PLAYER;
    }

    //Check if the player has won
    
    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }
    
    //Print board
    
    public void printBoard() {
        String border = repeatChar('=', width * 2 + 3);
        System.out.println("\n" + border);
        for (int y = 0; y < height; y++) {
            System.out.print("| ");
            for (int x = 0; x < width; x++) {
                System.out.print(displayBoard[y][x] + " ");
            }
            System.out.println("|");
        }
        System.out.println(border);
        System.out.println("Moves: " + moves + " | Boxes on targets: " + boxesOnTargets + "/" + boxesCount);
        System.out.println("Commands: w/up, s/down, a/left, d/right, u/undo, q/quit");
    }
    
    //Repeat a character
    private String repeatChar(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
    
    //Current number of moves
    public int getMoves() {
        return moves;
    } 
    //Main game
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Welcome to Sokoban ===");
        System.out.println();
        
        SokobanGame game = null;
        
        try {
            // Read inputs
            int W = readValidatedDimension(scanner, "Width (W >= 5): ", 5, Integer.MAX_VALUE);
            int H = readValidatedDimension(scanner, "Height (H >= 5): ", 5, Integer.MAX_VALUE);
            int maxBoxes = ((W - 2) * (H - 2) - 2) / 2;
            int K = readValidatedDimension(scanner, "Number of boxes (K <= " + maxBoxes + "): ", 1, maxBoxes);
            
            // Initialize game with exception handling
            game = new SokobanGame();
            game.initializeGame(W, H, K);
            
            System.out.println("\nGame initialized successfully!");
            System.out.println("Legend: @ = Player, B = Box, * = Target, O = Box on Target");
            
        } catch (InvalidGameStateException e) {
            System.err.println("Error initializing game: " + e.getMessage());
            scanner.close();
            return;
        }
        
        // Game
        while (!game.checkWin()) {
            try {
                game.printBoard();
                
                System.out.print("\nEnter command: ");
                String command = scanner.nextLine().trim().toLowerCase();
                
                if (command.equals("quit") || command.equals("q")) {
                    System.out.println("Game quit. Thanks for playing!");
                    break;
                } else if (command.equals("undo") || command.equals("u")) {
                    game.undo();
                } else {
                    game.move(command);
                }
                
            } catch (InvalidMoveException e) {
                // Handle invalid move with specific error message
                System.out.println("Invalid move: " + e.getMessage());
                
                // Optionally show what type of error it was
                switch (e.getErrorType()) {
                    case WALL_COLLISION:
                        System.out.println("   You cannot walk through walls!");
                        break;
                    case BOX_BLOCKED:
                        System.out.println("   Make sure there's space behind the box!");
                        break;
                    case INVALID_DIRECTION:
                        System.out.println("   Use w/a/s/d for movement");
                        break;
                    case OUT_OF_BOUNDS:
                        System.out.println("   Stay within the board boundaries!");
                        break;
                }
            } catch (Exception e) {
                // Catch any unexpected errors
                System.err.println("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Check if player won
        if (game.checkWin()) {
            game.printBoard();
            System.out.println("\nYou win in " + game.getMoves() + " moves!");
        }
        
        scanner.close();
    }
    
    //Read input
    private static int readValidatedDimension(Scanner scanner, String prompt, int min, int max) {
        int value;
        while (true) {
            System.out.print(prompt);
            try {
                value = scanner.nextInt();
                scanner.nextLine();
                
                if (value >= min && value <= max) {
                    return value;
                } 
                 else {
                        System.out.println("Invalid input. Value must be between " + min + " and " + max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}

