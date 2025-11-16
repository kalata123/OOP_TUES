package src;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.terminal.Attributes;

import java.util.Stack;

/**
 * The {@code Game} class manages the core gameplay logic and state for a Sokoban puzzle.
 * <p>
 * This class handles:
 * <ul>
 *   <li>Player movement and box pushing mechanics</li>
 *   <li>Game state tracking (moves, boxes on targets)</li>
 *   <li>Undo functionality using state deltas</li>
 *   <li>Interactive terminal-based gameplay</li>
 *   <li>Win condition detection</li>
 * </ul>
 * <p>
 * The game maintains a history of moves to support undo operations and ensures
 * that all moves are validated for legality and deadlock avoidance.
 */
class Game {
    private Board board, initialBoard;
    private int boxesOnTarget;
    private int moves;
    private Stack<StateDelta> history = new Stack<>();

    /**
     * Constructs a new Game with the specified board.
     * <p>
     * Initializes the game state by:
     * <ol>
     *   <li>Creating a backup of the initial board for reset functionality</li>
     *   <li>Setting up the initial visual representation of boxes and targets</li>
     *   <li>Placing the player on the board</li>
     * </ol>
     *
     * @param board the starting board configuration
     */
    public Game(Board board) {
        this.initialBoard = new Board(board);
        this.board = board;
        this.boxesOnTarget = 0;
    }

    /**
     * Calculates the new position based on movement direction.
     *
     * @param prev the current position
     * @param direction the movement direction ('w', 's', 'a', 'd')
     * @return the new position after movement
     */
    private Position getNewPos(Position prev, char direction) {
        int newRow = prev.row;
        int newCol = prev.col;

        switch (direction) {
            case 'w': newRow--; break;
            case 's': newRow++; break;
            case 'a': newCol--; break;
            case 'd': newCol++; break;
            default: return prev;
        }

        Position newPos = new Position(newRow, newCol);
        return newPos;
    }

    /**
     * Attempts to move the player in the specified direction.
     * <p>
     * The method handles:
     * <ul>
     *   <li>Wall collision detection</li>
     *   <li>Box pushing mechanics</li>
     *   <li>State delta recording for undo functionality</li>
     *   <li>Move validation and deadlock checking</li>
     * </ul>
     *
     * @param direction the direction to move ('w', 's', 'a', 'd')
     * @return {@code true} if the move was successful, {@code false} otherwise
     */
    public boolean movePlayer(char direction) {
        Position oldPos = board.player;
        Position newPos = getNewPos(board.player, direction);
        Tile nextTile = board.charAt(newPos);

        if (nextTile == Tile.WALL) return false;
        

        StateDelta delta = new StateDelta(oldPos, newPos);

        if (nextTile == Tile.BOX || nextTile == Tile.BOX_ON_TARGET) 
            if (!moveBox(newPos, direction, delta)) return false;
            
        
        Tile currentTile = board.charAt(board.player);
        board.setAt(board.player,
                (currentTile == Tile.PLAYER && board.isTarget(board.player)) ? Tile.TARGET : Tile.EMPTY);
        board.setAt(newPos, Tile.PLAYER);
        board.player = newPos;

        history.push(delta);
        moves++;

        return true;
    }

    /**
     * Attempts to move a box in the specified direction.
     * <p>
     * Handles recursive box pushing, deadlock detection, and target counting.
     *
     * @param boxPos the current position of the box to move
     * @param direction the direction to push the box
     * @param delta the state delta to record box movement
     * @return {@code true} if the box was successfully moved, {@code false} otherwise
     */
    private boolean moveBox(Position boxPos, char direction, StateDelta delta) {
        Position nextPos = getNewPos(boxPos, direction);
        Tile current = board.charAt(boxPos);
        Tile nextTile = board.charAt(nextPos);
    
        if (nextTile == Tile.WALL) return false;
        
    
        if (nextTile == Tile.BOX || nextTile == Tile.BOX_ON_TARGET) {
            if (!moveBox(nextPos, direction, delta)) return false;

            nextTile = board.charAt(nextPos);
        }
    
        boolean wasOnTarget = current == Tile.BOX_ON_TARGET;
        boolean movingToTarget = board.isTarget(nextPos);
    
        board.setBox(boxPos, false);
        board.setAt(boxPos, wasOnTarget ? Tile.TARGET : Tile.EMPTY);
        board.setBox(nextPos, true);
        board.setAt(nextPos, movingToTarget ? Tile.BOX_ON_TARGET : Tile.BOX);
    
        if (DeadlockDetector.isDeadlocked(board, boxPos, nextPos)) {
            board.setBox(nextPos, false);
            board.setAt(nextPos, movingToTarget ? Tile.TARGET : Tile.EMPTY);
            board.setBox(boxPos, true);
            board.setAt(boxPos, wasOnTarget ? Tile.BOX_ON_TARGET : Tile.BOX);

            throw new RuntimeException("Deadlock");
        }
    
        int boxDelta = 0;
        if (wasOnTarget != movingToTarget) {
            boxDelta = movingToTarget ? 1 : -1;
            boxesOnTarget += boxDelta;
        }
    
        // 7. Record box movement in delta
        delta.addBoxMove(boxPos, nextPos, boxDelta);
        return true;
    }
    
    /**
     * Reverts the game state to the previous move.
     * <p>
     * Restores player position, box positions, and target counters using
     * the recorded state delta from the most recent move.
     */
    public void undo() {
        if (history.isEmpty()) throw new RuntimeException("No moves to undo");

        StateDelta delta = history.pop();

        Tile currentPlayerTile = board.charAt(board.player);
        board.setAt(board.player,
                (currentPlayerTile == Tile.PLAYER && board.isTarget(board.player)) ? Tile.TARGET : Tile.EMPTY);

        board.player = delta.oldPlayerPos;
        board.setAt(board.player, Tile.PLAYER);

        for (int i = delta.boxMoves.size() - 1; i >= 0; i--) {
            StateDelta.BoxMove boxMove = delta.boxMoves.get(i);

            board.setBox(boxMove.newPos, false);
            boolean newPosIsTarget = board.isTarget(boxMove.newPos);
            board.setAt(boxMove.newPos, newPosIsTarget ? Tile.TARGET : Tile.EMPTY);

            board.setBox(boxMove.oldPos, true);
            boolean oldPosIsTarget = board.isTarget(boxMove.oldPos);
            board.setAt(boxMove.oldPos, oldPosIsTarget ? Tile.BOX_ON_TARGET : Tile.BOX);
        }

        boxesOnTarget -= delta.boxesOnTargetDelta;

        moves--;
    }

    /**
     * Starts the interactive game loop.
     * <p>
     * Provides a terminal-based interface with the following controls:
     * <ul>
     *   <li>WASD keys for movement</li>
     *   <li>'u' key for undo</li>
     *   <li>'r' key for reset</li>
     *   <li>'q' key to quit</li>
     * </ul>
     *
     * @throws java.io.IOException if terminal input/output fails
     */
    public void play() throws java.io.IOException {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)
                .build();

        Attributes originalAttributes = terminal.enterRawMode();

        try {
            var reader = terminal.reader();

            while (true) {
                board.display();
                int input = reader.read();
                if (input == -1)
                    break;

                char move = (char) input;

                switch (move) {
                    case 'q' -> {
                        System.out.println("\nExited game.");
                        return;
                    }
                    case 'u' -> {
                        undo();
                        continue;
                    }
                    case 'r' -> {
                        board = new Board(initialBoard);
                        boxesOnTarget = 0;
                        moves = 0;
                        history.clear();
                        continue;
                    }
                    default -> {
                        if (movePlayer(move)) 
                            board.display();
                    }
                }

                if (boxesOnTarget == board.totalBoxes) {
                    board.display();
                    System.out.println("🏁 Level complete!");
                    System.out.printf("IN %d MOVES\n", moves);
                    break;
                }
            }
        }catch(Exception e){
            System.err.print("Error: "+ e);
            try{Thread.sleep(1000);}catch(Exception _){};
        }
        finally {
            terminal.setAttributes(originalAttributes);
            terminal.close();
        }
    }

}