abstract class ClassroomDevice {

    abstract String operate();
}

interface Chargeable {
    String charge();
    String charge(int minutes);
}

class Tablet extends ClassroomDevice
        implements Chargeable {

    String assetTag;

    Tablet(String assetTag) {
        this.assetTag = assetTag;
    }

    String operate() {
        return "Tablet " + assetTag +
               " displaying lesson";
    }

    public String charge() {
        return assetTag + " charging";
    }

    public String charge(int minutes) {
        return assetTag +
               " charging for " +
               minutes + " minutes";
    }
}

public class DigitalClassroomSetup {

    public static void main(String[] args) {

        Tablet t = new Tablet("TAB-5");

        System.out.println(t.operate());
        System.out.println(t.charge());
        System.out.println(t.charge(30));
    }
}
