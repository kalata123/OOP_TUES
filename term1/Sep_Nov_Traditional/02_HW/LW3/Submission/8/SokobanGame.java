public static class SokobanGame {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    // Game state
    private int width, height, boxesCount;
    private int playerX, playerY;
    private int moves, boxesOnTargets;

    // Game board arrays
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;

    //undo
    private char[][] oldBoard;
    private int oldX, oldY;
    private boolean[][] hadBox;
    private int oldBOT;

    //random
    private final Random rand = new Random();

    public void initializeGame(int w, int h, int k, int l) {
        if (w < 5 || h < 5){
            System.err.println("Invalid width or height");
        }
        if (k < 1){
            System.err.println("Invalid number of boxes");
        }

        this.width = w;
        this.height = h;
        this.boxesCount = k;
        this.moves = 0;
        this.boxesOnTargets = 0;

        displayBoard = new char[h][w];
        isTarget = new boolean[h][w];
        hasBox = new boolean[h][w];

        for (int j = 0; j < h; j++){
            for (int i = 0; i < w; i++){
                if (j == 0 || j == h - 1 || i == 0 || i == w - 1){
                    displayBoard[j][i] = WALL;
                }else{
                    displayBoard[j][i] = EMPTY;
                }
            }
        }

        //pre-made levels
        String[][] level = new String[4][];

        level[1] = new String[]{
                "#####",
                "#...#",
                "#.@.#",
                "#.B*#",
                "#####"
        };

        level[2] = new String[]{
                "##########",
                "#.B...*..#",
                "#*B......#",
                "#....@B..#",
                "#..*.....#",
                "##########"
        };

        level[3] = new String[]{
                "################",
                "#.............*#",
                "#........B.....#",
                "#..............#",
                "#..*..*........#",
                "#...........B.B#",
                "#.......@.*....#",
                "#..*...........#",
                "#...B....B.....#",
                "#.B.....*......#",
                "#........B....*#",
                "################"
        };

        if(l > 0 && l <= 3){
            level(level[l]);
        }else{
            placePlayer();
            placeTargets();
            placeBoxes();
        }

        updateDisplay();
    }

    private void placePlayer() {
        playerX = width / 2;
        playerY = height / 2;
    }

    private void placeTargets() {
        for (int i = 0; i < boxesCount; i++){
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;

            if (x == playerX && y == playerY){
                y += 1;
            }

            if (isTarget[y][x]){
                continue;
            }

            isTarget[y][x] = true;
        }
    }

    private void placeBoxes() {
        int targetsTop = 0, targetsBottom = 0, targetsLeft = 0, targetsRight = 0;

        for (int i = 1; i <= width - 2; i++){
            if (isTarget[1][i]) targetsTop++;
            if (isTarget[height - 2][i]) targetsBottom++;
        }

        for (int j = 1; j <= height - 2; j++){
            if (isTarget[j][1]) targetsLeft++;
            if (isTarget[j][width - 2]) targetsRight++;
        }

        int boxesTop = 0, boxesBottom = 0, boxesLeft = 0, boxesRight = 0;
        int placed = 0;

        while (placed < boxesCount){
            int x = rand.nextInt(width - 2) + 1;
            int y = rand.nextInt(height - 2) + 1;

            if (x == playerX && y == playerY){
                continue;
            }
            if (isTarget[y][x]){
                continue;
            }
            if (hasBox[y][x]){
                continue;
            }

            boolean isCorner = (x == 1 && y == 1) || (x == width - 2 && y == 1) ||
                    (x == 1 && y == height - 2) || (x == width - 2 && y == height - 2);

            if (isCorner && !isTarget[y][x]){
                continue;
            }

            boolean nextToTop = (y == 1);
            boolean nextToBottom = (y == height - 2);
            boolean nextToLeft = (x == 1);
            boolean nextToRight = (x == width - 2);

            if (nextToTop && boxesTop + 1 > targetsTop) continue;
            if (nextToBottom && boxesBottom + 1 > targetsBottom) continue;
            if (nextToLeft && boxesLeft + 1 > targetsLeft) continue;
            if (nextToRight && boxesRight + 1 > targetsRight) continue;

            hasBox[y][x] = true;
            placed++;

            if (nextToTop) boxesTop++;
            if (nextToBottom) boxesBottom++;
            if (nextToLeft) boxesLeft++;
            if (nextToRight) boxesRight++;
        }

        if (placed < boxesCount){
            System.out.println("Failed to place boxes");
            System.exit(1);
        }
    }

    public boolean move(String direction) {
        oldBoard = new char[height][width];
        hadBox = new boolean[height][width];

        for (int j = 0; j < height; j++){
            for (int i = 0; i < width; i++){
                oldBoard[j][i] = displayBoard[j][i];
                hadBox[j][i] = hasBox[j][i];
            }
        }

        oldX = playerX;
        oldY = playerY;
        oldBOT = boxesOnTargets;

        if (direction == null || direction.isEmpty()){
            return false;
        }

        int dirX = 0, dirY = 0;
        char c = Character.toLowerCase(direction.charAt(0));

        switch (c) {
            case 'w':
                dirY = -1;
                break;
            case 's':
                dirY = 1;
                break;
            case 'a':
                dirX = -1;
                break;
            case 'd':
                dirX = 1;
                break;
            case 'u':
                break;
            default:
                return false;
        }

        int newX = playerX + dirX;
        int newY = playerY + dirY;

        if (newX == 0 || newX == width - 1 || newY == 0 || newY == height - 1) {
            return false;
        }

        if (hasBox[newY][newX]){
            int bX = newX + dirX;
            int bY = newY + dirY;

            if (bY == 0 || bY == height - 1 || bX == 0 || bX == width - 1) {
                return false;
            }

            if (hasBox[bY][bX]){
                return false;
            }

            boolean boxWasOnTarget = isTarget[newY][newX];
            boolean boxWillBeOnTarget = isTarget[bY][bX];

            hasBox[newY][newX] = false;
            hasBox[bY][bX] = true;

            if (boxWasOnTarget && !boxWillBeOnTarget){
                boxesOnTargets--;
            } else if (!boxWasOnTarget && boxWillBeOnTarget){
                boxesOnTargets++;
            }
        }

        playerX = newX;
        playerY = newY;
        moves++;
        updateDisplay();
        return true;
    }

    private void updateDisplay(){
        for (int j = 1; j < height - 1; j++){
            for (int i = 1; i < width - 1; i++){
                if (isTarget[j][i]){
                    displayBoard[j][i] = TARGET;
                }else{
                    displayBoard[j][i] = EMPTY;
                }
            }
        }

        for (int j = 1; j < height - 1; j++){
            for (int i = 1; i < width - 1; i++){
                if (hasBox[j][i]){
                    if (isTarget[j][i]){
                        displayBoard[j][i] = BOX_ON_TARGET;
                    }else{
                        displayBoard[j][i] = BOX;
                    }
                }
            }
        }

        displayBoard[playerY][playerX] = PLAYER;

        for (int i = 0; i < width; i++) {
            displayBoard[0][i] = WALL;
            displayBoard[height - 1][i] = WALL;
        }
        for (int j = 0; j < height; j++) {
            displayBoard[j][0] = WALL;
            displayBoard[j][width - 1] = WALL;
        }
    }

    public void printBoard() {
        updateDisplay();
        for (int j = 0; j < height; j++){
            System.out.println(displayBoard[j]);
        }
        System.out.printf("Moves: %d  |  Boxes on targets: %d / %d%n", moves, boxesOnTargets, boxesCount);
        System.out.println("Controls: W - up, A - left, D - right, S - down, U - undo, Q - quit");
    }

    public void undo(){
        if(moves > 0){
            for (int j = 0; j < height; j++){
                for (int i = 0; i < width; i++){
                    displayBoard[j][i] = oldBoard[j][i];
                    hasBox[j][i] = hadBox[j][i];
                }
            }

            playerX = oldX;
            playerY = oldY;
            boxesOnTargets = oldBOT;
            moves--;
            updateDisplay();
        }else{
            System.out.println("There are no moves left");
        }
    }

    public void level(String[] board){
        height = board.length;
        width = board[0].length();

        displayBoard = new char[height][width];
        isTarget = new boolean[height][width];
        hasBox = new boolean[height][width];

        boxesCount = 0;
        boxesOnTargets = 0;
        moves = 0;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                char c = board[j].charAt(i);
                displayBoard[j][i] = c;

                switch (c) {
                    case WALL:
                        displayBoard[j][i] = WALL;
                        break;
                    case EMPTY:
                        displayBoard[j][i] = EMPTY;
                        break;
                    case PLAYER:
                        playerX = i;
                        playerY = j;
                        break;
                    case BOX:
                        hasBox[j][i] = true;
                        boxesCount++;
                        break;
                    case TARGET:
                        isTarget[j][i] = true;
                        break;
                    case BOX_ON_TARGET:
                        isTarget[j][i] = true;
                        hasBox[j][i] = true;
                        boxesCount++;
                        boxesOnTargets++;
                        break;
                    default:
                        displayBoard[j][i] = EMPTY;
                }
            }
        }
        updateDisplay();
    }
}

public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Mini Sokoban Game");

    int w, h, k;
    int level;

    while (true){
        try{
            System.out.println("Enter which level you want to play! \n 0 - Custom (Generate a board)\n 1 - Easy\n 2 - Medium\n 3 - Hard ");
            level = sc.nextInt();
            if (level == 0) {
                System.out.println("Enter Width: ");
                w = sc.nextInt();
                System.out.println("Enter Height: ");
                h = sc.nextInt();
                System.out.println("Enter number of Boxes: ");
                k = sc.nextInt();

                if (w < 0 || h < 0 || k < 0) {
                    System.out.println("Board or boxes can't be a negative number. Try again:");
                    continue;
                }

                if (k >= ((w - 2) * (h - 2) - 2) / 2) {
                    System.out.println("Can't fit the boxes: The board is too small! Try again:");
                    continue;
                }
                break;
            }else{
                w = 5;
                h = 5;
                k = 1;
                break;
            }

        }catch (Exception e){
            System.out.println("Wrong input bruh! Try again:");
            main(new String[]{""});
        }
    }

    SokobanGame game = new SokobanGame();
    try{
        game.initializeGame(w, h, k, level);
    }catch (Exception e){
        System.out.println("Failed to initialize game: ");
        System.exit(1);
    }

    while (true) {
        if (game.boxesOnTargets == game.boxesCount) {
            game.printBoard();
            System.out.println("\nCongratulations! You beat it!");
            System.out.printf("Total moves: %d\n", game.moves);
            break;
        }
        game.printBoard();
        String cmd = sc.nextLine();

        if (cmd == null){
            break;
        }

        cmd = cmd.trim();

        if (cmd.equalsIgnoreCase("q")){
            System.out.println("Quitting.");
            System.exit(1);
        }else if(cmd.equalsIgnoreCase("u")) {
            game.undo();
            continue;
        }else if(cmd.equalsIgnoreCase("r")){
            main(new String[]{""});
        }

        if (cmd.isEmpty()){
            continue;
        }

        game.move(cmd);
    }
}
