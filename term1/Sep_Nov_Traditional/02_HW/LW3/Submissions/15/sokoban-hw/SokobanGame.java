import java.util.*;
import java.io.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;

// това е нормалната имплементация, бонусите са в SokobanGame
class SokobanGameBase {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';
    
    protected int width, height, boxesCount;
    protected int playerX, playerY;
    protected int moves, boxesOnTargets;
    
    protected char[][] displayBoard;
    protected boolean[][] isTarget;
    protected boolean[][] hasBox;
    
    private Random random;
    
    public SokobanGameBase() {
        this.random = new Random();
        this.moves = 0;
        this.boxesOnTargets = 0;
    }
    
    public void initialiseGame(int w, int h, int k) {
        this.width = w;
        this.height = h;
        this.boxesCount = k;
        this.moves = 0;
        this.boxesOnTargets = 0;
        
        this.displayBoard = new char[height][width];
        this.isTarget = new boolean[height][width];
        this.hasBox = new boolean[height][width];
        
        initialiseBoard();
        placePlayer();
        placeTargets();
        placeBoxes();
        updateDisplay();
    }

    private void initialiseBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    displayBoard[y][x] = WALL;
                } else {
                    displayBoard[y][x] = EMPTY;
                }
                isTarget[y][x] = false;
                hasBox[y][x] = false;
            }
        }
    }
    
    private void placePlayer() {
        this.playerX = width / 2;
        this.playerY = height / 2;
    }
    
    private void placeTargets() {
        List<int[]> availablePositions = new ArrayList<>();
  
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (x != playerX || y != playerY) {
                    availablePositions.add(new int[]{x, y});
                }
            }
        }
        
        Collections.shuffle(availablePositions, random);
        for (int i = 0; i < boxesCount; i++) {
            int[] pos = availablePositions.get(i);
            isTarget[pos[1]][pos[0]] = true;
        }
    }
    
    private void placeBoxes() {
        int boxesPlaced = 0;
        int attempts = 0;
        int maxAttempts = 1000;
        
        while (boxesPlaced < boxesCount && attempts < maxAttempts) {
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;
            
            if (isValidBoxPlacement(x, y)) {
                hasBox[y][x] = true;
                boxesPlaced++;
                if (isTarget[y][x]) {
                    boxesOnTargets++;
                }
            }
            attempts++;
        }
        
        if (boxesPlaced < boxesCount) {
            System.out.println("Внимание: Нямаше как да поставим всички кутии.");
        }
    }
   
    protected boolean isValidBoxPlacement(int x, int y) {
        // R1
        if (x == playerX && y == playerY) return false; 
        if (hasBox[y][x]) return false;
        if (isTarget[y][x]) return false; 
        
        // R2
        if (isInteriorCorner(x, y) && !isTarget[y][x]) {
            return false;
        }
        
        // R3
        if (violatesWallCapacity(x, y)) {
            return false;
        }
        
        return true;
    }
    
    private boolean isInteriorCorner(int x, int y) {
        return (x == 1 && y == 1) || 
               (x == width - 2 && y == 1) || 
               (x == 1 && y == height - 2) || 
               (x == width - 2 && y == height - 2);
    }
    
    private boolean violatesWallCapacity(int x, int y) {
        boolean adjacentToWall = (x == 1 || x == width - 2 || y == 1 || y == height - 2);
        
        if (!adjacentToWall) return false;
        
        if (x == 1) { // лява
            return countBoxesOnWall(1, 0, 1, height - 1) >= countTargetsOnWall(1, 0, 1, height - 1);
        } else if (x == width - 2) { // дясна
            return countBoxesOnWall(width - 2, 0, width - 2, height - 1) >= countTargetsOnWall(width - 2, 0, width - 2, height - 1);
        } else if (y == 1) { // горна
            return countBoxesOnWall(0, 1, width - 1, 1) >= countTargetsOnWall(0, 1, width - 1, 1);
        } else if (y == height - 2) { // долна
            return countBoxesOnWall(0, height - 2, width - 1, height - 2) >= countTargetsOnWall(0, height - 2, width - 1, height - 2);
        }
        
        return false;
    }
    
    private int countBoxesOnWall(int x1, int y1, int x2, int y2) {
        int count = 0;
        if (x1 == x2) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                if (y > 0 && y < height - 1 && hasBox[y][x1]) count++;
            }
        } else {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                if (x > 0 && x < width - 1 && hasBox[y1][x]) count++;
            }
        }
        return count;
    }
    
    private int countTargetsOnWall(int x1, int y1, int x2, int y2) {
        int count = 0;
        if (x1 == x2) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                if (y > 0 && y < height - 1 && isTarget[y][x1]) count++;
            }
        } else {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                if (x > 0 && x < width - 1 && isTarget[y1][x]) count++;
            }
        }
        return count;
    }
   
    protected boolean canPushBoxTo(int newX, int newY, int oldX, int oldY) {
        // R2
        if (isInteriorCorner(newX, newY) && !isTarget[newY][newX]) {
            return false;
        }
        
        // R3
        if (violatesWallCapacityDuringMove(newX, newY, oldX, oldY)) {
            return false;
        }
        
        return true;
    }
    
    private boolean violatesWallCapacityDuringMove(int newX, int newY, int oldX, int oldY) {
        boolean newPosAdjacentToWall = (newX == 1 || newX == width - 2 || newY == 1 || newY == height - 2);
        
        if (!newPosAdjacentToWall) return false;
        
        boolean oldPosOnSameWall = false;
        
        if (newX == 1) { // лява
            oldPosOnSameWall = (oldX == 1);
            int boxesOnWall = countBoxesOnWall(1, 0, 1, height - 1);
            if (!oldPosOnSameWall) boxesOnWall++;
            int targetsOnWall = countTargetsOnWall(1, 0, 1, height - 1);
            return boxesOnWall > targetsOnWall;
        } else if (newX == width - 2) { // дясна
            oldPosOnSameWall = (oldX == width - 2);
            int boxesOnWall = countBoxesOnWall(width - 2, 0, width - 2, height - 1);
            if (!oldPosOnSameWall) boxesOnWall++;
            int targetsOnWall = countTargetsOnWall(width - 2, 0, width - 2, height - 1);
            return boxesOnWall > targetsOnWall;
        } else if (newY == 1) { // горна 
            oldPosOnSameWall = (oldY == 1);
            int boxesOnWall = countBoxesOnWall(0, 1, width - 1, 1);
            if (!oldPosOnSameWall) boxesOnWall++;
            int targetsOnWall = countTargetsOnWall(0, 1, width - 1, 1);
            return boxesOnWall > targetsOnWall;
        } else if (newY == height - 2) { // долна
            oldPosOnSameWall = (oldY == height - 2);
            int boxesOnWall = countBoxesOnWall(0, height - 2, width - 1, height - 2);
            if (!oldPosOnSameWall) boxesOnWall++;
            int targetsOnWall = countTargetsOnWall(0, height - 2, width - 1, height - 2);
            return boxesOnWall > targetsOnWall;
        }
        
        return false;
    }
    
    public boolean move(String direction) {
        int dx = 0, dy = 0;
        
        switch (direction.toLowerCase()) {
            case "up":
            case "w":
                dy = -1;
                break;
            case "down":
            case "s":
                dy = 1;
                break;
            case "left":
            case "a":
                dx = -1;
                break;
            case "right":
            case "d":
                dx = 1;
                break;
            default:
                return false;
        }
        
        int newX = playerX + dx;
        int newY = playerY + dy;
        
        if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
            System.out.println("Invalid move");
            return false;
        }
        
        if (displayBoard[newY][newX] == WALL) {
            System.out.println("Invalid move");
            return false;
        }
        
        if (hasBox[newY][newX]) {
            int boxNewX = newX + dx;
            int boxNewY = newY + dy;
            
            if (boxNewX < 0 || boxNewX >= width || boxNewY < 0 || boxNewY >= height ||
                displayBoard[boxNewY][boxNewX] == WALL || hasBox[boxNewY][boxNewX]) {
                System.out.println("Invalid move");
                return false;
            }
            
            if (!canPushBoxTo(boxNewX, boxNewY, newX, newY)) {
                System.out.println("Invalid move");
                return false;
            }
            
            hasBox[newY][newX] = false;
            hasBox[boxNewY][boxNewX] = true;
            
            if (isTarget[newY][newX]) boxesOnTargets--;
            if (isTarget[boxNewY][boxNewX]) boxesOnTargets++;
        }
        
        playerX = newX;
        playerY = newY;
        moves++;
        
        updateDisplay();
        return true;
    }
    
    protected void updateDisplay() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    displayBoard[y][x] = WALL;
                } else if (isTarget[y][x]) {
                    displayBoard[y][x] = TARGET;
                } else {
                    displayBoard[y][x] = EMPTY;
                }
            }
        }
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (hasBox[y][x]) {
                    if (isTarget[y][x]) {
                        displayBoard[y][x] = BOX_ON_TARGET;
                    } else {
                        displayBoard[y][x] = BOX;
                    }
                }
            }
        }
        
        displayBoard[playerY][playerX] = PLAYER;
    }
    
    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }
   
    public void printBoard() {
        System.out.println("\nMoves: " + moves + " | Boxes on targets: " + boxesOnTargets + "/" + boxesCount);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(displayBoard[y][x]);
            }
            System.out.println();
        }
    }
    
    public int getMoves() {
        return moves;
    }
    
    public static boolean isValidInput(int w, int h, int k) {
        if (w < 5 || h < 5) return false;
        int maxBoxes = ((w - 2) * (h - 2) - 2) / 2;
        return k <= maxBoxes;
    }
}

// това е имплементацията с бонусите
public class SokobanGame extends SokobanGameBase {
    
    private Stack<GameState> gameHistory;
    private static final int MAX_UNDO_LEVELS = 50;
    
    private static class GameState {
        int playerX, playerY, moves, boxesOnTargets;
        boolean[][] hasBox;
        
        public GameState(int px, int py, int m, int bot, boolean[][] boxes) {
            this.playerX = px;
            this.playerY = py;
            this.moves = m;
            this.boxesOnTargets = bot;
            
            this.hasBox = new boolean[boxes.length][boxes[0].length];
            for (int i = 0; i < boxes.length; i++) {
                System.arraycopy(boxes[i], 0, this.hasBox[i], 0, boxes[i].length);
            }
        }
    }
    
    public SokobanGame() {
        super();
        this.gameHistory = new Stack<>();
    }
    
    @Override
    public void initialiseGame(int w, int h, int k) {
        super.initialiseGame(w, h, k);
        gameHistory.clear();
        saveCurrentState();
    }
   
    private void saveCurrentState() {
        if (gameHistory.size() >= MAX_UNDO_LEVELS) {
            gameHistory.remove(0);
        }
        
        gameHistory.push(new GameState(
            getPlayerX(), getPlayerY(), getMoves(), 
            getBoxesOnTargets(), getHasBoxArray()
        ));
    }
    
    public boolean undo() {
        if (gameHistory.size() <= 1) {
            System.out.println("No moves to undo!");
            return false;
        }
        
        gameHistory.pop();
        GameState previousState = gameHistory.peek();
        
        setPlayerPosition(previousState.playerX, previousState.playerY);
        setMoves(previousState.moves);
        setBoxesOnTargets(previousState.boxesOnTargets);
        setHasBoxArray(previousState.hasBox);
        
        updateDisplay();
        System.out.println("Move undone!");
        return true;
    }
    
    @Override
    public boolean move(String direction) {
        saveCurrentState();
        
        boolean moved = super.move(direction);
        
        if (!moved) {
            gameHistory.pop();
        }
        
        return moved;
    }
    
    protected int getPlayerX() { return super.playerX; }
    protected int getPlayerY() { return super.playerY; }
    protected int getWidth() { return super.width; }
    protected int getHeight() { return super.height; }
    protected int getBoxesOnTargets() { return super.boxesOnTargets; }
    protected boolean[][] getHasBoxArray() { return super.hasBox; }
    protected boolean[][] getIsTargetArray() { return super.isTarget; }
    protected char[][] getDisplayBoard() { return super.displayBoard; }
    
    protected void setPlayerPosition(int x, int y) { 
        super.playerX = x; 
        super.playerY = y; 
    }
    protected void setMoves(int moves) { super.moves = moves; }
    protected void setBoxesOnTargets(int count) { super.boxesOnTargets = count; }
    protected void setHasBoxArray(boolean[][] hasBox) {
        for (int i = 0; i < hasBox.length; i++) {
            System.arraycopy(hasBox[i], 0, super.hasBox[i], 0, hasBox[i].length);
        }
    }
    
    @Override
    public void printBoard() {
        super.printBoard();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== Welcome to Sokoban! ===");
        System.out.println("Choose game mode:");
        System.out.println("(L) Level System - Play 10 pre-built levels");
        System.out.println("(R) Random Generation - Play on randomly generated boards");
        System.out.println("(O) Own Map - Load a custom level from a file");
        System.out.print("Enter your choice (L/R/O): ");
        
        String modeChoice = scanner.nextLine().trim().toLowerCase();
        
        if (modeChoice.equals("l")) {
            playLevelSystem(scanner);
        } else if (modeChoice.equals("r")) {
            playRandomMode(scanner);
        } else if (modeChoice.equals("o")) {
            playOwnMapMode(scanner);
        } else {
            System.out.println("Invalid choice. Please enter L, R, or O.");
            scanner.close();
            main(args);
            return;
        }
        
        scanner.close();
    }
    
    private static void playLevelSystem(Scanner scanner) {
        System.out.println("\n=== Level System Mode ===\n");
        
        int currentLevel = 1;
        int totalLevels = 10;
        
        while (currentLevel <= totalLevels) {
            System.out.println("Loading Level " + currentLevel + "...");
            
            SokobanGame game = new SokobanGame();
            
            try {
                LevelParser.LevelData levelData = LevelParser.loadLevel(currentLevel);
                LevelParser.initialiseGameFromLevel(game, levelData);
                
                System.out.println("Level " + currentLevel + " loaded successfully!\n");
                System.out.println("Commands: W/A/S/D for movement , U for undo, Q to quit");
                System.out.println("Push all boxes (B) onto targets (*) to complete the level!");
                
                boolean levelComplete = playLevel(game, currentLevel, totalLevels);
                
                if (!levelComplete) {
                    System.out.println("\nGame quit. Thanks for playing!");
                    break;
                }
                
                currentLevel++;
                
            } catch (IOException e) {
                System.out.println("Error loading level: " + e.getMessage());
                System.out.println("Would you like to continue? (Y/N)");
                String continueChoice = scanner.nextLine().trim().toLowerCase();
                if (!continueChoice.equals("y")) {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid level format: " + e.getMessage());
                System.out.println("Would you like to continue? (Y/N)");
                String continueChoice = scanner.nextLine().trim().toLowerCase();
                if (!continueChoice.equals("y")) {
                    break;
                }
            }
        }
        
        if (currentLevel > totalLevels) {
            System.out.println("\nCongratulations! You have completed all " + totalLevels + " levels!");
        }
    }
    
    private static void playRandomMode(Scanner scanner) {
        System.out.println("\n=== Random Generation Mode ===\n");
        
        int W, H, K;
        
        do {
            System.out.print("Enter width (W >= 5): ");
            W = scanner.nextInt();
            if (W < 5) {
                System.out.println("Width must be at least 5. Please try again.");
            }
        } while (W < 5);
        
        do {
            System.out.print("Enter height (H >= 5): ");
            H = scanner.nextInt();
            if (H < 5) {
                System.out.println("Height must be at least 5. Please try again.");
            }
        } while (H < 5);
        
        int maxBoxes = ((W - 2) * (H - 2) - 2) / 2;
        do {
            System.out.print("Enter number of boxes (K <= " + maxBoxes + "): ");
            K = scanner.nextInt();
            if (K > maxBoxes) {
                System.out.println("Too many boxes for this board size. Maximum is " + maxBoxes + ". Please try again.");
            }
        } while (K > maxBoxes);
        
        SokobanGame game = new SokobanGame();
        game.initialiseGame(W, H, K);
        
        scanner.nextLine();
        
        System.out.println("Commands: W/A/S/D for movement, U for undo, Q to quit");
        System.out.println("Push all boxes (B) onto targets (*) to win!");
        
        playLevel(game, 0, 0);
    }
    
    private static void playOwnMapMode(Scanner scanner) {
        System.out.println("\n=== Own Map Mode ===\n");
        
        System.out.print("Enter the path to your level file: ");
        String filePath = scanner.nextLine().trim();
        
        SokobanGame game = new SokobanGame();
        
        try {
            LevelParser.LevelData levelData = LevelParser.parseLevel(filePath);
            LevelParser.initialiseGameFromLevel(game, levelData);
            
            System.out.println("Custom level loaded successfully!\n");
            System.out.println("Commands: W/A/S/D for movement, U for undo, Q to quit");
            System.out.println("Push all boxes (B) onto targets (*) to complete the level!");
            
            playLevel(game, 0, 0);
            
        } catch (IOException e) {
            System.out.println("Error loading level: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid level format: " + e.getMessage());
        }
    }
    
    private static boolean playLevel(SokobanGame game, int currentLevel, int totalLevels) {
        Terminal terminal = null;
        Attributes originalAttributes = null;
        
        try {
            // JLine терминал, за да можем да играем без да натискаме еnter
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(true)
                    .build();
            
            originalAttributes = terminal.enterRawMode();
            var reader = terminal.reader();
            
            game.printBoard();
            
            while (!game.checkWin()) {
                int input = reader.read();
                if (input == -1) break;
                
                char command = (char) input;
                
                if (command == 'q' || command == 'Q') {
                    System.out.println("\nLevel quit. Thanks for playing!");
                    return false;
                }
                
                if (command == 'u' || command == 'U') {
                    game.undo();
                    game.printBoard();
                    continue;
                }
                
                String direction = null;
                switch (command) {
                    case 'w':
                    case 'W':
                        direction = "up";
                        break;
                    case 's':
                    case 'S':
                        direction = "down";
                        break;
                    case 'a':
                    case 'A':
                        direction = "left";
                        break;
                    case 'd':
                    case 'D':
                        direction = "right";
                        break;
                }
                
                if (direction != null) {
                    if (game.move(direction)) {
                        game.printBoard();
                    }
                }
            }
            
            game.printBoard();
            System.out.println("\n🎉 Level Complete! You finished in " + game.getMoves() + " moves!");
            
            if (currentLevel > 0 && currentLevel < totalLevels) {
                System.out.println("\nPress any key to continue to Level " + (currentLevel + 1) + "...");
                reader.read();
            }
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error with terminal input: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // нормален мод на терминала
            if (terminal != null && originalAttributes != null) {
                try {
                    terminal.setAttributes(originalAttributes);
                    terminal.close();
                } catch (Exception e) {
                    System.err.println("Error restoring terminal: " + e.getMessage());
                }
            }
        }
    }
}