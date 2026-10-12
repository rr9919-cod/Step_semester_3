import java.util.*;

public class Problem2_CampusVehiclePassSystem {
    interface Chargeable {
        void charge();
    }

    static abstract class Vehicle {
        String passNumber, owner;
        Vehicle(String passNumber, String owner) {
            this.passNumber = passNumber;
            this.owner = owner;
        }
        abstract int passFee();
        abstract String type();
        void printPass() {
            System.out.println(passNumber + " (" + type() + ") pass fee " + passFee());
        }
    }

    static class Bike extends Vehicle {
        Bike(String p, String o) { super(p, o); }
        int passFee() { return 300; }
        String type() { return "Bike"; }
    }

    static class Car extends Vehicle {
        Car(String p, String o) { super(p, o); }
        int passFee() { return 1000; }
        String type() { return "Car"; }
    }

    static class EBike extends Vehicle implements Chargeable {
        EBike(String p, String o) { super(p, o); }
        int passFee() { return 300; }
        String type() { return "EBike"; }
        public void charge() { System.out.println(passNumber + " charging bay allotted"); }
    }

    static class ECar extends Vehicle implements Chargeable {
        ECar(String p, String o) { super(p, o); }
        int passFee() { return 1000; }
        String type() { return "ECar"; }
        public void charge() { System.out.println(passNumber + " charging bay allotted"); }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<String, Vehicle> vehicles = new LinkedHashMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            if (p[0].equalsIgnoreCase("PASS") && p.length >= 4) {
                String type = p[1], pass = p[2], owner = p[3];
                Vehicle v = null;
                switch (type.toLowerCase()) {
                    case "bike": v = new Bike(pass, owner); break;
                    case "car": v = new Car(pass, owner); break;
                    case "ebike": v = new EBike(pass, owner); break;
                    case "ecar": v = new ECar(pass, owner); break;
                }
                if (v != null) {
                    vehicles.put(pass, v);
                    v.printPass();
                }
            } else if (p[0].equalsIgnoreCase("CHARGE") && p.length >= 2) {
                Vehicle v = vehicles.get(p[1]);
                if (v instanceof Chargeable) ((Chargeable) v).charge();
                else if (v != null) System.out.println(p[1] + " rejected: charging unsupported");
                else System.out.println("Vehicle not found");
            }
        }
        sc.close();
    }
}
