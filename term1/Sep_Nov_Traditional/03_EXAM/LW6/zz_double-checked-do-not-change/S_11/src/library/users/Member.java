package library.users;

import static library.util.LibrarySettings.generateItemId;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name, String memberId) {
        validateNotBlank(name, "name");
        validateNotBlank(memberId, "memberId");
        this.name = name;
        this.memberId = memberId;
        this.borrowedCount = 0;
    }

    public Member(String name) {
        validateNotBlank(name, "name");
        this.name = name;
        this.borrowedCount = 0;

        memberId = generateItemId("MEM");
    }

    private void validateNotBlank(String text, String fieldName) {
        if(text == null || text.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
}
