import java.io.IOException;
import java.util.*;

static class WallException extends IOException {
    public WallException(String message) {
        super(message);
    }
}

static class BoxException extends IOException {
    public BoxException(String message) {
        super(message);
    }
}

static public class SokobanGame {
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
    private boolean[][] isWall;

    public void initializeGame(String[] level) {
        height = level.length;
        width = level[0].length();

        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];
        isWall = new boolean[height][width];
        boxesCount = 0;
        boxesOnTargets = 0;
        moves = 0;

        for (int row = 0; row < height; row++) {
            String line = level[row];
            for (int col = 0; col < width; col++) {
                char c = line.charAt(col);
                displayBoard[row][col] = c;

                switch (c) {
                    case WALL: isWall[row][col] = true; break;
                    case TARGET: isTarget[row][col] = true; break;
                    case BOX: hasBox[row][col] = true; boxesCount++; break;
                    case PLAYER: playerX = row; playerY = col; break;
                    case BOX_ON_TARGET:
                        hasBox[row][col] = true;
                        isTarget[row][col] = true;
                        boxesCount++;
                        boxesOnTargets++;
                        break;
                }
            }
        }
    }

    int countBoxesOnTopWall() {
        int boxes = 0;
        for(int col = 1; col < width - 1; col++) {
            if(hasBox[1][col])
                boxes++;
        }
        return boxes;
    }

    int countTargetsOnTopWall() {
        int targets = 0;
        for(int col = 1; col < width - 1; col++) {
            if(isTarget[1][col])
                targets++;
        }
        return targets;
    }

    int countBoxesOnBottomWall() {
        int boxes = 0;
        for(int col = 1; col < width - 1; col++) {
            if(hasBox[height - 2][col])
                boxes++;
        }
        return boxes;
    }

    int countTargetsOnBottomWall() {
        int targets = 0;
        for(int col = 1; col < width - 1; col++) {
            if(isTarget[height - 2][col])
                targets++;
        }
        return targets;
    }

    int countBoxesOnLeftWall() {
        int boxes = 0;
        for(int row = 1; row < height - 1; row++) {
            if(hasBox[row][1])
                boxes++;
        }
        return boxes;
    }

    int countTargetsOnLeftWall() {
        int targets = 0;
        for(int row = 1; row < height - 1; row++) {
            if(isTarget[row][1])
                targets++;
        }
        return targets;
    }

    int countBoxesOnRightWall() {
        int boxes = 0;
        for(int row = 1; row < height - 1; row++) {
            if(hasBox[row][width - 2])
                boxes++;
        }
        return boxes;
    }

    int countTargetsOnRightWall() {
        int targets = 0;
        for(int row = 1; row < height - 1; row++) {
            if(isTarget[row][width - 2])
                targets++;
        }
        return targets;
    }

    public void move(String direction) throws IOException {
        int dx = 0, dy = 0;

        switch (direction) {
            case "right", "d": dy = 1; break;
            case "left", "a": dy = -1; break;
            case "up", "w": dx = -1; break;
            case "down", "s": dx = 1; break;
            case "undo", "u": break;
            default: return;
        }

        int nextX = playerX + dx;
        int nextY = playerY + dy;

        if (isWall[nextX][nextY]) throw new WallException("Cannot move through walls.");

        if (hasBox[nextX][nextY]) {
            int boxX = nextX + dx;
            int boxY = nextY + dy;
            if (isWall[boxX][boxY] || hasBox[boxX][boxY]) throw new BoxException("Invalid box push attempt.");
            hasBox[nextX][nextY] = false;
            hasBox[boxX][boxY] = true;
            if (isTarget[boxX][boxY]) boxesOnTargets++;
            if (isTarget[nextX][nextY]) boxesOnTargets--;
        }

        playerX = nextX;
        playerY = nextY;
        moves++;
        updateDisplay();
    }

    private void updateDisplay() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (isWall[row][col]) {
                    displayBoard[row][col] = WALL;
                } else if (hasBox[row][col]) {
                    displayBoard[row][col] = isTarget[row][col] ? BOX_ON_TARGET : BOX;
                } else {
                    displayBoard[row][col] = isTarget[row][col] ? TARGET : EMPTY;
                }
            }
        }
        displayBoard[playerX][playerY] = PLAYER;
    }

    public boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }

    public boolean check2x2() {
        for(int row = 0; row < height - 1; row++) {
            for(int col = 0; col < width - 1; col++) {
                if (((hasBox[row][col] && !isTarget[row][col]) || isWall[row][col]) &&
                        ((hasBox[row + 1][col] && !isTarget[row + 1][col]) || isWall[row + 1][col]) &&
                        ((hasBox[row][col + 1] && !isTarget[row][col + 1]) || isWall[row][col + 1]) &&
                        ((hasBox[row + 1][col + 1] && !isTarget[row + 1][col + 1]) || isWall[row + 1][col + 1]))
                    return true;
            }
        }
        return false;
    }

    public boolean checkDeadlock() {
        return ((hasBox[1][1] && !isTarget[1][1]) ||
                (hasBox[1][width - 2] && !isTarget[1][width - 2]) ||
                (hasBox[height - 2][1] && !isTarget[height - 2][1]) ||
                (hasBox[height - 2][width - 2] && !isTarget[height - 2][width - 2]) ||
                countBoxesOnLeftWall() > countTargetsOnLeftWall() ||
                countBoxesOnRightWall() > countTargetsOnRightWall() ||
                countBoxesOnTopWall() > countTargetsOnTopWall() ||
                countBoxesOnBottomWall() > countTargetsOnBottomWall());
    }

    public void printBoard() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(displayBoard[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println("Moves: " + moves);
    }
}

void main() {
    Scanner sc = new Scanner(System.in);
    SokobanGame game = new SokobanGame();

    String[][] levels = {
            {
                    "#####",
                    "#*B.#",
                    "#.@.#",
                    "#*B.#",
                    "#####"
            },
            {
                    "#########",
                    "#*.....*#",
                    "#..B...B#",
                    "#..B@...#",
                    "#..B..*B#",
                    "#..*...*#",
                    "#########"
            },
            {
                    "#################",
                    "#..*....B.*.....#",
                    "#..B....*.......#",
                    "#..*.......B....#",
                    "#....B......*...#",
                    "#..*....@..B....#",
                    "#.......*....B..#",
                    "#..B....*....B..#",
                    "#..*....B.......#",
                    "#......*....B...#",
                    "#################"
            }
    };

    for (String[] level : levels) {
        game.initializeGame(level);

        while (!game.checkWin()) {
            game.printBoard();
            String command = sc.nextLine();
            if (command.equals("quit") || command.equals("q")) return;
            try {
                game.move(command);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if(game.check2x2()) {
                game.printBoard();
                System.out.println("2x2 Block found. Restarting level.");
                System.out.println();
                game.initializeGame(level);
            }
            if(game.checkDeadlock()) {
                game.printBoard();
                System.out.println("Deadlock found. Restarting level.");
                System.out.println();
                game.initializeGame(level);
            }
        }
        game.printBoard();
        System.out.println("Level completed! Total Moves: " + game.moves);
        System.out.println("=========================");
        System.out.println("|       YOU WIN!        |");
        System.out.println("=========================");
    }
    System.out.println("===================================");
    System.out.println("| Completed all 3 levels! Woohoo! |");
    System.out.println("===================================");
}
