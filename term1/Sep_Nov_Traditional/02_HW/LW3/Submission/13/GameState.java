public class GameState{
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';
    private int playerX, playerY;
    private int width, height, boxCount;
    private int moves = 0;
    private int boxOnTarget = 0;
    private boolean[][] isTarget;
    private boolean[][] hasBox;
    private char[][] board;
    public GameState(int width, int height, int boxCount)
    {
        this.width = width;
        this.height = height;
        this.boxCount = boxCount;
        board = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];
        for(int y = 0;y<height;y++)
        {
            for(int x = 0;x<width;x++)
            {
                if(x == 0||y == 0||x == width - 1||y == height-1)
                {
                    board[y][x] = WALL;
                } else{
                    board[y][x] = EMPTY;
                }
            }
        }
    }
    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }
    public int getBoxesCount()
    {
        return boxCount;
    }
    public int getPlayerX()
    {
        return playerX;
    }
    public int getPlayerY()
    {
        return playerY;
    }
    public int getMoves()
    {
        return moves;
    }
    public int getBoxesOnTargets()
    {
        return boxOnTarget;
    }
    public boolean[][] getTargets()
    {
        return isTarget;
    }
    public boolean[][] getBoxes()
    {
        return hasBox;
    }

    public void setPlayer(int x, int y)
    {
        this.playerX = x;
        this.playerY = y;
    }

    public void setTarget(int x, int y, boolean val)
    {
        isTarget[y][x] = val;
    }
    public void setBox(int x, int y, boolean val)
    {
        hasBox[y][x] = val;
    }

    public void addMoves()
    {
        moves++;
    }
    public void addBoxOnTarget()
    {
        boxOnTarget++;
    }
    public void removeBoxFromTarget()
    {
        boxOnTarget--;
    }

    public boolean isTarget(int x, int y)
    {
        return isTarget[y][x];
    }
    public boolean hasBox(int x, int y)
    {
        return hasBox[y][x];
    }
    public char getCell(int x, int y)
    {
        return board[y][x];
    }
    public void setCell(int x, int y, char val)
    {
        board[y][x] = val;
    }
    public boolean isWall(int x, int y)
    {
        return board[y][x] == WALL;
    }

    public void clearTargets()
    {
        for(int y = 1;y<height-1;y++)
        {
            for(int x = 1;x<width-1;x++)
            {
                isTarget[y][x] = false;
            }
        }
    }
    public void clearBoxes()
    {
        for(int y = 1;y<height-1;y++)
        {
            for(int x = 1;x<width-1;x++)
            {
                hasBox[y][x] = false;
            }
        }
    }
    public void resetBoard()
    {
        for(int y = 0;y<height;y++)
        {
            for(int x = 0;x<width;x++)
            {
                if(x == 0||y == 0||x == width-1||y == height-1)board[y][x] = WALL;
                else board[y][x] = EMPTY;
            }
        }
    }

    public void updateDisplay()
    {
        resetBoard();
        for(int y = 1;y<height-1;y++)
        {
            for(int x = 1;x<width-1;x++)
            {
                if(isTarget[y][x])board[y][x] = TARGET;
            }
        }
        int countOnTargets = 0;
        for(int y = 1;y<height-1;y++)
        {
            for(int x = 1 ;x<width-1;x++)
            {
                if(hasBox[y][x])
                {
                    if(isTarget[y][x])
                    {
                        board[y][x] = BOX_ON_TARGET;
                        countOnTargets++;
                    } else{
                        board[y][x] = BOX;
                    }
                }
            }
        }
        boxOnTarget = countOnTargets;
        if(playerY >= 0 && playerY < height && playerX >= 0 && playerX < width)
        {
            board[playerY][playerX] = PLAYER;
        }
    }
    public void print()
    {
        updateDisplay();
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                System.out.print(board[y][x]);
            }
            System.out.println();
        }
        System.out.println("Moves: " + moves + " | Boxes on targets: " + boxOnTarget + "/" + boxCount);
    }

    public boolean checkWin()
    {
        return boxOnTarget == boxCount;
    }
}
