package LW4.abstraction;

abstract class Animal {
    abstract void sound();

    public void breathe() {
        System.out.println("Breathing...");
    }
}
class Dog extends Animal {
    void sound() {
        System.out.println("Woof!");
    }
}

class Cat extends Animal {
    void sound() {
        System.out.println("Meow!");
    }
}

public class AnimalMain {
    public static void main(String[] args) {
        Dog dog = new Dog();
        dog.sound();
        dog.breathe();

        Cat cat = new Cat();
        cat.sound();
        cat.breathe();

        Animal dog1 = new Dog();
        dog1.sound();

    }
}