import java.util.*;

public class Problem4_ClassMarksGridAnalysis {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> names = new ArrayList<>();
        List<int[]> marksList = new ArrayList<>();

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            int left = line.indexOf('['), right = line.indexOf(']');
            if (left < 0 || right < 0) continue;
            String name = line.substring(0, left).trim();
            String[] values = line.substring(left + 1, right).split(",");
            if (values.length != 3) continue;
            try {
                int[] marks = new int[3];
                for (int i = 0; i < 3; i++) marks[i] = Integer.parseInt(values[i].trim());
                names.add(name);
                marksList.add(marks);
            } catch (NumberFormatException ignored) { }
        }

        if (names.isEmpty()) {
            sc.close();
            return;
        }

        int[] subjectTotals = new int[3];
        int topperIndex = 0;
        int highestTotal = -1;
        System.out.print("Totals ");
        for (int i = 0; i < names.size(); i++) {
            int total = 0;
            for (int j = 0; j < 3; j++) {
                total += marksList.get(i)[j];
                subjectTotals[j] += marksList.get(i)[j];
            }
            if (i > 0) System.out.print(", ");
            System.out.print(names.get(i) + " " + total);
            if (total > highestTotal) {
                highestTotal = total;
                topperIndex = i;
            }
        }
        System.out.println();

        System.out.printf(Locale.US, "averages %.2f, %.2f, %.2f%n",
                subjectTotals[0] / (double) names.size(),
                subjectTotals[1] / (double) names.size(),
                subjectTotals[2] / (double) names.size());
        System.out.println("topper " + names.get(topperIndex) + " (" + highestTotal + ")");
        sc.close();
    }
}
