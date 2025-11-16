import java.lang.annotation.Target;
import java.util.Random;
import java.util.Vector;

public class GameState
{
    int cols;
    int rows;
    char [][] board;
    int numbOfBoxes;
    int[][] BoxesCoordinates;
    int[] playerCoordinates;
    int numbOfTargets;
    int[][] targetCoordinates;
    char squareUnderPlayer;
    int boxesOnTarget;
    int numbOfMoves;
    Vector<int[][]> playerMoveLog;
    Vector<int[][]> boxesMoveLog;
    Vector<Integer> boxesMovedOnTurn;



    GameState(int rows, int cols, int K)
    {
        if(rows < 5)
        {
            throw new IllegalArgumentException("rows must be greater than or equal to 5!");
        }
        this.rows = rows;
        if(cols < 5)
        {
            throw new IllegalArgumentException("cols must be greater than or equal to 5!");
        }
        this.cols = cols;
        if(K > ((this.rows - 2) * (this.cols - 2) - 2) / 2)
        {
            throw new IllegalArgumentException("K must be smaller or equal to ((Width-2)*(Height-2) - 2) / 2");
        }
        this.numbOfBoxes = K;
        this.numbOfTargets = K;
        this.boxesOnTarget = 0;
        this.BoxesCoordinates = new int[this.numbOfBoxes][2];
        this.targetCoordinates = new int[this.numbOfTargets][2];
        this.playerCoordinates = new int[2];
        this.board = generateBoard();
        this.numbOfMoves = 0;
        this.playerMoveLog = new Vector<>();
        this.boxesMoveLog = new Vector<>();
        this.boxesMovedOnTurn = new Vector<>();
    }

    GameState(String[] board)
    {
        char[][] boardCopy = new char[board.length][];
        for(int i = 0; i < board.length; i++)
        {
            boardCopy[i] = board[i].toCharArray();
        }
        int rows = board.length;
        int cols = board[0].length();
        if(!isBoardValid(boardCopy, rows, cols))
        {
            throw new IllegalArgumentException("Board is not valid!");
        }
        this.rows = rows;
        this.cols = cols;
        this.board = boardCopy;
        this.numbOfBoxes = getNumbOfBoxes(boardCopy, rows, cols);
        this.numbOfTargets = getNumbOfTargets(boardCopy, rows, cols);
        this.playerCoordinates = getPlayerCoordinates(boardCopy, rows, cols);
        this.BoxesCoordinates = getBoxesCoordinates(boardCopy, rows, cols);
        this.targetCoordinates = getTargetsCoordinates(boardCopy, rows, cols);
        this.boxesOnTarget = 0;
        this.numbOfMoves = 0;
        this.playerMoveLog = new Vector<>();
        this.boxesMoveLog = new Vector<>();
        this.boxesMovedOnTurn = new Vector<>();
        this.squareUnderPlayer = ' ';


    }

    GameState(GameState other)
    {
        this.rows = other.rows;
        this.cols = other.cols;
        this.board = new char[rows][cols];
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                this.board[i][j] = other.board[i][j];
            }
        }
        this.numbOfBoxes = other.numbOfBoxes;
        this.numbOfTargets = other.numbOfTargets;
        this.playerCoordinates = new int[2];
        this.playerCoordinates[0] = other.playerCoordinates[0];
        this.playerCoordinates[1] = other.playerCoordinates[1];
        this.BoxesCoordinates = new int[this.numbOfBoxes][2];
        for(int i = 0; i < this.numbOfBoxes; i++)
        {
            this.BoxesCoordinates[i][0] = other.BoxesCoordinates[i][0];
            this.BoxesCoordinates[i][1] = other.BoxesCoordinates[i][1];
        }
        this.targetCoordinates = new int[this.numbOfTargets][2];
        for(int i = 0; i < this.numbOfTargets; i++)
        {
            this.targetCoordinates[i][0] = other.targetCoordinates[i][0];
            this.targetCoordinates[i][1] = other.targetCoordinates[i][1];
        }
        this.boxesOnTarget = other.boxesOnTarget;
        this.numbOfMoves = other.numbOfMoves;
        this.playerMoveLog = new Vector<>();
        for (int[][] move : other.playerMoveLog) {
            int[][] copiedMove = new int[move.length][];
            for (int i = 0; i < move.length; i++) {
                copiedMove[i] = move[i].clone();
            }
            this.playerMoveLog.add(copiedMove);
        }
        this.boxesMoveLog = new Vector<>();
        for (int[][] move : other.boxesMoveLog) {
            int[][] copiedMove = new int[move.length][];
            for (int i = 0; i < move.length; i++) {
                copiedMove[i] = move[i].clone();
            }
            this.boxesMoveLog.add(copiedMove);
        }
        this.boxesMovedOnTurn = new Vector<>();
        for(Integer boxes : other.boxesMovedOnTurn)
        {
            this.boxesMovedOnTurn.add(boxes);
        }
        this.squareUnderPlayer = other.squareUnderPlayer;
    }

    boolean isBoardValid(char[][] board, int rows, int cols)
    {
        if(!this.AreWallsRim(board, rows, cols))
        {
            System.out.println("the walls must be #");
            return false;
        }
        int numbOfBoxes = getNumbOfBoxes(board, rows, cols);
        int numbOfTargets = getNumbOfTargets(board, rows, cols);

        if(numbOfBoxes != numbOfTargets || numbOfBoxes == 0)
        {
            System.out.println("Number of boxes must be equal to number of targets and different from 0");
            return false;
        }
        if(numbOfBoxes > ((rows - 2) * (cols - 2) - 2) / 2)
        {
            System.out.println();
            System.out.println("Number of boxes must  be smaller or equal to ((Width-2)*(Height-2) - 2) / 2");
            return false;
        }
        if(board[1][cols - 2] == 'B' || board[rows-2][cols - 2] == 'B' || board[1][1] == 'B' || board[rows-2][1] == 'B')
        {
            System.out.println();
            System.out.println("Boxes must not be on the corner");
            return false;
        }
        if((this.numbOfSymbolOnTheWall(board, 1, -1, '*') > this.numbOfSymbolOnTheWall(board, 1, -1, 'B')) ||
           (this.numbOfSymbolOnTheWall(board, -1, 1, '*') > this.numbOfSymbolOnTheWall(board, -1, 1, 'B')) ||
           (this.numbOfSymbolOnTheWall(board, rows-2, -1, '*') > this.numbOfSymbolOnTheWall(board, rows-2, -1, 'B')) ||
           (this.numbOfSymbolOnTheWall(board, -1, cols-2, '*') > this.numbOfSymbolOnTheWall(board, -1, cols-2, 'B')))
        {
            System.out.println("The number of boxes on the wall must be equal to number of targets on the same wall");
            return false;
        }
        return true;
    }

    boolean isCoordinateInArray(int[] coordinates, int[][] arr)
    {
        for(int i = 0; i < arr.length; i++)
        {
            if(arr[i][0] == coordinates[0] && arr[i][1] == coordinates[1]) {
                return true;
            }
        }
        return false;

    }

    char[][] generateBoard()
    {

        char[][] board = new char[this.rows][this.cols];
        for(int i = 0; i < this.rows; i++)
        {
            for(int j = 0; j < this.cols; j++)
            {
                board[i][j] = ' ';
            }
        }
        for(int i = 0; i < this.rows; i++)
        {
            board[i][0] = '#';
            board[i][this.cols - 1] = '#';
        }
        for(int i = 0; i < this.cols; i++)
        {
            board[0][i] = '#';
            board[this.rows - 1][i] = '#';
        }
        board[this.rows / 2][this.cols / 2] = '@';
        this.playerCoordinates[0] = this.rows / 2;
        this.playerCoordinates[1] = this.cols / 2;
        this.squareUnderPlayer = ' ';
        Random rand = new Random();
        while(true)
        {
            int attemts = 0;
            for(int i = 0; i < this.numbOfTargets; i++)
            {
                while(true)
                {
                    int targetCoordinateRow = rand.nextInt(this.rows - 2);
                    int targetCoordinateCol = rand.nextInt(this.cols - 2);
                    int[] targetCoordinate =  {targetCoordinateRow, targetCoordinateCol};
                    if (targetCoordinateRow == 0 || targetCoordinateCol == 0)
                        continue;
                    if (this.playerCoordinates[0] == targetCoordinateRow && this.playerCoordinates[1] == targetCoordinateCol)
                        continue;
                    if(isCoordinateInArray(targetCoordinate, this.targetCoordinates))
                        continue;
                    this.targetCoordinates[i] = targetCoordinate;
                    break;
                }
                board[this.targetCoordinates[i][0]][this.targetCoordinates[i][1]] = '*';
            }
            for(int i = 0; i < this.numbOfTargets; i++)
            {
                while(true)
                {
                    if(attemts > 1000)
                    {
                        break;
                    }
                    int boxCoordinateRow = rand.nextInt(this.rows - 2);
                    int boxCoordinateCol = rand.nextInt(this.cols - 2);
                    int[] boxCoordinate = {boxCoordinateRow, boxCoordinateCol};
                    if (boxCoordinateRow == 0 || boxCoordinateCol == 0)
                    {
                        continue;
                    }
                    if (this.playerCoordinates[0] == boxCoordinateRow && this.playerCoordinates[1] == boxCoordinateCol)
                    {

                        continue;
                    }
                    if (isCoordinateInArray(boxCoordinate, this.BoxesCoordinates) ||
                            isCoordinateInArray(boxCoordinate, this.targetCoordinates))
                    {

                        continue;
                    }
                    if((boxCoordinateRow == 1 && boxCoordinateCol == 1) ||
                            (boxCoordinateRow == 1 && boxCoordinateCol == this.cols - 2) ||
                            (boxCoordinateRow == this.rows - 2 && boxCoordinateCol == 1) ||
                            (boxCoordinateRow == this.rows - 2 && boxCoordinateCol == this.cols - 2)
                    )
                    {
                        attemts++;
                        continue;
                    }

                    if(((boxCoordinateRow == 1 || boxCoordinateRow == this.rows - 2) && (numbOfSymbolOnTheWall(board, boxCoordinateRow, -1, 'B') == numbOfSymbolOnTheWall(board, boxCoordinateRow, -1, '*'))) ||
                       ((boxCoordinateCol == 1 || boxCoordinateCol == this.cols - 2) && (numbOfSymbolOnTheWall(board, -1, boxCoordinateCol, 'B') == numbOfSymbolOnTheWall(board, -1, boxCoordinateCol, '*')))
                    ) {
                        attemts++;
                        continue;
                    }

                    this.BoxesCoordinates[i] = boxCoordinate;
                    break;
                }
                if(attemts > 1000)
                {
                    break;
                }
                board[this.BoxesCoordinates[i][0]][this.BoxesCoordinates[i][1]] = 'B';
            }
            if(attemts < 1000)
            {
                break;
            }
            clearSymbolsOnBoard(board, this.rows, this.cols, '*');
            clearSymbolsOnBoard(board, this.rows, this.cols, 'B');
            this.BoxesCoordinates = new int[this.numbOfBoxes][2];
            this.targetCoordinates = new int[this.numbOfTargets][2];
        }

        return board;
    }

    void clearSymbolsOnBoard(char[][] board, int rows, int cols , char symbol)
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                if(board[i][j] == symbol)
                {
                    board[i][j] = ' ';
                }
            }
        }
    }


    int numbOfSymbolOnTheWall(char[][] board, int row, int col, char symbol)
    {
        int symbolCount = 0;
        if(col == -1)
        {
            for(int i = 0; i < this.cols; i++)
            {
                if(board[row][i] == symbol)
                {
                    symbolCount++;
                }
            }
        }
        if(row == -1)
        {
            for(int i = 0; i < this.rows; i++)
            {
                if(board[i][col] == symbol)
                {
                    symbolCount++;
                }
            }
        }
        return symbolCount;
    }

    boolean isWinning()
    {
        if(this.boxesOnTarget == this.numbOfTargets)
        {
            return true;
        }
        return false;
    }

    boolean AreWallsRim(char[][] board, int rows, int cols)
    {
        for(int i = 0; i < rows; i++)
        {
            if(board[i][0] == ' ' || board[i][cols - 1] == ' ')
            {
                return false;
            }
        }
        for(int j = 0; j < cols; j++)
        {
            if(board[0][j] == ' ' || board[rows - 1][j] == ' ')
            {
                return false;
            }
        }
        return true;
    }

    void move(char direction)
    {
        int[] endPos = {0, 0};
        int[][] playerMove = new int[2][2];
        int[] startSquare = {this.playerCoordinates[0], this.playerCoordinates[1]};
        this.boxesMovedOnTurn.add(0);
        if(direction == 'U')
        {
            endPos[0] = this.playerCoordinates[0] - 1;
            endPos[1] = this.playerCoordinates[1];
        }
        if(direction == 'D')
        {
            endPos[0] = this.playerCoordinates[0] + 1;
            endPos[1] = this.playerCoordinates[1];
        }
        if(direction == 'L')
        {
            endPos[0] = this.playerCoordinates[0];
            endPos[1] = this.playerCoordinates[1] - 1;
        }
        if(direction == 'R')
        {
            endPos[0] = this.playerCoordinates[0];
            endPos[1] = this.playerCoordinates[1] + 1;
        }

        if(this.board[endPos[0]][endPos[1]] == '#')
        {
            System.out.println("There is a wall in front of the player");
            this.boxesMovedOnTurn.removeLast();
            return;
        }
        if(this.board[endPos[0]][endPos[1]] == 'O')
        {
            moveBox(direction, endPos);
            if(this.board[endPos[0]][endPos[1]] != '*')
            {
                System.out.println("You can't move that box");
                this.boxesMovedOnTurn.removeLast();
                return;
            }
        }
        if(this.board[endPos[0]][endPos[1]] == 'B')
        {
            moveBox(direction, endPos);
            if(this.board[endPos[0]][endPos[1]] != ' ')
            {
                System.out.println("The box is blocked");
                this.boxesMovedOnTurn.removeLast();
                return;
            }
        }

        this.board[this.playerCoordinates[0]][this.playerCoordinates[1]] = this.squareUnderPlayer;
        this.squareUnderPlayer = this.board[endPos[0]][endPos[1]];
        this.board[endPos[0]][endPos[1]] = '@';
        this.playerCoordinates = endPos;
        this.numbOfMoves++;
        playerMove[0] = startSquare;
        playerMove[1] = endPos;
        this.playerMoveLog.add(playerMove);

    }

    void undoMove()
    {
        if(this.numbOfMoves == 0)
        {
            System.out.println(" You haven't made a move to undo");
            return;
        }
        int[][] lastPlayerMove = this.playerMoveLog.getLast();
        this.board[this.playerCoordinates[0]][this.playerCoordinates[1]] = this.squareUnderPlayer;
        this.squareUnderPlayer = this.board[lastPlayerMove[0][0]][lastPlayerMove[0][1]];
        this.board[lastPlayerMove[0][0]][lastPlayerMove[0][1]] = '@';
        this.playerCoordinates = lastPlayerMove[0];
        this.numbOfMoves--;
        this.playerMoveLog.removeLast();
        for(int i = 0; i < this.boxesMovedOnTurn.getLast(); i++)
        {
            int[][] lastBoxMove = this.boxesMoveLog.getLast();
            this.boxesMoveLog.removeLast();

            if (this.board[lastBoxMove[1][0]][lastBoxMove[1][1]] == 'O')
            {
                this.boxesOnTarget--;
                this.board[lastBoxMove[1][0]][lastBoxMove[1][1]] = '*';
            }
            else
            {
                this.board[lastBoxMove[1][0]][lastBoxMove[1][1]] = ' ';
            }

            if (this.board[lastBoxMove[0][0]][lastBoxMove[0][1]] == '*')
            {
                this.board[lastBoxMove[0][0]][lastBoxMove[0][1]] = 'O';
                this.boxesOnTarget++;
            }
            else
            {
                this.board[lastBoxMove[0][0]][lastBoxMove[0][1]] = 'B';
            }
        }
        this.boxesMovedOnTurn.removeLast();


    }

    void moveBox(char direction, int[] boxCoordinates)
    {
        int[][] boxMove = new int[2][2];
        int[] endPos = {0, 0};
        if(direction == 'U')
        {
            endPos[0] = boxCoordinates[0] - 1;
            endPos[1] = boxCoordinates[1];
        }
        if(direction == 'D')
        {
            endPos[0] = boxCoordinates[0] + 1;
            endPos[1] = boxCoordinates[1];
        }
        if(direction == 'L')
        {
            endPos[0] = boxCoordinates[0];
            endPos[1] = boxCoordinates[1] - 1;
        }
        if(direction == 'R')
        {
            endPos[0] = boxCoordinates[0];
            endPos[1] = boxCoordinates[1] + 1;
        }

        boxMove[0] = boxCoordinates;
        boxMove[1] = endPos;

        if(this.board[endPos[0]][endPos[1]] == '#')
        {
            return;
        }

        if(this.board[endPos[0]][endPos[1]] == '*')
        {
            this.boxesOnTarget++;
            this.board[endPos[0]][endPos[1]] = 'O';
            if(this.board[boxCoordinates[0]][boxCoordinates[1]] == 'B')
            {
                this.board[boxCoordinates[0]][boxCoordinates[1]] = ' ';
            }
            else
            {
                this.boxesOnTarget--;
                this.board[boxCoordinates[0]][boxCoordinates[1]] = '*';
            }
            this.boxesMoveLog.add(boxMove);
            this.boxesMovedOnTurn.set(this.numbOfMoves, this.boxesMovedOnTurn.get(this.numbOfMoves) + 1);
            return;
        }

        if(this.board[endPos[0]][endPos[1]] == 'B')
        {
            moveBox(direction, endPos);
            if(this.board[endPos[0]][endPos[1]] != ' ' && this.board[endPos[0]][endPos[1]] != '*')
            {
                return;
            }
        }
        this.board[endPos[0]][endPos[1]] = 'B';
        this.boxesMoveLog.add(boxMove);
        if(this.board[boxCoordinates[0]][boxCoordinates[1]] == 'B')
        {
            this.board[boxCoordinates[0]][boxCoordinates[1]] = ' ';
        }
        else
        {
            this.boxesOnTarget--;
            this.board[boxCoordinates[0]][boxCoordinates[1]] = '*';
        }
        this.boxesMovedOnTurn.set(this.numbOfMoves, this.boxesMovedOnTurn.get(this.numbOfMoves) + 1);

    }

    int[] getPlayerCoordinates(char[][] board, int rows, int cols)
    {
        int[] playerCoordinates = {-1, -1};
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < cols; j++)
            {
                if(board[i][j] == '@')
                {
                    playerCoordinates[0] = i;
                    playerCoordinates[1] = j;
                }
            }
        }
        return playerCoordinates;
    }

    int getNumbOfBoxes(char[][] board, int rows, int cols)
    {
        int boxesCount = 0;
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(board[i][j] == 'B')
                {
                    boxesCount++;
                }
            }
        }
        return boxesCount;

    }

    int[][] getBoxesCoordinates(char[][] board, int rows, int cols)
    {
        int boxesCount = this.getNumbOfBoxes(board, rows, cols);
        int[][] boxesCoordinates = new int[boxesCount][2];
        int index = 0;
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(board[i][j] == 'B')
                {
                    boxesCoordinates[index][0] = i;
                    boxesCoordinates[index][1] = j;
                    index++;
                }
            }
        }
        return boxesCoordinates;
    }

    int getNumbOfTargets(char[][] board, int rows, int cols)
    {
        int targetsCount = 0;
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(board[i][j] == '*')
                {
                    targetsCount++;
                }
            }
        }
        return targetsCount;

    }

    int[][] getTargetsCoordinates(char[][] board, int rows, int cols)
    {
        int targetsCount = this.getNumbOfBoxes(board, rows, cols);
        int[][] targetsCoordinates = new int[targetsCount][2];
        int index = 0;
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(board[i][j] == '*')
                {
                    targetsCoordinates[index][0] = i;
                    targetsCoordinates[index][1] = j;
                    index++;
                }
            }
        }
        return targetsCoordinates;
    }

    void PrintBoard()
    {
        for(int i = 0; i <this.rows; i++) {
            for (int j = 0; j < this.cols; j++)
            {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    void printPlayerMoveLog()
    {
        for(int i = 0; i < this.playerMoveLog.size(); i++) {
            System.out.println((i + 1) + " player move:");
            System.out.print("Start square: ");
            System.out.println(this.playerMoveLog.get(i)[0][0] + " " + this.playerMoveLog.get(i)[0][1]);
            System.out.print("End square: ");
            System.out.println(this.playerMoveLog.get(i)[1][0] + " " + this.playerMoveLog.get(i)[1][1]);
        }

    }

    void printBoxesMoveLog()
    {
        for(int i = 0; i < this.boxesMoveLog.size(); i++) {
            System.out.println((i + 1) + " boxes move:");
            System.out.print("Start square: ");
            System.out.println(this.boxesMoveLog.get(i)[0][0] + " " + this.boxesMoveLog.get(i)[0][1]);
            System.out.print("End square: ");
            System.out.println(this.boxesMoveLog.get(i)[1][0] + " " + this.boxesMoveLog.get(i)[1][1]);
        }
    }

    boolean isMoveMade(char[][] prevPosition)
    {
        for(int i = 0; i < this.rows; i++)
        {
            for(int j = 0; j < this.cols; j++)
            {
                if(this.board[i][j] != prevPosition[i][j])
                {
                    return true;
                }
            }
        }
        return false;
    }


}
