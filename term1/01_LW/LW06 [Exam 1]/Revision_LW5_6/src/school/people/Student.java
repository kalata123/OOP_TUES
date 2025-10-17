package school.people;

import school.core.Person;

public class Student extends Person{
    private int[] scores;

    Student(String name, int age){
        super(name, age);
    }

    @Override
    public String role(){
        return "Student";
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }
}
