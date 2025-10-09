public class Main {
    // Add two integers
    public int add(int a, int b) {
        return a + b;
    }

    // Add three integers
    public int add(int a, int b, int c) {
        return a + b + c;
    }

    // Add two doubles
    public double add(double a, double b) {
        return a + b;
    }

    // Add into to double
    public double add(int a, double b) {
        return a + b;
    }

//    public double add(int a, int b) { // ERROR: already defined (return type is irrelevant, only params matter)
//        return a + b;
//    }

    public double add(double a, int b) { // WORKS: param order differs
        return a + b;
    }

    static void main() {
        Main calc = new Main();      // uses second
        System.out.println(calc.add(2.5, 4.5));      // uses third
        System.out.println(calc.add(2, 4.5));      // uses forth
        System.out.println(calc.add(2.5, 4));
        System.out.println(calc.add(2, 3));         // uses first
        System.out.println(calc.add(1, 2, 3));     // uses forth
    }


//    static void main() {
//        Rectangle a = new Rectangle();
//        Rectangle b = new Rectangle(5, 3);
//        Rectangle c = new Rectangle(4);
//
//        System.out.println(a.area()); // 1
//        System.out.println(b.area()); // 15
//        System.out.println(c.area()); // 16
//    }
}


class Rectangle {
    int width;
    int height;

    // Constructor 1
    public Rectangle() {
        width = 1;
        height = 1;
    }

    // Constructor 2
    public Rectangle(int w, int h) {
        width = w;
        height = h;
    }

    // Constructor 3
    public Rectangle(int side) {
        width = side;
        height = side;
    }

    public int area() {
        return width * height;
    }
}
