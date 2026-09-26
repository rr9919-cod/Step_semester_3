import java.util.*;

// ===================== Question 1: The Hostel Laundry Queue =====================

/**
 * Abstract base for all wash types.
 * New wash types (e.g., Delicate) can be added by extending this class
 * WITHOUT modifying WashingMachine or WashCycle logic.
 */
abstract class WashType {
    public abstract String getName();
    public abstract int getDurationMinutes();
    public abstract double getCharge();
}

class QuickWash extends WashType {
    public String getName() { return "Quick"; }
    public int getDurationMinutes() { return 30; }
    public double getCharge() { return 20.0; }
}

class NormalWash extends WashType {
    public String getName() { return "Normal"; }
    public int getDurationMinutes() { return 45; }
    public double getCharge() { return 30.0; }
}

class HeavyWash extends WashType {
    public String getName() { return "Heavy"; }
    public int getDurationMinutes() { return 60; }
    public double getCharge() { return 45.0; }
}

// Example of extensibility: a new wash type added without touching existing logic.
class DelicateWash extends WashType {
    public String getName() { return "Delicate"; }
    public int getDurationMinutes() { return 40; }
    public double getCharge() { return 35.0; }
}

class Student {
    private final String name;

    public Student(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

/**
 * Represents one wash session: student + machine + wash type.
 */
class WashCycle {
    private final Student student;
    private final WashingMachine machine;
    private final WashType washType;
    private boolean completed;

    public WashCycle(Student student, WashingMachine machine, WashType washType) {
        this.student = student;
        this.machine = machine;
        this.washType = washType;
        this.completed = false;
    }

    public Student getStudent() { return student; }
    public WashingMachine getMachine() { return machine; }
    public WashType getWashType() { return washType; }
    public double getCharge() { return washType.getCharge(); }

    public void complete() {
        this.completed = true;
    }

    public boolean isCompleted() { return completed; }
}

/**
 * A single washing machine. Its "busy" status is private and can only be
 * changed through startWash()/completeCycle() — no outside code can mutate it directly.
 */
class WashingMachine {
    private final String machineId;
    private boolean busy;
    private WashCycle currentCycle;

    public WashingMachine(String machineId) {
        this.machineId = machineId;
        this.busy = false;
    }

    public String getMachineId() { return machineId; }
    public boolean isBusy() { return busy; }

    /**
     * Attempts to start a wash on this machine.
     * Returns the created WashCycle, or null if the machine is busy.
     */
    public WashCycle startWash(Student student, WashType washType) {
        if (busy) {
            return null; // machine unavailable
        }
        this.currentCycle = new WashCycle(student, this, washType);
        this.busy = true;
        return currentCycle;
    }

    /** Marks the current cycle complete and frees the machine. */
    public void completeCycle() {
        if (currentCycle != null) {
            currentCycle.complete();
        }
        this.busy = false;
        this.currentCycle = null;
    }
}

/**
 * Facade that coordinates students, machines and wash cycles,
 * and prints output matching the required format.
 */
class LaundryService {
    private final Map<String, WashingMachine> machines = new LinkedHashMap<>();

    public void addMachine(WashingMachine machine) {
        machines.put(machine.getMachineId(), machine);
    }

    public void requestWash(Student student, String machineId, WashType washType) {
        WashingMachine machine = machines.get(machineId);
        if (machine == null) {
            System.out.println("No such machine: " + machineId);
            return;
        }
        if (machine.isBusy()) {
            System.out.println("Machine " + machineId + " is currently busy.");
            return;
        }
        WashCycle cycle = machine.startWash(student, washType);
        System.out.printf("%s wash started on %s for %s (%d min). Charge: \u20B9%.2f.%n",
                washType.getName(), machineId, student.getName(),
                washType.getDurationMinutes(), cycle.getCharge());
    }

    public void completeMachine(String machineId) {
        WashingMachine machine = machines.get(machineId);
        if (machine == null || !machine.isBusy()) {
            System.out.println(machineId + " is already free or does not exist.");
            return;
        }
        machine.completeCycle();
        System.out.println(machineId + " cycle completed. " + machineId + " is now free.");
    }
}

public class HostelLaundryQueue {
    public static void main(String[] args) {
        LaundryService service = new LaundryService();
        service.addMachine(new WashingMachine("M1"));
        service.addMachine(new WashingMachine("M2"));

        Student asha = new Student("Asha");
        Student ravi = new Student("Ravi");
        Student neha = new Student("Neha");

        // Asha starts a Quick wash on Machine M1.
        service.requestWash(asha, "M1", new QuickWash());

        // Ravi attempts to start a Heavy wash on Machine M1 (busy).
        service.requestWash(ravi, "M1", new HeavyWash());

        // Ravi starts a Heavy wash on Machine M2.
        service.requestWash(ravi, "M2", new HeavyWash());

        // Machine M1 completes its cycle.
        service.completeMachine("M1");

        // Neha starts a Normal wash on Machine M1.
        service.requestWash(neha, "M1", new NormalWash());
    }
}
