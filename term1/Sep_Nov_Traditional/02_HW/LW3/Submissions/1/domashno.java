import java.util.Scanner;
import java.util.Random;

public class domashno {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SokobanGame game = new SokobanGame();
        game.initializeTestBoard();
        while (!game.checkWin()) {
            game.printBoard();
            System.out.print("Enter command (up/down/left/right or w/a/s/d, quit/q): ");
            String command = scanner.next().toLowerCase();
            if (command.equals("quit") || command.equals("q")) break;
            game.move(command);
        }
        System.out.println("Game over.");
    }
}

class SokobanGame {
    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets;
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    public void initializeGame(int w, int h, int k) {
        this.width = w;
        this.height = h;
        this.boxesCount = k;
        this.moves = 0;
        this.boxesOnTargets = 0;
        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];
        placePlayer();
        placeTargets();
        placeBoxes();
        updateDisplay();
    }
    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
    }
    private void placeTargets() {
        Random rand = new Random();
        int placed = 0;
        while (placed < boxesCount) {
            int tx = rand.nextInt(width - 2) + 1;
            int ty = rand.nextInt(height - 2) + 1;
            if (!isTarget[ty][tx] && !(tx == playerX && ty == playerY)) {
                isTarget[ty][tx] = true;
                placed++;
            }
        }
    }
    private void placeBoxes() {
        Random rand = new Random();
        int placed = 0;
        while (placed < boxesCount) {
            int bx = rand.nextInt(width - 2) + 1;
            int by = rand.nextInt(height - 2) + 1;
            boolean isCorner = (bx == 1 && by == 1) || (bx == width-2 && by == 1) || (bx == 1 && by == height-2) || (bx == width-2 && by == height-2);
            if (!hasBox[by][bx] && !(bx == playerX && by == playerY) && !isTarget[by][bx]) {
                if (!isCorner || isTarget[by][bx]) {
                    hasBox[by][bx] = true;
                    placed++;
                }
            }
        }
    }
    private void updateDisplay() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    displayBoard[i][j] = WALL;
                } else if (i == playerY && j == playerX) {
                    displayBoard[i][j] = PLAYER;
                } else if (hasBox[i][j] && isTarget[i][j]) {
                    displayBoard[i][j] = BOX_ON_TARGET;
                } else if (hasBox[i][j]) {
                    displayBoard[i][j] = BOX;
                } else if (isTarget[i][j]) {
                    displayBoard[i][j] = TARGET;
                } else {
                    displayBoard[i][j] = EMPTY;
                }
            }
        }
    }
    public void printBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(displayBoard[i][j]);
            }
            System.out.println();
        }
    }
    public boolean move(String direction) {
        int dx = 0, dy = 0;
        switch (direction) {
            case "w": case "up": dy = -1; break;
            case "s": case "down": dy = 1; break;
            case "a": case "left": dx = -1; break;
            case "d": case "right": dx = 1; break;
            default:
                System.out.println("Invalid command.");
                return false;
        }
        int nx = playerX + dx;
        int ny = playerY + dy;
        // Check bounds and wall
        if (displayBoard[ny][nx] == WALL) {
            System.out.println("Invalid move: wall.");
            return false;
        }
        // If next cell is empty or target
        if (!hasBox[ny][nx]) {
            playerX = nx;
            playerY = ny;
            moves++;
            updateDisplay();
            return true;
        }
        // If next cell has a box
        int bx = nx + dx;
        int by = ny + dy;
        // Check if box can be pushed
        if (hasBox[ny][nx] && bx >= 0 && bx < width && by >= 0 && by < height && displayBoard[by][bx] != WALL && !hasBox[by][bx]) {
            // Update boxesOnTargets counter
            if (isTarget[ny][nx]) boxesOnTargets--;
            if (isTarget[by][bx]) boxesOnTargets++;
            hasBox[ny][nx] = false;
            hasBox[by][bx] = true;
            playerX = nx;
            playerY = ny;
            moves++;
            updateDisplay();
            return true;
        }
        System.out.println("Invalid move: cannot push box.");
        return false;
    }
    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }
    public void initializeTestBoard() {
        // Example board:
        // #####
        // #*B.#
        // #.@.#
        // #...#
        // #####
        this.width = 5;
        this.height = 5;
        this.boxesCount = 1;
        this.moves = 0;
        this.boxesOnTargets = 0;
        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];
        // Set up walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1) {
                    displayBoard[i][j] = WALL;
                } else {
                    displayBoard[i][j] = EMPTY;
                }
            }
        }
        // Place target
        isTarget[1][1] = true;
        // Place box
        hasBox[1][2] = true;
        // Place player
        playerX = 2;
        playerY = 2;
        // Update display
        updateDisplay();
    }
}