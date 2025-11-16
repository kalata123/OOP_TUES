package LW4;//class Car {
//    public String model = "BMW";
//    private int speed = 120;
//    protected int year = 2020;
//    int weight = 1500; // default
//
//    public void printInfo() {
//        System.out.println("Model: " + model);
//        System.out.println("Speed: " + speed);
//        System.out.println("Year: " + year);
//        System.out.println("Weight: " + weight);
//    }
//}
//
//class LW4.Modifiers {
//    static void main(String[] args) {
//        Car c = new Car();
//        System.out.println(c.model);
//        // System.out.println(c.speed); // error: speed has private access in Car
//        System.out.println(c.year);
//        System.out.println(c.weight);
//    }
//}

//public class LW4.Modifiers {
//    public static final double PI = 3.14159;
//
//    public static double circleArea(double radius) {
//        return PI * radius * radius;
//    }
//
//    public static void main(String[] args) {
//        System.out.println(LW4.Modifiers.circleArea(3));
//        // Constants.PI = 3.14; // error: cannot assign a value to final variable PI
//    }
//}


abstract class Animal {
    abstract void sound();
    public void breathe() {
        System.out.println("Breathing...");
    }
}

class Dog extends Animal {
    @Override
    void sound() {
        System.out.println("Woof!");
    }
}

public class Modifiers {
    static void main(String[] args) {
        Animal a = new Dog();
        a.sound();
        a.breathe();
    }
}