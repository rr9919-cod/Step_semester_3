import java.util.Scanner;

public class TrafficSignalStreakAnalyzer {

    static void findLongestStreak(String signalLog) {

        if (signalLog.length() == 0) {
            System.out.println("Signal log is empty.");
            return;
        }

        char longestColor = signalLog.charAt(0);
        int longestStreak = 1;

        char currentColor = signalLog.charAt(0);
        int currentStreak = 1;

        for (int i = 1; i < signalLog.length(); i++) {

            if (signalLog.charAt(i) == currentColor) {

                currentStreak++;

            } else {

                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                    longestColor = currentColor;
                }

                currentColor = signalLog.charAt(i);
                currentStreak = 1;
            }
        }

        // Check the final streak
        if (currentStreak > longestStreak) {
            longestStreak = currentStreak;
            longestColor = currentColor;
        }

        System.out.println("Longest Streak: '"
                + longestColor
                + "' repeated "
                + longestStreak
                + " times");
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter signal log (R/Y/G): ");
        String signalLog = sc.nextLine();

        findLongestStreak(signalLog);

        sc.close();
    }
}
