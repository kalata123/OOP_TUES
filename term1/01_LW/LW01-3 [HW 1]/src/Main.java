
enum Skills {
    SOLDERING,
    MACHINING,
    HANDLING,
    CLEANING,
    NO_SKILLS;
}

abstract static class Company {
    String name;
    String address;

    Company(String name, String address){
        this.name = name;
        this.address = address;
    }
}

abstract static class Human {
    String name;

    Human(String name){
        this.name = name;
    }
}

static class Operator extends Human {
    Skills skill;

    Operator(String name, Skills skill){
        super(name);
        this.skill = skill;
    }
}

abstract static class Material{
    String name;
    static int count = 0;
    int id;

    Material (String name){
        this.name = name;
        id = count++;
    }
}

static class FinishedGood extends Material {
    Skills necessarySkill;

    FinishedGood(String name, Skills necessarySkill){
        super(name);
        this.necessarySkill = necessarySkill;
    }
}

static class Plant extends Company {
    int rawMaterials;
    int finishedGoods;
    int clients;
    int suppliers;


    public Plant(String name, String address, int rawMaterials, int finishedGoods, int clients, int suppliers){
        super(name, address);
        this.rawMaterials = rawMaterials;
        this.finishedGoods = finishedGoods;
        this.clients = clients;
        this.suppliers = suppliers;
    }

    public Plant(){
        super("Company Name", "Company Address");
    }

    void supplyMaterials(){
        rawMaterials += suppliers * 2;
    }

    void produce(){
        finishedGoods += rawMaterials / 2;

        if(rawMaterials % 2 == 0){
            rawMaterials = 0;
        }
        else rawMaterials = 1;
    }

    void sell(){
        finishedGoods -= clients;
    }

    String getStatus(){
        return "Name: " + name + "\nAddress: " + address + "\nRawMaterials: " + rawMaterials + "\nFinishedGoods: " + finishedGoods  + "\nClients: " + clients + "\nSuppliers:  " + suppliers;
    }

}

void main() {
    Plant plant = new Plant("myPlant", "test", 2, 2, 2, 2);

    System.out.println(plant.getStatus());

    plant.supplyMaterials();

    System.out.println("\nAfter Supply:\n");
    System.out.println(plant.getStatus());
    plant.produce();

    System.out.println("\nAfter Produce:\n");
    System.out.println(plant.getStatus());
    plant.sell();

    System.out.println("\nAfter Sell:\n");
    System.out.println(plant.getStatus());

}