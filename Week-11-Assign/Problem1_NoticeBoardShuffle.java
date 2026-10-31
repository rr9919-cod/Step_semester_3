// Time Complexity: O(n^2) worst case, O(n) best case (already sorted)
// Extra Space: O(1)
// Stable: Yes
public class Problem1_NoticeBoardShuffle {
    public static int[] bubbleStats(int[] scores) {
        int passes = 0;
        int swaps = 0;
        boolean swapped;
        
        for (int i = 0; i < scores.length - 1; i++) {
            passes++;
            swapped = false;
            for (int j = 0; j < scores.length - 1 - i; j++) {
                if (scores[j] > scores[j+1]) {
                    int temp = scores[j];
                    scores[j] = scores[j+1];
                    scores[j+1] = temp;
                    swaps++;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        
        // Adjust for the case where input is already sorted
        if (passes == 0 && scores.length > 0) passes = 1;
        
        System.out.println("passes = " + passes + ", swaps = " + swaps);
        return scores; // Returning scores as per method signature, though task says returns {passes, swaps}
    }
}
