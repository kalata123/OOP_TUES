import eventmanagement.model.Event;
import eventmanagement.model.Ticket;
import eventmanagement.model.Attendee;
import eventmanagement.manager.EventManager;
import eventmanagement.exception.EventManagementException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main interactive console application for Event Management System
 * Demonstrates all EventManager features through a menu-driven interface
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EventManager manager = new EventManager();

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("           EVENT MANAGEMENT SYSTEM");
        System.out.println("============================================================");
        System.out.println();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Choose option (1-8): ", 1, 8);

            switch (choice) {
                case 1:
                    addEvent();
                    break;
                case 2:
                    listAllEvents();
                    break;
                case 3:
                    purchaseTicket();
                    break;
                case 4:
                    viewEventDetails();
                    break;
                case 5:
                    showStatistics();
                    break;
                case 6:
                    sortEvents();
                    break;
                case 7:
                    filterEvents();
                    break;
                case 8:
                    running = false;
                    System.out.println();
                    System.out.println("============================================================");
                    System.out.println("     Thank you for using Event Management System!");
                    System.out.println("============================================================");
                    break;
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("=== Event Management System ===");
        System.out.println("1. Add Event");
        System.out.println("2. List All Events");
        System.out.println("3. Purchase Ticket");
        System.out.println("4. View Event Details");
        System.out.println("5. Show Statistics");
        System.out.println("6. Sort Events");
        System.out.println("7. Filter Events");
        System.out.println("8. Exit");
        System.out.println();
    }

    private static void addEvent() {
        System.out.println();
        System.out.println("--- Add Event ---");

        try {
            String id = getStringInput("Enter Event ID: ");
            String name = getStringInput("Enter Event Name: ");
            String type = getStringInput("Enter Event Type (Concert/Conference/Workshop/Festival): ");
            LocalDate date = getDateInput("Enter Event Date (YYYY-MM-DD): ");
            int capacity = getIntInput("Enter Event Capacity: ", 10, 1000);
            double price = getDoubleInput("Enter Base Price: ");

            Event event = new Event(id, name, type, date, capacity, price);
            manager.addEvent(event);

            System.out.println();
            System.out.println("✓ Event \"" + name + "\" created successfully!");

        } catch (EventManagementException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void listAllEvents() {
        System.out.println();
        System.out.println("--- All Events ---");

        List<Event> events = manager.getAllEvents();
        if (events.isEmpty()) {
            System.out.println("No events available");
        } else {
            printEventTable(events);
        }
        System.out.println();
    }

    private static void purchaseTicket() {
        System.out.println();
        System.out.println("--- Purchase Ticket ---");

        try {
            String eventId = getStringInput("Enter Event ID: ");
            String email = getStringInput("Enter Your Email: ");
            String name = getStringInput("Enter Your Name: ");
            String phone = getStringInput("Enter Your Phone (10 digits): ");
            String category = getStringInput("Enter Ticket Category (VIP/Standard/Student): ");

            Event event = manager.getEvent(eventId);
            if (event == null) {
                throw new EventManagementException("Event not found: " + eventId);
            }

            String ticketId = manager.purchaseTicket(eventId, email, name, phone, category);

            System.out.println();
            System.out.println("✓ Ticket purchased successfully!");
            System.out.println("  Ticket ID: " + ticketId);
            System.out.println("  Event: " + event.getName());
            System.out.println("  Price: $" + String.format("%.2f", event.getBasePrice()));
            System.out.println("  Category: " + category);

        } catch (EventManagementException e) {
            System.out.println();
            System.out.println("✗ Error: " + e.getMessage());
        }
        System.out.println();
    }

    private static void viewEventDetails() {
        System.out.println();
        System.out.println("--- View Event Details ---");

        String eventId = getStringInput("Enter Event ID: ");
        Event event = manager.getEvent(eventId);

        if (event == null) {
            System.out.println("✗ Event not found: " + eventId);
        } else {
            System.out.println();
            System.out.println("Event: " + event.getName());
            System.out.println("ID: " + event.getEventId());
            System.out.println("Type: " + event.getType());
            System.out.println("Date: " + event.getDate());
            System.out.println("Capacity: " + event.getCurrentAttendees() + "/" + event.getCapacity());
            System.out.println("Base Price: $" + String.format("%.2f", event.getBasePrice()));

            List<Attendee> attendees = manager.getEventAttendees(eventId);
            if (attendees.isEmpty()) {
                System.out.println("\nNo attendees yet");
            } else {
                System.out.println("\nAttendees:");
                int index = 1;
                for (Attendee attendee : attendees) {
                    for (String ticketId : attendee.getTicketIds()) {
                        Ticket ticket = manager.getTicket(ticketId);
                        if (ticket != null && ticket.getEventId().equals(eventId)) {
                            System.out.println("  " + index + ". " + attendee.getName() +
                                    " (" + attendee.getEmail() + ") - " + ticket.getCategory() +
                                    " - Ticket: " + ticket.getTicketId());
                            index++;
                        }
                    }
                }
            }
        }
        System.out.println();
    }

    private static void showStatistics() {
        System.out.println();
        System.out.println("--- System Statistics ---");
        System.out.println();

        List<Event> events = manager.getAllEvents();
        int totalEvents = events.size();
        int totalAttendees = manager.getTotalAttendees();
        double totalRevenue = manager.getTotalRevenue();
        double avgAttendance = totalEvents > 0 ? (double) totalAttendees / totalEvents : 0;

        System.out.println("Total Events: " + totalEvents);
        System.out.println("Total Attendees: " + totalAttendees);
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        System.out.println("Average Attendance per Event: " + String.format("%.2f", avgAttendance));

        if (!events.isEmpty()) {
            System.out.println();
            System.out.println("Event Breakdown:");
            for (Event event : events) {
                double revenue = manager.getEventRevenue(event.getEventId());
                System.out.println("  • " + event.getName() + ": " +
                        event.getCurrentAttendees() + " attendees, $" +
                        String.format("%.2f", revenue) + " revenue");
            }
        }
        System.out.println();
    }

    private static void sortEvents() {
        System.out.println();
        System.out.println("--- Sort Events ---");
        System.out.println("Sort by: (D)ate, (P)rice, (C)apacity");
        String choice = getStringInput("Choose: ").toUpperCase();

        List<Event> sorted;
        String sortType = "";

        switch (choice) {
            case "D":
                sorted = manager.sortEventsByDate();
                sortType = "Date (Ascending)";
                break;
            case "P":
                sorted = manager.sortEventsByPrice();
                sortType = "Price (Ascending)";
                break;
            case "C":
                sorted = manager.sortEventsByCapacity();
                sortType = "Capacity (Descending)";
                break;
            default:
                System.out.println("Invalid option");
                System.out.println();
                return;
        }

        System.out.println();
        System.out.println("--- Events Sorted by " + sortType + " ---");
        printEventTable(sorted);
        System.out.println();
    }

    private static void filterEvents() {
        System.out.println();
        System.out.println("--- Filter Events ---");
        System.out.println("Filter by: (T)ype, (A)vailability, (D)ate Range");
        String choice = getStringInput("Choose: ").toUpperCase();

        List<Event> filtered;
        String filterDesc = "";

        switch (choice) {
            case "T":
                System.out.println();
                String type = getStringInput("Enter Event Type (Concert/Conference/Workshop/Festival): ");
                filtered = manager.getEventsByType(type);
                filterDesc = "Type: " + type;
                break;

            case "A":
                filtered = manager.getAvailableEvents();
                filterDesc = "Available Events";
                break;

            case "D":
                System.out.println();
                LocalDate startDate = getDateInput("Enter Start Date (YYYY-MM-DD): ");
                LocalDate endDate = getDateInput("Enter End Date (YYYY-MM-DD): ");
                filtered = manager.getEventsInDateRange(startDate, endDate);
                filterDesc = "Date Range: " + startDate + " to " + endDate;
                break;

            default:
                System.out.println("Invalid option");
                System.out.println();
                return;
        }

        System.out.println();
        System.out.println("--- Filtered Events (" + filterDesc + ") ---");
        printEventTable(filtered);
        System.out.println();
        System.out.println("Found " + filtered.size() + " event(s)");
        System.out.println();
    }

    private static void printEventTable(List<Event> events) {
        if (events.isEmpty()) {
            System.out.println("No events to display");
            return;
        }

        System.out.println("┌──────────┬─────────────────────────┬────────────┬────────────┬──────────────┬──────────┐");
        System.out.println("│ Event ID │ Event Name              │ Type       │ Date       │ Capacity     │ Price    │");
        System.out.println("├──────────┼─────────────────────────┼────────────┼────────────┼──────────────┼──────────┤");

        for (Event event : events) {
            String eventId = padRight(event.getEventId(), 8);
            String name = padRight(event.getName(), 23);
            String type = padRight(event.getType(), 10);
            String date = padRight(event.getDate().toString(), 10);
            String capacity = padRight(event.getCurrentAttendees() + "/" + event.getCapacity(), 12);
            String price = padRight("$" + String.format("%.2f", event.getBasePrice()), 8);

            System.out.println("│ " + eventId + " │ " + name + " │ " + type + " │ " +
                    date + " │ " + capacity + " │ " + price + " │");
        }

        System.out.println("└──────────┴─────────────────────────┴────────────┴────────────┴──────────────┴──────────┘");
    }

    private static String padRight(String text, int length) {
        if (text.length() > length) {
            return text.substring(0, length);
        }
        StringBuilder sb = new StringBuilder(text);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine().trim();
                return LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD");
            }
        }
    }
}
