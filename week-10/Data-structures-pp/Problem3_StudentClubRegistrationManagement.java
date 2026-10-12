import java.util.*;

public class Problem3_StudentClubRegistrationManagement {
    static class Student {
        String roll, name;
        Student(String roll, String name) { this.roll = roll; this.name = name; }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Student)) return false;
            return roll.equals(((Student) obj).roll);
        }
        @Override public int hashCode() { return roll.hashCode(); }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Set<Student> members = new LinkedHashSet<>();
        List<String> containsResults = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] p = line.split("\\s+");
            if (p.length >= 3 && p[0].equalsIgnoreCase("ADD")) {
                Student s = new Student(p[1], p[2]);
                System.out.println(members.add(s) ? "Added" : "duplicate rejected");
            } else if (p.length >= 3 && p[0].equalsIgnoreCase("CONTAINS")) {
                containsResults.add("contains: " + members.contains(new Student(p[1], p[2])));
            }
        }
        System.out.println("member count " + members.size());
        for (String result : containsResults) System.out.println(result);
        sc.close();
    }
}
