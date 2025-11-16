import java.util.*;

public class SokobanGame{
    private GameState state;
    private Random rObj = new Random();
    private Stack<GameState> undoStack = new Stack<>();
    public SokobanGame(int width, int height, int boxCount)
    {
        state = new GameState(width, height, boxCount);
        initialize(width, height, boxCount);
    }
    private void initialize(int width, int height, int boxCount)
    {
        state.setPlayer(width / 2, height / 2);
        do
        {
            state.clearTargets();
            state.clearBoxes();
            state.resetBoard();
            positionTar(boxCount);
            positionBox(boxCount);
        }while (hasSolidBlock()||hasCornerTrap());

        state.updateDisplay();
    }
    private void positionTar(int boxCount)
    {
        int i = 0;
        while(i < boxCount)
        {
            int x = 1 + rObj.nextInt(state.getWidth() - 2);
            int y = 1 + rObj.nextInt(state.getHeight() - 2);
            if((x==state.getPlayerX() && y==state.getPlayerY()) || state.isTarget(x,y))continue;
            state.setTarget(x, y, true);
            i++;
        }
    }

    private void positionBox(int boxCount)
    {
        int i = 0;
        int tries = 0;
        while (i<boxCount&&tries<boxCount * 500)
        {
            tries++;
            int x = 1 + rObj.nextInt(state.getWidth() - 2);
            int y = 1 + rObj.nextInt(state.getHeight() - 2);
            if(x == state.getPlayerX()&&y == state.getPlayerY())continue;
            if(state.hasBox(x, y))continue;
            if(state.isTarget(x, y))continue;
            if(isCorner(x, y))continue;
            state.setBox(x, y, true);
            boolean createsSolid = false;
            for(int dy = -1;dy<= 0;dy++)
            {
                for(int dx = -1;dx<=0;dx++)
                {
                    int sx = x + dx;
                    int sy = y + dy;
                    if(sx >= 1 && sy >= 1&&sx + 1 < state.getWidth() - 0&&sy + 1 < state.getHeight()-0)
                    {
                        if(isSolid2x2(sx, sy))
                        {
                            createsSolid = true;
                        }
                    }
                }
            }
            if(createsSolid)
            {
                state.setBox(x, y, false);
                continue;
            }
            if(createsCornerAdjacencyTrapByPlacing(x, y))
            {
                state.setBox(x, y, false);
                continue;
            }
            i++;
        }

    }
    boolean hasSolidBlock()
    {
        for(int y = 1;y<state.getHeight()-1;y++)
        {
            for(int x = 1;x<state.getWidth()-1;x++)
            {
                if (isSolid2x2(x, y)) return true;
            }
        }
        return false;
    }

    boolean isSolid2x2(int x, int y)
    {
        if(x<1 || y<1 || x+1>=state.getWidth()-0 || y+1>=state.getHeight() - 0)return false;
        int count = 0;
        for(int dy = 0;dy<2;dy++)
        {
            for(int dx = 0;dx<2;dx++)
            {
                int nx = x + dx;
                int ny = y + dy;
                if(state.isWall(nx, ny)||state.hasBox(nx, ny))
                {
                    count++;
                }
            }
        }
        return count == 4;
    }

    boolean hasCornerTrap()
    {
        int w = state.getWidth();
        int h = state.getHeight();
        if(!state.isTarget(1, 1)&&state.hasBox(1, 2)&&state.hasBox(2, 1))
        {
            return true;
        }
        if(!state.isTarget(w-2, 1)&&state.hasBox(w-3, 1)&&state.hasBox(w-2, 2))
        {
            return true;
        }
        if(!state.isTarget(1, h-2)&&state.hasBox(1, h-3)&&state.hasBox(2, h-2))
        {
            return true;
        }
        if(!state.isTarget(w-2, h-2)&&state.hasBox(w-3, h-2) && state.hasBox(w-2,h-3))
        {
            return true;
        }
        return false;
    }
    private boolean createsCornerAdjacencyTrapByPlacing(int x, int y) {
        int w = state.getWidth();
        int h = state.getHeight();
        if((x==1 && y==2)||(x==2&&y==1))
        {
            if(!state.isTarget(1,1)&&state.hasBox(1,2)&&state.hasBox(2,1))
            {
                return true;
            }
        }
        if((x==state.getWidth()-2 && y==2)||(x==state.getWidth()-3 && y==1))
        {
            if(!state.isTarget(state.getWidth()-2,1)&&state.hasBox(state.getWidth()-3,1)&&state.hasBox(state.getWidth()-2,2))
            {
                return true;
            }
        }
        if((x==1&&y==state.getHeight()-3)||(x==2 && y==state.getHeight()-2))
        {
            if(!state.isTarget(1,state.getHeight()-2)&&state.hasBox(1,state.getHeight()-3)&&state.hasBox(2,state.getHeight()-2))
            {
                return true;
            }
        }
        if((x==state.getWidth()-2&&y==state.getHeight()-3)||(x==state.getWidth()-3&&y==state.getHeight()-2))
        {
            if(!state.isTarget(state.getWidth()-2,state.getHeight()-2)&&state.hasBox(state.getWidth()-3,state.getHeight()-2)&&state.hasBox(state.getWidth()-2,state.getHeight()-3))
            {
                return true;
            }
        }
        return false;
    }

    private boolean isCorner(int x, int y)
    {
        int w = state.getWidth();
        int h = state.getHeight();
        return (x==1||x==w-2)&&(y==1||y==h-2);
    }

    public void printBoard()
    {
        state.print();
    }

    public boolean checkWin()
    {
        return state.checkWin();
    }
    private GameState cloneGameState(GameState original)
    {
        GameState copy = new GameState(original.getWidth(), original.getHeight(), original.getBoxesCount());
        copy.setPlayer(original.getPlayerX(), original.getPlayerY());
        for(int y = 1;y<original.getHeight()-1;y++)
        {
            for(int x = 1;x<original.getWidth()-1;x++)
            {
                copy.setTarget(x, y, original.isTarget(x, y));
                copy.setBox(x, y, original.hasBox(x, y));
            }
        }
        for(int i = 0;i<original.getMoves();i++) {
            copy.addMoves();
        }
        copy.updateDisplay();
        return copy;
    }

    public void undo()
    {
        if(!undoStack.isEmpty())
        {
            state = undoStack.pop();
            state.updateDisplay();
            System.out.println("Undo successful!");
        } else {
            System.out.println("Nothing to undo!");
        }
    }

    public boolean move(String string)
    {
        String s = string.toLowerCase();
        int dx = 0, dy = 0;
        switch(s) {
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
                System.out.println("Invalid move");
                return false;
        }
        int px = state.getPlayerX();
        int py = state.getPlayerY();
        int nx = px + dx;
        int ny = py + dy;
        if(state.isWall(nx, ny))
        {
            System.out.println("Invalid move");
            return false;
        }
        if(state.hasBox(nx, ny))
        {
            int bx = nx + dx;
            int by = ny + dy;
            if(state.isWall(bx, by)||state.hasBox(bx, by))
            {
                System.out.println("Invalid move");
                return false;
            }
            undoStack.push(cloneGameState(state));
            state.setBox(nx, ny, false);
            state.setBox(bx, by, true);
            state.setPlayer(nx, ny);
            state.addMoves();
            state.updateDisplay();
            return true;
        } else {
            undoStack.push(cloneGameState(state));
            state.setPlayer(nx, ny);
            state.addMoves();
            state.updateDisplay();
            return true;
        }
    }

    public int getMoveCount()
    {
        return state.getMoves();
    }
}

