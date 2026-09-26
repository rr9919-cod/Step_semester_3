import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

// ================ Question 4: Hotel Booking System ================

class Customer2 {
    private final String name;

    public Customer2(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

/**
 * Abstract base for room categories. Each category implements its own
 * pricing rule via calculatePrice(). New categories can be added without
 * modifying Reservation/Hotel logic.
 */
abstract class Room {
    private final String roomNumber;
    private final List<Reservation> reservations = new ArrayList<>();

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() { return roomNumber; }
    public abstract String getCategory();
    public abstract double calculatePrice(long nights);

    /** True if the room has no active reservation overlapping the given period. */
    public boolean isAvailable(LocalDate start, LocalDate end) {
        for (Reservation r : reservations) {
            if (r.isActive() && r.overlaps(start, end)) {
                return false;
            }
        }
        return true;
    }

    void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }
}

class StandardRoom extends Room {
    private static final double NIGHTLY_RATE = 100.0;

    public StandardRoom(String roomNumber) { super(roomNumber); }
    public String getCategory() { return "Standard Room"; }
    public double calculatePrice(long nights) { return NIGHTLY_RATE * nights; }
}

class DeluxeRoom extends Room {
    private static final double NIGHTLY_RATE = 160.0;

    public DeluxeRoom(String roomNumber) { super(roomNumber); }
    public String getCategory() { return "Deluxe Room"; }
    public double calculatePrice(long nights) { return NIGHTLY_RATE * nights; }
}

class Suite extends Room {
    private static final double NIGHTLY_RATE = 250.0;

    public Suite(String roomNumber) { super(roomNumber); }
    public String getCategory() { return "Suite"; }
    public double calculatePrice(long nights) { return NIGHTLY_RATE * nights; }
}

/**
 * A reservation of a room by a customer for a date range.
 * Can only be cancelled before the cancellation deadline (start date).
 */
class Reservation {
    private final Customer2 customer;
    private final Room room;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final double price;
    private boolean active;

    public Reservation(Customer2 customer, Room room, LocalDate startDate, LocalDate endDate) {
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        this.price = room.calculatePrice(nights);
        this.active = true;
    }

    public Customer2 getCustomer() { return customer; }
    public Room getRoom() { return room; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getPrice() { return price; }
    public boolean isActive() { return active; }

    public boolean overlaps(LocalDate otherStart, LocalDate otherEnd) {
        return startDate.isBefore(otherEnd) && otherStart.isBefore(endDate);
    }

    /** Cancellation deadline: must cancel before the reservation's start date. */
    public boolean canCancel(LocalDate today) {
        return active && today.isBefore(startDate);
    }

    public void cancel() {
        this.active = false;
    }

    public String getDateRangeLabel() {
        String startMonth = capMonth(startDate);
        String endMonth = capMonth(endDate);
        if (startMonth.equals(endMonth)) {
            return startMonth + " " + startDate.getDayOfMonth() + "-" + endDate.getDayOfMonth();
        }
        return startMonth + " " + startDate.getDayOfMonth() + " - " + endMonth + " " + endDate.getDayOfMonth();
    }

    private String capMonth(LocalDate date) {
        String m = date.getMonth().toString();
        return m.substring(0, 1) + m.substring(1, 3).toLowerCase();
    }
}

/**
 * Facade coordinating rooms and reservations, printing required output.
 */
class HotelBookingSystem {
    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private LocalDate today = LocalDate.of(2025, 12, 1); // reference "today" for deadline checks

    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
    }

    public void checkAvailability(String roomNumber, LocalDate start, LocalDate end) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("No such room: " + roomNumber);
            return;
        }
        boolean available = room.isAvailable(start, end);
        String label = formatRange(start, end);
        if (available) {
            System.out.println(room.getCategory() + " " + roomNumber + " is available from " + label + ".");
        } else {
            System.out.println(room.getCategory() + " " + roomNumber + " is not available from " + label + ".");
        }
    }

    public Reservation reserve(Customer2 customer, String roomNumber, LocalDate start, LocalDate end) {
        Room room = rooms.get(roomNumber);
        if (room == null) {
            System.out.println("No such room: " + roomNumber);
            return null;
        }
        if (!room.isAvailable(start, end)) {
            System.out.println(room.getCategory() + " " + roomNumber + " is not available from " + formatRange(start, end) + ".");
            return null;
        }

        Reservation reservation = new Reservation(customer, room, start, end);
        room.addReservation(reservation);
        reservations.add(reservation);

        System.out.printf("Reservation confirmed for %s, %s %s (%s). Price: $%.2f.%n",
                customer.getName(), room.getCategory(), roomNumber, reservation.getDateRangeLabel(), reservation.getPrice());
        return reservation;
    }

    public void cancel(Reservation reservation) {
        if (reservation.canCancel(today)) {
            reservation.cancel();
            System.out.println("Reservation for " + reservation.getCustomer().getName() + ", " +
                    reservation.getRoom().getCategory() + " " + reservation.getRoom().getRoomNumber() +
                    " (" + reservation.getDateRangeLabel() + ") cancelled successfully.");
        } else {
            System.out.println("Cannot cancel: past the cancellation deadline.");
        }
    }

    private String formatRange(LocalDate start, LocalDate end) {
        String startMonth = capMonth(start);
        String endMonth = capMonth(end);
        return startMonth + " " + start.getDayOfMonth() + " to " + endMonth + " " + end.getDayOfMonth();
    }

    private String capMonth(LocalDate date) {
        String m = date.getMonth().toString();
        return m.substring(0, 1) + m.substring(1, 3).toLowerCase();
    }
}

public class HotelBookingSystemDemo {
    public static void main(String[] args) {
        HotelBookingSystem hotel = new HotelBookingSystem();
        hotel.addRoom(new StandardRoom("101"));
        hotel.addRoom(new DeluxeRoom("201"));

        Customer2 customerA = new Customer2("Customer A");
        Customer2 customerB = new Customer2("Customer B");
        Customer2 customerC = new Customer2("Customer C");

        // Customer A checks availability for Standard Room 101 from Jan 1 to Jan 5.
        hotel.checkAvailability("101", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));

        // Customer A reserves Standard Room 101 from Jan 1 to Jan 5.
        Reservation reservationA = hotel.reserve(customerA, "101", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 5));

        // Customer B attempts to reserve Standard Room 101 from Jan 3 to Jan 7 (overlaps).
        hotel.reserve(customerB, "101", LocalDate.of(2026, 1, 3), LocalDate.of(2026, 1, 7));

        // Customer A cancels reservation for Standard Room 101 (before deadline).
        hotel.cancel(reservationA);

        // Customer C reserves Deluxe Room 201 from Feb 10 to Feb 12.
        hotel.reserve(customerC, "201", LocalDate.of(2026, 2, 10), LocalDate.of(2026, 2, 12));
    }
}
