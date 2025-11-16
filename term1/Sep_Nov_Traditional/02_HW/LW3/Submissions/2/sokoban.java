import java.util.Scanner;
import java.util.Random;

public class sokoban {
    private static class GameState {
        int playerX, playerY;
        char[][] displayBoard;
        boolean[][] isTarget;
        boolean[][] hasBox;
        int boxesOnTargets;

        GameState() {
        }

        GameState(GameState other, int height, int width) {
            playerX = other.playerX;
            playerY = other.playerY;
            boxesOnTargets = other.boxesOnTargets;

            displayBoard = new char[height][];
            isTarget = new boolean[height][];
            hasBox = new boolean[height][];

            for (int i = 0; i < height; i++) {
                displayBoard[i] = new char[width];
                isTarget[i] = new boolean[width];
                hasBox[i] = new boolean[width];

                System.arraycopy(other.displayBoard[i], 0, displayBoard[i], 0, width);
                System.arraycopy(other.isTarget[i], 0, isTarget[i], 0, width);
                System.arraycopy(other.hasBox[i], 0, hasBox[i], 0, width);
            }
        }
    }

    private static class SokobanGame {
        private static final char WALL = '#';
        private static final char EMPTY = '.';
        private static final char PLAYER = '@';
        private static final char BOX = 'B';
        private static final char TARGET = '*';
        private static final char BOX_ON_TARGET = 'O';

        private int width, height, boxesCount;
        private int playerX, playerY;
        private int moves, boxesOnTargets;
        private char[][] displayBoard;
        private boolean[][] isTarget;
        private boolean[][] hasBox;
        private Random random;
        private java.util.Stack<GameState> undoStack; //TODO: what does that do?

        public SokobanGame() {
            random = new Random();
            undoStack = new java.util.Stack<>();
            moves = 0;
            boxesOnTargets = 0;
        }

        public void initializeGame(int w, int h, int k) {
            width = w;
            height = h;
            boxesCount = k;
            displayBoard = new char[height][width];
            isTarget = new boolean[height][width];
            hasBox = new boolean[height][width];
            undoStack.clear(); //TODO: Why?
            moves = 0;
            boxesOnTargets = 0;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    displayBoard[i][j] = EMPTY;
                    isTarget[i][j] = false;
                    hasBox[i][j] = false;
                }
            }

            for (int i = 0; i < width; i++) {
                displayBoard[0][i] = WALL;
                displayBoard[height - 1][i] = WALL;
            }
            for (int i = 0; i < height; i++) {
                displayBoard[i][0] = WALL;
                displayBoard[i][width - 1] = WALL;
            }

            placePlayer();
            placeTargets();

            boolean success = false;
            int attempts = 0;
            while (!success && attempts < 1000) {
                for (int i = 1; i < height - 1; i++) {
                    for (int j = 1; j < width - 1; j++) {
                        hasBox[i][j] = false;
                    }
                }
                boolean placedOk = placeBoxes();
                if (!placedOk) {
                    success = false;
                    attempts++;
                    continue;
                }
                success = true;
                for (int y = 1; y < height - 1 && success; y++) {
                    for (int x = 1; x < width - 1; x++) {
                        if (hasBox[y][x] && !isTarget[y][x]) {
                            boolean upWall = displayBoard[y - 1][x] == WALL;
                            boolean downWall = displayBoard[y + 1][x] == WALL;
                            boolean leftWall = displayBoard[y][x - 1] == WALL;
                            boolean rightWall = displayBoard[y][x + 1] == WALL;
                            if ((upWall && leftWall) || (upWall && rightWall) || (downWall && leftWall)
                                    || (downWall && rightWall)) {
                                success = false;
                                break;
                            }
                        }
                    }
                }
                attempts++;
            }
            if (!success) {
                System.out.println(
                        "Could not create a valid map after 1000 tries. Try with fewer boxes or a larger board.");
                System.exit(1);
            }

            recomputeBoxesOnTargets();
            updateDisplay();
        }

        private void placePlayer() {
            playerX = width / 2;
            playerY = height / 2;
            displayBoard[playerY][playerX] = PLAYER;
        }

        private boolean isValidPosition(int y, int x) {
            return y > 0 && y < height - 1 && x > 0 && x < width - 1 && displayBoard[y][x] != WALL;
        }

        private boolean isInteriorCorner(int y, int x) {
            return (y == 1 && x == 1) || (y == 1 && x == width - 2) || (y == height - 2 && x == 1)
                    || (y == height - 2 && x == width - 2);
        }

        private void placeTargets() {
            int placed = 0;
            while (placed < boxesCount) {
                int x = random.nextInt(width - 2) + 1;
                int y = random.nextInt(height - 2) + 1;
                if (isValidPosition(y, x) && !isTarget[y][x] && (y != playerY || x != playerX)) {
                    isTarget[y][x] = true;
                    placed++;
                }
            }
        }

        private int countTargetsOnWall(int y1, int y2, int x1, int x2) {
            int count = 0;
            for (int y = y1; y <= y2; y++)
                for (int x = x1; x <= x2; x++)
                    if (isTarget[y][x])
                        count++;
            return count;
        }

        private int countBoxesOnWall(int y1, int y2, int x1, int x2) {
            int count = 0;
            for (int y = y1; y <= y2; y++)
                for (int x = x1; x <= x2; x++)
                    if (hasBox[y][x])
                        count++;
            return count;
        }

        private boolean checkWallCapacityConstraint(int y, int x) {
            if (y == 1)
                return countBoxesOnWall(1, 1, 1, width - 2) < countTargetsOnWall(1, 1, 1, width - 2);
            if (y == height - 2)
                return countBoxesOnWall(height - 2, height - 2, 1, width - 2) < countTargetsOnWall(height - 2,
                        height - 2, 1, width - 2);
            if (x == 1)
                return countBoxesOnWall(1, height - 2, 1, 1) < countTargetsOnWall(1, height - 2, 1, 1);
            if (x == width - 2)
                return countBoxesOnWall(1, height - 2, width - 2, width - 2) < countTargetsOnWall(1, height - 2,
                        width - 2, width - 2);
            return true;
        }

        private boolean check2x2Block(int y, int x) {
            if (y >= height - 1 || x >= width - 1)
                return false;
            boolean hasWallOrBox = (displayBoard[y][x] == WALL || hasBox[y][x])
                    && (displayBoard[y][x + 1] == WALL || hasBox[y][x + 1])
                    && (displayBoard[y + 1][x] == WALL || hasBox[y + 1][x])
                    && (displayBoard[y + 1][x + 1] == WALL || hasBox[y + 1][x + 1]);
            boolean hasTarget = isTarget[y][x] || isTarget[y][x + 1] || isTarget[y + 1][x] || isTarget[y + 1][x + 1];
            return hasWallOrBox && !hasTarget;
        }

        private boolean checkCornerAdjacencyTrap(int y, int x) {
            boolean isAdjacentToCorner = (y == 1 && (x == 1 || x == width - 2))
                    || (y == height - 2 && (x == 1 || x == width - 2)) || (x == 1 && (y == 1 || y == height - 2))
                    || (x == width - 2 && (y == 1 || y == height - 2));
            if (!isAdjacentToCorner)
                return false;
            int adjacentBoxes = 0;
            if (y > 0 && hasBox[y - 1][x])
                adjacentBoxes++;
            if (y < height - 1 && hasBox[y + 1][x])
                adjacentBoxes++;
            if (x > 0 && hasBox[y][x - 1])
                adjacentBoxes++;
            if (x < width - 1 && hasBox[y][x + 1])
                adjacentBoxes++;
            return adjacentBoxes >= 2 && !isTarget[y][x];
        }

        private boolean checkWallLineCapacity(int y, int x) {
            if (y == 1 || y == height - 2) {
                int targetCount = 0, boxCount = 0;
                for (int i = 1; i < width - 1; i++) {
                    if (isTarget[y][i])
                        targetCount++;
                    if (hasBox[y][i])
                        boxCount++;
                }
                return boxCount <= targetCount;
            }
            if (x == 1 || x == width - 2) {
                int targetCount = 0, boxCount = 0;
                for (int i = 1; i < height - 1; i++) {
                    if (isTarget[i][x])
                        targetCount++;
                    if (hasBox[i][x])
                        boxCount++;
                }
                return boxCount <= targetCount;
            }
            return true;
        }

        private boolean placeBoxes() {
            int placed = 0;
            int attempts = 0;
            while (placed < boxesCount && attempts < 10000) {
                attempts++;
                int x = random.nextInt(width - 2) + 1;
                int y = random.nextInt(height - 2) + 1;
                if (!isValidPosition(y, x) || hasBox[y][x] || (y == playerY && x == playerX) || isTarget[y][x])
                    continue;
                if (isInteriorCorner(y, x) && !isTarget[y][x])
                    continue;
                if ((y == 1 || y == height - 2 || x == 1 || x == width - 2) && !checkWallCapacityConstraint(y, x))
                    continue;
                hasBox[y][x] = true;
                boolean valid = true;
                for (int i = 0; i < height - 1 && valid; i++)
                    for (int j = 0; j < width - 1; j++)
                        if (check2x2Block(i, j))
                            valid = false;
                if (valid && checkCornerAdjacencyTrap(y, x))
                    valid = false;
                if (valid && !checkWallLineCapacity(y, x))
                    valid = false;
                if (!valid) {
                    hasBox[y][x] = false;
                    continue;
                }
                placed++;
            }
            if (placed < boxesCount) {
                // failed to place all boxes within attempt limit: clear and report failure to
                // caller
                for (int i = 1; i < height - 1; i++)
                    for (int j = 1; j < width - 1; j++)
                        hasBox[i][j] = false;
                return false;
            }
            return true;
        }

        public boolean move(String direction) {
            if (direction.equals("undo"))
                return undo();
            GameState savedState = new GameState();
            savedState.playerX = playerX;
            savedState.playerY = playerY;
            savedState.boxesOnTargets = boxesOnTargets;
            savedState.displayBoard = new char[height][width];
            savedState.isTarget = new boolean[height][width];
            savedState.hasBox = new boolean[height][width];
            for (int i = 0; i < height; i++) {
                System.arraycopy(displayBoard[i], 0, savedState.displayBoard[i], 0, width);
                System.arraycopy(isTarget[i], 0, savedState.isTarget[i], 0, width);
                System.arraycopy(hasBox[i], 0, savedState.hasBox[i], 0, width);
            }
            int newX = playerX, newY = playerY;
            switch (direction.toLowerCase()) {
                case "up":
                case "w":
                    newY--;
                    break;
                case "down":
                case "s":
                    newY++;
                    break;
                case "left":
                case "a":
                    newX--;
                    break;
                case "right":
                case "d":
                    newX++;
                    break;
                default:
                    return false;
            }
            if (displayBoard[newY][newX] == WALL) {
                System.out.println("Invalid move");
                return false;
            }
            if (hasBox[newY][newX]) {
                int boxNewX = newX + (newX - playerX);
                int boxNewY = newY + (newY - playerY);
                if (displayBoard[boxNewY][boxNewX] == WALL || hasBox[boxNewY][boxNewX]) {
                    System.out.println("Invalid move");
                    return false;
                }
                hasBox[newY][newX] = false;
                hasBox[boxNewY][boxNewX] = true;
                if (isTarget[newY][newX])
                    boxesOnTargets--;
                if (isTarget[boxNewY][boxNewX])
                    boxesOnTargets++;
            }
            playerX = newX;
            playerY = newY;
            moves++;
            updateDisplay();
            undoStack.push(savedState);
            int restartAttempts = 0;
            while (isDeadlock() && restartAttempts < 10) {
                System.out.println("Deadlock detected! Restarting level...");
                initializeGame(width, height, boxesCount);
                moves = 0;
                restartAttempts++;
            }
            if (restartAttempts == 10 && isDeadlock()) {
                System.out.println("Unsolvable map. Please restart the game or try different parameters.");
                System.exit(1);
            }
            return true;
        }

        private boolean isDeadlock() {
            for (int y = 1; y < height - 1; y++)
                for (int x = 1; x < width - 1; x++)
                    if (hasBox[y][x] && !isTarget[y][x]) {
                        boolean upWall = displayBoard[y - 1][x] == WALL;
                        boolean downWall = displayBoard[y + 1][x] == WALL;
                        boolean leftWall = displayBoard[y][x - 1] == WALL;
                        boolean rightWall = displayBoard[y][x + 1] == WALL;
                        if ((upWall && leftWall) || (upWall && rightWall) || (downWall && leftWall)
                                || (downWall && rightWall))
                            return true;
                    }
            return false;
        }

        private void recomputeBoxesOnTargets() {
            boxesOnTargets = 0;
            for (int y = 1; y < height - 1; y++)
                for (int x = 1; x < width - 1; x++)
                    if (hasBox[y][x] && isTarget[y][x])
                        boxesOnTargets++;
        }

        public boolean undo() {
            if (undoStack.isEmpty()) {
                System.out.println("No moves to undo!");
                return false;
            }
            GameState previousState = undoStack.pop();
            playerX = previousState.playerX;
            playerY = previousState.playerY;
            boxesOnTargets = previousState.boxesOnTargets;
            for (int i = 0; i < height; i++) {
                System.arraycopy(previousState.displayBoard[i], 0, displayBoard[i], 0, width);
                System.arraycopy(previousState.isTarget[i], 0, isTarget[i], 0, width);
                System.arraycopy(previousState.hasBox[i], 0, hasBox[i], 0, width);
            }
            moves--;
            return true;
        }

        private void updateDisplay() {
            for (int i = 1; i < height - 1; i++)
                for (int j = 1; j < width - 1; j++)
                    displayBoard[i][j] = isTarget[i][j] ? TARGET : EMPTY;
            for (int i = 1; i < height - 1; i++)
                for (int j = 1; j < width - 1; j++)
                    if (hasBox[i][j])
                        displayBoard[i][j] = isTarget[i][j] ? BOX_ON_TARGET : BOX;
            displayBoard[playerY][playerX] = PLAYER;
        }

        public boolean checkWin() {
            return boxesOnTargets == boxesCount;
        }

        public void printBoard() {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++)
                    System.out.print(displayBoard[i][j]);
                System.out.println();
            }
            System.out.println("Moves: " + moves);
        }

        public int getMoves() {
            return moves;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int W, H, K;

        do {
            System.out.print("Enter width (W ≥ 5): ");
            W = scanner.nextInt();
        } while (W < 5);

        do {
            System.out.print("Enter height (H ≥ 5): ");
            H = scanner.nextInt();
        } while (H < 5);

        int maxBoxes = ((W - 2) * (H - 2) - 2) / 2;
        do {
            System.out.print("Enter number of boxes (K ≤ " + maxBoxes + "): ");
            K = scanner.nextInt();
        } while (K > maxBoxes);

        SokobanGame game = new SokobanGame();
        game.initializeGame(W, H, K);
        scanner.nextLine();
        while (!game.checkWin()) {
            game.printBoard();
            System.out.print("Enter command (w/a/s/d or up/down/left/right or q/quit or undo): ");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("q") || command.equals("quit"))
                break;
            game.move(command);
        }

        if (game.checkWin()) {
            System.out.println("You win in " + game.getMoves() + " moves!");
        }

        scanner.close();
    }
}
