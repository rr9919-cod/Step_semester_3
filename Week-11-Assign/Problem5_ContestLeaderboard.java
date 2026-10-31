// Time Complexity: O(n + K) where K is 101 (marks range 0-100) -> O(n)
// Extra Space: O(n + K) -> O(n)
// Stable: Yes
class Entry {
    String name;
    int marks;
    Entry(String name, int marks) {
        this.name = name;
        this.marks = marks;
    }
    @Override
    public String toString() { return name + " " + marks; }
}

public class Problem5_ContestLeaderboard {
    public static Entry[] leaderboard(Entry[] entries) {
        int n = entries.length;
        Entry[] output = new Entry[n];
        int maxMarks = 100;
        int[] count = new int[maxMarks + 1];
        
        // Count occurrences using key = 100 - marks for descending order
        for (int i = 0; i < n; i++) {
            int key = maxMarks - entries[i].marks;
            count[key]++;
        }
        
        // Calculate cumulative count
        for (int i = 1; i <= maxMarks; i++) {
            count[i] += count[i - 1];
        }
        
        // Build the output array from right to left to maintain stability
        for (int i = n - 1; i >= 0; i--) {
            int key = maxMarks - entries[i].marks;
            output[count[key] - 1] = entries[i];
            count[key]--;
        }
        
        return output;
    }
}
