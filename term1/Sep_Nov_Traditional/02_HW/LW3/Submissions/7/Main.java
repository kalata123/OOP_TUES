public class Game {
    private static final char
            WALL = '#',
            EMPTY = '.',
            PLAYER = '@',
            BOX = 'B',
            TARGET = '*',
            BOX_ON_TARGET = 'O';

    private int
            width,
            height,
            boxes,
            playerX,
            playerY,
            boxesOnTargets = 0,
            moves = 0;

    private static final Scanner scanner = new Scanner(System.in);

    private enum State{
        BOX,
        TARGET,
        BOX_ON_TARGET,
        PLAYER,
        EMPTY,
        PLAYER_ON_TARGET,
    }

    private class Point {
        public int x;
        public int y;
        public State state;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
            state = State.EMPTY;
        }

        public Point(Point p) {
            this.x = p.x;
            this.y = p.y;
            this.state = p.state;
        }

        public char getSymbol() {
            return switch (state) {
                case BOX -> BOX;
                case TARGET -> TARGET;
                case BOX_ON_TARGET -> BOX_ON_TARGET;
                case PLAYER, PLAYER_ON_TARGET -> PLAYER;
                case EMPTY -> EMPTY;
                //case BOX_READY, BOX_READY_TARGET -> '?';
            };
        }

    }

    private class GameState {
        public int playerX,playerY;
        public int boxesOnTargets;
        public int moves;
        LinkedList<Point> points;

        public GameState(int playerX, int playerY, int boxesOnTargets, int moves, LinkedList<Point> gamePoints) {
            this.playerX = playerX;
            this.playerY = playerY;
            this.boxesOnTargets = boxesOnTargets;
            this.moves = moves;
            this.points = new LinkedList<Point>();
            for (Point p: gamePoints) {
                Point newPoint = new Point(p.x, p.y);
                newPoint.state = p.state;
                this.points.add(newPoint);
            }
        }
    }

    Stack<GameState> gameStates = new Stack<>();

    private void saveState() {
        GameState newGameState = new GameState(
                playerX,
                playerY,
                boxesOnTargets,
                moves,
                points
        );
        gameStates.push(newGameState);
    }

    private void restoreState(GameState state) {
        this.playerX = state.playerX;
        this.playerY = state.playerY;
        this.boxesOnTargets = state.boxesOnTargets;
        this.moves = state.moves;
        for  (int i = 0; i < points.size(); i++) {
            this.points.get(i).state = state.points.get(i).state;
        }
    }

    private int scanNumber(int requirement, boolean greaterThan, String valueName) {
        IO.print(
                "Enter a number that is " +
                        (greaterThan ? "greater" : "lesser")
                        + " or equal than " + requirement + " for " +  valueName + ": ");

        int n = scanner.nextInt();

        if (greaterThan) {
            if (n >= requirement) {
                return n;
            }
        } else {
            if (n <= requirement) {
                return n;
            }
        }
        return scanNumber(requirement, greaterThan, valueName);
    }

    LinkedList<Point> points = new LinkedList<>();

    private Point getPointAt(int x, int y) {
        for (Point point : points)
            if (point.x == x && point.y == y)
                return point;
        //return null;

        //
        return points.getFirst();
    }

    private void placeBoxes(final int MAX_BOXES) {

        LinkedList<Point> boxPoints = new LinkedList<>();
        for (Point p : points) {
            boxPoints.add(new Point(p));
        }

        //zashtoto ne moga da naprawq func wyw func
        class BoxRules {

            public int topTarget = 0, bottomTarget = 0, leftTarget = 0, rightTarget = 0;
            public int topBox = 0, bottomBox = 0, leftBox = 0, rightBox = 0;
            public int frplacedboxes = 0;
            BoxRules() {
                for (int x = 1; x < width - 1; x++) {
                    if (getPointAt(x, 1).state == State.TARGET) topTarget++;
                    if (getPointAt(x, height - 2).state == State.TARGET) bottomTarget++;
                }
                for (int y = 1; y < height - 1; y++) {
                    if (getPointAt(1, y).state == State.TARGET) leftTarget++;
                    if (getPointAt(width - 2, y).state == State.TARGET) rightTarget++;
                }
            }
            public boolean canPlaceBox (Point p) {
                State s = p.state;
                if (s == State.PLAYER || s == State.BOX)
                    return false;

                boolean isCorner =
                        (p.x == 1 && p.y == 1) ||
                                (p.x == width - 2 && p.y == 1) ||
                                (p.x == 1 && p.y == height - 2) ||
                                (p.x == width - 2 && p.y == height - 2);

                if (isCorner && s != State.TARGET)
                    return false;

                if (p.y == 1 && topBox >= topTarget) return false;
                if (p.y == height - 2 && bottomBox >= bottomTarget) return false;
                if (p.x == 1 && leftBox >= leftTarget) return false;
                if (p.x == width - 2 && rightBox >= rightTarget) return false;

                return true;
            }
            public void placeBOX(Point p) {
                if (p.y == 1) topBox++;
                if (p.y == height - 2) bottomBox++;
                if (p.x == 1) leftBox++;
                if (p.x == width - 2) rightBox++;
                frplacedboxes++;
            }
        }

        BoxRules boxRules = new BoxRules();

        Collections.shuffle(boxPoints);

        for (Point p: boxPoints) {
            if (MAX_BOXES <= boxRules.frplacedboxes)
                break;

            if (boxRules.canPlaceBox(p)) {
                switch (p.state) {
                    case TARGET -> {
                        p.state = State.BOX_ON_TARGET;
                        boxRules.placeBOX(p);
                        getPointAt(p.x, p.y).state = State.BOX_ON_TARGET;
                    }
                    case EMPTY -> {
                        p.state = State.BOX;
                        boxRules.placeBOX(p);
                        getPointAt(p.x, p.y).state = State.BOX;
                    }
                    default -> p.state = State.EMPTY;
                }
            }
        }
    }

    public void movePlayer(int dx, int dy) {
        int newX = playerX + dx;
        int newY = playerY + dy;

        Point next = getPointAt(newX, newY);
        Point current = getPointAt(playerX, playerY);

        if (next == null) return;

        saveState();

        IO.println("Boxes on targets: " + boxesOnTargets);

        switch (next.state) {
            case EMPTY -> {
                switch (current.state) {
                    case PLAYER_ON_TARGET -> {
                        current.state = State.TARGET;
                    }
                    case PLAYER -> {
                        current.state = State.EMPTY;
                    }
                }
                next.state = State.PLAYER;
                playerX = newX;
                playerY = newY;
                moves++;
            }
            case TARGET -> {
                switch (current.state) {
                    case PLAYER_ON_TARGET -> {
                        current.state = State.TARGET;
                    }
                    case PLAYER -> {
                        current.state = State.EMPTY;
                    }
                }next.state = State.PLAYER_ON_TARGET;
                playerX = newX;
                playerY = newY;
                moves++;
            }
            case BOX, BOX_ON_TARGET -> {
                int boxX = newX + dx;
                int boxY = newY + dy;
                Point beyond = getPointAt(boxX, boxY);
                if (beyond != null && (beyond.state == State.EMPTY || beyond.state == State.TARGET)) {
                    // Push box
                    if (next.state == State.BOX_ON_TARGET) boxesOnTargets--;
                    if (beyond.state == State.TARGET) {
                        beyond.state = State.BOX_ON_TARGET;
                        boxesOnTargets++;
                    } else {
                        beyond.state = State.BOX;
                    }

                    // Move player
                    next.state = State.PLAYER;
                    getPointAt(playerX, playerY).state = State.EMPTY;
                    playerX = newX;
                    playerY = newY;

                    moves++;
                } else {
                    IO.println("Invalid move");
                }
            }
            default -> IO.println("Invalid move");
        }
    }

    private void playerSpawn() {
        for (int i = 1; i < this.height - 1; i++)
            for (int j = 1; j < this.width - 1; j++) {
                Point p = new Point(i, j);
                if (i == playerX && j == playerY)
                    p.state = State.PLAYER;
                points.add(p);
            }
    }

    public Game() {
        this.width = scanNumber(5, true, "width");
        this.height = scanNumber(5, true, "height");

        //offset maybe???
        this.playerX = this.width / 2;
        this.playerY = this.height / 2;

        int boxesRequired = ((this.width - 2) * (this.height - 2) - 2) / 2;
        this.boxes = scanNumber(boxesRequired, false, "boxes");

        // player spawn
        playerSpawn();

        // targets spawn
        final Random rand = new Random();
        for (int i = 0; i < this.boxes; i++) {
            int n;
            while(true) {
                n = rand.nextInt(points.size());

                if(points.get(n).state == State.EMPTY) {
                    points.get(n).state = State.TARGET;
                    break;
                }
                if(points.get(n).state == State.PLAYER) {
                    points.get(n).state = State.PLAYER_ON_TARGET;
                }
            }
        }

        placeBoxes(this.boxes);
    }

    public Game(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            int width = Integer.parseInt(lines.get(0).replace("w-", ""));
            int height = Integer.parseInt(lines.get(1).replace("h-", ""));
            int boxes = Integer.parseInt(lines.get(2).replace("k-", ""));


            if (width < 5 && height < 5 && boxes <= (((width - 2) * (height - 2) - 2) / 2)) {
                throw new RuntimeException("file error wtv idk");
            }

            this.width = width;
            this.height = height;
            this.boxes = boxes;

            this.playerX = this.width / 2;
            this.playerY = this.height / 2;

            playerSpawn();

            for (int i = 3; !lines.get(i).equals("-"); i++) {
                //IO.println(lines.get(i));
                String[] splits = lines.get(i).split(",");
                int cord1 =  Integer.parseInt(splits[0]);
                int cord2 = Integer.parseInt(splits[1]);
                if (getPointAt(cord1, cord2).state == State.PLAYER) {
                    getPointAt(cord1, cord2).state = State.TARGET;
                } else {
                    getPointAt(cord1, cord2).state = State.TARGET;
                }
            }

            for (int i = lines.size() - 1; !lines.get(i).equals("-"); i--) {
                String[] splits = lines.get(i).split(",");
                int cord1 =  Integer.parseInt(splits[0]);
                int cord2 = Integer.parseInt(splits[1]);
                switch (getPointAt(cord1, cord2).state) {
                    case PLAYER -> {
                        throw new RuntimeException("file error wtv idk");
                    }
                    case TARGET -> {
                        getPointAt(cord1, cord2).state = State.BOX_ON_TARGET;
                    }
                    case EMPTY -> {
                        getPointAt(cord1, cord2).state = State.BOX;
                    }
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.getCause();
        }
    }

    public void printBoard() {
        IO.println("Moves: " + moves);

        IO.println(String.valueOf(WALL).repeat(this.width));


        for (int i = 1; i < this.height - 1; i++)
        {
            IO.print(WALL);
            for(int j = 1; j < this.width - 1; j++)
                IO.print(points.get(j-1 + (i - 1) * (width - 2) ).getSymbol());
            IO.println(WALL);
        }
        IO.println(String.valueOf(WALL).repeat(this.width));
    }

    public void startGame() {
        while (true) {
            printBoard();
            IO.print("Enter move (w/a/s/d or quit): ");
            String command = scanner.next().toLowerCase();
            switch (command) {
                //case "read", "r" -> testReadFile();
                case "up", "w" -> movePlayer(-1, 0);
                case "down", "s" -> movePlayer(1, 0);
                case "left", "a" -> movePlayer(0, -1);
                case "right", "d" -> movePlayer(0, 1);
                case "quit", "q" -> {
                    IO.println("Quitting game.");
                    System.exit(0);
                }
                case "undo", "u" -> {
                    if (!gameStates.isEmpty()) {
                        restoreState(gameStates.pop());
                        IO.println("Undo successful!");
                    } else {
                        IO.println("No moves to undo.");
                    }
                }
                case "debug" -> {
                    IO.println(getPointAt(1,this.width - 2).state);
                }
                case "print", "p" -> {
                    printBoard();
                }
                default -> IO.println("Unknown command");
            }
            if (boxesOnTargets == boxes) {
                printBoard();
                IO.println("You've won!!!");
                break;
            }
        }
    }
}


void main() {
    Game game = new Game();
    game.startGame();

//    Game game2 = new Game(Path.of("D:/level.txt"));
//    game2.startGame();
}
