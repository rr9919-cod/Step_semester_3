class GymMember {
    int sessionsAttended = 0;

    GymMember(String id, int fee) {
    }

    void attendSession() {
        sessionsAttended++;
    }

    int getSessionsAttended() {
        return sessionsAttended;
    }

    void displayInfo() {
        System.out.println(
            "Standard Member | Sessions: "
            + sessionsAttended
        );
    }
}

class PremiumMember extends GymMember {
    String trainerName;

    PremiumMember(String id, int fee, String trainerName) {
        super(id, fee);
        this.trainerName = trainerName;
    }

    @Override
    void displayInfo() {
        System.out.println(
            "Premium Member | Trainer: " +
            trainerName +
            " | Sessions: " +
            sessionsAttended
        );
    }
}

class EliteMember extends PremiumMember {
    String lockerNumber;

    EliteMember(String id, int fee,
                String trainerName,
                String lockerNumber) {

        super(id, fee, trainerName);
        this.lockerNumber = lockerNumber;
    }

    @Override
    void displayInfo() {
        System.out.println(
            "Elite Member | Trainer: " +
            trainerName +
            " | Locker: " +
            lockerNumber +
            " | Sessions: " +
            sessionsAttended
        );
    }
}

class GroupClassMember extends GymMember {
    String className;

    GroupClassMember(String id, int fee, String className) {
        super(id, fee);
        this.className = className;
    }

    @Override
    void displayInfo() {
        System.out.println(
            "Group Class Member | Class: " +
            className +
            " | Sessions: " +
            sessionsAttended
        );
    }
}

public class GymMembershipTiers {

    static String classifyGeneration(GymMember member) {

        if (member instanceof EliteMember)
            return "Multilevel descendant (3 generations deep)";

        if (member instanceof GroupClassMember)
            return "Hierarchical sibling (independent branch)";

        return "Standard member";
    }

    static int getTotalSessionsAttended(
        GymMember[] members) {

        int total = 0;

        for (GymMember member : members)
            total += member.getSessionsAttended();

        return total;
    }

    public static void main(String[] args) {

        PremiumMember premium =
            new PremiumMember(
                "MEM2", 2000, "Coach Riya");

        EliteMember elite =
            new EliteMember(
                "MEM3", 3000,
                "Coach Arjun", "L12");

        GroupClassMember group =
            new GroupClassMember(
                "MEM4", 1500, "Zumba");

        premium.attendSession();
        premium.attendSession();
        premium.attendSession();

        elite.attendSession();
        elite.attendSession();

        group.attendSession();
        group.attendSession();
        group.attendSession();
        group.attendSession();

        premium.displayInfo();
        elite.displayInfo();
        group.displayInfo();

        System.out.println(
            classifyGeneration(elite)
        );

        System.out.println(
            classifyGeneration(group)
        );

        GymMember[] members = {
            premium, elite, group
        };

        System.out.println(
            getTotalSessionsAttended(members)
        );
    }
}
