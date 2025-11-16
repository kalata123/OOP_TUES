import java.io.*;
import java.util.*;

import static java.lang.System.exit;

public class ReadLevel {

    static final char WALL = '#';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    public int width, height;
    public int playerX, playerY;
    public int boxesCount = 0;
    public int targetsCount = 0;
    public int boxesOnTarget = 0;
    public int elemLimit = 0;

    public char[][] board;
    public boolean[][] isBox;
    public boolean[][] isTarget;
    public boolean[][] isBoxOnTarget;
    public boolean[][] isWall;

    public void loadLevel(String filename) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            height = lines.size();
            width = lines.getFirst().length();

            board = new char[height][width];
            isBox = new boolean[height][width];
            isTarget = new boolean[height][width];
            isBoxOnTarget = new boolean[height][width];
            isWall = new boolean[height][width];

            boolean playerFound = false;

            for (int y = 0; y < height; y++) {
                line = lines.get(y);
                for (int x = 0; x < width; x++) {
                    char c = line.charAt(x);
                    board[y][x] = c;

                    switch (c) {
                        case PLAYER:
                            if (playerFound) throw new IllegalArgumentException("Multiple players in map!");
                            playerFound = true;
                            playerX = x;
                            playerY = y;
                            break;
                        case BOX:
                            isBox[y][x] = true;
                            boxesCount++;
                            break;
                        case TARGET:
                            isTarget[y][x] = true;
                            targetsCount++;
                            elemLimit ++;
                            break;
                        case BOX_ON_TARGET:
                            isBoxOnTarget[y][x] = true;
                            boxesOnTarget++;
                            break;
                        case WALL:
                            isWall[y][x] = true;
                            break;
                    }
                }
            }
            reader.close();

            if (!playerFound) throw new IllegalArgumentException("No player found!");
            if (boxesCount > targetsCount) throw new IllegalArgumentException("More boxes than targets!");
        } catch (IOException e) {
            System.out.println("Error reading level file: " + e.getMessage());
            exit(1);
        }
    }
}
