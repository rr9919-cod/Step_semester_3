class GymMember {
    String memberId;
    int monthlyFee;
    int sessionsAttended = 0;

    public GymMember(String memberId, int monthlyFee) {
        if (memberId == null || memberId.trim().length() < 4)
            throw new IllegalArgumentException();

        this.memberId = memberId;
        this.monthlyFee = monthlyFee;
    }

    void attendSession() {
        sessionsAttended++;
    }

    int getSessionsAttended() {
        return sessionsAttended;
    }

    static String signUpBatch(String[] memberIds, int monthlyFee) {
        int signedUp = 0;
        int rejected = 0;

        for (String id : memberIds) {
            try {
                new GymMember(id, monthlyFee);
                signedUp++;
            } catch (IllegalArgumentException e) {
                rejected++;
            }
        }

        return "Signed Up: " + signedUp +
               " | Rejected: " + rejected;
    }
}

class PremiumMember extends GymMember {
    String trainerName;

    public PremiumMember(String memberId, int monthlyFee,
                         String trainerName) {
        super(memberId, monthlyFee);
        this.trainerName = trainerName;
    }
}

public class GymMembership {
    public static void main(String[] args) {

        PremiumMember p =
            new PremiumMember("MEM01", 2000, "Coach Riya");

        p.attendSession();
        p.attendSession();

        System.out.println(p.getSessionsAttended());

        String[] ids = {
            "MEM1", "GM1", "MEM2", " ", "MEM3"
        };

        System.out.println(
            GymMember.signUpBatch(ids, 1000)
        );
    }
}
