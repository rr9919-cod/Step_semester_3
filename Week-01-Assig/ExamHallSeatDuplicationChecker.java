import java.util.Scanner;

public class ExamHallSeatDuplicationChecker {

    static void checkDuplicateSeats(int[] seatNumbers) {

        boolean duplicateFound = false;

        for (int i = 0; i < seatNumbers.length; i++) {

            for (int j = i + 1; j < seatNumbers.length; j++) {

                if (seatNumbers[i] == seatNumbers[j]) {

                    System.out.println("Duplicate Seat Number Found: "
                            + seatNumbers[i]);

                    duplicateFound = true;
                    break;
                }
            }
        }

        if (!duplicateFound) {
            System.out.println("No Duplicate Seats Found");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        int n = sc.nextInt();

        int[] seatNumbers = new int[n];

        for (int i = 0; i < n; i++) {

            System.out.print("Enter seat number " + (i + 1) + ": ");
            seatNumbers[i] = sc.nextInt();
        }

        checkDuplicateSeats(seatNumbers);

        sc.close();
    }
}
