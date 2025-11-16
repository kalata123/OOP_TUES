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
    private int moves = 0;
    private int boxesOnTargets = 0;
    private int[] targetsOnWall = new int[4];
    private int[] boxesOnWall = new int[4];
    private Random rand = new Random();
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;
    public void initializeGame(int w, int h, int k) {
        this.width = w;
        this.height = h;
        this.boxesCount = k;

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
        placeTargets();
        placeBoxes();
        updateDisplay();
    }

    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
    }

    private void placeTargets() {
        Arrays.fill(targetsOnWall, 0);

        int placed = 0;
        int attempts = 0;
        int maxAttempts = 20000;

        while (placed < boxesCount && attempts < maxAttempts) {
            attempts++;
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;

            if (x == playerX && y == playerY) continue;
            if (isTarget[y][x]) continue;

            isTarget[y][x] = true;
            placed++;

            if (y == 1) targetsOnWall[0]++;
            if (y == height - 2) targetsOnWall[1]++;
            if (x == 1) targetsOnWall[2]++;
            if (x == width - 2) targetsOnWall[3]++;
        }

        if (placed < boxesCount) {
            throw new RuntimeException("Unable to place targets");
        }
    }

    private void placeBoxes() {
        Arrays.fill(boxesOnWall, 0);

        int placed = 0;
        int attempts = 0;
        int maxAttempts = 200000;

        while (placed < boxesCount && attempts < maxAttempts) {
            attempts++;
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;

            if (x == playerX && y == playerY) continue;
            if (isTarget[y][x]) continue;
            if (hasBox[y][x]) continue;

            boolean isInteriorCorner = (x == 1 && y == 1) ||(x == width - 2 && y == 1) ||(x == 1 && y == height - 2) ||(x == width - 2 && y == height - 2);
            if (isInteriorCorner && !isTarget[y][x]) continue;

            boolean adjacentTop = (y == 1);
            boolean adjacentBottom = (y == height - 2);
            boolean adjacentLeft = (x == 1);
            boolean adjacentRight = (x == width - 2);

            boolean violatesR3 = false;
            if (adjacentTop && boxesOnWall[0] + 1 > targetsOnWall[0]) violatesR3 = true;
            if (adjacentBottom && boxesOnWall[1] + 1 > targetsOnWall[1]) violatesR3 = true;
            if (adjacentLeft && boxesOnWall[2] + 1 > targetsOnWall[2]) violatesR3 = true;
            if (adjacentRight && boxesOnWall[3] + 1 > targetsOnWall[3]) violatesR3 = true;
            if (violatesR3) continue;

            hasBox[y][x] = true;
            placed++;
            if (adjacentTop) boxesOnWall[0]++;
            if (adjacentBottom) boxesOnWall[1]++;
            if (adjacentLeft) boxesOnWall[2]++;
            if (adjacentRight) boxesOnWall[3]++;
        }

        if (placed < boxesCount) {
            throw new RuntimeException("Unable to place boxes. Try different apects or restart.");
        }
    }

    private void updateDisplay() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == 0 || y == height - 1 || x == 0 || x == width - 1) {
                    displayBoard[y][x] = WALL;
                } else {
                    displayBoard[y][x] = EMPTY;
                }
            }
        }

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (isTarget[y][x]) {
                    displayBoard[y][x] = TARGET;
                }
            }
        }

        boxesOnTargets = 0;
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                if (hasBox[y][x]) {
                    if (isTarget[y][x]) {
                        displayBoard[y][x] = BOX_ON_TARGET;
                        boxesOnTargets++;
                    } else {
                        displayBoard[y][x] = BOX;
                    }
                }
            }
        }

        displayBoard[playerY][playerX] = PLAYER;
    }

    public void printBoard() {
        updateDisplay();
        System.out.println("Moves: " + moves + "   Boxes: " + boxesOnTargets + " / " + boxesCount);
        for (int y = 0; y < height; y++) {
            System.out.println(new String(displayBoard[y]));
        }
    }

    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }

    public boolean move(String dirInput) {
        String dir = dirInput.toLowerCase();
        int dx = 0, dy = 0;
        if (dir.equals("w")) { dy = -1; }
        else if (dir.equals("s")) { dy = 1; }
        else if (dir.equals("a")) { dx = -1; }
        else if (dir.equals("d")) { dx = 1; }
        else {
            System.out.println("Unknown command: " + dirInput);
            return false;
        }

        int nx = playerX + dx;
        int ny = playerY + dy;

        if (displayBoard[ny][nx] == WALL) {
            System.out.println("Invalid move");
            return false;
        }

        if (hasBox[ny][nx]) {
            int bx = nx + dx;
            int by = ny + dy;
            if (displayBoard[by][bx] == WALL || hasBox[by][bx]) {
                System.out.println("Invalid move");
                return false;
            }

            boolean boxWasOnTarget = isTarget[ny][nx];
            boolean boxWillBeOnTarget = isTarget[by][bx];
            if (boxWasOnTarget && !boxWillBeOnTarget) boxesOnTargets--;
            if (!boxWasOnTarget && boxWillBeOnTarget) boxesOnTargets++;

            hasBox[ny][nx] = false;
            hasBox[by][bx] = true;

            updateBoxWallCountsForMove(nx, ny, bx, by);

            playerX = nx;
            playerY = ny;

            moves++;
            return true;
        } else {
            playerX = nx;
            playerY = ny;
            moves++;
            return true;
        }
    }

    private void updateBoxWallCountsForMove(int ox, int oy, int nx, int ny) {
        if (oy == 1) boxesOnWall[0]--;
        if (oy == height - 2) boxesOnWall[1]--;
        if (ox == 1) boxesOnWall[2]--;
        if (ox == width - 2) boxesOnWall[3]--;

        if (ny == 1) boxesOnWall[0]++;
        if (ny == height - 2) boxesOnWall[1]++;
        if (nx == 1) boxesOnWall[2]++;
        if (nx == width - 2) boxesOnWall[3]++;
    }
    public void restart(int w, int h, int k) {
        this.moves = 0;
        this.boxesOnTargets = 0;
        initializeGame(w, h, k);
        System.out.println("Game restarted!");
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int W = 0, H = 0, K = 0;

        while (true) {
            System.out.print("Enter width (>=5): ");
            if (!sc.hasNextInt()) { sc.next(); System.out.println("Please enter an integer."); continue; }
            W = sc.nextInt();
            if (W >= 5) break;
            System.out.println("Invalid width. Must be >= 5.");
        }

        while (true) {
            System.out.print("Enter height (>=5): ");
            if (!sc.hasNextInt()) { sc.next(); System.out.println("Please enter an integer."); continue; }
            H = sc.nextInt();
            if (H >= 5) break;
            System.out.println("Invalid height. Must be >= 5.");
        }

        int maxK = ((W - 2) * (H - 2) - 2) / 2;
        while (true) {
            System.out.print("Enter boxes (0 <= boxes number <= " + maxK + "): ");
            if (!sc.hasNextInt()) { sc.next(); System.out.println("Please enter an integer."); continue; }
            K = sc.nextInt();
            if (K >= 0 && K <= maxK) break;
            System.out.println("Invalid number. Must be between 0 and " + maxK + ".");
        }

        SokobanGame game = new SokobanGame();
        try {
            game.initializeGame(W, H, K);
        } catch (RuntimeException ex) {
            System.out.println("Initialization failed: " + ex.getMessage());
            return;
        }

        Scanner cmdScanner = new Scanner(System.in);
        while (true) {
            game.printBoard();
            if (game.checkWin()) {
                System.out.println("You win in " + game.moves + " moves!");
                break;
            }
            game.printBoard();
            System.out.print("Command (w/a/s/d, quit, restart): ");
            String cmd = cmdScanner.nextLine().trim().toLowerCase();
            if (cmd.equals("quit")) {
                System.out.println("Game quit. Moves: " + game.moves);
                break;
            }
            if (cmd.equals("restart")) {
                game.restart(W, H, K);
                continue;
            }

            boolean moved = game.move(cmd);
            if (!moved) {
                System.out.println("Invalid move or command!");
            }
        }
    }
}
