package src;

import java.util.Random;

/**
 * The {@code BoardGenerator} class provides functionality for generating
 * random Sokoban puzzle boards with specified dimensions and number of boxes.
 * <p>
 * The generator creates playable boards by:
 * <ul>
 *   <li>Creating a bordered playing area with walls on the edges</li>
 *   <li>Placing the player in the center of the board</li>
 *   <li>Randomly distributing targets and boxes while avoiding deadlocks</li>
 *   <li>Validating each box placement to ensure solvability</li>
 * </ul>
 * <p>
 * The generated boards guarantee that no boxes are initially placed in
 * deadlocked positions that would make the puzzle unsolvable.
 */
public class BoardGenerator {
    private static final Random rand = new Random();
    
    /**
     * Generates a random Sokoban board with the specified dimensions and number of boxes.
     * <p>
     *
     * @param rows the number of rows in the generated board
     * @param cols the number of columns in the generated board  
     * @param numBoxes the number of boxes (and targets) to place on the board
     * @return a fully generated and validated {@link Board} instance
     */
    public static Board generate(int rows, int cols, int numBoxes) {
        Tile[][] grid = new Tile[rows][cols];
        boolean[][] boxes = new boolean[rows][cols];
        boolean[][] targets = new boolean[rows][cols];
        
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                grid[i][j] = (i == 0 || j == 0 || i == rows - 1 || j == cols - 1)
                    ? Tile.WALL
                    : Tile.EMPTY;
            
        Position player = new Position(rows / 2, cols / 2);
        grid[player.row][player.col] = Tile.PLAYER;
        
        for (int i = 0; i < numBoxes; i++) {
            Position target = getRandomEmptyPosition(grid);
            targets[target.row][target.col] = true;
            grid[target.row][target.col] = Tile.TARGET;
        }
        
        Board board = new Board(grid, player, boxes, targets, numBoxes);
                
        for(int i = 0; i < numBoxes;) {
            Position box = getRandomEmptyPosition(grid);
            board.boxes[box.row][box.col] = true;
            board.grid[box.row][box.col] = Tile.BOX;

            if(DeadlockDetector.isDeadlocked(board, box, box)) {
                board.boxes[box.row][box.col] = false;
                board.grid[box.row][box.col] = Tile.EMPTY;
                continue;
            }

            i++;
        }
        
        return board;
    }
    
    /**
     * Finds a random empty position on the grid.
     * <p>
     * This method continuously generates random coordinates until it finds
     * a position that contains an {@link Tile#EMPTY} tile, ensuring valid
     * placement locations for game elements.
     *
     * @param grid the game grid to search for empty positions
     * @return a {@link Position} with empty tile coordinates
     */
    private static Position getRandomEmptyPosition(Tile[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        while (true) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (grid[r][c] == Tile.EMPTY) return new Position(r, c);
        }
    }
}