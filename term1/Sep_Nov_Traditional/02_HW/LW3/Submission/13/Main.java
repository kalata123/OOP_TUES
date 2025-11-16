//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;

public class Main{
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int w = 0, h = 0, k = 0;
        while(w < 5)
        {
            System.out.print("Enter width (>4): ");
            w = sc.nextInt();
        }
        while(h < 5)
        {
            System.out.print("Enter height (>4): ");
            h = sc.nextInt();
        }
        int maxBoxes = ((w-2)*(h-2)-2)/2;
        do
        {
            System.out.print("Enter boxes K (<= "+maxBoxes + "): ");
            k = sc.nextInt();
        }
        while(k>maxBoxes);
        SokobanGame game = new SokobanGame(w, h, k);
        sc.nextLine();
        while(true)
        {
            if(game.checkWin())
            {
                System.out.println("You win in "+game.getMoveCount()+" moves!");
                break;
            }
            game.printBoard();
            System.out.print("Command (w/a/s/d, undo, quit): ");
            String cmd = sc.nextLine().trim();

            if(cmd.equals("quit")||cmd.equals("q"))
            {
                break;
            }
            else if(cmd.equals("undo"))
            {
                game.undo();
            }
            else
            {
                game.move(cmd);
            }
        }
        sc.close();
    }
}
