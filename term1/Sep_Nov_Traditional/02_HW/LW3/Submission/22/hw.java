import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

class SokobanGame {
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
    private final Random rand = new Random();

    private final Stack<Integer> playerXStack = new Stack<>();
    private final Stack<Integer> playerYStack = new Stack<>();
    private final Stack<boolean[][]> hasBoxStack = new Stack<>();
    private final Stack<Integer> boxesOnTargetsStack = new Stack<>();

    public void initializeGame(int w, int h, int k) {
        width = w;
        height = h;
        boxesCount = k;
        moves = 0;
        boxesOnTargets = 0;
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
        for (int i = 0; i < boxesCount; i++) {
            while (true) {
                int tx = rand.nextInt(width - 2) + 1;
                int ty = rand.nextInt(height - 2) + 1;
                if (ty == playerY && tx == playerX) continue;
                if (isTarget[ty][tx]) continue;
                isTarget[ty][tx] = true;
                break;
            }
        }
    }

    private void placeBoxes() {
        for (int i = 0; i < boxesCount; i++) {
            while (true) {
                int bx = rand.nextInt(width - 2) + 1;
                int by = rand.nextInt(height - 2) + 1;
                if (by == playerY && bx == playerX) continue;
                if (isTarget[by][bx]) continue;
                if (hasBox[by][bx]) continue;
                boolean isCorner = (by == 1 && bx == 1) || (by == 1 && bx == width - 2) ||
                                   (by == height - 2 && bx == 1) || (by == height - 2 && bx == width - 2);
                if (isCorner && !isTarget[by][bx]) continue;
                hasBox[by][bx] = true;
                if (checkWallCapacity()) {
                    break;
                } else {
                    hasBox[by][bx] = false;
                }
            }
        }
    }

    private boolean checkWallCapacity() {
        int[] boxC = new int[4];
        int[] tarC = new int[4];
        for (int c = 1; c < width - 1; c++) {
            if (hasBox[1][c]) boxC[0]++;
            if (isTarget[1][c]) tarC[0]++;
        }
        for (int c = 1; c < width - 1; c++) {
            if (hasBox[height - 2][c]) boxC[1]++;
            if (isTarget[height - 2][c]) tarC[1]++;
        }
        for (int r = 1; r < height - 1; r++) {
            if (hasBox[r][1]) boxC[2]++;
            if (isTarget[r][1]) tarC[2]++;
        }
        for (int r = 1; r < height - 1; r++) {
            if (hasBox[r][width - 2]) boxC[3]++;
            if (isTarget[r][width - 2]) tarC[3]++;
        }
        for (int j = 0; j < 4; j++) {
            if (boxC[j] > tarC[j]) return false;
        }
        return true;
    }

    public boolean move(String direction) {
        if (direction.equals("undo")) {
            if (playerXStack.isEmpty()) {
                System.out.println("No moves to undo");
                return false;
            }
            playerX = playerXStack.pop();
            playerY = playerYStack.pop();
            hasBox = hasBoxStack.pop();
            boxesOnTargets = boxesOnTargetsStack.pop();
            moves--;
            updateDisplay();
            return true;
        }
        int dx = 0, dy = 0;
        switch (direction) {
            case "up":
                dy = -1;
                break;
            case "down":
                dy = 1;
                break;
            case "left":
                dx = -1;
                break;
            case "right":
                dx = 1;
                break;
            default:
                return false;
        }

        int nx = playerX + dx, ny = playerY + dy;
        if (nx < 0 || nx >= width || ny < 0 || ny >= height || getDisplayChar(ny, nx) == WALL) {
            System.out.println("Invalid move");
            return false;
        }
        boolean pushed = false;
        int bx = -1, by = -1;
        if (hasBox[ny][nx]) {
            bx = nx + dx;
            by = ny + dy;
            if (bx < 0 || bx >= width || by < 0 || by >= height || getDisplayChar(by, bx) == WALL || hasBox[by][bx]) {
                System.out.println("Invalid move");
                return false;
            }
            pushed = true;
        }
        playerXStack.push(playerX);
        playerYStack.push(playerY);
        boolean[][] boxCopy = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            System.arraycopy(hasBox[i], 0, boxCopy[i], 0, width);
        }
        hasBoxStack.push(boxCopy);
        boxesOnTargetsStack.push(boxesOnTargets);
        if (pushed) {
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

    private char getDisplayChar(int r, int c) {
        if (r == 0 || r == height - 1 || c == 0 || c == width - 1) return WALL;
        return displayBoard[r][c];
    }

    private void updateDisplay() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (r == 0 || r == height - 1 || c == 0 || c == width - 1) {
                    displayBoard[r][c] = WALL;
                } else if (r == playerY && c == playerX) {
                    displayBoard[r][c] = PLAYER;
                } else if (hasBox[r][c]) {
                    displayBoard[r][c] = isTarget[r][c] ? BOX_ON_TARGET : BOX;
                } else {
                    displayBoard[r][c] = isTarget[r][c] ? TARGET : EMPTY;
                }
            }
        }
    }

    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }

    public void printBoard() {
        updateDisplay();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                System.out.print(displayBoard[r][c]);
            }
            System.out.println();
        }
    }

    public int getMoves() {
        return moves;
    }
}

class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int w, h, k;
            do {
                System.out.print("Enter width (>=5): ");
                w = sc.nextInt();
            } while (w < 5);
            do {
                System.out.print("Enter height (>=5): ");
                h = sc.nextInt();
            } while (h < 5);
            int maxK = ((w - 2) * (h - 2) - 2) / 2;
            do {
                System.out.print("Enter boxes (0 <= k <= " + maxK + "): ");
                k = sc.nextInt();
            } while (k > maxK || k < 0);

            SokobanGame game = new SokobanGame();
            game.initializeGame(w, h, k);

            while (!game.checkWin()) {
                game.printBoard();
                System.out.println("Moves: " + game.getMoves());
                System.out.print("Command (w/a/s/d/u/q): ");
                String cmd = sc.next().toLowerCase();
                if (cmd.equals("q") || cmd.equals("quit")) {
                    break;
                }
                String dir;
                switch (cmd) {
                    case "w":
                    case "up":
                        dir = "up";
                        break;
                    case "s":
                    case "down":
                        dir = "down";
                        break;
                    case "a":
                    case "left":
                        dir = "left";
                        break;
                    case "d":
                    case "right":
                        dir = "right";
                        break;
                    case "u":
                    case "undo":
                        dir = "undo";
                        break;
                    default:
                        System.out.println("Invalid command");
                        continue;
                }
                game.move(dir);
            }
            if (game.checkWin()) {
                System.out.println("You win in " + game.getMoves() + " moves!");
            }
        }
    }
}