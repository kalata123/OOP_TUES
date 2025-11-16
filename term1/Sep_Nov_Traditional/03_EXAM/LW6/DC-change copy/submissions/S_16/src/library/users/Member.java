package library.users;

import java.util.Random;

/** Represents a library user */
public class Member {

    private String name;
	private String memberId;
	private int borrowedCount;

	public Member(String name) 
	{ 
		this.name = name;
		Random r = new Random(); 
		this.memberId = "MEM-" + r.nextInt(100000);
	} 

	public Member(String name, String memberId)
	{
		this.name = name;
		this.memberId = memberId;
	}
    
	public void incrementBorrowed(){ borrowedCount++; };
    public void decrementBorrowed(){ borrowedCount--; };
        
	public boolean canBorrow(){ return borrowedCount < 20; }; // — returns true if current count < maximum limit (see Part B)    
}
