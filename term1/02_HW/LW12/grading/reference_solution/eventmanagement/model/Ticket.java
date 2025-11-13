package eventmanagement.model;

import eventmanagement.exception.EventManagementException;

/**
 * Represents a ticket for an event
 */
public class Ticket {
    private String ticketId;
    private String eventId;
    private String attendeeEmail;
    private double price;
    private String category;
    private boolean isUsed;

    public Ticket(String ticketId, String eventId, String attendeeEmail,
                  double price, String category) throws EventManagementException {
        // Validate ticket ID
        if (ticketId == null || ticketId.trim().isEmpty()) {
            throw new EventManagementException("Ticket ID cannot be null or empty");
        }

        // Validate event ID
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new EventManagementException("Event ID cannot be null or empty");
        }

        // Validate email format
        if (attendeeEmail == null || !isValidEmail(attendeeEmail)) {
            throw new EventManagementException("Invalid email format: " + attendeeEmail);
        }

        // Validate price
        if (price <= 0) {
            throw new EventManagementException("Price must be positive");
        }

        // Validate category
        if (!category.equals("VIP") && !category.equals("Standard") && !category.equals("Student")) {
            throw new EventManagementException("Category must be VIP, Standard, or Student");
        }

        this.ticketId = ticketId;
        this.eventId = eventId;
        this.attendeeEmail = attendeeEmail;
        this.price = price;
        this.category = category;
        this.isUsed = false;
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Count @ symbols (must be exactly one)
        long atCount = email.chars().filter(ch -> ch == '@').count();
        return atCount == 1 && !email.contains(" ");
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAttendeeEmail() {
        return attendeeEmail;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    // Business methods
    public void use() {
        this.isUsed = true;
    }

    public boolean isValid() {
        return !isUsed;
    }

    @Override
    public String toString() {
        return String.format("Ticket[%s: Event=%s, Email=%s, Category=%s, Price=$%.2f, Used=%b]",
            ticketId, eventId, attendeeEmail, category, price, isUsed);
    }
}
