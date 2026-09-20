class GymMember {

    void displayInfo() {
        System.out.print(
            "Standard | Sessions: 0 | "
        );
    }
}

class PremiumMember extends GymMember {

    String trainerName;

    PremiumMember(String trainerName) {
        this.trainerName = trainerName;
    }

    @Override
    void displayInfo() {
        System.out.print(
            "Premium | Trainer: " +
            trainerName +
            " | Sessions: 0 "
        );
    }
}

public class AttendanceAnnouncer {

    static String batchPrint(
        GymMember[] members) {

        StringBuilder sb =
            new StringBuilder();

        for (GymMember member : members) {

            member.displayInfo();

            if (member instanceof PremiumMember) {

                PremiumMember p =
                    (PremiumMember) member;

                sb.append(
                    "[Trainer via downcast: "
                    + p.trainerName
                    + "] | "
                );
            }
        }

        return sb.toString();
    }

    public static void main(String[] args) {

        GymMember[] members = {
            new GymMember("MEM6", 1000),
            new PremiumMember(
                "MEM7", 2000, "Coach Riya")
        };

        System.out.println(
            batchPrint(members)
        );
    }
}
