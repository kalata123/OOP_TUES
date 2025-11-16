package src;

/**
 * The {@code Tile} enum represents the different types of tiles
 * that can appear on a Sokoban game board.
 * <p>
 * Each tile type is associated with a character symbol that is used
 * for textual representation and parsing of board configurations.
 * <p>
 * <ul>
 *   <li>{@code WALL} - represents an impassable wall</li>
 *   <li>{@code EMPTY} - represents an empty floor space</li>
 *   <li>{@code PLAYER} - represents the player's position</li>
 *   <li>{@code BOX} - represents a movable box</li>
 *   <li>{@code TARGET} - represents a target location for boxes</li>
 *   <li>{@code BOX_ON_TARGET} - represents a box that is on a target</li>
 * </ul>
 */

public enum Tile {
    WALL('#'),
    EMPTY('.'),
    PLAYER('@'),
    BOX('B'),
    TARGET('*'),
    BOX_ON_TARGET('O');

    final char symbol;

    /**
     * Constructs a tile with the specified symbol.
     *
     * @param symbol the character representation of this tile
     */
    Tile(char symbol) { 
        this.symbol = symbol; 
    }
    
    /**
     * Returns the tile corresponding to the given character symbol.
     *
     * @param symbol the character symbol to look up
     * @return the {@code Tile} enum constant with the matching symbol
     * @throws IllegalArgumentException if no tile matches the given symbol
     */
    public static Tile fromSymbol(char symbol) {
        for (Tile tile : values()) {
            if (tile.symbol == symbol) {
                return tile;
            }
        }
        throw new IllegalArgumentException("Невалиден символ за Tile: " + symbol);
    }
}