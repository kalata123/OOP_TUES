package abstraction;

interface Vehicle {
    void start();
    void stop();
}

class Car implements Vehicle {
    public void start() {
        System.out.println("Car starts with a key.");
    }

    public void stop() {
        System.out.println("Car stops with brakes.");
    }
}

class Boat implements Vehicle {
    public void start() {
        System.out.println("Boat starts with ignition switch.");
    }

    public void stop() {
        System.out.println("Boat stops by reducing throttle.");
    }
}

public class VehicleMain {
    public static void main(String[] args) {
        Vehicle v1 = new Car();
        Vehicle v2 = new Boat();

        v1.start();
        v1.stop();
        v2.start();
        v2.stop();
    }
}
