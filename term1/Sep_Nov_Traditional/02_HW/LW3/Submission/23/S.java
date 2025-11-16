import java.util.Stack;

public class S {
    public static Stack<char[][]> stack = new Stack<>();

    public static char[][] undo() {
        if (stack.size() > 1) {
            stack.pop();
            return stack.peek();
        }
        return stack.peek();
    }

    public static void push(char[][] arr) {
        stack.push(copyBoard(arr));
    }

    private static char[][] copyBoard(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
}