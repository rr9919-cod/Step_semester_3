import java.util.*;

// ================ Question 3: The Campus Premiere Ticket Counter ================

class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

/**
 * Abstract base for seat categories. Each category defines its own price
 * via getPrice(). New categories can be added without touching Show/Booking logic.
 */
abstract class Seat {
    private final String seatId;

    public Seat(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatId() { return seatId; }
    public abstract double getPrice();
    public abstract String getCategory();
}

class RegularSeat extends Seat {
    public RegularSeat(String seatId) { super(seatId); }
    public double getPrice() { return 150.0; }
    public String getCategory() { return "Regular"; }
}

class PremiumSeat extends Seat {
    public PremiumSeat(String seatId) { super(seatId); }
    public double getPrice() { return 250.0; }
    public String getCategory() { return "Premium"; }
}

class ReclinerSeat extends Seat {
    public ReclinerSeat(String seatId) { super(seatId); }
    public double getPrice() { return 400.0; }
    public String getCategory() { return "Recliner"; }
}

/**
 * A booking of one or more seats for a show, made by a customer.
 */
class Booking {
    private final Customer customer;
    private final Show show;
    private final List<Seat> seats;
    private boolean cancelled;

    public Booking(Customer customer, Show show, List<Seat> seats) {
        this.customer = customer;
        this.show = show;
        this.seats = new ArrayList<>(seats);
        this.cancelled = false;
    }

    public Customer getCustomer() { return customer; }
    public List<Seat> getSeats() { return Collections.unmodifiableList(seats); }

    public double getTotal() {
        double total = 0;
        for (Seat seat : seats) total += seat.getPrice();
        return total;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() { return cancelled; }
}

/**
 * A movie show at a given time. Keeps track of which seats are already booked.
 */
class Show {
    private static final int MAX_SEATS_PER_BOOKING = 6;

    private final String showName;
    private final Map<String, Seat> seatCatalog = new LinkedHashMap<>();
    private final Set<String> bookedSeatIds = new HashSet<>();
    private boolean started;

    public Show(String showName) {
        this.showName = showName;
        this.started = false;
    }

    public String getShowName() { return showName; }

    public void addSeat(Seat seat) {
        seatCatalog.put(seat.getSeatId(), seat);
    }

    public boolean isSeatAvailable(String seatId) {
        return seatCatalog.containsKey(seatId) && !bookedSeatIds.contains(seatId);
    }

    public void markStarted() {
        this.started = true;
    }

    public boolean hasStarted() { return started; }

    /**
     * Attempts to book the given seat IDs for a customer.
     * Returns a BookingResult describing success/failure and any conflicting seat.
     */
    public BookingResult book(Customer customer, List<String> seatIds) {
        if (seatIds.isEmpty() || seatIds.size() > MAX_SEATS_PER_BOOKING) {
            return BookingResult.failure("A booking must include 1 to " + MAX_SEATS_PER_BOOKING + " seats.");
        }

        for (String seatId : seatIds) {
            if (!isSeatAvailable(seatId)) {
                return BookingResult.failure("Seat " + seatId + " is already booked for this show.");
            }
        }

        List<Seat> selected = new ArrayList<>();
        for (String seatId : seatIds) {
            selected.add(seatCatalog.get(seatId));
            bookedSeatIds.add(seatId);
        }

        Booking booking = new Booking(customer, this, selected);
        return BookingResult.success(booking);
    }

    /** Releases the seats of a cancelled booking back to availability. */
    public void releaseSeats(Booking booking) {
        for (Seat seat : booking.getSeats()) {
            bookedSeatIds.remove(seat.getSeatId());
        }
    }
}

/** Simple result wrapper for booking attempts. */
class BookingResult {
    private final boolean ok;
    private final Booking booking;
    private final String message;

    private BookingResult(boolean ok, Booking booking, String message) {
        this.ok = ok;
        this.booking = booking;
        this.message = message;
    }

    public static BookingResult success(Booking booking) {
        return new BookingResult(true, booking, null);
    }

    public static BookingResult failure(String message) {
        return new BookingResult(false, null, message);
    }

    public boolean isOk() { return ok; }
    public Booking getBooking() { return booking; }
    public String getMessage() { return message; }
}

/**
 * Facade coordinating bookings and printing required output.
 */
class TicketCounter {
    private final Map<Customer, Booking> activeBookings = new LinkedHashMap<>();

    public void bookSeats(Customer customer, Show show, List<String> seatIds) {
        BookingResult result = show.book(customer, seatIds);
        if (!result.isOk()) {
            System.out.println(result.getMessage());
            return;
        }
        Booking booking = result.getBooking();
        activeBookings.put(customer, booking);

        StringBuilder seatList = new StringBuilder();
        for (Seat seat : booking.getSeats()) {
            if (seatList.length() > 0) seatList.append(", ");
            seatList.append(seat.getSeatId());
        }
        System.out.printf("Booking confirmed for %s: %s. Total: \u20B9%.2f.%n",
                customer.getName(), seatList, booking.getTotal());
    }

    public void cancelBooking(Customer customer, Show show) {
        Booking booking = activeBookings.get(customer);
        if (booking == null || booking.isCancelled()) {
            System.out.println("No active booking to cancel for " + customer.getName() + ".");
            return;
        }
        if (show.hasStarted()) {
            System.out.println("Cannot cancel: the show has already started.");
            return;
        }

        booking.cancel();
        show.releaseSeats(booking);

        StringBuilder seatList = new StringBuilder();
        for (Seat seat : booking.getSeats()) {
            if (seatList.length() > 0) seatList.append(", ");
            seatList.append(seat.getSeatId());
        }
        System.out.println(customer.getName() + "'s booking cancelled. Seats " + seatList + " released.");
    }
}

public class CampusPremiereTicketCounter {
    public static void main(String[] args) {
        Show sevenPmShow = new Show("7 PM show");
        sevenPmShow.addSeat(new RegularSeat("A1"));
        sevenPmShow.addSeat(new RegularSeat("A2"));
        sevenPmShow.addSeat(new PremiumSeat("F5"));
        sevenPmShow.addSeat(new ReclinerSeat("R1"));

        TicketCounter counter = new TicketCounter();

        Customer asha = new Customer("Asha");
        Customer ravi = new Customer("Ravi");
        Customer neha = new Customer("Neha");

        // Asha books Regular seats A1, A2 and Premium seat F5.
        counter.bookSeats(asha, sevenPmShow, Arrays.asList("A1", "A2", "F5"));

        // Ravi attempts to book seat A2 (already booked).
        counter.bookSeats(ravi, sevenPmShow, Arrays.asList("A2"));

        // Ravi books Recliner seat R1.
        counter.bookSeats(ravi, sevenPmShow, Arrays.asList("R1"));

        // Asha cancels her booking before the show starts.
        counter.cancelBooking(asha, sevenPmShow);

        // Neha books seat A2 (now released).
        counter.bookSeats(neha, sevenPmShow, Arrays.asList("A2"));
    }
}
