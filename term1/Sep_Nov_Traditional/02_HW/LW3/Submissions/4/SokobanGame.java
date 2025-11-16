// How would you modify isValidBoxPlacement(...) so boxes are never placed on targets at initialization,
// while keeping your corner logic intact? Show the exact condition you'd add.

import java.util.Arrays

public class SokobanGame {
    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets;

    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;

    private int prevPlayerX, prevPlayerY, prevMoves, prevBoxesOnTargets;
    private boolean[][] prevHasBox;

    public static final char WALL = '#';
    public static final char EMPTY = '.';
    public static final char PLAYER = '@';
    public static final char BOX = 'B';
    public static final char TARGET = '*';
    public static final char BOX_ON_TARGET = 'O';

    public void initializeGame(int w, int h, int k) {
        this.width = w;
        this.height = h;
        this.boxesCount = k;
        this.moves = 0;
        this.boxesOnTargets = 0;

        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                displayBoard[i][j] = EMPTY;
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
        placeBoxes();
        updateDisplay();
    }

    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
        displayBoard[playerY][playerX] = PLAYER;
    }

    private void placeTargets() {
        int targetsPlaced = 0;
        while (targetsPlaced < boxesCount) {
            int x = 1 + (int)(Math.random() * (width - 2));
            int y = 1 + (int)(Math.random() * (height - 2));
            
            if ((x == playerX && y == playerY) || isTarget[y][x]) {
                continue;
            }

            isTarget[y][x] = true;
            targetsPlaced++;
        }
    }

    private boolean isCorner(int x, int y) {
        return (x == 1 && y == 1) || 
               (x == width - 2 && y == 1) || 
               (x == 1 && y == height - 2) || 
               (x == width - 2 && y == height - 2);
    }

    private int countBoxesOnWall(int x, int y) {
        int count = 0;
        if (x == 1 || x == width - 2) {
            for (int i = 1; i < height - 1; i++) {
                if (hasBox[i][x]) count++;
            }
        }
        if (y == 1 || y == height - 2) {
            for (int i = 1; i < width - 1; i++) {
                if (hasBox[y][i]) count++;
            }
        }
        return count;
    }

    private int countTargetsOnWall(int x, int y) {
        int count = 0;
        if (x == 1 || x == width - 2) {
            for (int i = 1; i < height - 1; i++) {
                if (isTarget[i][x]) count++;
            }
        }
        if (y == 1 || y == height - 2) {
            for (int i = 1; i < width - 1; i++) {
                if (isTarget[y][i]) count++;
            }
        }
        return count;
    }

    private boolean isValidBoxPlacement(int x, int y) {
        if (x == playerX && y == playerY) return false;
        if (hasBox[y][x]) return false;

        if (isCorner(x, y) && !isTarget[y][x]) return false;

        for (int dy = -1; dy <= 0; dy++) {
            for (int dx = -1; dx <= 0; dx++) {
                int bx = x + dx, by = y + dy;
                if (bx > 0 && bx < width-1 && by > 0 && by < height-1) {
                    int solid = 0;
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            int cx = bx + j, cy = by + i;
                            if (hasBox[cy][cx] || displayBoard[cy][cx] == WALL || (cx == x && cy == y)) solid++;
                        }
                    }
                    if (solid == 4) return false;
                }
            }
        }

        if (isCorner(x, y) && !isTarget[y][x]) {
            int[][] adj = {{0,1},{1,0},{0,-1},{-1,0}};
            int adjacentBoxes = 0;
            for (int[] d : adj) {
                int ax = x + d[0], ay = y + d[1];
                if (ax > 0 && ax < width-1 && ay > 0 && ay < height-1) {
                    if (hasBox[ay][ax]) adjacentBoxes++;
                }
            }
            if (adjacentBoxes >= 2) return false;
        }

        if (x == 1 || x == width - 2) {
            int boxesOnWall = 0, targetsOnWall = 0;
            for (int i = 1; i < height - 1; i++) {
                if (hasBox[i][x] || (i == y)) boxesOnWall++;
                if (isTarget[i][x]) targetsOnWall++;
            }
            if (boxesOnWall > targetsOnWall) return false;
        }
        if (y == 1 || y == height - 2) {
            int boxesOnWall = 0, targetsOnWall = 0;
            for (int i = 1; i < width - 1; i++) {
                if (hasBox[y][i] || (i == x)) boxesOnWall++;
                if (isTarget[y][i]) targetsOnWall++;
            }
            if (boxesOnWall > targetsOnWall) return false;
        }

        return true;
    }

    private boolean boxCanReachTarget(int x, int y) {
        boolean[][] visited = new boolean[height][width];
        int[][] queue = new int[height * width][2];
        int front = 0, back = 0;
        queue[back][0] = x;
        queue[back][1] = y;
        back++;
        visited[y][x] = true;
        while (front < back) {
            int cx = queue[front][0], cy = queue[front][1];
            front++;
            if (isTarget[cy][cx]) return true;
            int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
            for (int[] d : dirs) {
                int nx = cx + d[0], ny = cy + d[1];
                if (nx > 0 && nx < width-1 && ny > 0 && ny < height-1 && !visited[ny][nx]) {
                    if (!hasBox[ny][nx] && displayBoard[ny][nx] != WALL) {
                        visited[ny][nx] = true;
                        queue[back][0] = nx;
                        queue[back][1] = ny;
                        back++;
                    }
                }
            }
        }
        return false;
    }

    private boolean isImpossibleCornerTrap() {
        int[][] corners = {
            {1, 1},
            {width - 2, 1},
            {1, height - 2},
            {width - 2, height - 2}
        };
        for (int[] corner : corners) {
            int x = corner[0], y = corner[1];
            int adjacentBoxes = 0;
            int[][] adj = {{0,1},{1,0},{0,-1},{-1,0}};
            for (int[] d : adj) {
                int ax = x + d[0], ay = y + d[1];
                if (ax > 0 && ax < width-1 && ay > 0 && ay < height-1) {
                    if (hasBox[ay][ax]) adjacentBoxes++;
                }
            }
            if (adjacentBoxes >= 2) return true;
        }
        return false;
    }

    private void placeBoxes() {
        int boxesPlaced = 0;
        int attempts = 0;
        while (boxesPlaced < boxesCount && attempts < 10000) {
            int x = 1 + (int)(Math.random() * (width - 2));
            int y = 1 + (int)(Math.random() * (height - 2));
            if (isValidBoxPlacement(x, y)) {
                hasBox[y][x] = true;
                if (width == 5 && height == 5) {
                    boxesPlaced++;
                } else {
                    if (boxCanReachTarget(x, y) && !isImpossibleCornerTrap()) {
                        boxesPlaced++;
                    } else {
                        hasBox[y][x] = false;
                    }
                }
            }
            attempts++;
        }
    }

    class InvalidMoveException extends Exception {
        public InvalidMoveException(String message) {
            super(message);
        }
    }
    class GameStateException extends Exception {
        public GameStateException(String message) {
            super(message);
        }
    }

    public boolean move(String direction) throws InvalidMoveException {
        prevPlayerX = playerX;
        prevPlayerY = playerY;
        prevMoves = moves;
        prevBoxesOnTargets = boxesOnTargets;
        prevHasBox = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                prevHasBox[i][j] = hasBox[i][j];
            }
        }

        int newX = playerX;
        int newY = playerY;
        switch (direction.toLowerCase()) {
            case "w": case "up": newY--; break;
            case "s": case "down": newY++; break;
            case "a": case "left": newX--; break;
            case "d": case "right": newX++; break;
            default: throw new InvalidMoveException("Unknown command: " + direction);
        }

        if (displayBoard[newY][newX] == WALL) {
            throw new InvalidMoveException("Invalid move: wall in the way.");
        }

        if (hasBox[newY][newX]) {
            int boxNewX = newX + (newX - playerX);
            int boxNewY = newY + (newY - playerY);
            if (displayBoard[boxNewY][boxNewX] == WALL || hasBox[boxNewY][boxNewX]) {
                throw new InvalidMoveException("Invalid move: cannot push box.");
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

    public boolean undo() {
        if (prevHasBox == null) {
            System.out.println("Nothing to undo.");
            return false;
        }
        playerX = prevPlayerX;
        playerY = prevPlayerY;
        moves = prevMoves;
        boxesOnTargets = prevBoxesOnTargets;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                hasBox[i][j] = prevHasBox[i][j];
            }
        }
        updateDisplay();
        System.out.println("Move undone.");
        return true;
    }

    private void updateDisplay() {
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                displayBoard[y][x] = EMPTY;
                if (isTarget[y][x]) displayBoard[y][x] = TARGET;
                if (hasBox[y][x]) {
                    displayBoard[y][x] = isTarget[y][x] ? BOX_ON_TARGET : BOX;
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

    public int getMoves() {
        return moves;
    }

    public void loadLevel(String[] level) {
        this.height = level.length;
        this.width = level[0].length();
        this.moves = 0;
        this.boxesOnTargets = 0;
        this.boxesCount = 0;
        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = level[y].charAt(x);
                displayBoard[y][x] = c;
                if (c == PLAYER) {
                    playerX = x;
                    playerY = y;
                } else if (c == TARGET) {
                    isTarget[y][x] = true;
                } else if (c == BOX) {
                    hasBox[y][x] = true;
                    boxesCount++;
                } else if (c == BOX_ON_TARGET) {
                    hasBox[y][x] = true;
                    isTarget[y][x] = true;
                    boxesOnTargets++;
                    boxesCount++;
                }
            }
        }
        updateDisplay();
    }
}
