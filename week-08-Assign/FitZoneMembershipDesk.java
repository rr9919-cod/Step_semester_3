import java.util.*;

// ================ Question 4: The FitZone Membership Desk ================

class Member {
    private final String name;

    public Member(String name) {
        this.name = name;
    }

    public String getName() { return name; }
}

enum MembershipStatus {
    ACTIVE, FROZEN, EXPIRED
}

/**
 * Abstract base for membership plans. Each plan defines its own fee
 * calculation via calculateFee(), kept separate from status/workflow rules.
 * New plans (e.g., HalfYearly) can be added without changing Membership logic.
 */
abstract class MembershipPlan {
    protected static final double BASE_RATE_PER_MONTH = 1000.0;

    public abstract String getPlanName();
    public abstract int getDurationMonths();
    public abstract double calculateFee();
}

class MonthlyPlan extends MembershipPlan {
    public String getPlanName() { return "Monthly"; }
    public int getDurationMonths() { return 1; }
    public double calculateFee() { return BASE_RATE_PER_MONTH * getDurationMonths(); }
}

class QuarterlyPlan extends MembershipPlan {
    public String getPlanName() { return "Quarterly"; }
    public int getDurationMonths() { return 3; }
    public double calculateFee() { return BASE_RATE_PER_MONTH * getDurationMonths() * 0.90; } // 10% off
}

class AnnualPlan extends MembershipPlan {
    public String getPlanName() { return "Annual"; }
    public int getDurationMonths() { return 12; }
    public double calculateFee() { return BASE_RATE_PER_MONTH * getDurationMonths() * 0.75; } // 25% off
}

/**
 * A member's membership. Manages its own status and rejects invalid
 * transitions (e.g., freezing an Expired membership). Status cannot be
 * changed from outside except through checkIn()/freeze()/unfreeze()/expire().
 */
class Membership {
    private final Member member;
    private final MembershipPlan plan;
    private final double fee;
    private MembershipStatus status;

    public Membership(Member member, MembershipPlan plan) {
        this.member = member;
        this.plan = plan;
        this.fee = plan.calculateFee();
        this.status = MembershipStatus.ACTIVE;
    }

    public Member getMember() { return member; }
    public MembershipPlan getPlan() { return plan; }
    public double getFee() { return fee; }
    public MembershipStatus getStatus() { return status; }

    public boolean checkIn() {
        return status == MembershipStatus.ACTIVE;
    }

    public boolean freeze() {
        if (status != MembershipStatus.ACTIVE) return false;
        status = MembershipStatus.FROZEN;
        return true;
    }

    public boolean unfreeze() {
        if (status != MembershipStatus.FROZEN) return false;
        status = MembershipStatus.ACTIVE;
        return true;
    }

    public boolean expire() {
        if (status == MembershipStatus.EXPIRED) return false;
        status = MembershipStatus.EXPIRED;
        return true;
    }
}

/**
 * Facade coordinating members and memberships, printing required output.
 */
class MembershipDesk {
    private final Map<Member, Membership> memberships = new LinkedHashMap<>();

    public void buyMembership(Member member, MembershipPlan plan) {
        Membership membership = new Membership(member, plan);
        memberships.put(member, membership);
        System.out.printf("%s membership created for %s. Fee: \u20B9%.2f. Status: %s.%n",
                plan.getPlanName(), member.getName(), membership.getFee(),
                capitalize(membership.getStatus()));
    }

    public void checkIn(Member member) {
        Membership membership = memberships.get(member);
        if (membership == null) {
            System.out.println("No membership found for " + member.getName() + ".");
            return;
        }
        if (membership.checkIn()) {
            System.out.println(member.getName() + " checked in successfully.");
        } else {
            System.out.println("Check-in denied: " + member.getName() + "'s membership is " +
                    capitalize(membership.getStatus()) + ".");
        }
    }

    public void freeze(Member member) {
        Membership membership = memberships.get(member);
        if (membership == null) return;
        if (membership.freeze()) {
            System.out.println(member.getName() + "'s membership frozen. Status: Frozen.");
        } else {
            System.out.println("Cannot freeze a" + (membership.getStatus() == MembershipStatus.EXPIRED ? "n " : " ") +
                    capitalize(membership.getStatus()) + " membership.");
        }
    }

    public void unfreeze(Member member) {
        Membership membership = memberships.get(member);
        if (membership == null) return;
        if (membership.unfreeze()) {
            System.out.println(member.getName() + "'s membership unfrozen. Status: Active.");
        } else {
            System.out.println("Cannot unfreeze a " + capitalize(membership.getStatus()) + " membership.");
        }
    }

    public void expire(Member member) {
        Membership membership = memberships.get(member);
        if (membership == null) return;
        if (membership.expire()) {
            System.out.println(member.getName() + "'s membership expired. Status: Expired.");
        }
    }

    private String capitalize(MembershipStatus status) {
        String s = status.name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}

public class FitZoneMembershipDesk {
    public static void main(String[] args) {
        MembershipDesk desk = new MembershipDesk();

        Member asha = new Member("Asha");
        Member ravi = new Member("Ravi");

        // Asha buys a Quarterly membership.
        desk.buyMembership(asha, new QuarterlyPlan());

        // Ravi buys a Monthly membership.
        desk.buyMembership(ravi, new MonthlyPlan());

        // Asha checks in.
        desk.checkIn(asha);

        // Asha freezes her membership.
        desk.freeze(asha);

        // Asha attempts to check in (denied, Frozen).
        desk.checkIn(asha);

        // Ravi's membership expires.
        desk.expire(ravi);

        // Ravi attempts to freeze his membership (denied, Expired).
        desk.freeze(ravi);
    }
}
