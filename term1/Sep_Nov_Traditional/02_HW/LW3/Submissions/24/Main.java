import java.util.Scanner;
import java.util.Random;

static final char WALL = '#';
static final char EMPTY = '.';
static final char PLAYER = '@';
static final char BOX = 'B';
static final char TARGET = '*';
static final char BOX_ON_TARGET = 'O';//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or

Scanner my_scan = new Scanner(System.in);

public class SokobanGame {
    // Game state
    private int num_moves = 0;
    private int width, height, boxesCount;
    private int playerX, playerY;
    private char prev_player_pos = EMPTY;

    // Game board arrays
    private char[][] displayBoard;


    // Core methods to implement
    public void initializeGame(int w, int h, int k)
    {
        while(w < 5)
        {
            System.out.println("Invalid height please enter new one: ");
            w = my_scan.nextInt();
        }
        width = w;
        while(h < 5)
        {
            System.out.println("Invalid width please enter new one: ");
            h = my_scan.nextInt();
        }
        height = h;

        while(k > ((w-2)*(h-2) - 4) / 2)
        {
            System.out.println("Invalid box count please enter new one: ");
            k = my_scan.nextInt();
        }
        boxesCount = k;

        displayBoard = new char[w][h];
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(x == 0 || y == 0 || x == width - 1 || y == height - 1)
                {
                    displayBoard[x][y] = WALL;
                    continue;
                }
                if(x == width/2 && y == height/2)
                {
                    displayBoard[x][y] = PLAYER;
                    playerX = x;
                    playerY = y;
                    continue;
                }
                displayBoard[x][y] = EMPTY;
            }
        }
    }



    private void placeTargets()
    {
        int box_counted = boxesCount;
        int x, y;


        for(int i = 1; i < width - 2; i++) // LEFT AND RIGHT WALLS
        {
            if (displayBoard[i][1] == BOX)
            {
                do
                {
                    x = new Random().nextInt(1,width - 2);
                }while(displayBoard[x][1] != EMPTY);
                displayBoard[x][1] = TARGET;
                box_counted--;

            }
            if(displayBoard[i][height - 2] == BOX)
            {
                do
                {
                    x = new Random().nextInt(1,width - 2);
                }while(displayBoard[x][height - 2] != EMPTY);
                y = height - 2;

                displayBoard[x][y] = TARGET;
                box_counted--;

            }
        }

        for(int i = 1; i < height - 2; i++) // Floor and ceiling
        {
            if (displayBoard[1][i] == BOX)
            {
                do
                {
                    y = new Random().nextInt(1,height - 2);
                }while(displayBoard[1][y] != EMPTY);
                displayBoard[1][y] = TARGET;
                box_counted--;
            }
            if (displayBoard[width - 2][i] == BOX)
            {
                do
                {
                    y = new Random().nextInt(1,height - 2);
                }while(displayBoard[width - 2][y] != EMPTY);
                displayBoard[width - 2][y] = TARGET;
                box_counted--;
            }
        }



        while(box_counted > 0)
        {
            x = 1 + new Random().nextInt(width - 2);
            y = 1 + new Random().nextInt(height - 2);
            if(displayBoard[x][y] == EMPTY)
            {
                displayBoard[x][y] = TARGET;
                box_counted--;
            }
        }
    }


    private void placeBoxes()
    {
        int box_counted = boxesCount;
        int x = 1, y = 1;
        while(box_counted > 0 || x == width/2 && y == height/2)
        {
            while(true)
            {
                x = new Random().nextInt(1,width - 1);
                y = new Random().nextInt(1, height - 1);

                if(displayBoard[x][y] != EMPTY || x == 1 && y == 1 || x == width - 2 && y == height - 2 || x == width - 2 && y == 1 || x == 1 && y == height - 2) continue;//check for corner spawns
                if(displayBoard[x][y - 1] == WALL || displayBoard[x][y + 1] == WALL)//check for box next to box on side walls
                {
                    if(displayBoard[x + 1][y] == BOX || displayBoard[x - 1][y] == BOX)
                    {
                        continue;
                    }

                }
                if(displayBoard[x - 1][y] == WALL || displayBoard[x + 1][y] == WALL)//check for box next to box on floor and ceiling
                {
                    if(displayBoard[x][y + 1] == BOX || displayBoard[x][y - 1] == BOX)
                    {
                        continue;
                    }

                }
                if(x == 2 && y == 1 || x == 1 && y == 2)//check for trap in up left corner
                {
                    if(displayBoard[1][2] == BOX) continue;
                    if(displayBoard[2][1] == BOX) continue;
                }

                if(x == 1 && y == height - 3 || x == 2 && y == height - 2)//check for trap in up right corner
                {
                    if(displayBoard[1][height - 3] == BOX) continue;
                    if(displayBoard[2][height - 2] == BOX) continue;
                }

                if(x == width - 3 && y == height - 2 || x == width - 2 && y == height - 3)//check for trap in down left corner
                {
                    if(displayBoard[width - 3][height - 2] == BOX) continue;
                    if(displayBoard[width - 2][height - 3] == BOX) continue;
                }

                if(x == width - 3 && y == 1 || x == width - 2 && y == 2)//check for trap in up right corner
                {
                    if(displayBoard[width - 3][1] == BOX) continue;
                    if(displayBoard[width - 2][2] == BOX) continue;
                }
                break;
            }

            displayBoard[x][y] = BOX;
            box_counted--;
        }
    }


    public void move(String direction)
    {
        num_moves++;
        if(direction.equals("up") || direction.equals("w"))
        {
            if(displayBoard[playerX - 1][playerY] == WALL)
            {
                num_moves--;
                System.out.println("Invalid move please enter new one: ");
            }
            else
            {
                if(displayBoard[playerX - 1][playerY] == BOX)
                {
                    if (displayBoard[playerX - 2][playerY] != WALL && displayBoard[playerX - 2][playerY] != BOX && displayBoard[playerX - 2][playerY] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX - 2][playerY] == TARGET)
                        {
                            displayBoard[playerX - 2][playerY] = BOX_ON_TARGET;
                        }
                        else displayBoard[playerX - 2][playerY] = BOX;
                    }

                    else
                    {
                        num_moves--;
                        System.out.println("Invalid move please enter new one: ");
                        return;
                    }
                    displayBoard[playerX][playerY] = prev_player_pos;
                    prev_player_pos = EMPTY;
                    playerX--;
                    displayBoard[playerX][playerY] = PLAYER;
                    return;
                }
                else if(displayBoard[playerX - 1][playerY] == BOX_ON_TARGET)
                {
                    if (displayBoard[playerX - 2][playerY] != WALL && displayBoard[playerX - 2][playerY] != BOX && displayBoard[playerX - 2][playerY] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX - 2][playerY] == TARGET)
                        {
                            displayBoard[playerX - 2][playerY] = BOX_ON_TARGET;
                            displayBoard[playerX][playerY] = prev_player_pos;
                            prev_player_pos = TARGET;
                            playerX--;
                            displayBoard[playerX][playerY] = PLAYER;
                            return;
                        }
                        displayBoard[playerX - 2][playerY] = BOX;
                        displayBoard[playerX][playerY] = prev_player_pos;
                        prev_player_pos = TARGET;
                        playerX--;
                        displayBoard[playerX][playerY] = PLAYER;
                        return;
                    }

                    num_moves--;
                    System.out.println("Invalid input please enter new one: ");
                    return;
                }
                displayBoard[playerX][playerY] = prev_player_pos;
                playerX--;
                prev_player_pos = displayBoard[playerX][playerY];
                displayBoard[playerX][playerY] = PLAYER;
            }
        }

        else if(direction.equals("down") || direction.equals("s"))
        {
            if(displayBoard[playerX + 1][playerY] == WALL)
            {
                num_moves--;
                System.out.println("Invalid move please enter new one: ");
            }
            else
            {
                if(displayBoard[playerX + 1][playerY] == BOX)
                {
                    if (displayBoard[playerX + 2][playerY] != WALL && displayBoard[playerX + 2][playerY] != BOX && displayBoard[playerX + 2][playerY] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX + 2][playerY] == TARGET)
                        {
                            displayBoard[playerX + 2][playerY] = BOX_ON_TARGET;
                        }
                        else displayBoard[playerX + 2][playerY] = BOX;
                    }
                    else
                    {
                        num_moves--;
                        System.out.println("Invalid move please enter new one: ");
                        return;
                    }
                    displayBoard[playerX][playerY] = prev_player_pos;
                    prev_player_pos = EMPTY;
                    playerX++;
                    displayBoard[playerX][playerY] = PLAYER;
                    return;
                }
                else if(displayBoard[playerX + 1][playerY] == BOX_ON_TARGET)
                {
                    if (displayBoard[playerX + 2][playerY] != WALL && displayBoard[playerX + 2][playerY] != BOX && displayBoard[playerX + 2][playerY] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX + 2][playerY] == TARGET)
                        {
                            displayBoard[playerX + 2][playerY] = BOX_ON_TARGET;
                            displayBoard[playerX][playerY] = prev_player_pos;
                            prev_player_pos = TARGET;
                            playerX++;
                            displayBoard[playerX][playerY] = PLAYER;
                            return;
                        }
                        displayBoard[playerX + 2][playerY] = BOX;
                        displayBoard[playerX][playerY] = prev_player_pos;
                        prev_player_pos = TARGET;
                        playerX++;
                        displayBoard[playerX][playerY] = PLAYER;
                        return;
                    }
                    num_moves--;
                    System.out.println("Invalid input please enter new one: ");
                    return;
                }
                displayBoard[playerX][playerY] = prev_player_pos;
                playerX++;
                prev_player_pos = displayBoard[playerX][playerY];
                displayBoard[playerX][playerY] = PLAYER;
            }
        }

        else if(direction.equals("left") || direction.equals("a"))
        {
            if(displayBoard[playerX][playerY - 1] == WALL)
            {
                num_moves--;
                System.out.println("Invalid move please enter new one: ");
            }
            else
            {
                if(displayBoard[playerX][playerY - 1] == BOX)
                {
                    if (displayBoard[playerX][playerY - 2] != WALL && displayBoard[playerX][playerY - 2] != BOX && displayBoard[playerX][playerY - 2] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX][playerY - 2] == TARGET)
                        {
                            displayBoard[playerX][playerY - 2] = BOX_ON_TARGET;
                        }
                        else displayBoard[playerX][playerY -2] = BOX;
                    }
                    else
                    {
                        num_moves--;
                        System.out.println("Invalid move please enter new one: ");
                        return;
                    }
                    displayBoard[playerX][playerY] = prev_player_pos;
                    prev_player_pos = EMPTY;
                    playerY--;
                    displayBoard[playerX][playerY] = PLAYER;
                    return;
                }
                else if(displayBoard[playerX][playerY - 1] == BOX_ON_TARGET)
                {
                    if (displayBoard[playerX][playerY - 2] != WALL && displayBoard[playerX][playerY - 2] != BOX && displayBoard[playerX][playerY - 2] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX][playerY - 2] == TARGET)
                        {
                            displayBoard[playerX][playerY - 2] = BOX_ON_TARGET;
                            displayBoard[playerX][playerY] = prev_player_pos;
                            prev_player_pos = TARGET;
                            playerY--;
                            displayBoard[playerX][playerY] = PLAYER;
                            return;
                        }
                        displayBoard[playerX][playerY - 2] = BOX;
                        displayBoard[playerX][playerY] = prev_player_pos;
                        prev_player_pos = TARGET;
                        playerY--;
                        displayBoard[playerX][playerY] = PLAYER;
                        return;
                    }
                    num_moves--;
                    System.out.println("Invalid input please enter new one: ");
                    return;
                }
                displayBoard[playerX][playerY] = prev_player_pos;
                playerY--;
                prev_player_pos = displayBoard[playerX][playerY];
                displayBoard[playerX][playerY] = PLAYER;
            }
        }

        else if(direction.equals("right") || direction.equals("d"))
        {
            if(displayBoard[playerX][playerY + 1] == WALL)
            {
                num_moves--;
                System.out.println("Invalid move please enter new one: ");
            }
            else
            {
                if(displayBoard[playerX][playerY + 1] == BOX)
                {
                    if (displayBoard[playerX][playerY + 2] != WALL && displayBoard[playerX][playerY + 2] != BOX && displayBoard[playerX][playerY + 2] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX][playerY + 2] == TARGET)
                        {
                            displayBoard[playerX][playerY + 2] = BOX_ON_TARGET;
                        }
                        else displayBoard[playerX][playerY + 2] = BOX;
                    }
                    else
                    {
                        num_moves--;
                        System.out.println("Invalid move please enter new one: ");
                        return;
                    }
                    displayBoard[playerX][playerY] = prev_player_pos;
                    prev_player_pos = EMPTY;
                    playerY++;
                    displayBoard[playerX][playerY] = PLAYER;
                    return;
                }
                else if(displayBoard[playerX][playerY + 1] == BOX_ON_TARGET)
                {
                    if (displayBoard[playerX][playerY + 2] != WALL && displayBoard[playerX][playerY + 2] != BOX && displayBoard[playerX][playerY + 2] != BOX_ON_TARGET)
                    {
                        if (displayBoard[playerX][playerY + 2] == TARGET)
                        {
                            displayBoard[playerX][playerY + 2] = BOX_ON_TARGET;
                            displayBoard[playerX][playerY] = prev_player_pos;
                            prev_player_pos = TARGET;
                            playerY++;
                            displayBoard[playerX][playerY] = PLAYER;
                            return;
                        }
                        displayBoard[playerX][playerY + 2] = BOX;
                        displayBoard[playerX][playerY] = prev_player_pos;
                        prev_player_pos = TARGET;
                        playerY++;
                        displayBoard[playerX][playerY] = PLAYER;
                        return;
                    }
                    num_moves--;
                    System.out.println("Invalid input please enter new one: ");
                    return;
                }
                displayBoard[playerX][playerY] = prev_player_pos;
                playerY++;
                prev_player_pos = displayBoard[playerX][playerY];
                displayBoard[playerX][playerY] = PLAYER;
            }
        }
        else
        {
            num_moves--;
            System.out.println("Invalid move please enter new one: ");
        }
    }



    public boolean checkWin()
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                if(displayBoard[x][y] == BOX)
                {
                    return false;
                }
            }
        }

        return true;
    }
    public void printBoard()
    {
        System.out.println("Num of moves: " + num_moves);
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                System.out.print(displayBoard[x][y]);
                System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
}

void main() {
    SokobanGame game = new SokobanGame();
    System.out.println("Enter width: ");
    int h = my_scan.nextInt();
    System.out.println("Enter height: ");
    int w = my_scan.nextInt();
    System.out.println("Enter box count: ");
    int k = my_scan.nextInt();


    game.initializeGame(w, h, k);
    game.placeBoxes();
    game.placeTargets();
    game.printBoard();
    String input = "0";
    while(input.equals("q") == false && input.equals("quit") == false && game.checkWin() == false)
    {
        System.out.println("Enter your move: ");
        input = my_scan.next();
        game.move(input);
        game.printBoard();
    }
    if(game.checkWin())System.out.println(" __      __  ______   __    __        __       __  ______  __    __ \n" +
            "/  \\    /  |/      \\ /  |  /  |      /  |  _  /  |/      |/  \\  /  |\n" +
            "$$  \\  /$$//$$$$$$  |$$ |  $$ |      $$ | / \\ $$ |$$$$$$/ $$  \\ $$ |\n" +
            " $$  \\/$$/ $$ |  $$ |$$ |  $$ |      $$ |/$  \\$$ |  $$ |  $$$  \\$$ |\n" +
            "  $$  $$/  $$ |  $$ |$$ |  $$ |      $$ /$$$  $$ |  $$ |  $$$$  $$ |\n" +
            "   $$$$/   $$ |  $$ |$$ |  $$ |      $$ $$/$$ $$ |  $$ |  $$ $$ $$ |\n" +
            "    $$ |   $$ \\__$$ |$$ \\__$$ |      $$$$/  $$$$ | _$$ |_ $$ |$$$$ |\n" +
            "    $$ |   $$    $$/ $$    $$/       $$$/    $$$ |/ $$   |$$ | $$$ |\n" +
            "    $$/     $$$$$$/   $$$$$$/        $$/      $$/ $$$$$$/ $$/   $$/ \n" +
            "                                                                    \n" +
            "                                                                    \n" +
            "                                                                     in: " + game.num_moves + " moves");
    my_scan.close();
}