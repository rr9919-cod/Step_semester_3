import java.util.*;

// ================ Question 1: Vehicle Rental System ================

class Customer {
    private final String name;

    public Customer(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

/**
 * Abstract base for all vehicle categories. Each category defines its own
 * daily rate / charge calculation via calculateCharge(). New categories
 * (e.g., Bike) can be added without modifying RentalService logic.
 */
abstract class Vehicle {
    private final String vehicleId;
    private boolean available;

    public Vehicle(String vehicleId) {
        this.vehicleId = vehicleId;
        this.available = true;
    }

    public String getVehicleId() { return vehicleId; }
    public boolean isAvailable() { return available; }

    /** Only RentalService (via package-private-like access here) should flip this. */
    void markUnavailable() { this.available = false; }
    void markAvailable() { this.available = true; }

    public abstract String getCategory();
    public abstract double calculateCharge(int days);
}

class Sedan extends Vehicle {
    private static final double DAILY_RATE = 40.0;

    public Sedan(String vehicleId) { super(vehicleId); }
    public String getCategory() { return "Sedan"; }
    public double calculateCharge(int days) { return DAILY_RATE * days; }
}

class SUV extends Vehicle {
    private static final double DAILY_RATE = 60.0;

    public SUV(String vehicleId) { super(vehicleId); }
    public String getCategory() { return "SUV"; }
    public double calculateCharge(int days) { return DAILY_RATE * days; }
}

class Truck extends Vehicle {
    private static final double DAILY_RATE = 80.0;

    public Truck(String vehicleId) { super(vehicleId); }
    public String getCategory() { return "Truck"; }
    public double calculateCharge(int days) { return DAILY_RATE * days; }
}

/**
 * Tracks a single rental: which vehicle, which customer, and for how long.
 */
class Rental {
    private final Vehicle vehicle;
    private final Customer customer;
    private final int days;
    private final double charge;
    private boolean active;

    public Rental(Vehicle vehicle, Customer customer, int days) {
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
        this.charge = vehicle.calculateCharge(days);
        this.active = true;
    }

    public Vehicle getVehicle() { return vehicle; }
    public Customer getCustomer() { return customer; }
    public double getCharge() { return charge; }
    public boolean isActive() { return active; }

    public void close() { this.active = false; }
}

/**
 * Facade coordinating rentals and printing required output. No outside
 * code can change a Vehicle's availability directly.
 */
class RentalService {
    private final Map<String, Vehicle> vehicles = new LinkedHashMap<>();
    private final Map<String, Rental> activeRentals = new LinkedHashMap<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.put(vehicle.getVehicleId(), vehicle);
    }

    public void rent(Customer customer, String vehicleId, int days) {
        Vehicle vehicle = vehicles.get(vehicleId);
        if (vehicle == null) {
            System.out.println("No such vehicle: " + vehicleId);
            return;
        }
        if (!vehicle.isAvailable()) {
            System.out.println(vehicle.getCategory() + " " + vehicleId + " is currently unavailable.");
            return;
        }

        Rental rental = new Rental(vehicle, customer, days);
        vehicle.markUnavailable();
        activeRentals.put(vehicleId, rental);

        System.out.printf("%s %s rented successfully by %s. Rental charge: $%.2f.%n",
                vehicle.getCategory(), vehicleId, customer.getName(), rental.getCharge());
    }

    public void returnVehicle(String vehicleId) {
        Vehicle vehicle = vehicles.get(vehicleId);
        Rental rental = activeRentals.get(vehicleId);
        if (vehicle == null || rental == null || !rental.isActive()) {
            System.out.println(vehicleId + " has no active rental to return.");
            return;
        }

        rental.close();
        vehicle.markAvailable();
        activeRentals.remove(vehicleId);

        System.out.println(vehicle.getCategory() + " " + vehicleId + " returned by " + rental.getCustomer().getName() + ".");
    }
}

public class VehicleRentalSystem {
    public static void main(String[] args) {
        RentalService service = new RentalService();
        service.addVehicle(new Sedan("Sedan A"));
        service.addVehicle(new SUV("SUV B"));

        Customer customer1 = new Customer("Customer 1");
        Customer customer2 = new Customer("Customer 2");
        Customer customer3 = new Customer("Customer 3");

        // Customer 1 rents Sedan A for 3 days.
        service.rent(customer1, "Sedan A", 3);

        // Customer 2 attempts to rent Sedan A for 2 days (unavailable).
        service.rent(customer2, "Sedan A", 2);

        // Customer 1 returns Sedan A.
        service.returnVehicle("Sedan A");

        // Customer 3 rents SUV B for 5 days.
        service.rent(customer3, "SUV B", 5);
    }
}
