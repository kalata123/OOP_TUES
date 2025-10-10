package animals;

abstract class Animal{
    public void breathe(){
        System.out.println("Breathing...");
    }

    abstract void eat();
    abstract void animalSound();
}

class Dog extends Animal{

    public void eat(){
        System.out.println("Eats meat");
    }

    public void animalSound(){
        System.out.println("Woof, woof!");
    }
}
