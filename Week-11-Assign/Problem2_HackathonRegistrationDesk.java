// Time Complexity: O(n^2)
// Extra Space: O(1)
// Stable: Yes
class Team {
    String name;
    int size;
    Team(String name, int size) {
        this.name = name;
        this.size = size;
    }
    @Override
    public String toString() { return name + "(" + size + ")"; }
}

public class Problem2_HackathonRegistrationDesk {
    public static void sortTeams(Team[] teams) {
        for (int i = 1; i < teams.length; i++) {
            Team key = teams[i];
            int j = i - 1;
            
            while (j >= 0 && teams[j].size > key.size) {
                teams[j + 1] = teams[j];
                j = j - 1;
            }
            teams[j + 1] = key;
        }
    }
}
