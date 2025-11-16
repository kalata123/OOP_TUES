package edu.sokoban;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;

public class SokobanGame {
    // Game state
    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets;

    private Deque<MoveState> undoStack = new ArrayDeque<>();

    // Game board arrays
    private char[][] displayBoard;
    private boolean[][] isWall;
    private boolean[][] isTarget;
    private boolean[][] hasBox;

    // Constants
    public static final char WALL = '#';
    public static final char EMPTY = '.';
    public static final char PLAYER = '@';
    public static final char BOX = 'B';
    public static final char TARGET = '*';
    public static final char BOX_ON_TARGET = 'O';

    private static class MoveState {
        int playerX, playerY, moves, boxesOnTargets;
        boolean[][] boxesSnapshot;

        MoveState(int px, int py, int mv, int bot, boolean[][] boxes) {
            this.playerX = px;
            this.playerY = py;
            this.moves = mv;
            this.boxesOnTargets = bot;
            this.boxesSnapshot = new boolean[boxes.length][boxes[0].length];
            for (int i = 0; i < boxes.length; i++) {
                System.arraycopy(boxes[i], 0, this.boxesSnapshot[i], 0, boxes[i].length);
            }
        }
    }

    public void initializeGame(int w, int h, int k) {
        this.width = w;
        this.height = h;
        this.boxesCount = k;
        this.moves = 0;
        this.boxesOnTargets = 0;

        displayBoard = new char[width][height];
        isWall = new boolean[width][height];
        isTarget = new boolean[width][height];
        hasBox = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            isWall[i][0] = true;
            isWall[i][height - 1] = true;
        }
        for (int j = 0; j < height; j++) {
            isWall[0][j] = true;
            isWall[width - 1][j] = true;
        }

        undoStack.clear();

        int globalAttempts = 0;
        int maxGlobalAttempts = 2000;
        boolean success = false;

        while (globalAttempts < maxGlobalAttempts && !success) {
            globalAttempts++;
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    displayBoard[i][j] = EMPTY;
                    isTarget[i][j] = false;
                    hasBox[i][j] = false;
                    if (i > 0 && i < width - 1 && j > 0 && j < height - 1) isWall[i][j] = false;
                }
            }

            placePlayer();
            try {
                this.boxesOnTargets = 0;
                placeTargets();
                placeBoxes();
                success = true;
            } catch (IllegalStateException ex) {
                success = false;
            }
        }

        if (!success) {
            throw new IllegalStateException("Unable to initialize game after multiple attempts");
        }

        updateDisplay();
    }

    public void loadBuiltInLevelFromFile(int fileIndex) {
        String path = "hw1/resources/levels/level" + fileIndex + ".txt";
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read built-in level file: " + path + " -> " + e.getMessage(), e);
        }

        if (lines.isEmpty()) throw new IllegalStateException("Built-in level file is empty: " + path);

        int h = lines.size();
        int w = 0;
        for (String ln : lines) if (ln.length() > w) w = ln.length();

        this.width = w;
        this.height = h;
        this.moves = 0;
        this.boxesOnTargets = 0;
        this.boxesCount = 0;

        displayBoard = new char[width][height];
        isWall = new boolean[width][height];
        isTarget = new boolean[width][height];
        hasBox = new boolean[width][height];
        undoStack.clear();

        boolean playerSpecified = false;
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                char ch = x < line.length() ? line.charAt(x) : EMPTY;
                switch (ch) {
                    case '#':
                        isWall[x][y] = true;
                        displayBoard[x][y] = WALL;
                        break;
                    case '@':
                        playerX = x;
                        playerY = y;
                        playerSpecified = true;
                        displayBoard[x][y] = PLAYER;
                        break;
                    case 'B':
                        hasBox[x][y] = true;
                        boxesCount++;
                        displayBoard[x][y] = BOX;
                        break;
                    case '*':
                        isTarget[x][y] = true;
                        displayBoard[x][y] = TARGET;
                        break;
                    case 'O':
                        hasBox[x][y] = true;
                        isTarget[x][y] = true;
                        boxesCount++;
                        boxesOnTargets++;
                        displayBoard[x][y] = BOX_ON_TARGET;
                        break;
                    case '.':
                    case ' ':
                    default:
                        displayBoard[x][y] = EMPTY;
                        break;
                }
            }
        }

        if (!playerSpecified) {
            playerX = width / 2;
            playerY = height / 2;
        }

        updateDisplay();
    }

    private void placePlayer() {
        this.playerX = width / 2;
        this.playerY = height / 2;
    }

    private void placeTargets() {
        int placed = 0;
        int attempts = 0;
        int maxAttempts = Math.max(1000, width * height * 10);
        while (placed < boxesCount && attempts < maxAttempts) {
            attempts++;
            int x = 1 + (int) (Math.random() * (width - 2));
            int y = 1 + (int) (Math.random() * (height - 2));
            if (!isTarget[x][y] && (x != playerX || y != playerY)) {
                isTarget[x][y] = true;
                placed++;
            }
        }
        if (placed < boxesCount) {
            throw new IllegalStateException("Unable to place required number of targets with given constraints.");
        }
    }

    private void placeBoxes() {
        int[] targetsOnWall = new int[4];
        for (int x = 1; x <= width - 2; x++) {
            if (isTarget[x][1]) targetsOnWall[0]++;
            if (isTarget[x][height - 2]) targetsOnWall[1]++;
        }
        for (int y = 1; y <= height - 2; y++) {
            if (isTarget[1][y]) targetsOnWall[2]++;
            if (isTarget[width - 2][y]) targetsOnWall[3]++;
        }
        int[] boxesOnWall = new int[4];

        int placed = 0;
        int attempts = 0;
        int maxAttempts = Math.max(20000, width * height * 100);
        while (placed < boxesCount && attempts < maxAttempts) {
            attempts++;
            int x = 1 + (int) (Math.random() * (width - 2));
            int y = 1 + (int) (Math.random() * (height - 2));

            if (x == playerX && y == playerY) continue;
            if (hasBox[x][y]) continue;

            boolean isInteriorCorner = (x == 1 && y == 1) || (x == width - 2 && y == 1) || (x == 1 && y == height - 2) || (x == width - 2 && y == height - 2);

            if (isTarget[x][y] && !isInteriorCorner) {
                continue;
            }

            if (isInteriorCorner && !isTarget[x][y]) continue;

            boolean R3 = false;
            if (y == 1 && boxesOnWall[0] + 1 > targetsOnWall[0]) R3 = true;
            if (y == height - 2 && boxesOnWall[1] + 1 > targetsOnWall[1]) R3 = true;
            if (x == 1 && boxesOnWall[2] + 1 > targetsOnWall[2]) R3 = true;
            if (x == width - 2 && boxesOnWall[3] + 1 > targetsOnWall[3]) R3 = true;
            if (R3) continue;

            boolean blocksNearWall = false;
            int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
            for (int[] d : dirs) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 1 && nx <= width - 2 && ny >= 1 && ny <= height - 2) {
                    if (hasBox[nx][ny]) {
                        int beyondNx = nx + d[0], beyondNy = ny + d[1];
                        int beyondCx = x - d[0], beyondCy = y - d[1];
                        if (isBorder(beyondNx, beyondNy) || isBorder(beyondCx, beyondCy)) {
                            blocksNearWall = true;
                            break;
                        }
                    }
                }
            }
            if (blocksNearWall) continue;

            hasBox[x][y] = true;
            if (isTarget[x][y]) boxesOnTargets++;
            placed++;
            if (y == 1) boxesOnWall[0]++;
            if (y == height - 2) boxesOnWall[1]++;
            if (x == 1) boxesOnWall[2]++;
            if (x == width - 2) boxesOnWall[3]++;
        }

        if (placed < boxesCount) {
            throw new IllegalStateException("Unable to place required number of boxes with given constraints.");
        }
    }

    public boolean move(String direction) {
        if (direction == null) {
            System.out.println("Invalid move");
            return false;
        }
        String input = direction.trim().toLowerCase();

        if (input.equals("quit") || input.equals("q")) {
            System.out.println("Game quit after " + moves + " moves.");
            System.exit(0);
        }

        int dx = 0, dy = 0;
        switch (input) {
            case "up", "w" -> dy = -1;
            case "down", "s" -> dy = 1;
            case "left", "a" -> dx = -1;
            case "right", "d" -> dx = 1;
            default -> {
                System.out.println("Invalid move");
                return false;
            }
        }
        int newX = playerX + dx;
        int newY = playerY + dy;

        if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
            System.out.println("Invalid move");
            return false;
        }
        if (isWall[newX][newY]) {
            System.out.println("Invalid move");
            return false;
        }

        boolean willPushBox = hasBox[newX][newY];
        int boxNewX = 0, boxNewY = 0;
        if (willPushBox) {
            boxNewX = newX + dx;
            boxNewY = newY + dy;
            if (boxNewX < 0 || boxNewX >= width || boxNewY < 0 || boxNewY >= height) {
                System.out.println("Invalid move");
                return false;
            }
            if (isWall[boxNewX][boxNewY] || hasBox[boxNewX][boxNewY]) {
                System.out.println("Invalid move");
                return false;
            }
        }

        pushSnapshot();

        if (willPushBox) {
            if (isTarget[boxNewX][boxNewY]) boxesOnTargets++;
            if (isTarget[newX][newY]) boxesOnTargets--;
            hasBox[newX][newY] = false;
            hasBox[boxNewX][boxNewY] = true;
        }

        playerX = newX;
        playerY = newY;
        moves++;
        updateDisplay();
        return true;
    }

    private void pushSnapshot() {
        undoStack.push(new MoveState(playerX, playerY, moves, boxesOnTargets, hasBox));
    }

    public boolean undo() {
        if (undoStack.isEmpty()) return false;
        MoveState st = undoStack.pop();
        for (int i = 0; i < width; i++) {
            System.arraycopy(st.boxesSnapshot[i], 0, hasBox[i], 0, height);
        }
        this.playerX = st.playerX;
        this.playerY = st.playerY;
        this.moves = st.moves;
        this.boxesOnTargets = st.boxesOnTargets;
        updateDisplay();
        return true;
    }

    private void updateDisplay() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (isWall[i][j]) displayBoard[i][j] = WALL;
                else displayBoard[i][j] = EMPTY;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (isTarget[i][j] && !hasBox[i][j]) {
                    displayBoard[i][j] = TARGET;
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (hasBox[i][j]) {
                    displayBoard[i][j] = isTarget[i][j] ? BOX_ON_TARGET : BOX;
                }
            }
        }
        if (playerX >= 0 && playerX < width && playerY >= 0 && playerY < height) {
            displayBoard[playerX][playerY] = PLAYER;
        }
    }

    public boolean checkWin() {
        if (boxesOnTargets == boxesCount) {
            System.out.println("You win in " + moves + " moves!");
            return true;
        }
        return false;
    }

    public void printBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(displayBoard[j][i]);
            }
            System.out.println();
        }
        System.out.println("Moves: " + moves + "  Boxes on targets: " + boxesOnTargets + "/" + boxesCount);
    }

    public int getMoves() {
        return moves;
    }

    private boolean isBorder(int x, int y) {
        return x <= 0 || x >= width - 1 || y <= 0 || y >= height - 1;
    }
}
