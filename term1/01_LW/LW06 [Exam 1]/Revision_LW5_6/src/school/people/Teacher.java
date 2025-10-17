package school.people;

import school.core.Person;

public class Teacher extends Person {
    private String subject;

    Teacher(String name) {
        super(name);
        subject = "General";
    }

    Teacher(String name, int age, String subject) {
        super(name, age);
        try{
            validateNonEmpty(subject, "Subject");
        }catch(IllegalArgumentException e)
        {
            e.getMessage();
        }
        this.subject = subject;
    }

    @Override
    public String role() {
        return "Teacher";
    }
}
