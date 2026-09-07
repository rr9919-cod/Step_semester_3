import java.util.Scanner;

public class TypingSpeedAccuracyChecker {

    static void checkTypingAccuracy(String original, String typed) {

        int matched = 0;
        int firstMismatch = -1;

        int length = Math.min(original.length(), typed.length());

        for (int i = 0; i < length; i++) {

            if (original.charAt(i) == typed.charAt(i)) {
                matched++;
            } else if (firstMismatch == -1) {
                firstMismatch = i;
            }
        }

        double accuracy = (matched * 100.0) / original.length();

        System.out.println("Matched: " + matched + "/" + original.length());
        System.out.printf("Accuracy: %.2f%%%n", accuracy);

        if (firstMismatch == -1) {

            if (original.length() == typed.length()) {
                System.out.println("No Mismatches");
            } else {
                System.out.println("Length Mismatch");
            }

        } else {

            System.out.println("First Mismatch at position "
                    + (firstMismatch + 1)
                    + " ('" + original.charAt(firstMismatch)
                    + "' vs '" + typed.charAt(firstMismatch) + "')");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter original text: ");
        String original = sc.nextLine();

        System.out.print("Enter typed text: ");
        String typed = sc.nextLine();

        checkTypingAccuracy(original, typed);

        sc.close();
    }
}
