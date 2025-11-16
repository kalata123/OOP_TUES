package library.materials;

import library.core.LibraryItem;

public class Book extends LibraryItem {
    private String author;

    public Book(String title, String author) {
        super(title);
        validateNotBlank(author, "Author");
        this.author = author;
    }

    public Book(String title, String author, String itemId) {
        super(title, itemId);
        validateNotBlank(author, "Author");
        this.author = author;
    }

    public String getItemType() {
        return "Book";
    }

    public String getAuthor() { return author;
    }

    private void validateNotBlank(String text, String fieldName) {
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " ne moje da e null or blank");
        }
    }
}
