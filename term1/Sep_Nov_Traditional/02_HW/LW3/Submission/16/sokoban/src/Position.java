package src;

import java.util.Objects;

/**
 * The {@code Position} class represents a coordinate position on the game board
 * using row and column indices.
 * <p>
 * <p>
 */
public class Position {
    /** The row index (zero-based from top) */
    public final int row;
    
    /** The column index (zero-based from left) */
    public final int col;

    /**
     * Constructs a new position with the given coordinates.
     *
     * @param row the row index
     * @param col the column index
     */
    Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Compares this position to the specified object for equality.
     * Two positions are considered equal if they have the same row and column values.
     *
     * @param o the object to compare with
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)                return true;
        if (!(o instanceof Position)) return false;
        
        Position other = (Position) o;
        return row == other.row && col == other.col;
    }

    /**
     * Returns a hash code value for this position.
     * The hash code is based on both row and column values.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}