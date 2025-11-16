package src;

/**
 * The {@code DeadlockDetector} class provides a set of static methods
 * for identifying various types of deadlock conditions in a Sokoban board.
 * <p>
 * Deadlocks occur when one or more boxes become impossible to move
 * to a target position, thus rendering the current game state unsolvable.
 * This class implements two main detection strategies:
 *
 * <ul>
 *   <li><b>Wall capacity deadlock:</b> Detects horizontal or vertical lines of boxes
 *       trapped between walls when there are fewer targets than boxes in that region.</li>
 *   <li><b>Corner lock:</b> Detects boxes pushed into corners where at least two
 *       adjacent sides are walls and no target is present.</li>
 * </ul>

 */

 public class DeadlockDetector {

    /**
     * Checks for a "wall capacity" deadlock.
     * <p>
     * This condition occurs when a box is pushed into a row or column that
     * lies between two walls, and the number of boxes in that segment exceeds
     * the number of target cells within it. Such a configuration is unsolvable.
     *
     * @param board  the current {@link Board} state
     * @param oldPos the previous position of the moved box
     * @param newPos the new position of the moved box
     * @return {@code true} if a wall-capacity deadlock is detected, otherwise {@code false}
     */
    public static boolean checkWallCapacityDeadlock(Board board, Position oldPos, Position newPos)
    {    
        int r = newPos.row, c = newPos.col;
        
        // Граници на дъската
        if (r < 1 || r > board.grid.length - 1 || c < 1 || c > board.grid[0].length - 1) {
            board.boxes[oldPos.row][oldPos.col] = true;
            return false;
        }

        boolean deadlock = false; 

        // Проверка за хоризонтална линия между стени
        boolean topWallLine = 
            (board.grid[r - 1][c - 1] == Tile.WALL &&
             board.grid[r - 1][c] == Tile.WALL &&
             board.grid[r - 1][c + 1] == Tile.WALL);
        boolean bottomWallLine = 
            (board.grid[r + 1][c - 1] == Tile.WALL &&
             board.grid[r + 1][c] == Tile.WALL &&
             board.grid[r + 1][c + 1] == Tile.WALL);

        if (topWallLine || bottomWallLine) {
            int left = c, right = c;

            // Разширяваме сегмента наляво и надясно, докато няма вътрешна стена
            while (left > 0 && board.grid[r][left - 1] != Tile.WALL) left--;
            while (right < board.grid[0].length - 1 && board.grid[r][right + 1] != Tile.WALL) right++;

            int boxes = 1, targets = 0;
            
            for (int i = left; i <= right; i++) {
                if (board.targets[r][i])         targets++;
                if (board.boxes[r][i] && i != c) boxes++;
            }
            
            deadlock |= boxes > targets; 
        }

        // Проверка за вертикална линия между стени
        boolean leftWallLine = 
            (board.grid[r - 1][c - 1] == Tile.WALL && 
             board.grid[r][c - 1] == Tile.WALL &&
             board.grid[r + 1][c - 1] == Tile.WALL);

        boolean rightWallLine = 
            (board.grid[r - 1][c + 1] == Tile.WALL &&
             board.grid[r][c + 1] == Tile.WALL &&
             board.grid[r + 1][c + 1] == Tile.WALL);

        if (leftWallLine || rightWallLine) {
            int up = r, down = r;

            // Разширяваме сегмента нагоре и надолу, докато няма вътрешна стена
            while (up > 0 && board.grid[up - 1][c] != Tile.WALL) up--;
            while (down < board.grid.length - 1 && board.grid[down + 1][c] != Tile.WALL) down++;

            int boxes = 1, targets = 0;
            
            for (int i = up; i <= down; i++) {
                if (board.targets[i][c])         targets++;                
                if (board.boxes[i][c] && i != r) boxes++;
            }

            deadlock |= boxes > targets; 
        }

        return deadlock;
    }

    /**
     * Checks whether a given box is trapped in a corner.
     * <p>
     * A "corner lock" occurs when a box is located at a position where
     * two adjacent cells are walls (forming a 90° angle), and the box
     * is not placed on a target. Such boxes can never be moved again.
     *
     * @param board  the current {@link Board} state
     * @param boxPos the position of the box to check
     * @return {@code true} if the box is locked in a non-target corner,
     *         otherwise {@code false}
     */
    public static boolean isCornerLocked(Board board, Position boxPos) 
    {
        if (board.isTarget(boxPos)) return false;

        int r = boxPos.row;
        int c = boxPos.col;
    
        boolean up    = board.charAt(new Position(r - 1, c)) == Tile.WALL;
        boolean down  = board.charAt(new Position(r + 1, c)) == Tile.WALL;
        boolean left  = board.charAt(new Position(r, c - 1)) == Tile.WALL;
        boolean right = board.charAt(new Position(r, c + 1)) == Tile.WALL;
    
        return (up && left) || (up && right) || (down && left) || (down && right);
    }

    /**
     * Performs a combined deadlock check that includes both
     * {@link #checkWallCapacityDeadlock(Board, Position, Position)} and
     * {@link #isCornerLocked(Board, Position)}.
     * <p>
     *
     * @param board  the current {@link Board} state
     * @param oldPos the previous position of the moved box
     * @param newPos the new position of the moved box
     * @return {@code true} if a deadlock is detected, otherwise {@code false}
     */
    public static boolean isDeadlocked(Board board, Position oldPos, Position newPos)
    {  
        return checkWallCapacityDeadlock(board, oldPos, newPos) || isCornerLocked(board, newPos);
    }

}
