import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SokobanGame {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    private enum CellState {
        WALL,
        EMPTY,
        TARGET,
        BOX,
        BOX_ON_TARGET
    }

    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets;

    private CellState[][] state;
    
    private Random random;
    
    private CellState[][] prevState;
    private int prevPlayerX, prevPlayerY;
    private boolean canUndo;

    private static class Level {
        String name;
        String difficulty;
        String[] board;

        Level(String name, String difficulty, String[] board) {
            this.name = name;
            this.difficulty = difficulty;
            this.board = board;
        }
    }

    private static final Level[] PRESET_LEVELS = {
        new Level("Beginner", "Easy", new String[] {
            "########",
            "#......#",
            "#..B*..#",
            "#......#",
            "#..@...#",
            "#......#",
            "#..B*..#",
            "########"
        }),
        new Level("Intermediate", "Medium", new String[] {
            "##########",
            "#........#",
            "#.##..##.#",
            "#.*B..B*.#",
            "#..#..#..#",
            "#...@....#",
            "#..#..#..#",
            "#.*B..B*.#",
            "#.##..##.#",
            "##########"
        }),
        new Level("Death Trap", "Hard", new String[] {
            "#####################",
            "#....#.....#....*...#",
            "#....#..B..#....**..#",
            "#....###.###........#",
            "#..B....B....###.B..#",
            "#.#####.#####.#....##",
            "#.#...#...#...#.##..#",
            "#.#.B.#.B.#.B.#.#...#",
            "#.#...#...#...#.#.@.#",
            "#.#####.###.#####.###",
            "#...B.....#.....B...#",
            "###.###.###.###.###.#",
            "#*..#...B.....#...*.#",
            "#*.#.#.#####.#.#.#*.#",
            "#..#...#...#...#....#",
            "#...**.....B.....B..#",
            "#####################"
        })
    };

    public SokobanGame() {
        random = new Random();
        canUndo = false;
    }

    public void initializeGame(int w, int h, int k) {
        width = w;
        height = h;
        boxesCount = k;
        moves = 0;
        boxesOnTargets = 0;

        state = new CellState[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    state[i][j] = CellState.WALL;
                } else {
                    state[i][j] = CellState.EMPTY;
                }
            }
        }

        placePlayer();
        placeTargets();
        placeBoxes();
    }

    public void stringToStateMapper(String[] board) throws InvalidGameStateException {
        if (board == null || board.length == 0) {
            throw new InvalidGameStateException("Board cannot be empty");
        }
        
        height = board.length;
        width = board[0].length();
        moves = 0;
        boxesOnTargets = 0;
        boxesCount = 0;

        state = new CellState[height][width];
        
        boolean playerFound = false;
        int boxCount = 0;
        int targetCount = 0;

        for (int i = 0; i < height; i++) {
            if (board[i].length() != width) {
                throw new InvalidGameStateException("All rows must have same width");
            }
            
            for (int j = 0; j < width; j++) {
                char c = board[i].charAt(j);
                switch (c) {
                    case '#':
                        state[i][j] = CellState.WALL;
                        break;
                    case '@':
                        if (playerFound) {
                            throw new InvalidGameStateException("Multiple players found - only one @ allowed");
                        }
                        state[i][j] = CellState.EMPTY;
                        playerX = j;
                        playerY = i;
                        playerFound = true;
                        break;
                    case '*':
                        state[i][j] = CellState.TARGET;
                        targetCount++;
                        break;
                    case 'B':
                        state[i][j] = CellState.BOX;
                        boxCount++;
                        break;
                    case 'O':
                        state[i][j] = CellState.BOX_ON_TARGET;
                        boxesOnTargets++;
                        boxCount++;
                        targetCount++;
                        break;
                    case '.':
                        state[i][j] = CellState.EMPTY;
                        break;
                    default:
                        throw new InvalidGameStateException("Invalid character '" + c + "' at position (" + j + "," + i + ")");
                }
            }
        }
        
        if (!playerFound) {
            throw new InvalidGameStateException("No player found - board must contain @");
        }
        
        if (boxCount == 0) {
            throw new InvalidGameStateException("No boxes found - board must contain at least one B");
        }
        
        if (boxCount != targetCount) {
            throw new InvalidGameStateException("Box count (" + boxCount + ") must equal target count (" + targetCount + ")");
        }
        
        boxesCount = boxCount;
    }

    public void loadFromFile(String filename) throws FileNotFoundException, InvalidGameStateException {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(filename);
        Scanner fileScanner = new Scanner(file);
        
        while (fileScanner.hasNextLine()) {
            lines.add(fileScanner.nextLine());
        }
        fileScanner.close();
        
        String[] board = lines.toArray(new String[0]);
        stringToStateMapper(board);
    }

    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
    }

    private void placeTargets() {
        int placed = 0;
        while (placed < boxesCount) {
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;

            if (x == playerX && y == playerY) {
                continue;
            }
            if (state[y][x] == CellState.TARGET) {
                continue;
            }

            state[y][x] = CellState.TARGET;
            placed++;
        }
    }

    private void placeBoxes() {
        int placed = 0;
        while (placed < boxesCount) {
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;

            if (!canPlaceBox(x, y)) {
                continue;
            }

            if (state[y][x] == CellState.TARGET) {
                state[y][x] = CellState.BOX_ON_TARGET;
                boxesOnTargets++;
            } else {
                state[y][x] = CellState.BOX;
            }
            placed++;
        }
    }

    private boolean canPlaceBox(int x, int y) {
        if (x == playerX && y == playerY) {
            return false;
        }
        if (state[y][x] == CellState.TARGET) {
            return false;
        }
        if (state[y][x] == CellState.BOX || state[y][x] == CellState.BOX_ON_TARGET) {
            return false;
        }

        if ((x == 1 && y == 1) || (x == width - 2 && y == 1) ||
            (x == 1 && y == height - 2) || (x == width - 2 && y == height - 2)) {
            if (state[y][x] != CellState.TARGET) {
                return false;
            }
        }

        if (x == 1 || x == width - 2) {
            int boxesOnWall = 0;
            int targetsOnWall = 0;
            for (int row = 1; row < height - 1; row++) {
                if (state[row][x] == CellState.BOX || state[row][x] == CellState.BOX_ON_TARGET) {
                    boxesOnWall++;
                }
                if (state[row][x] == CellState.TARGET || state[row][x] == CellState.BOX_ON_TARGET) {
                    targetsOnWall++;
                }
            }
            if (boxesOnWall + 1 > targetsOnWall) {
                return false;
            }
        }

        if (y == 1 || y == height - 2) {
            int boxesOnWall = 0;
            int targetsOnWall = 0;
            for (int col = 1; col < width - 1; col++) {
                if (state[y][col] == CellState.BOX || state[y][col] == CellState.BOX_ON_TARGET) {
                    boxesOnWall++;
                }
                if (state[y][col] == CellState.TARGET || state[y][col] == CellState.BOX_ON_TARGET) {
                    targetsOnWall++;
                }
            }
            if (boxesOnWall + 1 > targetsOnWall) {
                return false;
            }
        }

        if (creates2x2SolidBlock(x, y)) {
            return false;
        }

        return true;
    }

    private boolean creates2x2SolidBlock(int x, int y) {
        for (int startX = x - 1; startX <= x; startX++) {
            for (int startY = y - 1; startY <= y; startY++) {
                boolean allSolid = true;
                for (int dx = 0; dx < 2; dx++) {
                    for (int dy = 0; dy < 2; dy++) {
                        int checkX = startX + dx;
                        int checkY = startY + dy;
                        
                        if (!isSolid(checkX, checkY, x, y)) {
                            allSolid = false;
                            break;
                        }
                    }
                    if (!allSolid) break;
                }
                if (allSolid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSolid(int checkX, int checkY, int newBoxX, int newBoxY) {
        if (checkX == newBoxX && checkY == newBoxY) {
            return true;
        }
        return state[checkY][checkX] == CellState.WALL ||
               state[checkY][checkX] == CellState.BOX ||
               state[checkY][checkX] == CellState.BOX_ON_TARGET;
    }

    public boolean move(String direction) throws InvalidMoveException {
        int dx = 0, dy = 0;

        switch (direction) {
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
                throw new InvalidMoveException("Unknown direction: " + direction);
        }

        int newX = playerX + dx;
        int newY = playerY + dy;

        if (state[newY][newX] == CellState.WALL) {
            throw new InvalidMoveException("Cannot move into wall");
        }

        prevPlayerX = playerX;
        prevPlayerY = playerY;
        if (prevState == null) {
            prevState = new CellState[height][width];
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                prevState[i][j] = state[i][j];
            }
        }

        if (state[newY][newX] == CellState.BOX || state[newY][newX] == CellState.BOX_ON_TARGET) {
            int boxNewX = newX + dx;
            int boxNewY = newY + dy;

            if (state[boxNewY][boxNewX] == CellState.WALL ||
                state[boxNewY][boxNewX] == CellState.BOX || 
                state[boxNewY][boxNewX] == CellState.BOX_ON_TARGET) {
                throw new InvalidMoveException("Cannot push box - blocked");
            }

            if (state[newY][newX] == CellState.BOX_ON_TARGET) {
                boxesOnTargets--;
                state[newY][newX] = CellState.TARGET;
            } else {
                state[newY][newX] = CellState.EMPTY;
            }

            if (state[boxNewY][boxNewX] == CellState.TARGET) {
                state[boxNewY][boxNewX] = CellState.BOX_ON_TARGET;
                boxesOnTargets++;
            } else {
                state[boxNewY][boxNewX] = CellState.BOX;
            }
        }

        playerX = newX;
        playerY = newY;
        moves++;
        canUndo = true;
        return true;
    }

    public boolean undo() throws InvalidGameStateException {
        if (!canUndo) {
            throw new InvalidGameStateException("Nothing to undo");
        }

        playerX = prevPlayerX;
        playerY = prevPlayerY;
        boxesOnTargets = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                state[i][j] = prevState[i][j];
                if (state[i][j] == CellState.BOX_ON_TARGET) {
                    boxesOnTargets++;
                }
            }
        }
        moves++;
        canUndo = false;
        return true;
    }

    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }

    public void printBoard() {
        System.out.println();
        System.out.println("Moves: " + moves + " | Boxes on targets: " + boxesOnTargets + "/" + boxesCount);
        System.out.println();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == playerY && j == playerX) {
                    System.out.print(PLAYER);
                } else {
                    switch (state[i][j]) {
                        case WALL:
                            System.out.print(WALL);
                            break;
                        case BOX_ON_TARGET:
                            System.out.print(BOX_ON_TARGET);
                            break;
                        case BOX:
                            System.out.print(BOX);
                            break;
                        case TARGET:
                            System.out.print(TARGET);
                            break;
                        case EMPTY:
                            System.out.print(EMPTY);
                            break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public int getMoves() {
        return moves;
    }

    private static String readCommand(Scanner scanner) {
        System.out.print("Enter command (w/up, a/left, s/down, d/right, u/undo, q/quit): ");
        return scanner.nextLine().trim().toLowerCase();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SOKOBAN GAME ===");
        System.out.println("1. Play Preset Levels");
        System.out.println("2. Load Level from File");
        System.out.println("3. Custom Game");
        System.out.print("Choose option (1, 2, or 3): ");
        
        int choice;
        while (true) {
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
                if (choice >= 1 && choice <= 3) break;
            } else {
                scanner.nextLine();
            }
            System.out.print("Invalid choice! Enter 1, 2, or 3: ");
        }

        if (choice == 1) {
            playPresetLevels(scanner);
        } else if (choice == 2) {
            playFileLevel(scanner);
        } else {
            playCustomGame(scanner);
        }
        
        scanner.close();
    }

    private static void playPresetLevels(Scanner scanner) {
        System.out.println("\n=== PRESET LEVELS ===");
        for (int i = 0; i < PRESET_LEVELS.length; i++) {
            Level level = PRESET_LEVELS[i];
            int height = level.board.length;
            int width = level.board[0].length();
            int boxes = 0;
            for (String row : level.board) {
                for (char c : row.toCharArray()) {
                    if (c == '*' || c == 'O') boxes++;
                }
            }
            System.out.println((i + 1) + ". " + level.name + " (" + level.difficulty + ") - " + 
                             width + "x" + height + ", " + boxes + " boxes");
        }
        
        int levelChoice;
        while (true) {
            System.out.print("\nSelect level (1-" + PRESET_LEVELS.length + ") or 0 to go back: ");
            if (scanner.hasNextInt()) {
                levelChoice = scanner.nextInt();
                scanner.nextLine();
                if (levelChoice >= 0 && levelChoice <= PRESET_LEVELS.length) break;
            } else {
                scanner.nextLine();
            }
            System.out.println("Invalid selection!");
        }

        if (levelChoice == 0) {
            System.out.println("Returning to main menu...");
            return;
        }

        Level selectedLevel = PRESET_LEVELS[levelChoice - 1];
        int height = selectedLevel.board.length;
        int width = selectedLevel.board[0].length();
        int boxes = 0;
        for (String row : selectedLevel.board) {
            for (char c : row.toCharArray()) {
                if (c == '*' || c == 'O') boxes++;
            }
        }
        
        System.out.println("\n=== " + selectedLevel.name.toUpperCase() + " ===");
        System.out.println("Difficulty: " + selectedLevel.difficulty);
        System.out.println("Board: " + width + "x" + height);
        System.out.println("Boxes: " + boxes + "\n");

        SokobanGame game = new SokobanGame();
        try {
            game.stringToStateMapper(selectedLevel.board);
            playGame(scanner, game);
        } catch (InvalidGameStateException e) {
            System.out.println("Error loading level: " + e.getMessage());
        }
    }

    private static void playFileLevel(Scanner scanner) {
        System.out.println("\n=== LOAD LEVEL FROM FILE ===");
        System.out.print("Enter filename (e.g., level.txt): ");
        String filename = scanner.nextLine().trim();

        SokobanGame game = new SokobanGame();
        try {
            game.loadFromFile(filename);
            System.out.println("Level loaded successfully!\n");
            playGame(scanner, game);
        } catch (FileNotFoundException e) {
            System.out.println("Error: File '" + filename + "' not found!");
            System.out.println("Make sure the file exists in the current directory.");
        } catch (InvalidGameStateException e) {
            System.out.println("Error: Invalid level format!");
            System.out.println(e.getMessage());
        }
    }

    private static void playCustomGame(Scanner scanner) {
        System.out.println("\n=== CUSTOM GAME ===");
        
        int width, height, boxesCount;

        while (true) {
            System.out.print("Enter width (W >= 5): ");
            width = scanner.nextInt();
            if (width >= 5) break;
            System.out.println("Width must be at least 5!");
        }

        while (true) {
            System.out.print("Enter height (H >= 5): ");
            height = scanner.nextInt();
            if (height >= 5) break;
            System.out.println("Height must be at least 5!");
        }

        int maxBoxes = ((width - 2) * (height - 2) - 2) / 2;
        while (true) {
            System.out.print("Enter number of boxes (K <= " + maxBoxes + "): ");
            boxesCount = scanner.nextInt();
            if (boxesCount >= 1 && boxesCount <= maxBoxes) break;
            System.out.println("Invalid number of boxes! Must be between 1 and " + maxBoxes);
        }

        scanner.nextLine();

        SokobanGame game = new SokobanGame();
        game.initializeGame(width, height, boxesCount);

        playGame(scanner, game);
    }

    private static void playGame(Scanner scanner, SokobanGame game) {
        while (!game.checkWin()) {
            game.printBoard();
            String command = readCommand(scanner);
            if (command.equals("quit") || command.equals("q")) {
                System.out.println("Game quit!");
                break;
            }
            if (command.equals("undo") || command.equals("u")) {
                try {
                    game.undo();
                } catch (InvalidGameStateException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                try {
                    game.move(command);
                } catch (InvalidMoveException e) {
                    System.out.println("Invalid move: " + e.getMessage());
                }
            }
        }

        if (game.checkWin()) {
            game.printBoard();
            System.out.println("You win in " + game.getMoves() + " moves!");
        }
    }
}

class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }
}

class InvalidGameStateException extends Exception {
    public InvalidGameStateException(String message) {
        super(message);
    }
}
