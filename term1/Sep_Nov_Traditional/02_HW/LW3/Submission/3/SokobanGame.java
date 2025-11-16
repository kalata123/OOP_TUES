import java.util.*;
import java.io.*;

public class SokobanGame {

    private int width, height, boxesCount;
    private int playerX, playerY;
    public int moves;
    private int boxesOnTargets;

    private char[][] board;
    private boolean[][] isTarget;
    private boolean[][] hasBox;

    private final static char WALL = '#';
    private final static char EMPTY = '.';
    private final static char PLAYER = '@';
    private final static char BOX = 'B';
    private final static char TARGET = '*';
    private final static char BOX_ON_TARGET = 'O';

    private Random rand = new Random();
    private Stack<GameState> history = new Stack<>();

    private char[][] initialBoard;
    private boolean[][] initialTarget;
    private boolean[][] initialBoxes;
    private int initialPlayerX, initialPlayerY;

    public void initializeGame(int w, int h, int k) {
        width = w;
        height = h;
        boxesCount = k;
        moves = 0;
        boxesOnTargets = 0;

        board = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    board[y][x] = WALL;
                } else {
                    board[y][x] = EMPTY;
                }
            }
        }

        placePlayer();
        placeTargets();
        placeBoxes();
        updateDisplay();
        saveInitialState();
    }

    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
    }

    private void placeTargets() {
        int placed = 0;
        int attempts = 0;
        int maxAttempts = 1000;

        while (placed < boxesCount && attempts < maxAttempts) {
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;

            if (!isTarget[y][x] && (x != playerX || y != playerY)) {
                isTarget[y][x] = true;
                placed++;
            }
            attempts++;
        }

        if (placed < boxesCount) {
            throw new InvalidLevelException("Failed to place all boxes. Try a larger board or a smaller number of boxes.");
        }
    }

    private void placeBoxes() {
        int placed = 0;
        int attempts = 0;
        int maxAttempts = 2000;

        while (placed < boxesCount && attempts < maxAttempts) {
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;

            if (isValidBoxPlacement(x, y)) {
                hasBox[y][x] = true;
                placed++;
            }
            attempts++;
        }

        if (placed < boxesCount) {
            throw new InvalidLevelException("Failed to place all boxes. Try a larger board or a smaller number of boxes.");
        }
    }

    private boolean isValidBoxPlacement(int x, int y) {
        if (hasBox[y][x]) return false;
        if (isTarget[y][x]) return false;
        if (x == playerX && y == playerY) return false;

        if ((x == 1 && y == 1) || (x == 1 && y == height - 2) ||
                (x == width - 2 && y == 1) || (x == width - 2 && y == height - 2)) {
            if (!isTarget[y][x]) return false;
        }

        if (!checkWallCapacity(x, y)) return false;

        return true;
    }

    private boolean checkWallCapacity(int x, int y) {
        int[] wallBoxCount = new int[4];
        int[] wallTargetCount = new int[4];

        for (int i = 1; i < width - 1; i++) {
            if (hasBox[1][i] || (y == 1 && x == i)) wallBoxCount[0]++;
            if (isTarget[1][i]) wallTargetCount[0]++;
            if (hasBox[height - 2][i] || (y == height - 2 && x == i)) wallBoxCount[1]++;
            if (isTarget[height - 2][i]) wallTargetCount[1]++;
        }
        for (int j = 1; j < height - 1; j++) {
            if (hasBox[j][1] || (y == j && x == 1)) wallBoxCount[2]++;
            if (isTarget[j][1]) wallTargetCount[2]++;
            if (hasBox[j][width - 2] || (y == j && x == width - 2)) wallBoxCount[3]++;
            if (isTarget[j][width - 2]) wallTargetCount[3]++;
        }

        for (int i = 0; i < 4; i++) {
            if (wallBoxCount[i] > wallTargetCount[i]) return false;
        }
        return true;
    }

    public boolean move(String direction) {
        int dx = 0, dy = 0;
        switch (direction.toLowerCase()) {
            case "w": case "up": dy = -1; break;
            case "s": case "down": dy = 1; break;
            case "a": case "left": dx = -1; break;
            case "d": case "right": dx = 1; break;
            default:
                System.out.println("Invalid command");
                return false;
        }

        int nx = playerX + dx;
        int ny = playerY + dy;

        if (board[ny][nx] == WALL) {
            System.out.println("Invalid move");
            return false;
        }

        saveStateForUndo();

        if (hasBox[ny][nx]) {
            int bx = nx + dx;
            int by = ny + dy;

            if (board[by][bx] == WALL || hasBox[by][bx]) {
                history.pop();
                System.out.println("Invalid move");
                return false;
            }

            hasBox[ny][nx] = false;
            hasBox[by][bx] = true;

            if (isTarget[ny][nx]) boxesOnTargets--;
            if (isTarget[by][bx]) boxesOnTargets++;
        }

        playerX = nx;
        playerY = ny;
        moves++;
        updateDisplay();
        return true;
    }

    private void saveInitialState() {
        initialBoard = copyBoard(board);
        initialTarget = copyBool(isTarget);
        initialBoxes = copyBool(hasBox);
        initialPlayerX = playerX;
        initialPlayerY = playerY;
    }

    private void saveStateForUndo() {
        history.push(new GameState(copyBool(hasBox), playerX, playerY, boxesOnTargets, moves));
    }

    public void undo() {
        if (history.isEmpty()) {
            System.out.println("No moves to undo.");
            return;
        }
        GameState prev = history.pop();
        hasBox = prev.boxes;
        playerX = prev.px;
        playerY = prev.py;
        boxesOnTargets = prev.boxesOnTargets;
        moves = prev.moves;
        updateDisplay();
    }

    public void restart() {
        board = copyBoard(initialBoard);
        isTarget = copyBool(initialTarget);
        hasBox = copyBool(initialBoxes);
        playerX = initialPlayerX;
        playerY = initialPlayerY;
        boxesOnTargets = 0;
        moves = 0;
        history.clear();
        updateDisplay();
    }

    private char[][] copyBoard(char[][] src) {
        char[][] copy = new char[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) copy[i] = Arrays.copyOf(src[i], src[i].length);
        return copy;
    }

    private boolean[][] copyBool(boolean[][] src) {
        boolean[][] copy = new boolean[src.length][src[0].length];
        for (int i = 0; i < src.length; i++) copy[i] = Arrays.copyOf(src[i], src[i].length);
        return copy;
    }

    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }

    public void printBoard() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(board[y][x]);
            }
            System.out.println();
        }
        System.out.println("Moves: " + moves + " | Boxes on target: " + boxesOnTargets + "/" + boxesCount);
    }

    private void updateDisplay() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    board[y][x] = WALL;
                } else if (hasBox[y][x] && isTarget[y][x]) {
                    board[y][x] = BOX_ON_TARGET;
                } else if (hasBox[y][x]) {
                    board[y][x] = BOX;
                } else if (isTarget[y][x]) {
                    board[y][x] = TARGET;
                } else {
                    board[y][x] = EMPTY;
                }
            }
        }
        board[playerY][playerX] = PLAYER;
    }

    public void loadLevelFromFile(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }

        height = lines.size();
        width = lines.get(0).length();
        boxesCount = 0;
        boxesOnTargets = 0;
        moves = 0;

        board = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = lines.get(y).charAt(x);
                board[y][x] = c;
                switch (c) {
                    case '#': break;
                    case '.': break;
                    case '@': playerX = x; playerY = y; break;
                    case '*': isTarget[y][x] = true; break;
                    case 'B': hasBox[y][x] = true; boxesCount++; break;
                    case 'O': hasBox[y][x] = true; isTarget[y][x] = true; boxesCount++; boxesOnTargets++; break;
                }
            }
        }

        saveInitialState();
    }

    private static class GameState {
        boolean[][] boxes;
        int px, py;
        int boxesOnTargets;
        int moves;
        GameState(boolean[][] b, int x, int y, int bot, int m) {
            boxes = new boolean[b.length][b[0].length];
            for (int i = 0; i < b.length; i++) boxes[i] = Arrays.copyOf(b[i], b[i].length);
            px = x;
            py = y;
            boxesOnTargets = bot;
            moves = m;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SokobanGame game = new SokobanGame();

        System.out.println("Welcome to Sokoban!");

        while (true) {
            try {
                System.out.println("1. Load random level");
                System.out.println("2. Load level from file");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 2) {
                    System.out.print("Enter filename (e.g. level1.txt): ");
                    String file = sc.nextLine();
                    game.loadLevelFromFile(file);
                } else {
                    System.out.print("Enter width (>=5): ");
                    int W = sc.nextInt();
                    while (W < 5) { System.out.print("Try again: "); W = sc.nextInt(); }

                    System.out.print("Enter height (>=5): ");
                    int H = sc.nextInt();
                    while (H < 5) { System.out.print("Try again: "); H = sc.nextInt(); }

                    int maxBoxes = ((W - 2) * (H - 2) - 2) / 2;
                    System.out.print("Enter number of boxes (<= " + maxBoxes + "): ");
                    int K = sc.nextInt();
                    while (K > maxBoxes) { System.out.print("Try again: "); K = sc.nextInt(); }
                    sc.nextLine();

                    game = new SokobanGame();
                    game.initializeGame(W, H, K);
                }
                break;
            } catch (InvalidLevelException e) {
                System.out.println(e.getMessage());
                System.out.println("Let's try again...");
            } catch (IOException e) {
                System.out.println("Error loading file: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                return;
            }
        }

        while (!game.checkWin()) {
            game.printBoard();
            System.out.print("Command (w/a/s/d, u=undo, r=restart, q=quit): ");
            String cmd = sc.nextLine();
            if (cmd.equalsIgnoreCase("q")) break;
            if (cmd.equalsIgnoreCase("u")) { game.undo(); continue; }
            if (cmd.equalsIgnoreCase("r")) { game.restart(); continue; }
            game.move(cmd);
        }

        if (game.checkWin()) {
            System.out.println("You win in " + game.moves + " moves!");
        }

        sc.close();
    }
}

class InvalidLevelException extends RuntimeException {
    public InvalidLevelException(String message) {
        super(message);
    }
}
