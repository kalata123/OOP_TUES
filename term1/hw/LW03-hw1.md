# Homework Task – Mini Sokoban (Week 3)

## Goal
Create a simplified Sokoban-style console game in Java. The player can move on a board and push boxes onto target positions.

---

## Base Requirements

### 1. Input from Console
- Read the board width `W` (integer).
- Read the board height `H` (integer).
- Read the number of boxes `K` (integer).
- Validate:
    - `W ≥ 5`, `H ≥ 5`.
    - Boxes cannot be placed in the 4 corners.
    - The player must have space to maneuver (central cell + 4 neighbors are reserved).
    - Maximum allowed `K` = `( (W-2)*(H-2) - 4 - 5 ) / 2`.
    - If `K` is too large, ask again for a valid number.

### 2. Board Representation
- Use `char[][] board`.
- Symbols:
    - `#` → wall
    - `.` → empty space
    - `@` → player
    - `B` → box
    - `*` → target
    - `O` → box on target

### 3. Initialization
- Draw walls around the border.
- Place the player in the center (with 4 empty neighbors).
- Place `K` targets in valid random cells (not corners, not occupied, not next to the player).
- Place `K` boxes in valid random cells (not corners, not occupied, not next to the player).

### 4. Commands
- Input commands from console:
    - `up`, `down`, `left`, `right` → move player.
    - `q` → quit game.
- Rules for moves:
    - If next cell is empty `.` or target `*` → move player.
    - If next cell has a box (`B` or `O`) → check the next cell in the same direction:
        - If it is empty `.` or target `*` → push box, then move player.
        - Otherwise -> invalid move.
    - If next cell is a wall `#` → invalid move.
- Print “Invalid move” if the action cannot be performed.

### 5. Win Condition
- The game ends with **victory** when all boxes are on targets (`O`).
- Print `You win!` and stop the game.

---

## Expected Program Structure

### Class `Game`
- Fields:
    - `char[][] board`
    - `int width, height`
    - `int playerX, playerY`
- Constructor `Game(int w, int h, int k)`:
    - Validates input.
    - Builds the board with walls.
    - Places player, targets, and boxes.
- Methods:
    - `void printBoard()`
    - `void move(String direction)`
    - `boolean isWin()`

### Main Method
- Reads `W, H, K` with `Scanner`.
- Validates and possibly asks again for `K`.
- Creates a `Game` instance.
- Infinite loop:
    - Print board.
    - Read input.
    - Call `move(direction)`.
    - Check win condition.

---

## Example Board (7 × 7 with 1 box)
```
#######
#.....#
#..*..#
#..B..#
#..@..#
#.....#
#######
```

---

## Advanced Requirements (Optional, for stronger students; bonus points)

1. **Multiple Boxes**
    - Allow more than one box.
    - Win condition: all boxes must be on targets.

2. **Move Counter**
    - Count number of moves and print `You win in X moves!`.

3. **Invalid Command Handling**
    - If input is not one of `up/down/left/right/q`, throw an exception and catch it in `main` with message `Invalid command`.

4. **Levels**
    - Prepare multiple board layouts (e.g., 2–3 levels).
    - After winning one, automatically load the next.

5. **Undo**
    - Allow command `undo` to revert the last move (store previous states).

6. **Limited Moves**
    - Add difficulty: maximum number of allowed moves (e.g., 30).
    - If the limit is reached without victory → print `Game over`.

---

## Grading

| points | functionality                                        |
|--------|------------------------------------------------------|
| 1      | Compiles without errors                              |
| 4      | Class Game has proper fields and constructors        |
| 5      | Class Game has proper methods (which work correctly) |
| 5      | Whole program works correctly                        |
|        | advanced - extra points                              |
| 5      | 4 / 6 advanced requirements work properly            |
