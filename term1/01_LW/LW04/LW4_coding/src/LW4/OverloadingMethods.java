package LW4;

public class OverloadingMethods {
//    Exercise 1: Overloaded Methods (5–10 minutes)
//    Create a class LW4.Printer with several versions of print:
//    print(String message)
//    print(int number)
//    print(double number)
//    print(String message, int times) → prints message multiple times
//    Run and observe which method is called for each argument.

//    static void main(){
//        LW4.Printer newPrinter = new LW4.Printer();
//        newPrinter.print("asd");
//        newPrinter.print(1);
//        newPrinter.print(1.21);
//        newPrinter.print("asd", 2);
//    }

    static void main(){
        Printer.print("asd");
        Printer.print(1);
        Printer.print(1.21);
        Printer.print("test", 3);

    }
}

class Printer {
    static public void print(String message){
        System.out.println(message);
    }

    static public void print(int number){
        System.out.println(number);
    }

    static public void print(double number){
        System.out.print(number);
    }

    static public  void print(String message, int times){
        for (int i=0; i < times; i++){
            System.out.println(message);
        }
    }
}