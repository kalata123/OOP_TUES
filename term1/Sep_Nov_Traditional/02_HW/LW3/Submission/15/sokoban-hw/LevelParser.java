import java.io.*;
import java.util.*;

public class LevelParser {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    
    public static class LevelData {
        public int width;
        public int height;
        public int boxCount;
        public char[][] board;
        public int playerX, playerY;
        public List<int[]> targets;
        public List<int[]> boxes;
        
        public LevelData() {
            this.targets = new ArrayList<>();
            this.boxes = new ArrayList<>();
        }
    }
    
    public static LevelData parseLevel(String filePath) throws IOException, IllegalArgumentException {
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new IOException("Level file not found: " + filePath);
        }
        
        if (!file.canRead()) {
            throw new IOException("Cannot read level file: " + filePath);
        }
        
        LevelData levelData = new LevelData();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String headerLine = reader.readLine();
            
            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IllegalArgumentException("Level file is empty or missing header");
            }
            
            String[] headerParts = headerLine.trim().split("\\s+");
            
            if (headerParts.length != 3) {
                throw new IllegalArgumentException("Invalid header format. Expected: WIDTH HEIGHT BOXCOUNT");
            }
            
            try {
                levelData.width = Integer.parseInt(headerParts[0]);
                levelData.height = Integer.parseInt(headerParts[1]);
                levelData.boxCount = Integer.parseInt(headerParts[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Header values must be integers. Found: " + String.join(" ", headerParts));
            }
            
            if (levelData.width < 5 || levelData.height < 5) {
                throw new IllegalArgumentException("Board dimensions must be at least 5x5. Found: " + levelData.width + "x" + levelData.height);
            }
            
            if (levelData.boxCount <= 0) {
                throw new IllegalArgumentException("Box count must be greater than 0. Found: " + levelData.boxCount);
            }
            
            levelData.board = new char[levelData.height][levelData.width];
            
            for (int y = 0; y < levelData.height; y++) {
                String line = reader.readLine();
                
                if (line == null) {
                    throw new IllegalArgumentException("Board is incomplete. Expected " + levelData.height + " rows, but got only " + y);
                }
                
                if (line.length() != levelData.width) {
                    throw new IllegalArgumentException("Row " + y + " has invalid length. Expected: " + levelData.width + ", Found: " + line.length());
                }
                
                for (int x = 0; x < levelData.width; x++) {
                    char cell = line.charAt(x);
                    
                    if (cell != WALL && cell != EMPTY && cell != PLAYER && cell != BOX && cell != TARGET) {
                        throw new IllegalArgumentException("Invalid character '" + cell + "' at (" + x + "," + y + "). Valid characters: # . @ B *");
                    }
                    
                    levelData.board[y][x] = cell;
                    
                    if (cell == PLAYER) {
                        if (levelData.playerX != 0 || levelData.playerY != 0) {
                            throw new IllegalArgumentException("Multiple player positions found. Player can only appear once.");
                        }
                        levelData.playerX = x;
                        levelData.playerY = y;
                    } else if (cell == BOX) {
                        levelData.boxes.add(new int[]{x, y});
                    } else if (cell == TARGET) {
                        levelData.targets.add(new int[]{x, y});
                    }
                }
            }
            
            if (levelData.playerX == 0 && levelData.playerY == 0 && levelData.board[0][0] != PLAYER) {
                throw new IllegalArgumentException("Player (@) not found in level");
            }
            
            if (levelData.boxes.size() != levelData.boxCount) {
                throw new IllegalArgumentException("Box count mismatch. Header says " + levelData.boxCount + ", but found " + levelData.boxes.size() + " boxes (B)");
            }
            
            if (levelData.targets.size() != levelData.boxCount) {
                throw new IllegalArgumentException("Target count mismatch. Expected " + levelData.boxCount + " targets (*), but found " + levelData.targets.size());
            }
            
            validateBoardBoundaries(levelData);
            
        } catch (FileNotFoundException e) {
            throw new IOException("Level file not found: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Error reading level file: " + e.getMessage());
        }
        
        return levelData;
    }
    
    private static void validateBoardBoundaries(LevelData levelData) throws IllegalArgumentException {
        for (int y = 0; y < levelData.height; y++) {
            for (int x = 0; x < levelData.width; x++) {
                if (y == 0 || y == levelData.height - 1 || x == 0 || x == levelData.width - 1) {
                    if (levelData.board[y][x] != WALL) {
                        throw new IllegalArgumentException("Border cells must be walls. Invalid cell at (" + x + "," + y + ")");
                    }
                }
            }
        }
    }
    
    public static void initialiseGameFromLevel(SokobanGame game, LevelData levelData) {
        game.width = levelData.width;
        game.height = levelData.height;
        game.boxesCount = levelData.boxCount;
        game.moves = 0;
        game.boxesOnTargets = 0;
        
        game.displayBoard = new char[levelData.height][levelData.width];
        game.isTarget = new boolean[levelData.height][levelData.width];
        game.hasBox = new boolean[levelData.height][levelData.width];
        
        for (int y = 0; y < levelData.height; y++) {
            for (int x = 0; x < levelData.width; x++) {
                game.displayBoard[y][x] = levelData.board[y][x];
                game.isTarget[y][x] = false;
                game.hasBox[y][x] = false;
            }
        }
        
        game.playerX = levelData.playerX;
        game.playerY = levelData.playerY;
        
        for (int[] target : levelData.targets) {
            game.isTarget[target[1]][target[0]] = true;
        }
        
        for (int[] box : levelData.boxes) {
            game.hasBox[box[1]][box[0]] = true;
            if (game.isTarget[box[1]][box[0]]) {
                game.boxesOnTargets++;
            }
        }
        
        game.updateDisplay();
    }
    
    public static String getLevelFilePath(int levelNumber) {
        return "levels/level" + levelNumber + ".txt";
    }
    
    public static LevelData loadLevel(int levelNumber) throws IOException, IllegalArgumentException {
        if (levelNumber < 1 || levelNumber > 10) {
            throw new IllegalArgumentException("Level number must be between 1 and 10. Found: " + levelNumber);
        }
        
        String filePath = getLevelFilePath(levelNumber);
        return parseLevel(filePath);
    }
}
