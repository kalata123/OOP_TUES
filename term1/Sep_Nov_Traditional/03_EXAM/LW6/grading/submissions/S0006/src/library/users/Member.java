package library.users;

import library.util.LibrarySettings;
import library.transactions.*;

public class Member {
    private String name;
    private String memberId;
    private int borrowedCount;

    public Member(String name) {
        if(name.isBlank() || name.isEmpty())
            throw new IllegalArgumentException("(Member Name Constructor) Name cannot be empty!");
        this.name = name;
        int randomNum = (int)(Math.random() * 101);
        this.memberId = "MEM-" + randomNum;
    }

    public Member(String name, String memberId) {
        if(name.isBlank() || name.isEmpty())
            throw new IllegalArgumentException("(Member Full Constructor) Name cannot be empty!");
        this.name = name;
        if(memberId.isBlank() || memberId.isEmpty())
            throw new IllegalArgumentException("(Member Full Constructor) MemberId cannot be empty!");
        this.memberId = memberId;
    }

//    public boolean canBorrow() {
//        return borrowedCount < MAX_BORROWED_LIMIT; //Doesn't work for some reason
//    }
}
