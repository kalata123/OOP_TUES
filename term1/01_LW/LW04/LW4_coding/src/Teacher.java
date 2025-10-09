

public class Teacher {
    private String firstName = "test";
    public String lastName = "testov";
    private int age = 12;
    protected int teacherID = 12123;
    public String subject = "OOP";
    boolean suicidal = true;


    void printName(){
        System.out.println("Mr./Mrs." + lastName);
    }
//    Create:
//
//    Class Teacher (has public, private, protected, and default fields)
//    Student with class TestTeacher that tries to access them.
//    Observe which fields are accessible and explain why.ss
    static void main(){
        Teacher newTeacher =  new Teacher();
        newTeacher.lastName = "Simpsons";
        newTeacher.teacherID = 12123;
        newTeacher.printName();

        System.out.println(MathHelper.square(3));
        System.out.println(MathHelper.cube(3));
        System.out.println(MathHelper.max(3, 5));

    }
}


//
//
//Write class MathHelper:
//
//Contains static methods: square(int), cube(int), max(int, int)
//Add a static field count tracking how many times a method was used.
//Test all in main.

class MathHelper{
    static int counter = 0;

    static public int square(int number)
    {
        counter++;
        return number * number;
    }

    static public int cube(int number)
    {
        counter++;
        return number * number * number;
    }

    static public int max(int a, int b)
    {
        counter++;
        return a > b ? a : b;
    }


}