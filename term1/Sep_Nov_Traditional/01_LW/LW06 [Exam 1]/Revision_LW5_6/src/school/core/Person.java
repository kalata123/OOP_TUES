package school.core;

abstract public class Person {
    private String name;
    private int age;

    public Person(String name) {
        this.name = name;
        this.age = 0;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = Math.max(age, 0);
    }

    abstract public String role();

    String idCard() {
        return name + " " + role() + ", age" + age; // "NAME (ROLE), age AGE"
    }

    // pls someone else do this
    protected void validateNonEmpty(String s, String fieldName) {
        if(s.isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " is empty");
        }
    }
}
