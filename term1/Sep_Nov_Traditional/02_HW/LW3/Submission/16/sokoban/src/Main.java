package src;

import java.io.FileReader;
import java.io.IOException;

/**
 * Main entry point for the Sokoban game terminal game.
 * <p>
 * Supports the following commands:
 * <ul>
 *   <li>{@code game -p} - Play with a randomly generated board</li>
 *   <li>{@code game -ps [filename]} - Generate board and save to file</li>
 *   <li>{@code game -g} - Generate and print board</li>
 *   <li>{@code game -gs [filename]} - Generate and save board to file</li>
 *   <li>{@code game -o filename} - Parse board from file and print</li>
 *   <li>{@code game -po filename} - Parse board from file and play</li>
 *   <li>{@code game -h} - Display help menu</li>
 * </ul>
 */
public class Main {
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        String command = args[0];

        try {
            switch (command) {
                case "-p" -> playGenerated(args);
                case "-ps", "-sp" -> playAndSave(args);
                case "-g" -> generateAndPrint(args);
                case "-gs", "-sg" -> generateAndSave(args);
                case "-o" -> parseAndPrint(args);
                case "-po", "-op"-> parseAndPlay(args);
                case "-h" -> printHelp();
                default -> {
                    System.err.println("Unknown command: " + command);
                    System.err.println("Use 'game -h' for help.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates a random board and starts the game.
     * 
     * @param args command line arguments (rows, cols, boxes at indices 1, 2, 3)
     */
    private static void playGenerated(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("Error: Missing arguments for -p command");
            System.err.println("Usage: game -p <rows> <cols> <boxes>");
            return;
        }
        
        int rows = Integer.parseInt(args[1]);
        int cols = Integer.parseInt(args[2]);
        int boxes = Integer.parseInt(args[3]);
        
        Board board = BoardGenerator.generate(rows, cols, boxes);
        Game game = new Game(board);
        game.play();
    }

    /**
     * Generates a random board, plays the game, and saves the board to a file.
     * 
     * @param args command line arguments (rows, cols, boxes, and optional filename)
     */
    private static void playAndSave(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("Error: Missing arguments for -ps command");
            System.err.println("Usage: game -ps <rows> <cols> <boxes> [filename]");
            return;
        }
        
        int rows = Integer.parseInt(args[1]);
        int cols = Integer.parseInt(args[2]);
        int boxes = Integer.parseInt(args[3]);
        
        Board board = BoardGenerator.generate(rows, cols, boxes);
        
        if (args.length > 4) 
          BoardSaver.saveBoard(board, args[4]);
        else
          BoardSaver.saveBoard(board, null);

        
        
        System.out.println("Board saved successfully!");
        System.out.println();
        
        Game game = new Game(board);
        game.play();
    }

    /**
     * Generates a random board and prints it to the console.
     * 
     * @param args command line arguments (rows, cols, boxes at indices 1, 2, 3)
     */
    private static void generateAndPrint(String[] args) {
        if (args.length < 4) {
            System.err.println("Error: Missing arguments for -g command");
            System.err.println("Usage: game -g <rows> <cols> <boxes>");
            return;
        }
        
        int rows = Integer.parseInt(args[1]);
        int cols = Integer.parseInt(args[2]);
        int boxes = Integer.parseInt(args[3]);
        
        Board board = BoardGenerator.generate(rows, cols, boxes);
        board.display();
    }

    /**
     * Generates a random board and saves it to a file.
     * 
     * @param args command line arguments (rows, cols, boxes, and optional filename)
     */
    private static void generateAndSave(String[] args) throws IOException {
        if (args.length < 4) {
            System.err.println("Error: Missing arguments for -gs command");
            System.err.println("Usage: game -gs <rows> <cols> <boxes> [filename]");
            return;
        }
        
        int rows = Integer.parseInt(args[1]);
        int cols = Integer.parseInt(args[2]);
        int boxes = Integer.parseInt(args[3]);
        
        Board board = BoardGenerator.generate(rows, cols, boxes);
        
        String savedPath;
        if (args.length > 4) 
          savedPath = BoardSaver.saveBoard(board, args[4]);
        else
          savedPath = BoardSaver.saveBoard(board, null);
        
        
        System.out.println("Board generated and saved to: " + savedPath);
    }

    /**
     * Parses a board from a file and prints it to the console.
     * 
     * @param args command line arguments (filename at index 1)
     */
    private static void parseAndPrint(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Error: Filename required for -o command");
            System.err.println("Usage: game -o <filename>");
            return;
        }
        
        String filename = args[1];
        Board board = BoardParser.parse(new FileReader(filename));
        board.display();
    }

    /**
     * Parses a board from a file and starts the game.
     * 
     * @param args command line arguments (filename at index 1)
     */
    private static void parseAndPlay(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println("Error: Filename required for -po command");
            System.err.println("Usage: game -po <filename>");
            return;
        }
        
        String filename = args[1];
        Board board = BoardParser.parse(new FileReader(filename));
        Game game = new Game(board);
        game.play();
    }

    /**
     * Prints the help menu with all available commands.
     */
    private static void printHelp() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              SOKOBAN - Terminal Game                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("USAGE: game [command] [options]");
        System.out.println();
        System.out.println("COMMANDS:");
        System.out.println("  -p <rows> <cols> <boxes>           Play with generated board");
        System.out.println("  -ps <rows> <cols> <boxes> [file]   Play and save board");
        System.out.println("  -g <rows> <cols> <boxes>           Generate and print board");
        System.out.println("  -gs <rows> <cols> <boxes> [file]   Generate and save board");
        System.out.println("  -o <filename>                      Parse board from file and print");
        System.out.println("  -po <filename>                     Parse board from file and play");
        System.out.println("  -h                                 Display this help menu");
        System.out.println();
        System.out.println("GAME CONTROLS:");
        System.out.println("  W/A/S/D         Move player up/left/down/right");
        System.out.println("  U               Undo last move");
        System.out.println("  R               Reset level");
        System.out.println("  Q               Quit game");
        System.out.println();
        System.out.println("EXAMPLES:");
        System.out.println("  game -p 10 10 5                # Play 10x10 board with 5 boxes");
        System.out.println("  game -ps 8 8 3 level1.txt      # Save and play custom board");
        System.out.println("  game -g 12 15 6                # Generate 12x15 board with 6 boxes");
        System.out.println("  game -gs 10 10 4 myboard.txt   # Generate and save board");
        System.out.println("  game -po level1.txt            # Load and play saved board");
        System.out.println();
    }
}