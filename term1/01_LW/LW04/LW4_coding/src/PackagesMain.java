import abstraction.*;
import animals.*;
public class PackagesMain {

    void main() {
        Animal dog = new Dog();
        Flyable drone = new Drone();


        dog.breathe();
        dog.sound();

        drone.fly();
        Dog d1 = new Dog();
        d1.breathe();
        d1.animalSound();
        d1.eat();
    }


}
