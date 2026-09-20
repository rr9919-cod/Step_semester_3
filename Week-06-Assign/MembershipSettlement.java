class GymMember {

    static int count = 0;

    final String membershipNumber;

    int feesPaid = 0;

    public GymMember(int monthlyFee) {

        count++;

        membershipNumber =
            "GYM-" + (2000 + count);
    }

    void payFee(int amount) {
        feesPaid += amount;
    }

    void payFee(int amount, String mode) {

        System.out.println("Mode: " + mode);

        payFee(amount);
    }

    int getFeesPaid() {
        return feesPaid;
    }

    static boolean isValidReferralCode(
        String code) {

        if (code == null || code.length() != 4)
            return false;

        return code.charAt(0) == 'G'
            && Character.isDigit(code.charAt(1))
            && Character.isDigit(code.charAt(2))
            && Character.isUpperCase(code.charAt(3));
    }

    static int getMembersEnrolled() {
        return count;
    }
}

class GroupClassMember extends GymMember {

    String className;

    public GroupClassMember(
        int monthlyFee,
        String className) {

        super(monthlyFee);
        this.className = className;
    }
}

public class MembershipSettlement {

    static String processWeeklyCheckIn(
        GymMember[] members) {

        int processed = 0;
        int nullSkipped = 0;
        int group = 0;
        int individual = 0;

        for (GymMember member : members) {

            if (member == null) {
                nullSkipped++;
                continue;
            }

            processed++;

            if (member instanceof GroupClassMember)
                group++;
            else
                individual++;
        }

        return processed + " processed | "
            + nullSkipped + " null skipped | "
            + group + " group | "
            + individual + " individual";
    }

    public static void main(String[] args) {

        GymMember m1 =
            new GymMember(1000);

        System.out.println(
            m1.membershipNumber
        );

        System.out.println(
            GymMember.getMembersEnrolled()
        );

        System.out.println(
            GymMember.isValidReferralCode("G45B")
        );

        System.out.println(
            GymMember.isValidReferralCode("G4B")
        );

        System.out.println(
            GymMember.isValidReferralCode("X45B")
        );

        m1.payFee(500);
        m1.payFee(500, "UPI");

        System.out.println(
            m1.getFeesPaid()
        );

        GymMember[] members = {
            new GroupClassMember(1500, "Zumba"),
            null,
            new GymMember(1000)
        };

        System.out.println(
            processWeeklyCheckIn(members)
        );
    }
}
