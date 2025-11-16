package src;

import java.io.*;

/**
 * The {@code BoardParser} class provides functionality for parsing
 * Sokoban-like board configurations from a structured textual format
 * into a fully initialized {@link Board} object.
 * <p>
 * The parser expects an input in the following format:
 *
 * <pre>
 * 10x10                 // Board size: 10 rows, 10 columns
 * 5,5                   // Player position: row 5, column 5
 * 4: 5,2 5,4 7,4 8,3    // 4 boxes at coordinates (5,2), (5,4), (7,4), (8,3)
 * 4: 1,7 6,3 7,8 8,6    // 4 targets at coordinates (1,7), (6,3), (7,8), (8,6)
 * </pre>
 *
 * <p>All coordinates are zero-based and expressed as {@code row,column}.
 * Each line must follow the exact structure described above.
 *
 */
class BoardParser {

    /**
     * Parses a textual board description from a {@link Reader}
     * and returns a new {@link Board} instance.
     *
     * @param reader the input source (for example, {@link StringReader} or {@link FileReader})
     * @return a fully constructed {@link Board}
     * @throws IOException if reading fails
     * @throws IllegalArgumentException if the format is malformed or inconsistent
     */
    public static Board parse(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);

        String sizeLine = br.readLine();
        if (sizeLine == null || !sizeLine.contains("x"))
            throw new IllegalArgumentException("Expected board size in format NxM");
        String[] sizeParts = sizeLine.trim().split("x");
        int rows = Integer.parseInt(sizeParts[0]);
        int cols = Integer.parseInt(sizeParts[1]);

        String playerLine = br.readLine();
        if (playerLine == null || !playerLine.contains(","))
            throw new IllegalArgumentException("Expected player position in format r,c");
        String[] playerParts = playerLine.trim().split(",");
        Position player = new Position(
            Integer.parseInt(playerParts[0].trim()),
            Integer.parseInt(playerParts[1].trim())
        );

        String boxesLine = br.readLine();
        if (boxesLine == null || !boxesLine.contains(":"))
            throw new IllegalArgumentException("Expected boxes line in format count: r1,c1 ...");
        String[] boxesParts = boxesLine.split(":");
        int boxCount = Integer.parseInt(boxesParts[0].trim());
        boolean[][] boxes = new boolean[rows][cols];
        if (boxesParts.length > 1 && !boxesParts[1].isBlank()) {
            String[] boxCoords = boxesParts[1].trim().split("\\s+");
            for (String coord : boxCoords) {
                String[] rc = coord.split(",");
                int r = Integer.parseInt(rc[0]);
                int c = Integer.parseInt(rc[1]);
                boxes[r][c] = true;
            }
        }

        String targetsLine = br.readLine();
        if (targetsLine == null || !targetsLine.contains(":"))
            throw new IllegalArgumentException("Expected targets line in format count: r1,c1 ...");
        String[] targetsParts = targetsLine.split(":");

        boolean[][] targets = new boolean[rows][cols];
        if (targetsParts.length > 1 && !targetsParts[1].isBlank()) {
            String[] targetCoords = targetsParts[1].trim().split("\\s+");
            for (String coord : targetCoords) {
                String[] rc = coord.split(",");
                int r = Integer.parseInt(rc[0]);
                int c = Integer.parseInt(rc[1]);
                targets[r][c] = true;
            }
        }

        Tile[][] grid = new Tile[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = (i == 0 || j == 0 || i == rows - 1 || j == cols - 1)
                    ? Tile.WALL
                    : Tile.EMPTY;

        return new Board(grid, player, boxes, targets, boxCount);
    }
}
