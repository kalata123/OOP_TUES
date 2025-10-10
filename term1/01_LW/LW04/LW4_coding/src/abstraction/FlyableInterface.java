package abstraction;

class Bird implements Flyable {
    public void fly() {
        System.out.println("Bird flaps wings to fly.");
    }
}

public class FlyableInterface {
    public static void main(String[] args) {
        Flyable f1 = new Bird();
        Flyable f2 = new Drone();

        f1.fly();
        f2.fly();
    }
}

