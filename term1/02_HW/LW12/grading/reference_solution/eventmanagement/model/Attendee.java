package eventmanagement.model;

import eventmanagement.exception.EventManagementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an attendee in the event management system
 */
public class Attendee {
    private String email;
    private String name;
    private String phone;
    private List<String> ticketIds;

    public Attendee(String email, String name, String phone) throws EventManagementException {
        // Validate email
        if (email == null || !isValidEmail(email)) {
            throw new EventManagementException("Invalid email format: " + email);
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new EventManagementException("Name cannot be null or empty");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new EventManagementException("Name must be 2-50 characters");
        }

        // Validate phone
        if (phone == null || !isValidPhone(phone)) {
            throw new EventManagementException("Phone number must be exactly 10 digits");
        }

        this.email = email;
        this.name = name;
        this.phone = phone;

        // COLLECTION CHOICE: I chose ArrayList for ticketIds because:
        // 1. We need to maintain the order of ticket purchases (chronological)
        // 2. We frequently need to add/remove tickets (dynamic size required)
        // 3. We may need to access specific tickets by index
        // 4. Duplicate ticket IDs should not occur (handled at business logic level)
        this.ticketIds = new ArrayList<>();
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // Count @ symbols (must be exactly one)
        long atCount = email.chars().filter(ch -> ch == '@').count();
        return atCount == 1 && !email.contains(" ");
    }

    private boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        // Remove any non-digit characters for validation
        String digitsOnly = phone.replaceAll("[^0-9]", "");
        return digitsOnly.length() == 10;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    // Return defensive copy to protect internal list
    public List<String> getTicketIds() {
        return new ArrayList<>(ticketIds);
    }

    // Ticket management
    public void addTicket(String ticketId) {
        if (ticketId != null && !ticketId.trim().isEmpty()) {
            ticketIds.add(ticketId);
        }
    }

    public void removeTicket(String ticketId) {
        ticketIds.remove(ticketId);
    }

    public int getTicketCount() {
        return ticketIds.size();
    }

    @Override
    public String toString() {
        return String.format("Attendee[Email=%s, Name=%s, Tickets=%d]",
            email, name, ticketIds.size());
    }
}
