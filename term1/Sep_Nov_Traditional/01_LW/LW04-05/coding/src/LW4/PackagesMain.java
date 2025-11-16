package LW4;

import LW4.abstraction.*;

//import LW4.animals.*; // gives Error with animalSound because compiler does not know which LW4.Dog to get - the one in LW4.Modifiers or the one in the package. Prioritizes the one in LW4.Modifiers (because it is one level higher)
import LW4.animals.Animal;
import LW4.animals.Dog;

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
