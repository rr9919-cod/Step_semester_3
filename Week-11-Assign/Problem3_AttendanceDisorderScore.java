// Time Complexity: O(n log n)
// Extra Space: O(n)
// Stable: N/A (Counting problem)
public class Problem3_AttendanceDisorderScore {
    public static long countInversions(int[] roll) {
        if (roll == null || roll.length < 2) return 0;
        int[] temp = new int[roll.length];
        return mergeSortAndCount(roll, temp, 0, roll.length - 1);
    }
    
    private static long mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        long count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;
            count += mergeSortAndCount(arr, temp, left, mid);
            count += mergeSortAndCount(arr, temp, mid + 1, right);
            count += mergeAndCount(arr, temp, left, mid, right);
        }
        return count;
    }
    
    private static long mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left, j = mid + 1, k = left;
        long swaps = 0;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
                swaps += (mid + 1 - i); 
            }
        }
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        while (j <= right) {
            temp[k++] = arr[j++];
        }
        for (i = left; i <= right; i++) {
            arr[i] = temp[i];
        }
        return swaps;
    }
}
