import java.util.*;

public class Sokoban_game {
    //CONSTANTS
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    //GAME STATE
    private int width, height, boxes_count;
    private int player_x, player_y;
    private int moves, boxes_on_targets;

    private char[][] display_board;
    private boolean[][] is_target;
    private boolean[][] is_box;

    private Random rand = new Random();

    //UNDO SYSTEM  
    private class GameState {
        char[][] boardCopy;
        int playerX, playerY;
        int boxesOnTargets;
        int moves;

        GameState(char[][] board, int px, int py, int boxes, int moves) {
            this.boardCopy = new char[height][width];
            for (int i = 0; i < height; i++) {
                System.arraycopy(board[i], 0, this.boardCopy[i], 0, width);
            }
            this.playerX = px;
            this.playerY = py;
            this.boxesOnTargets = boxes;
            this.moves = moves;
        }
    }

    private Stack<GameState> history = new Stack<>();

    //INITIALIZATION  
    public Sokoban_game() {}

    public void initialize_game(int width, int height, int boxes_count) {
        this.width = width;
        this.height = height;
        this.boxes_count = boxes_count;
        this.moves = 0;
        this.boxes_on_targets = 0;

        display_board = new char[height][width];
        is_target = new boolean[height][width];
        is_box = new boolean[height][width];

        //Fill board with empty spaces and border walls
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    display_board[y][x] = WALL;
                } else {
                    display_board[y][x] = EMPTY;
                }
            }
        }

        place_player();
        place_targets();
        place_boxes();
        update_display();
    }

    //PLACE PLAYER  
    private void place_player() {
        player_x = width / 2;
        player_y = height / 2;
    }

    //PLACE TARGETS  
    private void place_targets() {
        int placed = 0;
        while (placed < boxes_count) {
            int x = 1 + rand.nextInt(width - 2);
            int y = 1 + rand.nextInt(height - 2);
            if ((x != player_x || y != player_y) && !is_target[y][x]) {
                is_target[y][x] = true;
                placed++;
            }
        }
    }

    //PLACE BOXES  
    private void place_boxes() {
        int placed = 0;
        while (placed < boxes_count) {
            int x = 1 + rand.nextInt(width - 2);
            int y = 1 + rand.nextInt(height - 2);
            if (!is_box[y][x] && !is_target[y][x] && (x != player_x || y != player_y)) {
                is_box[y][x] = true;
                placed++;
            }
        }
    }

    //UPDATE DISPLAY  
    private void update_display() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    display_board[y][x] = WALL;
                } else {
                    if (is_box[y][x] && is_target[y][x]) {
                        display_board[y][x] = BOX_ON_TARGET;
                    } else if (is_box[y][x]) {
                        display_board[y][x] = BOX;
                    } else if (is_target[y][x]) {
                        display_board[y][x] = TARGET;
                    } else {
                        display_board[y][x] = EMPTY;
                    }
                }
            }
        }
        display_board[player_y][player_x] = PLAYER;
    }

    //MOVE PLAYER  
    public boolean move(String direction) {
        int dx = 0, dy = 0;
        switch (direction.toLowerCase()) {
            case "up":
            case "w":
                dy = -1;
                break;
            case "down":
            case "s":
                dy = 1;
                break;
            case "left":
            case "a":
                dx = -1;
                break;
            case "right":
            case "d":
                dx = 1;
                break;
            default:
                System.out.println("Invalid command!");
                return false;
        }

        //Save state before moving
        history.push(new GameState(display_board, player_x, player_y, boxes_on_targets, moves));

        int nx = player_x + dx;
        int ny = player_y + dy;

        //Wall check
        if (display_board[ny][nx] == WALL) {
            System.out.println("Invalid move: wall ahead!");
            return false;
        }

        //If next cell has a box
        if (is_box[ny][nx]) {
            int bx = nx + dx;
            int by = ny + dy;

            //Check if box can move
            if (display_board[by][bx] == WALL || is_box[by][bx]) {
                System.out.println("Invalid move: box cannot be pushed!");
                return false;
            }

            //Move box
            is_box[ny][nx] = false;
            is_box[by][bx] = true;

            //Update targets counter
            if (is_target[ny][nx]) boxes_on_targets--;
            if (is_target[by][bx]) boxes_on_targets++;
        }

        //Move player
        player_x = nx;
        player_y = ny;
        moves++;
        update_display();

        return true;
    }

    //CHECK WIN  
    public boolean check_win() {
        if (boxes_on_targets == boxes_count) {
            System.out.println("🎉 You win in " + moves + " moves!");
            return true;
        }
        return false;
    }

    //PRINT BOARD  
    public void print_board() {
        update_display();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(display_board[y][x]);
            }
            System.out.println();
        }
        System.out.println("Moves: " + moves);
    }

    //UNDO MOVE  
    public boolean undo_move() {
        if (history.isEmpty()) {
            System.out.println("No moves to undo!");
            return false;
        }

        GameState previous = history.pop();
        for (int i = 0; i < height; i++) {
            System.arraycopy(previous.boardCopy[i], 0, display_board[i], 0, width);
        }

        player_x = previous.playerX;
        player_y = previous.playerY;
        boxes_on_targets = previous.boxesOnTargets;
        moves = previous.moves;

        System.out.println("Move undone!");
        return true;
    }

    //GETTER  
    public int getMoves() {
        return moves;
    }

    //MAIN  
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Sokoban_game game = new Sokoban_game();

        int W, H, K;

        // Input with validation
        while (true) {
            System.out.print("Enter width (>=5): ");
            W = sc.nextInt();
            System.out.print("Enter height (>=5): ");
            H = sc.nextInt();
            System.out.print("Enter number of boxes: ");
            K = sc.nextInt();

            if (W >= 5 && H >= 5 && K <= ((W - 2) * (H - 2) - 2) / 2) break;
            System.out.println("Invalid input, please try again!");
        }

        game.initialize_game(W, H, K);

        while (true) {
            game.print_board();
            System.out.print("Command (w/a/s/d or up/down/left/right, undo, quit): ");
            String cmd = sc.next();

            if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("q")) {
                System.out.println("Game exited.");
                break;
            }

            if (cmd.equalsIgnoreCase("undo")) {
                game.undo_move();
                continue;
            }

            game.move(cmd);

            if (game.check_win()) break;
        }

        sc.close();
    }
}
