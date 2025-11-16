package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code BoardSaver} class provides functionality for saving Sokoban board states
 * to files in a structured textual format.
 * All coordinates are zero-based and saved in row,column format. The class provides
 * both explicit filename saving and automatic filename generation options.
 */
public class BoardSaver {
    /**
     * Saves the specified board state to a file with the given filename.
     * <p>
     * The method writes the board data in the following format:
     * <pre>
     * 10x10 // Board size: 10 rows, 10 columns
     * 5,5 // Player position: row 5, column 5
     * 4: 5,2 5,4 7,4 8,3 // 4 boxes at coordinates (5,2), (5,4), (7,4), (8,3)
     * 4: 1,7 6,3 7,8 8,6 // 4 targets at coordinates (1,7), (6,3), (7,8), (8,6)
     * </pre>
     *
     * @param board the board to save
     * @param filename the name of the file to create
     * @return the filename where the board was saved
     * @throws IOException if an I/O error occurs during writing
     */
    public static String saveBoard(Board board, String filename) throws IOException {
        // If filename is null, generate one automatically
        if (filename == null) {
            filename = "board_" + System.currentTimeMillis() + ".sok";
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            int rows = board.grid.length;
            int cols = board.grid[0].length;
            
            writer.println(rows + "x" + cols);
            writer.println(board.player.row + "," + board.player.col);
            
            List<String> boxPositions = new ArrayList<>();
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    if (board.boxes[i][j]) boxPositions.add(i + "," + j);
            
            writer.println(boxPositions.size() + ": " + String.join(" ", boxPositions));
            
            List<String> targetPositions = new ArrayList<>();
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    if (board.targets[i][j]) targetPositions.add(i + "," + j);
            
            writer.println(targetPositions.size() + ": " + String.join(" ", targetPositions));
        }
        
        return filename;
    }
}