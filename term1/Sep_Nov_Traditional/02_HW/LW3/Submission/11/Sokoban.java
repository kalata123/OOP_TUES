import java.util.Random;
import java.util.Scanner;

class TimeOutLimitExceeded extends Exception {
    public TimeOutLimitExceeded(String message) {
        super(message);
    }
}

class InvalidBoxMove extends Exception {
    public InvalidBoxMove(String message) {
        super(message);
    }
}

class InvalidPlayerMove extends Exception {
    public InvalidPlayerMove(String message) {
        super(message);
    }
}

public class Sokoban {
    // Consts for the different symbols on the board
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char TARGET = '*';
    static final char BOX = 'B';
    static final char BOX_ON_TARGET = 'O';
    static final char PLAYER = '@';

    // Player movement and quit consts
    static final char UP = 'w';
    static final char DOWN = 's';
    static final char LEFT = 'a';
    static final char RIGHT = 'd';
    static final char QUIT = 'q';

    // Other
    static final int TIMEOUTLIMIT = 10000;
    static final int GENERATEBOARDATTEMPTS = 5;

    // Info about the game board
    private char[][] board;
    private int rows = 0, columns = 0, numBoxes, boxesOnTargets = 0;

    // Info about the player
    private char underPlayer = EMPTY;
    private int playerRow, playerColumn, numMoves = 0;

    public static void main(String[] args) {
        // Creating a Scanner to read input from console
        Scanner input = new Scanner(System.in);

        Sokoban game = new Sokoban();

        // Creating a board
        game.createBoard(input);
        game.placePlayer();

        // Generates a board
        if (game.generateBoardCorrectly()) {
            game.startGame(input);
        }
    }

    // If it fails GENERATEBOARDATTEMPTS times the program ends
    public boolean generateBoardCorrectly() {
        for (int i = 0; i < GENERATEBOARDATTEMPTS; i++) {
            try {
                generateBoard();    // If the func trows an exception it skips straight to catch and doesnt return
                return true;
            } catch (TimeOutLimitExceeded e) {
                System.out.println(e.getMessage());
                System.out.println("Trying again...");

                // Clears the board
                setUpBoard();
            }
        }
        System.out.println("Generation attempts exceeded. Quitting...");
        // System.exit(0);
        return false;
    }

    public void startGame(Scanner input) {
        char move;
        while (true) {
            printBoard();
            System.out.print("Enter move " + (numMoves + 1) + ": ");
            move = input.next().charAt(0);

            if (move == QUIT) break;

            try {
                movePlayer(move);
            } catch (InvalidPlayerMove e) {
                System.out.println(e.getMessage());
            }

            if(checkWin()) {
                printBoard();

                System.out.println("""
                          __   __   U  ___ u   _   _                     U  ___ u  _   _     _   \s
                          \\ \\ / /    \\/"_ \\/U |"|u| |     __        __    \\/"_ \\/ | \\ |"|  U|"|u \s
                           \\ V /     | | | | \\| |\\| |     \\"\\      /"/    | | | |<|  \\| |> \\| |/ \s
                          U_|"|_u.-,_| |_| |  | |_| |     /\\ \\ /\\ / /\\.-,_| |_| |U| |\\  |u  |_|  \s
                            |_|   \\_)-\\___/  <<\\___/     U  \\ V  V /  U\\_)-\\___/  |_| \\_|   (_)  \s
                        .-,//|(_       \\\\   (__) )(      .-,_\\ /\\ /_,-.     \\\\    ||   \\\\,-.|||_ \s
                         \\_) (__)     (__)      (__)      \\_)-'  '-(_/     (__)   (_")  (_/(__)_)\s""");

//                System.out.println("                                                                                                                                                             \n" +
//                        "                                                                                                                                                             \n" +
//                        "YYYYYYY       YYYYYYY     OOOOOOOOO     UUUUUUUU     UUUUUUUU     WWWWWWWW                           WWWWWWWW     OOOOOOOOO     NNNNNNNN        NNNNNNNN !!! \n" +
//                        "Y:::::Y       Y:::::Y   OO:::::::::OO   U::::::U     U::::::U     W::::::W                           W::::::W   OO:::::::::OO   N:::::::N       N::::::N!!:!!\n" +
//                        "Y:::::Y       Y:::::Y OO:::::::::::::OO U::::::U     U::::::U     W::::::W                           W::::::W OO:::::::::::::OO N::::::::N      N::::::N!:::!\n" +
//                        "Y::::::Y     Y::::::YO:::::::OOO:::::::OUU:::::U     U:::::UU     W::::::W                           W::::::WO:::::::OOO:::::::ON:::::::::N     N::::::N!:::!\n" +
//                        "YYY:::::Y   Y:::::YYYO::::::O   O::::::O U:::::U     U:::::U       W:::::W           WWWWW           W:::::W O::::::O   O::::::ON::::::::::N    N::::::N!:::!\n" +
//                        "   Y:::::Y Y:::::Y   O:::::O     O:::::O U:::::D     D:::::U        W:::::W         W:::::W         W:::::W  O:::::O     O:::::ON:::::::::::N   N::::::N!:::!\n" +
//                        "    Y:::::Y:::::Y    O:::::O     O:::::O U:::::D     D:::::U         W:::::W       W:::::::W       W:::::W   O:::::O     O:::::ON:::::::N::::N  N::::::N!:::!\n" +
//                        "     Y:::::::::Y     O:::::O     O:::::O U:::::D     D:::::U          W:::::W     W:::::::::W     W:::::W    O:::::O     O:::::ON::::::N N::::N N::::::N!:::!\n" +
//                        "      Y:::::::Y      O:::::O     O:::::O U:::::D     D:::::U           W:::::W   W:::::W:::::W   W:::::W     O:::::O     O:::::ON::::::N  N::::N:::::::N!:::!\n" +
//                        "       Y:::::Y       O:::::O     O:::::O U:::::D     D:::::U            W:::::W W:::::W W:::::W W:::::W      O:::::O     O:::::ON::::::N   N:::::::::::N!:::!\n" +
//                        "       Y:::::Y       O:::::O     O:::::O U:::::D     D:::::U             W:::::W:::::W   W:::::W:::::W       O:::::O     O:::::ON::::::N    N::::::::::N!!:!!\n" +
//                        "       Y:::::Y       O::::::O   O::::::O U::::::U   U::::::U              W:::::::::W     W:::::::::W        O::::::O   O::::::ON::::::N     N:::::::::N !!! \n" +
//                        "       Y:::::Y       O:::::::OOO:::::::O U:::::::UUU:::::::U               W:::::::W       W:::::::W         O:::::::OOO:::::::ON::::::N      N::::::::N     \n" +
//                        "    YYYY:::::YYYY     OO:::::::::::::OO   UU:::::::::::::UU                 W:::::W         W:::::W           OO:::::::::::::OO N::::::N       N:::::::N !!! \n" +
//                        "    Y:::::::::::Y       OO:::::::::OO       UU:::::::::UU                    W:::W           W:::W              OO:::::::::OO   N::::::N        N::::::N!!:!!\n" +
//                        "    YYYYYYYYYYYYY         OOOOOOOOO           UUUUUUUUU                       WWW             WWW                 OOOOOOOOO     NNNNNNNN         NNNNNNN !!! \n" +
//                        "                                                                                                                                                             ");

                System.out.println("And it only took you " + numMoves + " moves! Good job :D");

                break;
            }
        }
    }

    public int getNumMoves() {
        return numMoves;
    }

    // Reading input - width (columns), height (rows) and number of boxes and set up a board
    public void createBoard(Scanner input) {
        // Validating width (columns), height (rows) and number of boxes
        while (rows < 5) {
            System.out.println("Enter number of rows (rows >= 5): ");
            rows = input.nextInt();
        }

        while (columns < 5) {
            System.out.println("Enter number of columns (columns >= 5): ");
            columns = input.nextInt();
        }

        do {
            System.out.println("Enter number of boxes: ");
            numBoxes = input.nextInt();
        } while (numBoxes > ((rows - 2) * (columns - 2) - 4) / 2 || numBoxes < 1);

        this.setUpBoard();
    }

    // Overloaded function to validate input and set up a board, currently not in use
    public void createBoard(int rows, int columns, int numBoxes) {
        // Validating width (columns), height (rows) and number of boxes
        if (rows < 5) {
            System.out.println("Invalid input. Rows must be at least 5.");
            throw new IllegalArgumentException("Invalid input");
        }

        if (columns < 5) {
            System.out.println("Invalid input. Columns must be at least 5.");
            throw new IllegalArgumentException("Invalid input");
        }

        if (numBoxes > ((rows - 2) * (columns - 2) - 4) / 2 || numBoxes < 1) {
            System.out.println("Invalid input");
            throw new IllegalArgumentException("Invalid input.");
        }

        this.setUpBoard();
    }

    // Set up the basic stuff for the board like the walls and empty spaces
    private void setUpBoard() {
        board = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(i == 0 || i == rows-1) board[i][j] = WALL;
                else if(j == 0 || j == columns-1) board[i][j] = WALL;
                else board[i][j] = EMPTY;
            }
        }
    }

    // Function which sets the initial player position
    private void placePlayer() {
        playerRow = rows/2;
        playerColumn = columns/2;
        board[playerRow][playerColumn] = PLAYER;
    }

    // Prints the board
    public void printBoard() {
        for (char[] row : board) {
            for (char aChar : row) {
                System.out.print(aChar);
            }
            System.out.println();
        }
    }

    // Places boxes and targets
    private void generateBoard() throws TimeOutLimitExceeded {
        int row, col;
        Random random = new Random();
        int timeout = TIMEOUTLIMIT;
        for (int i = 0; i < numBoxes; i++) {
            timeout--;
            if(timeout < 0) {
                throw new TimeOutLimitExceeded("Timeout reached. Failed creating board.");
            }

            row = random.nextInt(1, rows-1);    // From 1 to rows-1 to avoid generating on walls
            col = random.nextInt(1, columns-1); // From 1 to columns-1 to avoid generating on walls

            // Stops boxes from generating on top of something other than EMPTY
            if (board[row][col] != EMPTY) {
                i--;
                continue;
            }

            // Stops boxes from generating in the corners
            if((row == 1 || row == rows - 2) && (col == 1 || col == columns - 2)) {
                i--;
                continue;
            }

            // Wall capacity constraint: Having too many boxes next to the same wall will make it unsolvable
            // -2 for the walls themselves, -2 for the corners where there cant be boxes == -4
            // /2 so there are no boxes next to each other since we can't move two boxes at the same time or pull boxes
            // int allowedBoxesTop = (rows - 4)/2, allowedBoxesLeft = (columns - 4)/2;
            // int allowedBoxesBottom = (rows - 4)/2, allowedBoxesRight = (columns - 4)/2;
            // Above code not needed anymore - 2 boxes on a wall can't generate next to each other, so there will always be enough space for the targets

            // Touching the top wall
            if(row == 1) {
                // Stops in case are next to a box
                if((board[1][col-1] == BOX || board[1][col+1] == BOX)) {
                    i--;
                }
                // This and the next else if are for stopping two boxes from blocking a corner making it unsolvable
                else if(col == 2 && board[2][1] == BOX) {
                    i--;
                }
                else if(col == columns - 3 && board[2][columns - 2] == BOX) {
                    i--;
                }
                else {
                    board[row][col] = BOX;

                    // Generate target that is on the wall for the box
                    do {
                        // row = 1;
                        col = random.nextInt(1, columns - 1);
                    } while (board[row][col] != EMPTY); // Stops target from generating on top of something other than EMPTY
                    board[row][col] = TARGET;
                }

                continue;
            }

            // Touching the bottom wall
            else if(row == rows - 2) {
                // Stops in case are next to a box
                if((board[row][col-1] == BOX || board[row][col+1] == BOX)) {
                    i--;
                }
                // This and the next else if are for stopping two boxes from blocking a corner making it unsolvable
                else if(col == 2 && board[rows - 3][1] == BOX) {
                    i--;
                }
                else if(col == columns - 3 && board[rows - 3][columns - 2] == BOX) {
                    i--;
                }
                else {
                    board[row][col] = BOX;

                    // Generate target that is on the wall for the box
                    do {
                        // row = rows - 2;
                        col = random.nextInt(1, columns - 1);
                    } while (board[row][col] != EMPTY); // Stops target from generating on top of something other than EMPTY
                    board[row][col] = TARGET;
                }

                continue;
            }

            // Touching the left wall
            else if(col == 1) {
                // Stops in case are next to a box
                if((board[row-1][col] == BOX || board[row+1][col] == BOX)) {
                    i--;
                }
                // This and the next else if are for stopping two boxes from blocking a corner making it unsolvable
                else if(row == 2 && board[1][2] == BOX) {
                    i--;
                }
                else if(row == rows - 3 && board[rows - 2][2] == BOX) {
                    i--;
                }
                else {
                    board[row][col] = BOX;

                    // Generate target that is on the wall for the box
                    do {
                        row = random.nextInt(1, rows-1);
                        // col = 1;
                    } while (board[row][col] != EMPTY); // Stops target from generating on top of something other than EMPTY
                    board[row][col] = TARGET;
                }

                continue;
            }

            // Touching the right wall
            else if(col == columns - 2) {
                // Stops in case are next to a box
                if((board[row-1][col] == BOX || board[row+1][col] == BOX)) {
                    i--;
                }
                // This and the next else if are for stopping two boxes from blocking a corner making it unsolvable
                else if(row == 2 && board[1][columns - 3] == BOX) {
                    i--;
                }
                else if(row == rows - 3 && board[rows - 2][columns - 3] == BOX) {
                    i--;
                }
                else {
                    board[row][col] = BOX;

                    // Generate target that is on the wall for the box
                    do {
                        row = random.nextInt(1, rows-1);
                        // col = columns - 2;
                    } while (board[row][col] != EMPTY); // Stops target from generating on top of something other than EMPTY
                    board[row][col] = TARGET;
                }

                continue;
            }

            // Stops boxes from generating in a 2x2 square
            else{
                // There is a box above the new one
                if(board[row-1][col] == BOX)
                {
                    // There is a box to the left of the new one AND There is a box in the corner
                    // OR
                    // There is a box to the right of the new one AND There is a box in the corner
                    if( (board[row][col-1] == BOX && board[row-1][col-1] == BOX)
                            || (board[row][col+1] == BOX && board[row-1][col+1] == BOX) )
                    {
                        i--;
                        continue;
                    }
                }
                // There is a box below the new one
                else if(board[row+1][col] == BOX)
                {
                    // There is a box to the left of the new one AND There is a box in the corner
                    // OR
                    // There is a box to the right of the new one AND There is a box in the corner
                    if( (board[row][col-1] == BOX && board[row+1][col-1] == BOX)
                            || (board[row][col+1] == BOX && board[row+1][col+1] == BOX) )
                    {
                        i--;
                        continue;
                    }
                }
                // continue;
            }

            board[row][col] = BOX;

            // Generating a target
            do {
                row = random.nextInt(1, rows-1);
                col = random.nextInt(1, columns-1);
            } while(board[row][col] != EMPTY); // Stops targets from generating on top of something other than EMPTY
            board[row][col] = TARGET;
        }
    }

    // Handles all player movement
    public void movePlayer(char move) throws InvalidPlayerMove {
        int row_offset = 0, col_offset = 0;
        switch(move) {
            case UP: row_offset = -1; break;
            case DOWN: row_offset = 1; break;
            case LEFT: col_offset = -1; break;
            case RIGHT: col_offset = 1; break;
            default: throw new InvalidPlayerMove("Invalid move");
        }

        int newRow = playerRow + row_offset, newCol = playerColumn + col_offset;

        if (board[newRow][newCol] == WALL) {
            throw new InvalidPlayerMove("Invalid move");
        }
        if (board[newRow][newCol] == BOX || board[newRow][newCol] == BOX_ON_TARGET)
        {
            try{
                this.moveBox(move, newRow, newCol);
            } catch(InvalidBoxMove e){
                // System.out.println(e.getMessage());
                throw new InvalidPlayerMove(e.getMessage());
            }
        }

        board[playerRow][playerColumn] = underPlayer;
        underPlayer = board[newRow][newCol];
        board[newRow][newCol] = PLAYER;

        playerRow = newRow;
        playerColumn = newCol;
        numMoves++;
    }

    // Handles all box movement (box on target included)
    private void moveBox(char move, int boxRow, int boxColumn) throws InvalidBoxMove {
        int row_offset = 0, col_offset = 0;
        switch(move) {
            case UP: row_offset = -1; break;
            case DOWN: row_offset = 1; break;
            case LEFT: col_offset = -1; break;
            case RIGHT: col_offset = 1; break;
            default: throw new InvalidBoxMove("Invalid move");
        }

        int newRow = boxRow + row_offset, newCol = boxColumn + col_offset;

        if(board[newRow][newCol] == WALL || board[newRow][newCol] == BOX || board[newRow][newCol] == BOX_ON_TARGET) {
            throw new InvalidBoxMove("Invalid move.");
        }
        else if(board[newRow][newCol] == EMPTY || board[newRow][newCol] == TARGET) {

            if(board[boxRow][boxColumn] == BOX) {
                board[boxRow][boxColumn] = EMPTY;
            }
            else {
                board[boxRow][boxColumn] = TARGET;
                boxesOnTargets--;
            }
        }

        if(board[newRow][newCol] == EMPTY)
        {
            board[newRow][newCol] = BOX;
        }
        else {
            board[newRow][newCol] = BOX_ON_TARGET;
            boxesOnTargets++;
        }
    }

    // Function to check if you won. IDK why I even made a separate function for it
    public boolean checkWin() {
        return numBoxes == boxesOnTargets;
    }

}