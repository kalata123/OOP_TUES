public class Abstraction {
//    Exercise 1: Abstract Shape
//    Create an abstract class Shape with abstract methods area and perimeter, and a concrete method describe that prints a simple message.
//    Implement Rectangle and Circle classes that define area and perimeter.
//    Create both in main and print their calculated results.

    public static void main(String[] args) {

        Square s1 = new Square(2.5);
        System.out.println(s1.calculateArea());
        System.out.println(s1.calculatePerimeter());
        s1.printSomething();

        Appliance wM = new WashingMachine("ficata");
        wM.turnOn();
        wM.turnOff();

        Appliance microwave = new Microwave("mark");
        System.out.println(microwave.brand);
        microwave.turnOff();
        microwave.turnOn();

        //GameCharacter

        GameCharacter mage = new Mage("Mage");
        GameCharacter warrior = new Warrior("Warrior");

        mage.attack();
        warrior.attack();

    }


}

abstract class Shape {
    abstract double calculateArea();
    abstract  double calculatePerimeter();
    public void printSomething(){
        System.out.println("This is a Shape");
    }
}

class Square extends Shape {
    double side;

    Square(double side){
        this.side = side;
    }

    public double calculateArea() {
        return side*side;
    }

    public double calculatePerimeter(){
        return 4*side;
    }
}








//Exercise 2: Abstract Appliance
//Create an abstract class Appliance with field brand and abstract methods turnOn and turnOff.
//Implement WashingMachine and Microwave classes with unique messages for each method (turnOn and turnOff).
//Demonstrate them in main.

abstract class Appliance {
    public String brand;
    abstract void turnOn();
    abstract void turnOff();
}

class WashingMachine extends Appliance{
    WashingMachine(String brand){
        this.brand = brand;
    }

    public void turnOn(){
        System.out.println("Washing machine turns on");
    }

    public void turnOff(){
        System.out.println("Washing machine turns off");
    }
}



class Microwave extends Appliance{
    Microwave(String brand){
        this.brand = brand;
    }

    public void turnOn(){
        System.out.println("Microwave turns on");
    }

    public void turnOff(){
        System.out.println("Microwave turns off");
    }
}



//Exercise 3
//Create an abstract class GameCharacter with field name and abstract method attack.
//Implement Warrior and Mage classes with unique attack messages.
//Create them in main and call their attack methods.

abstract class GameCharacter {
    private String name;

    GameCharacter(String name) {
        this.name = name;
    }

    abstract void attack();

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Warrior extends GameCharacter{
    Warrior(String name) {
        super(name);
    }

    @Override
    public void attack() {
        System.out.println("Warrior attacks");
    }
}


class Mage extends GameCharacter{
    Mage(String name) {
        super(name);
    }

    @Override
    public void attack() {
        System.out.println("Mage attacks");
    }
}