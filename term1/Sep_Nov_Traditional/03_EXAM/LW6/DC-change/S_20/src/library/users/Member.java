package library.users;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
        validateNotBlank(name, "name");
    }
    public Member(String name) {
        this.name = name;
        int randomNum = (int)(Math.random() * 101);
        this.memberId = "MEM-" + randomNum;
        validateNotBlank(name, "name");
    }

    public String getName() {
        return name;
    }
    public String getMemberId() {
        return memberId;
    }
    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void incrementBorrowed() {
        borrowedCount++;
    }
    public void decrementBorrowed() {
        borrowedCount--;
    }

    void validateNotBlank(String text, String fieldName) {
        if (text == null || text.isEmpty()){
            throw new IllegalArgumentException(fieldName + " is null or empty");
        }
    }
}
