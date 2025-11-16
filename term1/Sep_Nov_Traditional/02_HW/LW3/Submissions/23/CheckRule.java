public class CheckRule {
    static final char WALL = '#';
    static final char EMPTY = '.';
    static final char PLAYER = '@';
    static final char BOX = 'B';
    static final char TARGET = '*';
    static final char BOX_ON_TARGET = 'O';

    private static boolean first(char[][] arr, int x, int y){
        return arr[y][x] != WALL && arr[y][x] != PLAYER && arr[y][x] != BOX
                && arr[y][x] != TARGET;
    }

    private static boolean second(char[][] arr, int x, int y, int width,int height){
        int numboxes = 1;
        int numtargets = 0;
        int ok = 1;
        if(arr[y][x+1] == WALL || arr[y][x-1] == WALL){//ima li stena lqvo ili dqsno
            for(int i = 0;i < height;i++){
                if(arr[i][x] == BOX){
                    numboxes++;
                }
                if(arr[i][x] == TARGET){
                    numtargets++;
                }
            }
            ok=0;
        }
        else if(arr[y+1][x] == WALL || arr[y-1][x] == WALL){//ima li stena gore ili dolu
            for(int i = 0;i < width;i++){
                if(arr[y][i] == BOX){
                    numboxes++;
                }
                if(arr[y][i] == TARGET){
                    numtargets++;
                }
            }
            ok=0;
        }
        if(ok == 1){return true;}
        return numboxes <= numtargets;
    }

    private static boolean third(int width, int height, int x, int y){
        if(x==1){
            if(y==1)return false;
            if(y==height - 2)return false;
        }
        if(x==width-2){
            if(y==1)return false;
            if(y==height-2)return false;
        }
        return true;
    }
    public static boolean checkAll(int width, int height, int x, int y, char[][]arr){
        return first(arr,x,y) && second(arr, x, y,width,height) && third(width, height, x, y);
    }

    public static boolean moveNoBox(char [][]arr, int playerY, int playerX, int y, int x){
        return  arr[playerY + y][playerX + x] == EMPTY || arr[playerY + y][playerX + x] == TARGET;
    }

    public static boolean moveBox(boolean[][] arr1, char[][] arr2, int playerY, int playerX, int y, int x){
        return arr1[playerY + y][playerX + x] && (arr2[playerY + y*2][playerX + x*2] == EMPTY || arr2[playerY + y*2][playerX + x*2] == TARGET);
    }

    //test this func
    public static boolean checkFourBoxOrWall(char[][] arr, int width, int height){
        for(int i = 0;i < height-2;i++){
            for(int j = 0;j< width-2;j++){
                if(helper(j, i, arr)){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean helper(int x, int y, char[][] arr){
        if ((arr[x][y] == WALL || arr[x][y] == BOX || arr[x][y] == BOX_ON_TARGET)
        &&(arr[x+1][y] == WALL || arr[x+1][y] == BOX || arr[x+1][y] == BOX_ON_TARGET)
        &&(arr[x][y+1] == WALL || arr[x][y+1] == BOX || arr[x][y+1] == BOX_ON_TARGET)
        &&(arr[x+1][y+1] == WALL || arr[x+1][y+1] == BOX || arr[x+1][y+1] == BOX_ON_TARGET)
        ) {
            return true;
        }
        return false;
    }
}
