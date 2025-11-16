package src;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code StateDelta} class represents the changes between two game states
 * in a Sokoban game. It tracks movements of the player and boxes, as well as
 * changes in the number of boxes placed on targets.
 * <p>
 * This class is used to efficiently update the game state without storing
 * complete copies of the board, enabling undo functionality and state tracking.
 */
class StateDelta {
    /** The player's previous position */
    Position oldPlayerPos;
    
    /** The player's new position */
    Position newPlayerPos;
    
    /** List of box movements that occurred in this state change */
    List<BoxMove> boxMoves;  
    
    /** The change in the number of boxes on targets */
    int boxesOnTargetDelta; 
    
    /**
     * Constructs a new {@code StateDelta} with the given player positions.
     *
     * @param oldPlayerPos the player's position before the move
     * @param newPlayerPos the player's position after the move
     */
    public StateDelta(Position oldPlayerPos, Position newPlayerPos) {
        this.oldPlayerPos = oldPlayerPos;
        this.newPlayerPos = newPlayerPos;
        this.boxMoves = new ArrayList<>();
        this.boxesOnTargetDelta = 0;
    }
    
    /**
     * Adds a box movement to this state delta.
     *
     * @param oldPos the box's previous position
     * @param newPos the box's new position
     * @param delta the change in boxes-on-target count caused by this move
     */
    public void addBoxMove(Position oldPos, Position newPos, int delta) {
        boxMoves.add(new BoxMove(oldPos, newPos));
        this.boxesOnTargetDelta += delta;
    }
    
    /**
     * The {@code BoxMove} class represents the movement of a single box
     * from one position to another.
     */
    static class BoxMove {
        /** The box's previous position */
        Position oldPos;
        
        /** The box's new position */
        Position newPos;
        
        /**
         * Constructs a new box movement record.
         *
         * @param oldPos the box's starting position
         * @param newPos the box's ending position
         */
        BoxMove(Position oldPos, Position newPos) {
            this.oldPos = oldPos;
            this.newPos = newPos;
        }
    }
}