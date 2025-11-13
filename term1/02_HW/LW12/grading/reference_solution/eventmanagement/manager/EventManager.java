package eventmanagement.manager;

import eventmanagement.model.Event;
import eventmanagement.model.Ticket;
import eventmanagement.model.Attendee;
import eventmanagement.exception.EventManagementException;
import java.time.LocalDate;
import java.util.*;

/**
 * Generic Event Manager for managing events, tickets, and attendees
 * @param <T> Event type (must extend Event)
 */
public class EventManager<T extends Event> {
    private List<T> events;
    private Map<String, Ticket> ticketCatalog;
    private Set<String> registeredEmails;
    private Map<String, Attendee> attendees;
    private int ticketCounter;

    // COLLECTION CHOICE: I chose ArrayList for events because:
    // 1. We need dynamic sizing (events are added/removed frequently)
    // 2. We need to maintain insertion order for display purposes
    // 3. We need index-based access for sorting and filtering operations
    // 4. ArrayList provides O(1) access time for get() operations

    // COLLECTION CHOICE: I chose HashMap for ticketCatalog because:
    // 1. We need fast O(1) lookup by ticket ID (primary operation)
    // 2. Each ticket ID is unique (natural key-value mapping)
    // 3. No need for ordering (tickets accessed individually)
    // 4. HashMap provides efficient put/get/containsKey operations

    // COLLECTION CHOICE: I chose HashSet for registeredEmails because:
    // 1. We need to enforce uniqueness (no duplicate registrations per event)
    // 2. We only need to check contains() - O(1) operation
    // 3. No ordering required (just checking membership)
    // 4. HashSet automatically handles uniqueness constraint

    public EventManager() {
        this.events = new ArrayList<>();
        this.ticketCatalog = new HashMap<>();
        this.registeredEmails = new HashSet<>();
        this.attendees = new HashMap<>();
        this.ticketCounter = 1;
    }

    // ========== Event Management ==========

    public boolean addEvent(T event) throws EventManagementException {
        if (event == null) {
            throw new EventManagementException("Event cannot be null");
        }

        // Check for duplicate ID
        for (T existingEvent : events) {
            if (existingEvent.getEventId().equals(event.getEventId())) {
                throw new EventManagementException("Event with this ID already exists: " + event.getEventId());
            }

            // DESIGN DECISION: Check for duplicate names (case-insensitive) to prevent
            // user confusion and ensure each event is uniquely identifiable by name
            if (existingEvent.getName().equalsIgnoreCase(event.getName())) {
                throw new EventManagementException("Event with duplicate name already exists: " + event.getName());
            }
        }

        return events.add(event);
    }

    // DESIGN DECISION: When removing an event, we must also remove all associated
    // tickets and update attendees because tickets become invalid when event is cancelled
    public boolean removeEvent(String eventId) {
        T event = getEvent(eventId);
        if (event == null) {
            return false;
        }

        // Remove all tickets for this event
        List<String> ticketsToRemove = new ArrayList<>();
        for (Map.Entry<String, Ticket> entry : ticketCatalog.entrySet()) {
            if (entry.getValue().getEventId().equals(eventId)) {
                ticketsToRemove.add(entry.getKey());
            }
        }

        for (String ticketId : ticketsToRemove) {
            Ticket ticket = ticketCatalog.get(ticketId);
            String email = ticket.getAttendeeEmail();

            // Remove from catalog
            ticketCatalog.remove(ticketId);

            // Update attendee
            Attendee attendee = attendees.get(email);
            if (attendee != null) {
                attendee.removeTicket(ticketId);

                // Remove attendee if no more tickets
                if (attendee.getTicketCount() == 0) {
                    attendees.remove(email);
                }
            }

            // Remove from registered emails (event-specific)
            registeredEmails.remove(eventId + ":" + email);
        }

        return events.remove(event);
    }

    public T getEvent(String eventId) {
        for (T event : events) {
            if (event.getEventId().equals(eventId)) {
                return event;
            }
        }
        return null;
    }

    public List<T> getAllEvents() {
        // Defensive copy
        return new ArrayList<>(events);
    }

    public List<T> getEventsByType(String type) {
        List<T> result = new ArrayList<>();
        for (T event : events) {
            if (event.getType().equals(type)) {
                result.add(event);
            }
        }
        return result;
    }

    // ========== Ticket Management ==========

    // INTEGRATION LOGIC: purchaseTicket() is the most complex method because it must:
    // 1. Validate event exists and has capacity
    // 2. Check email not already registered for THIS event (per-event uniqueness)
    // 3. Create or update attendee in attendees map
    // 4. Add ticket to ticketCatalog with unique ID
    // 5. Add email to registeredEmails set with event prefix
    // 6. Update event's currentAttendees counter
    // All these updates must succeed together or fail together for data consistency
    public String purchaseTicket(String eventId, String attendeeEmail,
                                 String attendeeName, String phone,
                                 String category) throws EventManagementException {
        // Validate event exists
        T event = getEvent(eventId);
        if (event == null) {
            throw new EventManagementException("Event not found: " + eventId);
        }

        // Check capacity
        if (!event.hasAvailableSeats()) {
            throw new EventManagementException("Event is sold out: " + event.getName());
        }

        // DESIGN DECISION: We use a composite key (eventId:email) for registeredEmails
        // because one email should be able to register for DIFFERENT events, but not
        // for the SAME event twice. This prevents double-booking per event while
        // allowing attendance at multiple events.
        String registrationKey = eventId + ":" + attendeeEmail;
        if (registeredEmails.contains(registrationKey)) {
            throw new EventManagementException("Email is already registered for this event: " + attendeeEmail);
        }

        // Create or get attendee
        Attendee attendee = attendees.get(attendeeEmail);
        if (attendee == null) {
            attendee = new Attendee(attendeeEmail, attendeeName, phone);
            attendees.put(attendeeEmail, attendee);
        }

        // Create ticket
        String ticketId = "TKT" + String.format("%03d", ticketCounter++);
        double price = event.getBasePrice(); // Could vary by category in bonus
        Ticket ticket = new Ticket(ticketId, eventId, attendeeEmail, price, category);

        // Update all collections
        ticketCatalog.put(ticketId, ticket);
        registeredEmails.add(registrationKey);
        attendee.addTicket(ticketId);
        event.addAttendee();

        return ticketId;
    }

    public boolean cancelTicket(String ticketId) {
        Ticket ticket = ticketCatalog.get(ticketId);
        if (ticket == null) {
            return false;
        }

        String eventId = ticket.getEventId();
        String email = ticket.getAttendeeEmail();
        String registrationKey = eventId + ":" + email;

        // Remove from catalog
        ticketCatalog.remove(ticketId);

        // Remove from registered emails
        registeredEmails.remove(registrationKey);

        // Update attendee
        Attendee attendee = attendees.get(email);
        if (attendee != null) {
            attendee.removeTicket(ticketId);

            // Remove attendee if no more tickets
            if (attendee.getTicketCount() == 0) {
                attendees.remove(email);
            }
        }

        // Update event
        T event = getEvent(eventId);
        if (event != null) {
            event.removeAttendee();
        }

        return true;
    }

    public Ticket getTicket(String ticketId) {
        return ticketCatalog.get(ticketId);
    }

    public List<Ticket> getTicketsForEvent(String eventId) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : ticketCatalog.values()) {
            if (ticket.getEventId().equals(eventId)) {
                result.add(ticket);
            }
        }
        return result;
    }

    public boolean isEmailRegisteredForEvent(String eventId, String email) {
        String registrationKey = eventId + ":" + email;
        return registeredEmails.contains(registrationKey);
    }

    public List<Attendee> getEventAttendees(String eventId) {
        List<Attendee> result = new ArrayList<>();
        for (Attendee attendee : attendees.values()) {
            for (String ticketId : attendee.getTicketIds()) {
                Ticket ticket = ticketCatalog.get(ticketId);
                if (ticket != null && ticket.getEventId().equals(eventId)) {
                    result.add(attendee);
                    break;
                }
            }
        }
        return result;
    }

    // ========== Statistics ==========

    public int getTotalAttendees() {
        return attendees.size();
    }

    public Map<String, Integer> getAttendeeCount() {
        Map<String, Integer> counts = new HashMap<>();
        for (T event : events) {
            counts.put(event.getEventId(), event.getCurrentAttendees());
        }
        return counts;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Ticket ticket : ticketCatalog.values()) {
            total += ticket.getPrice();
        }
        return total;
    }

    public double getEventRevenue(String eventId) {
        double total = 0;
        for (Ticket ticket : ticketCatalog.values()) {
            if (ticket.getEventId().equals(eventId)) {
                total += ticket.getPrice();
            }
        }
        return total;
    }

    // ========== Sorting ==========

    // DESIGN DECISION: I implemented Comparable<Event> in the Event class for natural
    // ordering by date (most common use case), and use Comparator for alternative
    // sorting criteria (price, capacity). This follows Java best practices where
    // Comparable defines the "natural" ordering and Comparator provides flexibility
    // for custom orderings.

    public List<T> sortEventsByDate() {
        List<T> sorted = new ArrayList<>(events);
        Collections.sort(sorted);
        return sorted;
    }

    public List<T> sortEventsByPrice() {
        List<T> sorted = new ArrayList<>(events);
        Collections.sort(sorted, new Comparator<T>() {
            @Override
            public int compare(T e1, T e2) {
                return Double.compare(e1.getBasePrice(), e2.getBasePrice());
            }
        });
        return sorted;
    }

    public List<T> sortEventsByCapacity() {
        List<T> sorted = new ArrayList<>(events);
        Collections.sort(sorted, new Comparator<T>() {
            @Override
            public int compare(T e1, T e2) {
                // Descending order (largest first)
                return Integer.compare(e2.getCapacity(), e1.getCapacity());
            }
        });
        return sorted;
    }

    // ========== Filtering ==========

    public List<T> getEventsInDateRange(LocalDate startDate, LocalDate endDate) {
        List<T> result = new ArrayList<>();
        for (T event : events) {
            LocalDate eventDate = event.getDate();
            if ((eventDate.isEqual(startDate) || eventDate.isAfter(startDate)) &&
                (eventDate.isEqual(endDate) || eventDate.isBefore(endDate))) {
                result.add(event);
            }
        }
        return result;
    }

    public List<T> getAvailableEvents() {
        List<T> result = new ArrayList<>();
        for (T event : events) {
            if (event.hasAvailableSeats()) {
                result.add(event);
            }
        }
        return result;
    }

    public List<T> getSoldOutEvents() {
        List<T> result = new ArrayList<>();
        for (T event : events) {
            if (!event.hasAvailableSeats()) {
                result.add(event);
            }
        }
        return result;
    }

    // VALIDATION LOGIC: getEventsByTypeAndPriceRange requires BOTH conditions to be
    // satisfied because this is a filtering operation with AND logic. If we used OR,
    // we'd get too many irrelevant results. The business requirement is to find events
    // that are of a specific type AND within a price range (e.g., "cheap concerts")
    public List<T> getEventsByTypeAndPriceRange(String type, double minPrice, double maxPrice) {
        List<T> result = new ArrayList<>();
        for (T event : events) {
            if (event.getType().equals(type) &&
                event.getBasePrice() >= minPrice &&
                event.getBasePrice() <= maxPrice) {
                result.add(event);
            }
        }
        return result;
    }
}
