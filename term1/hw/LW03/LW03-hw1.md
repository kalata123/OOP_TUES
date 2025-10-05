# Mini Java Sokoban Homework LW03

## Learning Objectives
- Practice 2D array manipulation
- Implement basic game logic
- Handle user input and validation
- Apply object-oriented programming principles
- Transition from C++ to Java syntax and conventions

---

## Assignment Overview
Create a console-based Sokoban game where the player pushes boxes onto targets. This is a foundation version focusing on core gameplay mechanics.

---

## 1) Basic Requirements

### Input
Read integers: `W` (width), `H` (height), `K` (boxes).

**Validation Rules**
- `W ≥ 5`, `H ≥ 5`
- Maximum boxes: `K ≤ ((W-2)*(H-2) - 2) / 2`
- If invalid: re-ask only the invalid value(s)

---

### Game Board Setup

```java
static final char WALL = '#';
static final char EMPTY = '.';
static final char PLAYER = '@';
static final char BOX = 'B';
static final char TARGET = '*';
static final char BOX_ON_TARGET = 'O';
```

**Board Structure**
- Border cells are walls (row `0`, row `H-1`, col `0`, col `W-1`)
- Player starts at center: `(px, py) = (W/2, H/2)` using integer division
- Place `K` targets on random interior cells (not on player position)
- Place `K` boxes on random interior cells following Basic Placement Rules

---

### Basic Placement Rules

**R1 — Basic Availability**
- Box cannot be placed on player position `(px, py)`
- Box cannot be placed on a target cell
- Box cannot be placed on another box

**R2 — No Corner Traps (Simple Version)**
- Boxes cannot be placed in the four interior corners unless that corner is a target
- Interior corners: `(1,1)`, `(W-2,1)`, `(1,H-2)`, `(W-2,H-2)`

**R3 - Wall capacity constraint**
- Boxes cannot be placed next to a wall if the amount of boxes on the wall would exceed the amount of targets on the wall

---

### Movement System

**Commands:** `up`, `down`, `left`, `right`, `quit`  
**Aliases:** `w`, `a`, `s`, `d`, `q`

**Movement Rules**
- If next cell is empty or target → move player
- If next cell has a box and the cell beyond is empty/target → push box and move player
- If next cell is wall or box cannot be pushed → print `Invalid move`
- Update `boxesOnTargets` counter when boxes move on/off targets

---

### Win Condition
- Game ends when `boxesOnTargets == K`
- Print: `You win in X moves!` (include move counter)

---

## 2) Suggested* Class Structure

```java
public class SokobanGame {
// Game state
private int width, height, boxesCount;
private int playerX, playerY;
private int moves, boxesOnTargets;

    // Game board arrays
    private char[][] displayBoard;
    private boolean[][] isTarget;
    private boolean[][] hasBox;

    // Core methods to implement
    public void initializeGame(int w, int h, int k) { }
    private void placePlayer() { }
    private void placeTargets() { }
    private void placeBoxes() { }
    public boolean move(String direction) { return false; }
    private void updateDisplay() { }
    public boolean checkWin() { return false; }
    public void printBoard() { }
}
```

*Students may use other structure to their liking.

---

## 3) Main Game Loop

```java
public static void main(String[] args) {
// Read W, H, K with validation
// Initialize game
SokobanGame game = new SokobanGame();
// game.initializeGame(W, H, K);

    // Game loop:
    while (!game.checkWin()) {
        game.printBoard();
        String command = readCommand();
        if (command.equals("quit")) break;
        game.move(command);
    }
}
```
*Students may use other implementation to their liking.

---


## Testing Examples

### Invalid 5×5 Setup (Violates R3)
```text
#####
#.*.#
#.@.#
#.B.#   ← Boxes on wall exceed targets - NOT ALLOWED
#####
```
### Valid 5×5 Setup
```text
#####
#*B.#
#.@.#
#...#
#####
```
### Invalid Placement (Violates R2)
```text
#####
#...#
#.@.#
#..B#   ← Box in non-target corner - NOT ALLOWED
#####
```

### Winning State
```text
#####
#.O.#  ← All boxes on targets
#.@.#
#...#
#####
```

---

## Implementation Tips
- Start simple: get basic movement working before box pushing
- Test incrementally: verify each rule works before adding the next
- Use helper methods: break complex tasks into smaller methods
- Debug visualization: print boolean arrays to verify placement
- C++ to Java: use `boolean[][]` instead of `vector<vector<bool>>`

---

## Getting Started
1. Implement the basic class structure
2. Get board rendering working
3. Add player movement
4. Implement box pushing
5. Add placement rules
6. Test thoroughly

Focus on clean, working core gameplay first — bonuses can be added later.

---
## Grading Rubric (20 + 20 points total)

| points               | functionality                                    |
|----------------------|--------------------------------------------------|
| 3                    | Compiles without errors and clean code structure |
| 2                    | Correct input handling with validation           |
| 3                    | Player movement and basic collision detection    |
| 3                    | Box pushing mechanics and target counting        |
| 3                    | Basic placement rules (R1 & R2) implemented      |
| 2                    | Wall capacity constraint (R3)                    |
| 4                    | Proper board initialization and rendering        |
| Total: **20**        |                                                  |
| +20 with all bonuses | See below                                        |

---


## Easy Bonuses (+1 point each)
- Undo System: Implement `undo()` to revert the last move (store previous game state)
- Move Counter: Display move count during gameplay and in win message

---

## Intermediate Bonuses (+2 points each)
- R3 — 2×2 Solid Block: Detect and prevent 2×2 areas filled with walls/boxes that create unsolvable states
- R4 — Corner Adjacency Trap: Prevent two boxes adjacent to a non-target corner from creating deadlocks
- R5 — Wall Capacity: Ensure each interior wall line has enough targets for adjacent boxes

---

## Advanced Bonuses (+3 points each)
- Level System: Design multiple pre-built levels with progressive difficulty
- Board Parser: Load custom level layouts from text files with wall/box/target/player placement
- Exception Handling: Implement custom exceptions for invalid moves and game state errors
- Smart Restart: Auto-restart level when a proven deadlock occurs