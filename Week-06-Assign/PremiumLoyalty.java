class GymMember {

    private int[] lateFeeHistory = new int[10];
    private int count = 0;

    protected void chargeLateFee(int amount) {
        lateFeeHistory[count] = amount;
        count++;
    }

    int[] getLateFeeHistory() {

        int[] copy = new int[count];

        for (int i = 0; i < count; i++)
            copy[i] = lateFeeHistory[i];

        return copy;
    }

    int getTotalLateFees() {

        int total = 0;

        for (int i = 0; i < count; i++)
            total += lateFeeHistory[i];

        return total;
    }
}

class PremiumMember extends GymMember {

    @Override
    protected void chargeLateFee(int amount) {
        super.chargeLateFee(amount / 2);
    }
}

public class PremiumLoyalty {

    public static void main(String[] args) {

        PremiumMember p =
            new PremiumMember();

        p.chargeLateFee(200);

        System.out.println(
            p.getTotalLateFees()
        );

        int[] history = p.getLateFeeHistory();

        history[0] = 999;

        System.out.println(
            p.getLateFeeHistory()[0]
        );
    }
}
