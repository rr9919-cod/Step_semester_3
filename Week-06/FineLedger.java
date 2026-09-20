class LibraryMember {
    private int[] fineHistory = new int[10];
    private int count = 0;

    protected void chargeFine(int amount) {
        fineHistory[count] = amount;
        count++;
    }

    int[] getFineHistory() {

        int[] copy = new int[count];

        for (int i = 0; i < count; i++)
            copy[i] = fineHistory[i];

        return copy;
    }

    int getTotalFine() {

        int total = 0;

        for (int i = 0; i < count; i++)
            total += fineHistory[i];

        return total;
    }
}

class StudentMember extends LibraryMember {

    @Override
    protected void chargeFine(int amount) {
        super.chargeFine(amount / 2);
    }
}

public class FineLedger {

    public static void main(String[] args) {

        StudentMember s =
            new StudentMember();

        s.chargeFine(100);

        System.out.println(s.getTotalFine());

        int[] history = s.getFineHistory();

        history[0] = 999;

        System.out.println(
            s.getFineHistory()[0]
        );
    }
}
