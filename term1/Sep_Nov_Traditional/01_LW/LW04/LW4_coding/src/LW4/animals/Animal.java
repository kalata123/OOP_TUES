package LW4.animals;

public abstract class Animal{
    public void breathe(){
        System.out.println("Breathing...");
    }

    abstract void eat();
    abstract void animalSound();
}
