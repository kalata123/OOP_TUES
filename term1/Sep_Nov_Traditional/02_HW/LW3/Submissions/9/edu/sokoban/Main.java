package edu.sokoban;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        SokobanGame game = new SokobanGame();

        System.out.println("Select mode:");
        System.out.println("  1) Random-generated level (enter W,H,K)");
        System.out.println("  2) Built-in level 1");
        System.out.println("  3) Built-in level 2");
        System.out.println("  4) Built-in level 3");
        System.out.println("  5) Built-in level 4");
        System.out.print("Choose option (1-5): ");

        String opt = scanner.nextLine().trim();

        if (opt.equals("1")) {
            int width = 0, height = 0, boxes = 0;
            while (true) {
                System.out.print("Enter width W (integer, >= 5): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next();
                    continue;
                }
                width = scanner.nextInt();
                if (width >= 5) break;
                System.out.println("Width must be at least 5.");
            }

            while (true) {
                System.out.print("Enter height H (integer, >= 5): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next();
                    continue;
                }
                height = scanner.nextInt();
                if (height >= 5) break;
                System.out.println("Height must be at least 5.");
            }

            int interiorCells = (width - 2) * (height - 2);
            int maxBoxes = Math.max(0, (interiorCells - 2) / 2);

            while (true) {
                System.out.print("Enter number of boxes K (integer, 0.." + maxBoxes + "): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter an integer.");
                    scanner.next();
                    continue;
                }
                boxes = scanner.nextInt();
                if (boxes >= 0 && boxes <= maxBoxes) break;
                System.out.println("Invalid boxes count. Must be between 0 and " + maxBoxes + ".");
            }

            scanner.nextLine();

            game.initializeGame(width, height, boxes);
        } else if (opt.equals("2") || opt.equals("3") || opt.equals("4") || opt.equals("5")) {
            try {
                int fileIndex = Integer.parseInt(opt) - 1;
                game.loadBuiltInLevelFromFile(fileIndex);
            } catch (Exception ex) {
                System.out.println("Failed to load built-in level: " + ex.getMessage());
                return;
            }
        } else {
            System.out.println("Invalid option, exiting.");
            return;
        }

        while (true) {
            if (game.checkWin()) break;
            game.printBoard();
            System.out.print("Enter command (up/down/left/right or w/a/s/d, z/undo to undo, q to quit): ");
            String line = scanner.nextLine();
            if (line == null) break;
            String cmd = line.trim();
            if (cmd.isEmpty()) continue;
            String norm = cmd.toLowerCase();

            if (norm.equals("quit") || norm.equals("q")) {
                System.out.println("Game quit after " + game.getMoves() + " moves.");
                break;
            }

            if (norm.equals("undo") || norm.equals("z")) {
                boolean undone = game.undo();
                if (!undone) System.out.println("Nothing to undo");
                continue;
            }

            game.move(cmd);
        }

        scanner.close();
    }
}