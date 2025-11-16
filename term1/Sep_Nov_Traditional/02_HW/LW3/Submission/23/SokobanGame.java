import java.util.Scanner;

//do predi malko raboteshe sig ima problem s placetarget debugni go i vij
public class SokobanGame {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    // Game state
    private final Scanner scanner = new Scanner(System.in);
    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets = 1;

    // Game board arrays
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;

    // Core methods to implement
    private void initializeGame() {
        do{
            System.out.println("Whats the width of the map? (minimum 5)");
            width = scanner.nextInt();
        }while(width < 5);

        do{
            System.out.println("Whats the height of the map (minimum 5)");
            height = scanner.nextInt();
        }while(height < 5);

        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];

        do
        {
            System.out.println("How many boxes are there?");
            boxesCount = scanner.nextInt();
            if(boxesCount == 1) boxesOnTargets = 0;
        }
        while(boxesCount > ((width-2)*(height-2) - 2) / 2);
        //make map
        for(int i = 0;i < width;i++){
            displayBoard[0][i] = WALL;
            displayBoard[height - 1][i] = WALL;
        }
        for(int i = 0;i < height;i++){
            displayBoard[i][0] = WALL;
            displayBoard[i][width - 1] = WALL;
        }
        emptyMap(displayBoard, width, height);
        placePlayer();
        placeTargets();
        placeBoxes();
        S.push(displayBoard);
    }

    private void placePlayer() {
        playerY = height/2;
        playerX = width/2;
        displayBoard[playerY][playerX] = PLAYER;
    }
    private void emptyMap(char[][] arr,int width, int height){
        for(int i = 0;i < height;i++){
            for(int j = 0;j < width;j++){
                if(arr[i][j] != WALL){
                    arr[i][j] = EMPTY;
                }
            }
        }
    }
    private void placeTargets() {
        for(int i = 0;i < boxesCount;){
            int randomX = (int)(Math.random() * width);
            int randomY = (int)(Math.random() * height);
            if(!(displayBoard[randomY][randomX] == WALL) && !isTarget[randomY][randomX])// ne raboti?/raboti
            {
                if(displayBoard[randomY][randomX] != PLAYER){
                    displayBoard[randomY][randomX] = TARGET;
                }
                isTarget[randomY][randomX] = true;
                i++;
            }
        }
    }

    private void placeBoxes() {
        for(int i = 0;i < boxesCount;){
            int randomX = (int)(Math.random() * width);
            int randomY = (int)(Math.random() * height);
            if(CheckRule.checkAll(width, height, randomX, randomY, displayBoard))//R1
            {
                displayBoard[randomY][randomX] = BOX;
                hasBox[randomY][randomX] = true;
                i++;
            }
        }
    }

    private void move(String direction) {
        int y = 0;
        int x = 0;
        switch (direction){
            case "left":
                y = 0;
                x = -1;
                break;
            case "right":
                y = 0;
                x = 1;
                break;
            case "up":
                y = -1;
                x = 0;
                break;
            case "down":
                y = 1;
                x = 0;
                break;
        }

        if(CheckRule.moveNoBox(displayBoard, playerY, playerX, y, x))
        {
            if(!isTarget[playerY][playerX])displayBoard[playerY][playerX] = EMPTY;
            else displayBoard[playerY][playerX] = TARGET;
            playerY += y;
            playerX += x;
            displayBoard[playerY][playerX] = PLAYER;
            S.push(displayBoard);
        }
        else if(CheckRule.moveBox(hasBox, displayBoard, playerY, playerX, y, x))
        {
            if(!isTarget[playerY][playerX])displayBoard[playerY][playerX] = EMPTY;
            else displayBoard[playerY][playerX] = TARGET;
            playerY += y;
            playerX += x;
            displayBoard[playerY][playerX] = PLAYER;
            hasBox[playerY][playerX] = false;
            int boxY = playerY + y;
            int boxX = playerX + x;
            displayBoard[boxY][boxX] = BOX;
            hasBox[boxY][boxX] = true;
            if (isTarget[playerY + y][playerX + x] ^ isTarget[playerY + 2 * y][playerX + 2 * x]) {
                if (isTarget[boxY][boxX]) {
                    boxesOnTargets++;
                    displayBoard[boxY][boxX] = BOX_ON_TARGET;
                }
                else {
                    boxesOnTargets--;
                    displayBoard[boxY][boxX] = BOX;
                }
            }
            S.push(displayBoard);
        }
        else System.out.println("Invalid move");
    }

    private boolean checkWin() {
        return boxesOnTargets == boxesCount;
    }

    private String readCommand() throws CommandException, BoardException {
        System.out.println("Write command a/s/d/w: ");
        String ch = scanner.next();
        if(ch.equals("check")){
            if(CheckRule.checkFourBoxOrWall(displayBoard, width, height)){
                throw new BoardException("The displayed level cant be solved");
            };
        }

        switch (ch){
            case "a":
                return "left";
            case "s":
                return "down";
            case "d":
                return "right";
            case "w":
                return "up";
            case "q":
                return "quit";
            case "u":
                return "undo";
            case "check":
                return "check";
            default:
                throw new CommandException("WRONG COMMAND");
        }
    };
    private void printBoard() {
        for(int i = 0;i < height;i++){
            for(int j = 0;j < width;j++){
                System.out.print(displayBoard[i][j]);
            }
            System.out.println();
        }
    };


    private void playGame() throws CommandException, BoardException {
        int count = 0;
        initializeGame();
        System.out.println("Game starts...");
        while (!checkWin()) {
            printBoard();
            //System.out.println(STR."Current move: \{count}");
            String command = scanner.nextLine();
            do{
                command = readCommand();
            }while(command.equals("wrong"));
            if(command.equals("check")) continue;
            if (command.equals("quit")) return;
            if(!command.equals("undo")){
                move(command);
                count++;
            }
            else{
                if(count != 0)count--;
                displayBoard = S.undo();
            }//
        }
        printBoard();
        //System.out.println(STR."You win in \{count} moves!");
    }

    public void run() throws CommandException, BoardException {
        playGame();
    }
}


class CommandException extends Exception{
    public CommandException(String message) {
        super(message);
    }
}

class BoardException extends Exception{
    public BoardException(String message) {
        super(message);
    }
}