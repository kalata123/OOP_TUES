import abstraction.*;

//import animals.*; // gives Error with animalSound because compiler does not know which Dog to get - the one in Modifiers or the one in the package. Prioritizes the one in Modifiers (because it is one level higher)
import animals.Animal;
import animals.Dog;

public class PackagesMain {

    void main() {
        Animal dog = new Dog();
        Flyable drone = new Drone();

        drone.fly();
        Dog d1 = new Dog();
        d1.breathe();
        d1.animalSound();
        d1.eat();
    }


}
