import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static final String[][] LEVELS = {
        {
            "#####",
            "#.@*#",
            "#.B.#",
            "#...#",
            "#####"
        },
        {
            "######",
            "#..@.#",
            "#*BB.#",
            "#..*.#",
            "######"
        },
        {
            "#######",
            "#@B.*.#",
            "#.BB..#",
            "#.*..*#",
            "#######"
        }
    };

    private static int readValidInt(String prompt, int minValue) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                if (value >= minValue) {
                    return value;
                }
            } else {
                scanner.next();
            }
            System.out.println("Please enter a valid number ≥ " + minValue);
        }
    }

    private static String readCommand() {
        System.out.print("Enter move (w/a/s/d or up/down/left/right, q to quit): ");
        return scanner.next().toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println("Choose an option:");
        System.out.println("1. Random game");
        for (int i = 0; i < LEVELS.length; i++) {
            System.out.println((i+2) + ". Level " + (i+1));
        }
        int choice = readValidInt("Enter option number: ", 1);
        switch (choice) {
            case 1: {
                int W, H, K;
                boolean validInput = false;
                do {
                    W = readValidInt("Enter width (W ≥ 5): ", 5);
                    H = readValidInt("Enter height (H ≥ 5): ", 5);
                    int maxBoxes;
                    if (W == 5 && H == 5) {
                        maxBoxes = 1;
                    } else {
                        maxBoxes = ((W-2)*(H-2)) / 4;
                    }
                    K = readValidInt("Enter number of boxes (K ≤ " + maxBoxes + "): ", 1);
                    if (K <= maxBoxes) {
                        validInput = true;
                    } else {
                        System.out.println("Too many boxes! Maximum allowed: " + maxBoxes);
                    }
                } while (!validInput);
                SokobanGame game = new SokobanGame();
                game.initializeGame(W, H, K);
                while (!game.checkWin()) {
                    game.printBoard();
                    String command = readCommand();
                    if (command.equals("q") || command.equals("quit")) {
                        System.out.println("Game aborted after " + game.getMoves() + " moves.");
                        break;
                    }
                    if (command.equals("u") || command.equals("undo")) {
                        game.undo();
                        continue;
                    }
                    try {
                        game.move(command);
                    } catch (InvalidMoveException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (GameStateException e) {
                        System.out.println("Game state error: " + e.getMessage());
                        break;
                    }
                    if (game.checkWin()) {
                        game.printBoard();
                        System.out.println("You win in " + game.getMoves() + " moves!");
                    }
                }
                break;
            }
            case 2:
            case 3:
            case 4: {
                SokobanGame game = new SokobanGame();
                game.loadLevel(LEVELS[choice-2]);
                while (!game.checkWin()) {
                    game.printBoard();
                    String command = readCommand();
                    if (command.equals("q") || command.equals("quit")) {
                        System.out.println("Game aborted after " + game.getMoves() + " moves.");
                        break;
                    }
                    if (command.equals("u") || command.equals("undo")) {
                        game.undo();
                        continue;
                    }
                    try {
                        game.move(command);
                    } catch (InvalidMoveException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (GameStateException e) {
                        System.out.println("Game state error: " + e.getMessage());
                        break;
                    }
                    if (game.checkWin()) {
                        game.printBoard();
                        System.out.println("You win in " + game.getMoves() + " moves!");
                    }
                }
                break;
            }
            default:
                System.out.println("Invalid option.");
        }
        scanner.close();
    }
}
