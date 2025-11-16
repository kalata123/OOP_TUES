package library;

import library.materials.Book;
import library.transactions.BorrowingSystem;
import library.transactions.LibraryException;
import library.users.Member;
import library.util.LibrarySettings;
import library.util.SimpleDateUtils;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== LIBRARY MANAGEMENT SYSTEM DEMONSTRATION ===");

        Book book1 = new Book("PogIgoto", "Ivan Vazov", "ITEM-100");
        Book book2 = new Book("Baj Ganyo", "Aleko Konstantinov");

        Member member1 = new Member("Venzhi", "MEM-999");
        Member member2 = new Member("Ted");

        System.out.println("Member 1 Name: " + member1.getName());
        System.out.println("Member 1 ID: " + member1.getMemberId());

        System.out.println("Max Borrow Limit: " + LibrarySettings.MAX_BORROW_LIMIT);

        System.out.println(SimpleDateUtils.calculateDueDate(10));
        System.out.println(SimpleDateUtils.calculateDueDate("2025-11-01"));
        System.out.println(SimpleDateUtils.calculateDueDate(7, true));

        BorrowingSystem borrowingSystem = new BorrowingSystem();
        try {
            borrowingSystem.borrowItem(book1, SimpleDateUtils.calculateDueDate(10));
            member1.incrementBorrowed();
            System.out.println("Borrowed: " + book1.getTitle());
            System.out.println("Borrowed Count: " + member1.getBorrowedCount());
            Book book5 = new Book("Razkazi", "Elin Pelin");
            borrowingSystem.borrowItem(book5, SimpleDateUtils.calculateDueDate(10));
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            boolean returned = borrowingSystem.returnItem(book1.getItemId());
            if (returned) {
                member1.decrementBorrowed();
                System.out.println("Returned: " + book1.getTitle());
                System.out.println("Borrowed Count: " + member1.getBorrowedCount());
            }
        } catch (LibraryException e) {
            System.out.println("Error returning item: " + e.getMessage());
        }

        try {
            Member member3 = new Member("");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            Book book3 = new Book("Zaglavie", "");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            borrowingSystem.borrowItem(null, "Nqma data");
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        try {
            borrowingSystem.returnItem("ne sushtestvuvashto ID");
        } catch (LibraryException e) {
            System.out.println("Caught: " + e.getMessage());
        }

        System.out.println("=== DEMONSTRATION COMPLETED ===");
    }
}