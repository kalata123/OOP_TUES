package eventmanagement.model;

import eventmanagement.exception.EventManagementException;
import java.time.LocalDate;
import java.time.DayOfWeek;

/**
 * Represents an event in the management system
 */
public class Event implements Comparable<Event> {
    private String eventId;
    private String name;
    private String type;
    private LocalDate date;
    private int capacity;
    private double basePrice;
    private int currentAttendees;

    public Event(String eventId, String name, String type, LocalDate date,
                 int capacity, double basePrice) throws EventManagementException {
        // Validate event ID
        if (eventId == null || eventId.trim().isEmpty()) {
            throw new EventManagementException("Event ID cannot be null or empty");
        }

        // Validate name
        if (name == null || name.trim().isEmpty()) {
            throw new EventManagementException("Event name must be 3-50 characters");
        }
        if (name.length() < 3 || name.length() > 50) {
            throw new EventManagementException("Event name must be 3-50 characters");
        }

        // Validate type
        if (!type.equals("Concert") && !type.equals("Conference") &&
            !type.equals("Workshop") && !type.equals("Festival")) {
            throw new EventManagementException("Event type must be Concert, Conference, Workshop, or Festival");
        }

        // Validate date
        LocalDate now = LocalDate.now();
        if (date.isBefore(now)) {
            throw new EventManagementException("Event date cannot be in the past");
        }

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            throw new EventManagementException("Event date cannot be on weekends");
        }

        // VALIDATION LOGIC: Capacity must be divisible by 10 to ensure proper venue
        // organization and seating arrangements in blocks of 10
        if (capacity % 10 != 0) {
            throw new EventManagementException("Event capacity must be divisible by 10");
        }

        // Validate capacity range
        if (capacity < 10 || capacity > 1000) {
            throw new EventManagementException("Event capacity must be between 10 and 1000");
        }

        // VALIDATION LOGIC: Base price must end in .00 or .50 for cleaner
        // financial transactions and easier change management
        double cents = (basePrice * 100) % 100;
        if (cents != 0 && cents != 50) {
            throw new EventManagementException("Base price must end in .00 or .50");
        }

        this.eventId = eventId;
        this.name = name;
        this.type = type;
        this.date = date;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.currentAttendees = 0;
    }

    // Getters
    public String getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public int getCurrentAttendees() {
        return currentAttendees;
    }

    // Business methods
    public boolean hasAvailableSeats() {
        return currentAttendees < capacity;
    }

    public boolean addAttendee() throws EventManagementException {
        if (currentAttendees >= capacity) {
            throw new EventManagementException("Event is sold out: " + name);
        }
        currentAttendees++;
        return true;
    }

    public boolean removeAttendee() {
        if (currentAttendees > 0) {
            currentAttendees--;
            return true;
        }
        return false;
    }

    // DESIGN DECISION: Implemented compareTo() to sort by date because events are
    // naturally ordered chronologically, making it intuitive for users to see
    // upcoming events in time order
    @Override
    public int compareTo(Event other) {
        return this.date.compareTo(other.date);
    }

    @Override
    public String toString() {
        return String.format("Event[%s: %s, Type=%s, Date=%s, Capacity=%d/%d, Price=$%.2f]",
            eventId, name, type, date, currentAttendees, capacity, basePrice);
    }
}
