import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    public static void main(String[] args) throws IOException {

       startMenu();
    }

    public static void startMenu()
    {
        while(true)
        {
            System.out.println("Menu:");
            System.out.println("1. Play on random generated board");
            System.out.println("2. Play on levels");
            System.out.println("3. Exit");
            Scanner input = new Scanner(System.in);
            int choice = input.nextInt();
            switch(choice)
                {
                    case 1:playOnRandomBoard();break;
                    case 2:playOnLevels();break;
                    case 3:System.exit(0);
                    default:System.out.println("Invalid choice");
                }
        }
    }

    public static void playOnRandomBoard()
    {
        Scanner input = new Scanner(System.in);
        try
        {
            System.out.println("Enter number of rows: ");
            int rows = input.nextInt();
            System.out.println("Enter number of columns: ");
            int cols = input.nextInt();
            System.out.println("Enter number of boxes: ");
            int K = input.nextInt();

            GameState gs = new GameState(rows, cols, K);

            gs.PrintBoard();
            while (true) {
                if (gs.isWinning()) {
                    System.out.println("Congratulations! You win in " + gs.numbOfMoves);
                    break;
                }

                char choice = input.next().charAt(0);
                if (choice == 'q')
                    break;

                switch (choice) {
                    case 'w' -> gs.move('U');
                    case 's' -> gs.move('D');
                    case 'a' -> gs.move('L');
                    case 'd' -> gs.move('R');
                    case 'z' -> gs.undoMove();
                    default -> System.out.println("Invalid choice");

                }

                gs.PrintBoard();
                System.out.println("Number of moves: " + gs.numbOfMoves);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void playOnLevels()
    {
        Scanner input = new Scanner(System.in);
        GridReader gr = new GridReader();
        Vector<String[]> grids = gr.readGridsFromFile("levels.txt");
        if(grids.isEmpty())
        {
            System.out.println("File doesn't have any levels");
            System.exit(0);
        }

        while(true)
        {
            Vector<GameState> levels = new Vector<>();

            for (String[] grid : grids) {
                try
                {
                    levels.add(new GameState(grid));
                }
                catch (Exception e)
                {
                    System.out.println("Error in level number " + (grids.indexOf(grid) + 1) + " " + e.getMessage());
                }
            }

            for(GameState level: levels)
            {
               System.out.println("Level " + (levels.indexOf(level) + 1));
               level.PrintBoard();
            }
            System.out.println((levels.size() + 1) + ".Exit");
            int choice = input.nextInt();
            if(choice == levels.size() + 1)
                break;
            if(choice > levels.size() + 1 || choice < 0)
            {
                System.out.println("Invalid choice");
                continue;
            }
            levels.get(choice-1).PrintBoard();
            while(true)
            {

                if (levels.get(choice-1).isWinning()) {
                    System.out.println("Congratulations! You win in " + levels.get(choice-1).numbOfMoves);
                    break;
                }

                char direction = input.next().charAt(0);
                if (direction == 'q')
                    break;
                if (direction == 'z')
                {
                   levels.get(choice-1).undoMove();
                }
                else {
                    switch (direction) {
                        case 'w' -> levels.get(choice - 1).move('U');
                        case 's' -> levels.get(choice - 1).move('D');
                        case 'a' -> levels.get(choice - 1).move('L');
                        case 'd' -> levels.get(choice - 1).move('R');
                        default -> System.out.println("Invalid choice");

                    }
                }


                levels.get(choice-1).PrintBoard();
                System.out.println("Number of moves: " + levels.get(choice-1).numbOfMoves);
            }


        }

    }
}