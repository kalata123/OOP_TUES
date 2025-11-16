import java.util.Arrays;
import java.awt.Point;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

class GameState{
    Point playerPosition;
    boolean[][] hasBox;
    int boxesOnTargets;

    GameState(Point playerPosition, boolean[][] hasBox, int boxesOnTargets){
        this.playerPosition = new Point(playerPosition);
        this.hasBox = new boolean[hasBox.length][hasBox[0].length];

        for (int i = 0; i < hasBox.length; i++) {
            this.hasBox[i] = hasBox[i].clone();
        }

        this.boxesOnTargets = boxesOnTargets;
    }
}

public class SokobanGame {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';
    static final int MAX_UNDO_COUNT = 3;


    // Game state
    private int width, height, boxesCount;
    private Point playerPosition;
    private int moves, boxesOnTargets, undoCounter;

    // Game board arrays
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;
    private boolean initialized = false;
    private Stack<GameState> history = new Stack<>();

    private int maximum_boxes(){
        return ((this.width - 2)*(this.height - 2) - 2) / 2;
    }

    // Core methods to implement

    public void initializeGame(int w, int h, int k) {
        if(initialized){
            throw new IllegalStateException("Game already initialized!");
        }

        if (w < 5 || h < 5) {
            throw new IllegalArgumentException("Board must be at least 5x5.");
        }
        if (k < 1) {
            throw new IllegalArgumentException("At least one box required.");
        }

        this.width = w;
        this.height = h;
        this.boxesCount = k;

        this.moves = 0;

        int max_boxes = maximum_boxes();
        if(this.boxesCount >= max_boxes) {
            throw new IllegalArgumentException("boxes count exceeds maximum");
        }

        char[] filledRow = new char[this.width];
        char[] edgeFilledRow = new char[this.width];

        Arrays.fill(filledRow, WALL);
        Arrays.fill(edgeFilledRow, EMPTY);

        edgeFilledRow[0] = WALL;
        edgeFilledRow[this.width - 1] = WALL;

        this.displayBoard = new char[this.height][this.width];

        this.displayBoard[0] = filledRow.clone();
        this.displayBoard[this.height - 1] = filledRow.clone();
        for(int row = 1; row < this.height -1; row++){
            displayBoard[row] = edgeFilledRow.clone();
        }

        this.isTarget = new boolean[this.height][this.width];
        this.hasBox = new boolean[this.height][this.width];

        this.placePlayer();
        this.placeTargets();
        this.placeBoxes();

        initialized = true;
    }

    private void placePlayer() {
        int x = this.width / 2;
        int y = this.height / 2;
        this.playerPosition = new Point(x, y);

        this.displayBoard[y][x] = PLAYER;
    }

    private boolean hasSolidBlock(){
        for(int row = 0; row < height - 1; row++){
            for(int col = 0; col < width - 1; col++){
                int filled = 0;
                boolean hasBoxInBlock = false;

                for(int dy = 0; dy < 2; dy++){
                    for(int dx = 0; dx < 2; dx++){
                        char tile = displayBoard[row + dy][col + dx];
                        if(tile == WALL || tile == BOX || tile == BOX_ON_TARGET){
                            filled++;

                            if(tile == BOX || tile == BOX_ON_TARGET){
                                hasBoxInBlock = true;
                            }
                        }
                    }
                }

                if(filled == 4 && hasBoxInBlock) return true;
            }
        }
        return false;
    }

    private boolean isNonTargetCorner(int row, int col){
        if(isTarget[row][col]){
            return false;
        }

        boolean topWall = (row > 0 && displayBoard[row - 1][col] == WALL);
        boolean bottomWall = (row < this.height - 1 && displayBoard[row + 1][col] == WALL);
        boolean leftWall = (col > 0 && displayBoard[row][col - 1] == WALL);
        boolean rightWall = (col < this.width - 1 && displayBoard[row][col + 1] == WALL);

        return (topWall && leftWall) || (topWall && rightWall)
                || (bottomWall && leftWall) || (bottomWall && rightWall);
    }

    private boolean hasSingleCornerDeadlock(){
        for(int row = 0; row < height - 1; row++){
            for(int col = 0; col < width - 1; col++){
                if (hasBox[row][col] && isNonTargetCorner(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean violatesWallCapacity(){
        for(int row = 1; row < height - 1; row++){
            int boxesOnWall = 0, targetsOnWall = 0;
            for(int col = 1; col < width - 1; col++){
                if(displayBoard[row - 1][col] == WALL || displayBoard[row + 1][col] == WALL){
                    if(hasBox[row][col]){
                        boxesOnWall++;
                    }
                    if(isTarget[row][col]){
                        targetsOnWall++;
                    }
                }
            }

            if(boxesOnWall > targetsOnWall){
                return true;
            }

        }

        for(int col = 1; col < width - 1; col++){
            int boxeOnWall = 0, targetsOnWall = 0;
            for(int row = 1; row < height - 1; row++){
                if(displayBoard[row][col - 1] == WALL || displayBoard[row][col + 1] == WALL){
                    if(hasBox[row][col]){
                        boxeOnWall++;
                    }
                    if(isTarget[row][col]){
                        targetsOnWall++;
                    }
                }
            }
            if(boxeOnWall > targetsOnWall){
                return true;
            }
        }

        return false;
    }

    private void placeBoxes() {
        int placedBoxes = 0;
        Random rand = new Random();
        int attempts = 0;
        int maxAttempts = 10000;

        while (placedBoxes < this.boxesCount && attempts++ < maxAttempts) {
            int x = rand.nextInt(this.width - 2) + 1;
            int y = rand.nextInt(this.height - 2) + 1;

            if (playerPosition.equals(new Point(x, y))) continue;
            if (isTarget[y][x]) continue;
            if (hasBox[y][x]) continue;
            if (isNonTargetCorner(y, x)) continue;

            hasBox[y][x] = true;
            displayBoard[y][x] = BOX;

            if (violatesWallCapacity() || hasSolidBlock()) {
                hasBox[y][x] = false;
                displayBoard[y][x] = EMPTY;
                continue;
            }

            placedBoxes++;
        }

        if (placedBoxes < this.boxesCount) {
            throw new IllegalStateException("Failed to place all boxes (board too small)");
        }

        this.boxesOnTargets = 0;
    }

    private void placeTargets() {
        int placedTargets = 0;
        Random rand = new Random();
        int attempts = 0;
        int maxAttempts = 10000;

        while (placedTargets < this.boxesCount && attempts++ < maxAttempts) {
            int x = rand.nextInt(this.width - 2) + 1;
            int y = rand.nextInt(this.height - 2) + 1;

            if (isTarget[y][x]) continue;
            if (playerPosition.equals(new Point(x, y))) continue;
            if (hasBox[y][x]) continue;

            isTarget[y][x] = true;
            displayBoard[y][x] = TARGET;

            if (hasSolidBlock()) {
                isTarget[y][x] = false;
                displayBoard[y][x] = EMPTY;
                continue;
            }

            placedTargets++;
        }

        if (placedTargets < this.boxesCount) {
            throw new IllegalStateException("Failed to place all targets (board too small)");
        }
    }

    public boolean undo() {
        if (undoCounter >= MAX_UNDO_COUNT) {
            System.out.println("No undos left");
            return false;
        }

        if (history.isEmpty()) {
            System.out.println("No moves to undo");
            return false;
        }


        GameState previous = history.pop();
        this.playerPosition = new Point(previous.playerPosition);
        for (int i = 0; i < hasBox.length; i++) {
            this.hasBox[i] = previous.hasBox[i].clone();
        }

        this.boxesOnTargets = previous.boxesOnTargets;
        this.moves = Math.max(0, this.moves - 1);
        this.undoCounter++;
        updateDisplay();

        return true;
    }

    private boolean tryMoveBox(Point boxPos, Point delta){
        Point nextPos = new Point(boxPos.x + delta.x, boxPos.y + delta.y);

        if(displayBoard[nextPos.y][nextPos.x] == WALL) {
            return false;
        }

        if(hasBox[nextPos.y][nextPos.x]) {
            if(!tryMoveBox(nextPos, delta)) {
                return false;
            }
        }

        boolean wasOnTarget = isTarget[boxPos.y][boxPos.x];
        boolean willBeOnTarget = isTarget[nextPos.y][nextPos.x];

        hasBox[nextPos.y][nextPos.x] = true;
        hasBox[boxPos.y][boxPos.x] = false;

        if (wasOnTarget && !willBeOnTarget) {
            boxesOnTargets--;
        } else if (!wasOnTarget && willBeOnTarget) {
            boxesOnTargets++;
        }

        return true;
    }

    public boolean move(String direction) {
        Point newPosition = new Point(playerPosition.x, playerPosition.y);
        Point deltaPoint = new Point(0, 0);

        switch (direction) {
            case "w": deltaPoint.y--; break;
            case "s": deltaPoint.y++; break;
            case "d": deltaPoint.x++; break;
            case "a": deltaPoint.x--; break;
            default: return false;
        }

        history.push(new GameState(this.playerPosition, this.hasBox, this.boxesOnTargets));

        newPosition.x += deltaPoint.x;
        newPosition.y += deltaPoint.y;

        if (displayBoard[newPosition.y][newPosition.x] == WALL) {
            return false;
        }

        if (hasBox[newPosition.y][newPosition.x]) {
            if (!tryMoveBox(newPosition, deltaPoint)) {
                return false;
            }
        }

        playerPosition = newPosition;
        moves++;
        updateDisplay();
        return true;
    }

    private void updateDisplay() {
        for(int row = 1; row < this.height - 1; row++){
            for(int col = 1; col < this.width - 1; col++){
                if(!hasBox[row][col] && !isTarget[row][col]){
                    displayBoard[row][col] = EMPTY;
                } else if (isTarget[row][col] && !hasBox[row][col]) {
                    displayBoard[row][col] = TARGET;
                } else if (hasBox[row][col] && !isTarget[row][col]) {
                    displayBoard[row][col] = BOX;
                } else if (hasBox[row][col] && isTarget[row][col]) {
                    displayBoard[row][col] = BOX_ON_TARGET;
                }
            }
        }

        displayBoard[playerPosition.y][playerPosition.x] = PLAYER;
    }

    public boolean checkWin() {
        return this.boxesOnTargets >= this.boxesCount;
    }

    private boolean deadlockDetected(){
        return hasSingleCornerDeadlock() || hasSolidBlock() || violatesWallCapacity();
    }

    private boolean checkGameOver() {
        return this.deadlockDetected() && undoCounter == MAX_UNDO_COUNT;
    }

    static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void printBoard() {
        for (int row = 0; row < height; row++) {
            System.out.println(new String(displayBoard[row]));
        }
        System.out.println("Undos left: " + (MAX_UNDO_COUNT - this.undoCounter));
        if(this.deadlockDetected()) {
            System.out.println("Deadlock Detected (Hint: Use Undo)");
        }
    }

    public void debugPrintBoxes(){
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(hasBox[row][col] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    public void debugPrintTargets(){
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(isTarget[row][col] ? "1 " : "0 ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        try (Scanner keyboardInput = new Scanner(System.in)) {
            clearConsole();
            System.out.print("Enter board width (>=5): ");
            int width = keyboardInput.nextInt();

            System.out.print("Enter board height (>=5): ");
            int height = keyboardInput.nextInt();

            System.out.print("Enter k (number of boxes and targets): ");
            int k = keyboardInput.nextInt();
            keyboardInput.nextLine();

            try {
                SokobanGame game = new SokobanGame();
                game.initializeGame(width, height, k);
                System.out.println("Game initialized successfully!");
                game.printBoard();

                while (!game.checkWin() && !game.checkGameOver()) {
                    String input = keyboardInput.nextLine();

                    if (input.equals("q")) {
                        System.out.println("Quitting");
                        break;
                    }
                    if (input.equals("u")) {
                        game.undo();
                        game.clearConsole();
                        game.printBoard();
                        continue;
                    }

                    boolean moved = game.move(input);
                    if (!moved) {
                        System.out.println("Invalid move");
                    } else {
                        game.clearConsole();
                        game.printBoard();
                    }
                }

                if (game.checkWin()) {
                    System.out.println("""
                    ██    ██  ██████  ██    ██     ██     ██  ██████  ███    ██ ██ 
                     ██  ██  ██    ██ ██    ██     ██     ██ ██    ██ ████   ██ ██ 
                      ████   ██    ██ ██    ██     ██  █  ██ ██    ██ ██ ██  ██ ██ 
                       ██    ██    ██ ██    ██     ██ ███ ██ ██    ██ ██  ██ ██    
                       ██     ██████   ██████       ███ ███   ██████  ██   ████ ██ 
                                                               
                    """);
                    System.out.println("In " + game.moves + " moves");
                }

                if (game.checkGameOver()) {
                    System.out.println("""
                    ██    ██  ██████  ██    ██     ██       ██████  ███████ ████████ ██     
                     ██  ██  ██    ██ ██    ██     ██      ██    ██ ██         ██    ██     
                      ████   ██    ██ ██    ██     ██      ██    ██ ███████    ██    ██     
                       ██    ██    ██ ██    ██     ██      ██    ██      ██    ██           
                       ██     ██████   ██████      ███████  ██████  ███████    ██    ██                                                          
                    """);
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid parameters: " + e.getMessage());
            }
        }
    }




}
