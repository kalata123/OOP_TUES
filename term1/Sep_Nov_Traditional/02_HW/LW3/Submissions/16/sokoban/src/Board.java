package src;

/**
 * The {@code Board} class represents the game board for a Sokoban puzzle.
 * It maintains the complete state of the game including player position, 
 * box positions, target locations, and dynamically generates the visual grid.
 * <p>
 * The board consists of:
 * <ul>
 *   <li>A player position tracking the current location of the player</li>
 *   <li>Boolean matrices for box and target positions for efficient state management</li>
 *   <li>A count of total boxes in the level</li>
 *   <li>A dynamically generated 2D grid of {@link Tile} objects representing the visual layout</li>
 * </ul>
 * <p>
 * The class provides methods for querying and modifying the board state,
 * as well as creating deep copies for undo functionality and state tracking.
 * The visual grid is automatically synchronized with the current game state.
 */
class Board {
    /** The 2D grid representing the visual layout of tiles on the board (dynamically generated) */
    public Tile[][] grid;
    
    /** The current position of the player on the board */
    public Position player;
    
    /** Boolean matrix where {@code true} indicates a box at that position */
    public boolean[][] boxes;     
    
    /** Boolean matrix where {@code true} indicates a target at that position */
    public boolean[][] targets;    

    /** The total number of boxes in this level */
    public int totalBoxes;         
    
    /** The number of rows in the board */
    public final int rows;
    
    /** The number of columns in the board */
    public final int cols;
    
    /**
     * Constructs a new Board with the specified components and dynamically generates the visual grid.
     * <p>
     * The grid is automatically built from the current state including boxes, targets, and player position,
     * ensuring visual consistency with the logical game state.
     *
     * @param rows the number of rows in the board
     * @param cols the number of columns in the board
     * @param player the starting position of the player
     * @param boxes boolean matrix indicating box positions
     * @param targets boolean matrix indicating target positions
     * @param totalBoxes the total number of boxes in the level
     * @throws IllegalArgumentException if any component is null or if array dimensions are inconsistent
     */
    public Board(int rows, int cols, Position player, boolean[][] boxes, boolean[][] targets, int totalBoxes) 
    throws IllegalArgumentException
    {
        if (boxes == null || targets == null || player == null) 
            throw new IllegalArgumentException("Board components cannot be null");
        
        if (boxes.length != rows || targets.length != rows) 
            throw new IllegalArgumentException("Boxes and targets must have same dimensions as specified rows");
        
        if (boxes.length > 0 && boxes[0].length != cols) 
            throw new IllegalArgumentException("Array dimensions mismatch with specified columns");
        
        this.rows = rows;
        this.cols = cols;
        this.player = player;
        this.boxes = boxes;
        this.targets = targets;
        this.totalBoxes = totalBoxes;
        
        // Dynamically generate the visual grid from current state
        this.grid = buildGridFromState();
    }

    /**
     * Constructs a new Board with the specified components including a pre-existing grid.
     * <p>
     * This constructor is maintained for backward compatibility but the grid will be
     * regenerated to ensure consistency with the box and target positions.
     *
     * @param grid the 2D array of tiles representing the board layout (will be regenerated)
     * @param player the starting position of the player
     * @param boxes boolean matrix indicating box positions
     * @param targets boolean matrix indicating target positions
     * @param totalBoxes the total number of boxes in the level
     * @throws IllegalArgumentException if any component is null or if array dimensions are inconsistent
     */
    public Board(Tile[][] grid, Position player, boolean[][] boxes, boolean[][] targets, int totalBoxes) 
    throws IllegalArgumentException
    {
        this(grid.length, grid[0].length, player, boxes, targets, totalBoxes);
    }

    /**
     * Creates a deep copy of the specified Board.
     * <p>
     * This constructor performs a complete deep copy of all board components,
     * including cloning all arrays to ensure independent state management.
     * The visual grid is automatically regenerated from the copied state.
     * This is essential for undo functionality and state tracking.
     *
     * @param other the Board to copy
     */
    public Board(Board other) 
    {
        this.rows = other.rows;
        this.cols = other.cols;
        this.totalBoxes = other.totalBoxes;
        this.player = new Position(other.player.row, other.player.col);
        
        // Deep copy boolean matrices
        this.boxes = new boolean[other.boxes.length][];
        for (int i = 0; i < other.boxes.length; i++) 
            this.boxes[i] = other.boxes[i].clone();
        
        this.targets = new boolean[other.targets.length][];
        for (int i = 0; i < other.targets.length; i++) 
            this.targets[i] = other.targets[i].clone();
        
        // Regenerate grid from the copied state to ensure consistency
        this.grid = buildGridFromState();
    }

    /**
     * Builds the visual grid representation from the current game state.
     * <p>
     * This method constructs the tile grid by analyzing the current positions
     * of boxes, targets, and the player, ensuring the visual representation
     * always matches the logical game state.
     *
     * @return a newly generated 2D array of tiles representing the current board state
     */
    private Tile[][] buildGridFromState() {
        Tile[][] newGrid = new Tile[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Determine tile based on current state
                if (boxes[i][j] && targets[i][j]) {
                    newGrid[i][j] = Tile.BOX_ON_TARGET;
                } else if (boxes[i][j]) {
                    newGrid[i][j] = Tile.BOX;
                } else if (targets[i][j]) {
                    newGrid[i][j] = Tile.TARGET;
                } else if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                    newGrid[i][j] = Tile.WALL;
                } else {
                    newGrid[i][j] = Tile.EMPTY;
                }
            }
        }
        
        // Place player on the grid (overriding the underlying tile)
        newGrid[player.row][player.col] = Tile.PLAYER;
        
        return newGrid;
    }

    /**
     * Returns the tile at the specified position.
     *
     * @param pos the position to query
     * @return the {@code Tile} at the given position
     */
    public Tile charAt(Position pos) { 
        return grid[pos.row][pos.col]; 
    }
        
    /**
     * Checks if the specified position contains a target.
     *
     * @param pos the position to check
     * @return {@code true} if the position is a target, {@code false} otherwise
     */
    public boolean isTarget(Position pos) { 
        return targets[pos.row][pos.col]; 
    }
    
    /**
     * Sets the tile type at the specified position.
     * <p>
     * Note: This method primarily affects visual representation. For logical
     * state changes, use the appropriate methods like {@link #setBox}.
     *
     * @param pos the position to modify
     * @param tile the new tile type for this position
     */
    public void setAt(Position pos, Tile tile) { grid[pos.row][pos.col] = tile; }

    /**
     * Sets or removes a box at the specified position.
     *
     * @param pos the position to modify
     * @param value {@code true} to place a box, {@code false} to remove it
     */
    public void setBox(Position pos, boolean value) { boxes[pos.row][pos.col] = value; }
    
    /**
     * Displays the current board state to the console.
     * <p>
     * This method prints the textual representation of the board using
     * the symbol characters from each tile. Useful for debugging and
     * command-line interface display.
     */
    public void display() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++)
                System.out.print(grid[i][j].symbol);
            System.out.println();
        }
    }
}