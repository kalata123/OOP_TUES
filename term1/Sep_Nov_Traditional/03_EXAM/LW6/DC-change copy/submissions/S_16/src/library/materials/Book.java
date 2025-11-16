package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem{

	Book(String title, String author) {
		super(title, "title");
		this.validateNotBlank(author, "author");
		this.author = author;
	}

	private String author;

	@Override
	public String getItemType() { return "Book"; }

	
}
