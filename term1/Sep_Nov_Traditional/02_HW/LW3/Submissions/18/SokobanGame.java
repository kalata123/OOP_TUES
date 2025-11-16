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

    private Deque<char[][]> undoStack = new ArrayDeque<>();
    private Random rand = new Random();

    public void initializeGame(int w, int h, int k) {
        width = w;
        height = h;
        boxesCount = k;
        moves = 0;
        boxesOnTargets = 0;

        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    displayBoard[y][x] = WALL;
                } else {
                    displayBoard[y][x] = EMPTY;
                }
            }
        }

        placePlayer();

        boolean ok = false;
        int attempts = 0;

        while (!ok && attempts < 300) {
            attempts++;
            clearInterior();
            placeTargets();
            ok = placeBoxes();
        }

        if (!ok) {
            throw new IllegalStateException("Failed to generate valid board.");
        }

        updateDisplay();
    }

    private void clearInterior() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                isTarget[y][x] = false;
                hasBox[y][x] = false;

                if (displayBoard[y][x] != PLAYER) {
                    displayBoard[y][x] = EMPTY;
                }
            }
        }

        boxesOnTargets = 0;
    }

    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
        displayBoard[playerY][playerX] = PLAYER;
    }

    private void placeTargets() {
        int placed = 0;

        while (placed < boxesCount) {
            int x = 1 + rand.nextInt(width - 2);
            int y = 1 + rand.nextInt(height - 2);

            if (x == playerX && y == playerY) {
                continue;
            }

            if (!isTarget[y][x]) {
                isTarget[y][x] = true;
                placed++;
            }
        }
    }

    private boolean placeBoxes() {
        int placed = 0;
        int tries = 0;

        while (placed < boxesCount && tries < 20000) {
            tries++;
            int x = 1 + rand.nextInt(width - 2);
            int y = 1 + rand.nextInt(height - 2);

            if (x == playerX && y == playerY) {
                continue;
            }

            if (isTarget[y][x]) {
                continue;
            }

            if (hasBox[y][x]) {
                continue;
            }

            boolean corner = (x == 1 && y == 1)
                    || (x == width - 2 && y == 1)
                    || (x == 1 && y == height - 2)
                    || (x == width - 2 && y == height - 2);

            if (corner) {
                continue;
            }

            if (y == 1 && countBoxesTop() + 1 > countTargetsTop()) {
                continue;
            }

            if (y == height - 2 && countBoxesBottom() + 1 > countTargetsBottom()) {
                continue;
            }

            if (x == 1 && countBoxesLeft() + 1 > countTargetsLeft()) {
                continue;
            }

            if (x == width - 2 && countBoxesRight() + 1 > countTargetsRight()) {
                continue;
            }

            hasBox[y][x] = true;

            if (creates2x2Block() || createsDeadlock()) {
                hasBox[y][x] = false;
                continue;
            }

            placed++;
        }

        return placed == boxesCount;
    }

    private int countBoxesTop() {
        int c = 0;

        for (int x = 1; x < width - 1; x++) {
            if (hasBox[1][x]) {
                c++;
            }
        }

        return c;
    }

    private int countBoxesBottom() {
        int c = 0;

        for (int x = 1; x < width - 1; x++) {
            if (hasBox[height - 2][x]) {
                c++;
            }
        }

        return c;
    }

    private int countBoxesLeft() {
        int c = 0;

        for (int y = 1; y < height - 1; y++) {
            if (hasBox[y][1]) {
                c++;
            }
        }

        return c;
    }

    private int countBoxesRight() {
        int c = 0;

        for (int y = 1; y < height - 1; y++) {
            if (hasBox[y][width - 2]) {
                c++;
            }
        }

        return c;
    }

    private int countTargetsTop() {
        int c = 0;

        for (int x = 1; x < width - 1; x++) {
            if (isTarget[1][x]) {
                c++;
            }
        }

        return c;
    }

    private int countTargetsBottom() {
        int c = 0;

        for (int x = 1; x < width - 1; x++) {
            if (isTarget[height - 2][x]) {
                c++;
            }
        }

        return c;
    }

    private int countTargetsLeft() {
        int c = 0;

        for (int y = 1; y < height - 1; y++) {
            if (isTarget[y][1]) {
                c++;
            }
        }

        return c;
    }

    private int countTargetsRight() {
        int c = 0;

        for (int y = 1; y < height - 1; y++) {
            if (isTarget[y][width - 2]) {
                c++;
            }
        }

        return c;
    }

    private boolean creates2x2Block() {
        for (int y = 1; y < height - 2; y++) {
            for (int x = 1; x < width - 2; x++) {
                if (isWallOrBox(y, x)
                        && isWallOrBox(y + 1, x)
                        && isWallOrBox(y, x + 1)
                        && isWallOrBox(y + 1, x + 1)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isWallOrBox(int y, int x) {
        return displayBoard[y][x] == WALL || hasBox[y][x];
    }

    private boolean createsDeadlock() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (hasBox[y][x] && !isTarget[y][x]) {
                    boolean upWall = displayBoard[y - 1][x] == WALL || hasBox[y - 1][x];
                    boolean downWall = displayBoard[y + 1][x] == WALL || hasBox[y + 1][x];
                    boolean leftWall = displayBoard[y][x - 1] == WALL || hasBox[y][x - 1];
                    boolean rightWall = displayBoard[y][x + 1] == WALL || hasBox[y][x + 1];

                    if ((upWall && leftWall)
                            || (upWall && rightWall)
                            || (downWall && leftWall)
                            || (downWall && rightWall)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean move(String dir) {
        dir = dir.toLowerCase();
        int dx = 0;
        int dy = 0;

        switch (dir) {
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
            case "undo":
                undo();
                return false;
            default:
                return false;
        }

        saveState();
        int nx = playerX + dx;
        int ny = playerY + dy;
        int nnx = playerX + 2 * dx;
        int nny = playerY + 2 * dy;

        if (displayBoard[ny][nx] == WALL) {
            return false;
        }

        if (hasBox[ny][nx]) {
            if (displayBoard[nny][nnx] == WALL || hasBox[nny][nnx]) {
                return false;
            }

            hasBox[ny][nx] = false;
            hasBox[nny][nnx] = true;

            if (isTarget[ny][nx]) {
                boxesOnTargets--;
            }

            if (isTarget[nny][nnx]) {
                boxesOnTargets++;
            }
        }

        playerX = nx;
        playerY = ny;
        moves++;

        updateDisplay();

        if (createsDeadlock() || creates2x2Block()) {
            System.out.println("\n⚠️ Deadlock detected. Restarting level...\n");
            initializeGame(width, height, boxesCount);
        }

        return true;
    }

    private void saveState() {
        char[][] copy = new char[height][width];

        for (int i = 0; i < height; i++) {
            copy[i] = displayBoard[i].clone();
        }

        undoStack.push(copy);
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            displayBoard = undoStack.pop();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (displayBoard[y][x] == PLAYER) {
                        playerX = x;
                        playerY = y;
                    }
                }
            }

            moves = Math.max(0, moves - 1);
        }
    }

    private void updateDisplay() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (displayBoard[y][x] != WALL) {
                    if (isTarget[y][x]) {
                        displayBoard[y][x] = TARGET;
                    } else {
                        displayBoard[y][x] = EMPTY;
                    }
                }

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
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(displayBoard[y][x]);
            }
            System.out.println();
        }

        System.out.println("Moves: " + moves);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SokobanGame game = new SokobanGame();

        int w;
        int h;
        int k;

        while (true) {
            System.out.print("Enter width (>=5): ");
            w = sc.nextInt();

            if (w >= 5) {
                break;
            }
        }

        while (true) {
            System.out.print("Enter height (>=5): ");
            h = sc.nextInt();

            if (h >= 5) {
                break;
            }
        }

        while (true) {
            System.out.print("Enter number of boxes: ");
            k = sc.nextInt();

            if (k <= ((w - 2) * (h - 2) - 2) / 2) {
                break;
            }
        }

        game.initializeGame(w, h, k);

        while (!game.checkWin()) {
            game.printBoard();
            System.out.print("Command (w/a/s/d to move, undo, quit): ");
            String cmd = sc.next();

            if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("q")) {
                break;
            }

            game.move(cmd);
        }

        if (game.checkWin()) {
            System.out.println("You win in " + game.moves + " moves!");
        }
    }
}
